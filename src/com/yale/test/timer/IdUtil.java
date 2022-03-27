package com.yale.test.timer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.UUIDHexGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 53 bits unique id:
 * |--------|--------|--------|--------|--------|--------|--------|--------|
 * |00000000|00011111|11111111|11111111|11111111|11111111|11111111|11111111|
 * |--------|---xxxxx|xxxxxxxx|xxxxxxxx|xxxxxxxx|xxx-----|--------|--------|
 * |--------|--------|--------|--------|--------|---xxxxx|xxxxxxxx|xxx-----|
 * |--------|--------|--------|--------|--------|--------|--------|---xxxxx|
 *
 * Maximum ID = 11111_11111111_11111111_11111111_11111111_11111111_11111111
 *
 * Maximum TS = 11111_11111111_11111111_11111111_111
 *
 * Maximum NT = ----- -------- -------- -------- ---11111_11111111_111 = 65535
 *
 * Maximum SH = ----- -------- -------- -------- -------- -------- ---11111 = 31
 *
 * It can generate 64k unique id per IP and up to 2106-02-07T06:28:15Z.
 * 源码来自廖雪峰github:https://github.com/michaelliao/itranswarp/blob/master/src/main/java/com/itranswarp/util/IdUtil.java
 * 廖雪峰知乎:https://www.zhihu.com/people/liaoxuefeng/posts
 * tags:雪花ID 雪花算法 Twitter的Snowflake算法
 * 
 * 作者：廖雪峰
 * 链接：https://zhuanlan.zhihu.com/p/65095562
 * 在应用程序中，经常需要全局唯一的ID作为数据库主键。如何生成全局唯一ID？首先，需要确定全局唯一ID是整型还是字符串？如果是字符串，那么现有的UUID就完全满足需求，不需要额外的工作。
 * UUID相关信息可以看TimerTest2类
 * 缺点是字符串作为ID占用空间大，索引效率比整型低。如果采用整型作为ID，那么首先排除掉32位int类型，因为范围太小，必须使用64位long型。
 * 采用整型作为ID时，如何生成自增、全局唯一且不重复的ID？
 * 方案一：利用数据库的自增ID，从1开始，基本可以做到连续递增。Oracle可以用SEQUENCE，MySQL可以用主键的AUTO_INCREMENT，虽然不能保证全局唯一，但每个表唯一，也基本满足需求。
 * 数据库自增ID的缺点是数据在插入前，无法获得ID。数据在插入后，获取的ID虽然是唯一的，但一定要等到事务提交后，ID才算是有效的。有些双向引用的数据，不得不插入后再做一次更新，比较麻烦。
 * 第二种方式是采用一个集中式ID生成器，它可以是Redis，也可以是ZooKeeper，也可以利用数据库的表记录最后分配的ID。这种方式最大的缺点是复杂性太高，需要严重依赖第三方服务，而且代码配置繁琐。
 * 一般来说，越是复杂的方案，越不可靠，并且测试越痛苦。
 * 第三种方式是类似Twitter的Snowflake算法，它给每台机器分配一个唯一标识，然后通过时间戳+标识+自增实现全局唯一ID。这种方式好处在于ID生成算法完全是一个无状态机，无网络调用，高效可靠。
 * 缺点是如果唯一标识有重复，会造成ID冲突。Snowflake算法采用41bit毫秒时间戳，加上10bit机器ID，加上12bit序列号，理论上最多支持1024台机器每秒生成4096000个序列号，对于Twitter的规模来说够用了。
 * 但是对于绝大部分普通应用程序来说，根本不需要每秒超过400万的ID，机器数量也达不到1024台。
 * 所以，我们可以改进一下，使用更短的ID生成方式：53bitID由32bit秒级时间戳+16bit自增+5bit机器标识组成，累积32台机器，每秒可以生成65万个序列号，核心代码：nextId
 * 时间戳减去一个固定值，此方案最高可支持到2106年。如果每秒65万个序列号不够怎么办？没关系，可以继续递增时间戳，向前“借”下一秒的65万个序列号。同时还解决了时间回拨的问题。
 * 机器标识采用简单的主机名方案，只要主机名符合host-1，host-2就可以自动提取机器标识，无需配置。最后，为什么采用最多53位整型，而不是64位整型？
 * 这是因为考虑到大部分应用程序是Web应用，如果要和JavaScript打交道，由于JavaScript支持的最大整型就是53位，超过这个位数，JavaScript将丢失精度。
 * 因此，使用53位整数可以直接由JavaScript读取，而超过53位时，就必须转换成字符串才能保证JavaScript处理正确，这会给API接口带来额外的复杂度。
 * 
 * 读者疑问:
 * 
 * 知乎用户andylizi 问:然而UUID并不一定要是字符串啊。“xxxxxxxx-xxxx-Mxxx-Nxxx-xxxxxxxxxxxx”只是它最常见的表现形式而已，
 * 但跟IP地址一样，它也可以被表示为整数形式。具体来说是一个 128 位的整数或两个 64 位的整数。
 * Java 里通过 uid.getMostSignificantBits() 和 uid.getLeastSignificantBits() 两个方法获得。
 * 廖雪峰答:问题是没有原生128bit整数
 * 知乎用户andylizi 问: 那就把两个 64 位整数给异或一下就得到 64 位了。总之用位运算的话要多少位都能搞出来啊。
 * oobleck回复andylizi:异或出来和随机生成一个64bit整数安全上就没太大区别了，很容易冲突
 * andylizi回复oobleck:SHA 512/256 就是把生成出来 512 位的 hash 给缩减到 256 位（我随手查了一下好像没查到具体的缩减方法？），也不一样没事。
 * 以及最理想的情况下（生成的64个bit里每个bit的可能性都相等），直接生成64位跟生成128位再异或到64感觉不会有区别吧？我对这方面的数学也不了解，但光一句“很容易冲突”实在没有什么说服力。
 * oobleck回复andylizi: 因为 256 位已经足够长了，但是 64 位就不够了。
 * 了解下生日悖论，64 位随机数的话，只要 2^32 个数就有近 50% 的概率发生碰撞，也就几十亿的数量级，很多系统里是完全不够用的。
 * 用时间戳就可以保证有可能发生碰撞的桶足够小，新生成的 ID 也绝不可能和很久之前的碰撞，而用异或就把时间戳给抹掉了。
 * andylizi回复oobleck： 有道理。
 * https://www.zhihu.com/people/liaoxuefeng/posts
 * 
 * 知乎airingHan用户问:大神，为啥我看别人JAVA版本的snowflake算法，都是等一秒，你这里是直接加1，有啥区别吗？return nextId(epochSecond + 1); 
 * 廖雪峰 答:如果你对时间精度要求很高，比如按id排序，就等1秒，如果只是要求唯一，往前借1秒就更快
 * 知乎 不死鸟 用户问:这不是与身份证号分配类似吗？每个地区先拿一个唯一标识，比如帝都就是110开头。每个出生人口 号码为地区唯一标识+时间戳+后面4位。
 * 知乎 李无夜回复不死鸟 答: 技术来源于生活
 * 知乎 wbget 问:2^16=65536，没太理解65万/s怎么得出来的？求告知。
 * 廖雪峰 (作者) 回复wbget: 6.5万
 * 
 * 知乎 张猩猩 问:超过53位，js怎么存储的？
 * 知乎 廖雪峰 答:你可以试试在js给一个变量赋值超过53bit的整数，末尾自动变0了，精度丢失.
 * 知乎 sandogeek 答: Long.js可以解决此烦恼
 * 廖雪峰 答 sandogeek:要求越多,用的人越麻烦
 * 知乎Xiongfei-Shi 答 张猩猩: 一个基于long.js实现的63bit版本 https://github.com/shixiongfei/tsuid
 * 知乎Xiongfei-Shi 答 张猩猩:后端辛苦点，long转成String给前端
 * 
 * 知乎 Alter 问:有js版本的实现吗？
 * wbget回复Alter: 我撸了一个，https://github.com/wbget/uuid-int
 */
public class IdUtil {
	private static final Logger logger = LoggerFactory.getLogger(IdUtil.class);

	private static final Pattern PATTERN_LONG_ID = Pattern.compile("^([0-9]{15})([0-9a-f]{32})([0-9a-f]{3})$");

	private static final Pattern PATTERN_HOSTNAME = Pattern.compile("^.*\\D+([0-9]+)$");

	private static final long OFFSET = LocalDate.of(2000, 1, 1).atStartOfDay(ZoneId.of("Z")).toEpochSecond();

	private static final long MAX_NEXT = 0b11111_11111111_111L;

	private static final long SHARD_ID = getServerIdAsLong();

	private static long offset = 0;

	private static long lastEpoch = 0;

	public static void main(String[] args) {
		System.out.println("雪花id:" + IdUtil.nextId());
		System.out.println("雪花id:" + IdUtil.nextId());
		
		IdentifierGenerator gen = new UUIDHexGenerator();//主键生成UUIDHex对象hibernate-core-5.4.2.Final.jar
		System.out.println("利用hibernate生成主键随机ID:" + gen.generate(null, null));
		//plyFeeVO.setCPkId((String)gen.generate(null, null));//主键  永久代存储的都是什么数据？ 
	}
	
	public static long nextId() {
		return nextId(System.currentTimeMillis() / 1000);
	}

	private static synchronized long nextId(long epochSecond) {
		if (epochSecond < lastEpoch) {
			// warning: clock is turn back:
			logger.warn("clock is back: " + epochSecond + " from previous:" + lastEpoch);
			epochSecond = lastEpoch;
		}
		if (lastEpoch != epochSecond) {
			lastEpoch = epochSecond;
			reset();
		}
		offset++;
		long next = offset & MAX_NEXT;
		if (next == 0) {
			logger.warn("maximum id reached in 1 second in epoch: " + epochSecond);
			return nextId(epochSecond + 1);
		}
		return generateId(epochSecond, next, SHARD_ID);
	}

	private static void reset() {
		offset = 0;
	}

	private static long generateId(long epochSecond, long next, long shardId) {
		return ((epochSecond - OFFSET) << 21) | (next << 5) | shardId;
	}

	/**
	 * 知乎鸿哎用户问:getServerIdAsLong() 方法里，为什么是n<8 而不是 n<32 呢？
	 * 知乎南瓜姑娘回复鸿哎:应该写32，作者只是示意一下，毕竟实际生产的话这里需要根据部署环境自己改造
	 * @return
	 */
	private static long getServerIdAsLong() {
		try {
			String hostname = InetAddress.getLocalHost().getHostName();
			Matcher matcher = PATTERN_HOSTNAME.matcher(hostname);
			if (matcher.matches()) {
				long n = Long.parseLong(matcher.group(1));
				if (n >= 0 && n < 8) {
					logger.info("detect server id from host name {}: {}.", hostname, n);
					return n;
				}
			}
		} catch (UnknownHostException e) {
			logger.warn("unable to get host name. set server id = 0.");
		}
		return 0;
	}

	public static long stringIdToLongId(String stringId) {
		// a stringId id is composed as timestamp (15) + uuid (32) + serverId (000~fff).
		Matcher matcher = PATTERN_LONG_ID.matcher(stringId);
		if (matcher.matches()) {
			long epoch = Long.parseLong(matcher.group(1)) / 1000;
			String uuid = matcher.group(2);
			byte[] sha1 = HashUtil.sha1AsBytes(uuid);
			long next = ((sha1[0] << 24) | (sha1[1] << 16) | (sha1[2] << 8) | sha1[3]) & MAX_NEXT;
			long serverId = Long.parseLong(matcher.group(3), 16);
			return generateId(epoch, next, serverId);
		}
		throw new IllegalArgumentException("Invalid id: " + stringId);
	}
}
