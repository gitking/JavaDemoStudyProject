package com.yale.test.ps;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/*
 * Hmac算法
 * HMAC(Hash Message Authentication Code，散列消息鉴别码)
 * 在前面讲到哈希算法时，我们说，存储用户的哈希口令时，要加盐存储，目的就在于抵御彩虹表攻击。
 * 我们回顾一下哈希算法：digest = hash(input),正是因为相同的输入会产生相同的输出，我们加盐的目的就在于，使得输入有所变化：digest = hash(salt + input)
 * 这个salt可以看作是一个额外的“认证码”，同样的输入，不同的认证码，会产生不同的输出。因此，要验证输出的哈希，必须同时提供“认证码”。
 * Hmac算法就是一种基于密钥的消息认证码算法，它的全称是Hash-based Message Authentication Code，是一种更安全的消息摘要算法。
 * Hmac算法总是和某种哈希算法配合起来用的。例如，我们使用MD5算法，对应的就是HmacMD5算法，它相当于“加盐”的MD5：
 * HmacMD5 ≈ md5(secure_random_key, input)
 * 因此，HmacMD5可以看作带有一个安全的key的MD5。使用HmacMD5而不是用MD5加salt，有如下好处：
 * 	HmacMD5使用的key长度是64字节，更安全；
 * 	Hmac是标准算法，同样适用于SHA-1等其他哈希算法；
 * 	Hmac输出和原有的哈希算法长度一致。
 * 可见，Hmac本质上就是把key混入摘要的算法。验证此哈希时，除了原始的输入数据，还要提供key。
 * 为了保证安全，我们不会自己指定key，而是通过Java标准库的KeyGenerator生成一个安全的随机的key。下面是使用HmacMD5的代码：
 * 
 * HMAC(Hash Message Authentication Code，散列消息鉴别码，基于密钥的Hash算法的认证协议。消息鉴别码实现鉴别的原理是，用公开函数和密钥产生一个固定长度的值作为认证标识，
 * 用这个标识鉴别消息的完整性。使用一个密钥生成一个固定大小的小数据块，即MAC，并将其加入到消息中，然后传输。接收方利用与发送方共享的密钥进行鉴别认证等。
 * http://www.jfh.com/jfperiodical/article/818
 */
public class Hmac {
	public static void main(String[] args) {
		try {
			KeyGenerator keyGen = KeyGenerator.getInstance("HmacMD5");
	        SecretKey key = keyGen.generateKey();
	        byte[] skey = key.getEncoded();//打印随机生成的key
	        System.out.println(Arrays.toString(skey));
	        System.out.println("打印随机生成的key:" + new BigInteger(1, skey).toString(16));
	        
	        Mac mac = Mac.getInstance("HmacMD5");
	        mac.init(key);
	        mac.update("HelloWorld".getBytes("UTF-8"));
	        byte[] result = mac.doFinal();
	        System.out.println("Hmac算法:" + new BigInteger(1, result).toString(16));
	        /*
	         * 和MD5相比，使用HmacMD5的步骤是：
	         * 通过名称HmacMD5获取KeyGenerator实例；
	         * 通过KeyGenerator创建一个SecretKey实例；
	         * 通过名称HmacMD5获取Mac实例；
	         * 用SecretKey初始化Mac实例；
	         * 对Mac实例反复调用update(byte[])输入数据；
	         * 调用Mac实例的doFinal()获取最终的哈希值。
	         * 我们可以用Hmac算法取代原有的自定义的加盐算法，因此，存储用户名和口令的数据库结构如下：
	         * username	secret_key (64 bytes)	password
				bob		a8c06e05f92e...5e16		7e0387872a57c85ef6dddbaa12f376de
				alice	e6a343693985...f4be		c1f929ac2552642b302e739bc0cdbaac
				tim		f27a973dfdc0...6003		af57651c3a8a73303515804d4af43790
			 * 有了Hmac计算的哈希和SecretKey，我们想要验证怎么办？这时，SecretKey不能从KeyGenerator生成，而是从一个byte[]数组恢复：
	         */
	        byte[] hkey = new byte[]{106, 70, -110, 125, 39, -20, 52, 56, 85, 9, -19, -72, 52, -53, 52, -45, -6, 119, -63,
	                30, 20, -83, -28, 77, 98, 109, -32, -76, 121, -106, 0, -74, -107, -114, -45, 104, -104, -8, 2, 121, 6,
	                97, -18, -13, -63, -30, -125, -103, -80, -46, 113, -14, 68, 32, -46, 101, -116, -104, -81, -108, 122,
	                89, -106, -109};
	        /*
	         * 为什么是这个数组？
	         * 我觉得这里只是为了表达一种思路与特定数组无关：
	         * 在获取key并加密文本存储下来后，key.encode()转成byte[]进行存储
	         * 然后验证的时候从存储的byte[]（例子里直接写了一个）复原出原来的key，然后进行验证。
	         *（因为我发现这个数组转16进制输出长度和key.encode()的输出长度是一样的）
	         * 你打印上面的System.out.println(Arrays.toString(skey));就知道了
	         * 这个数组就相当于第一个例子中key.getEncoded()中获取的数组，然后通过new SecretKeySpec(hkey, "HmacMD5")，得到SecretKey key，相当于第一个例子中keyGen.generateKey()
	         */
	        SecretKey ansKey = new SecretKeySpec(hkey, "HmacMD5");
	        Mac ansMac = Mac.getInstance("HmacMD5");
	        ansMac.init(ansKey);
	        ansMac.update("HelloWorld".getBytes("UTF-8"));
	        byte[] res = ansMac.doFinal();
	        //恢复SecretKey的语句就是new SecretKeySpec(hkey, "HmacMD5")。
	        //Hmac算法是一种标准的基于密钥的哈希算法，可以配合MD5、SHA-1等哈希算法，计算的摘要长度和原摘要算法长度相同。
	        System.out.println("验证" +Arrays.toString(res));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}

//可以通过缓存来存储secretkey。
//https://www.liaoxuefeng.com/wiki/1252599548343744/1305366354722849#0
class Hash {

    public static void main(String[] args) {
        testHmac("bobo");
        testHmac("bobo");
        testHmac("hello123");
        testHmac("hello123");
    }

    public static void testHmac(String pwd){
        String input = pwd;
        try {
            String enc = Hash.hash2(input);
            System.out.println(enc);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
 
     //缓存存储secretkey
     private static Map<String, byte[]> store = new HashMap<>();
   
    //返回消息摘要
    public static String hash2(String source) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException {
        SecretKey key = new SecretKeySpec(getSecretyKey(source), "HmacMD5");
        Mac mac = Mac.getInstance("HmacMD5");
        mac.init(key);
        mac.update(source.getBytes("UTF-8"));
        byte[] result = mac.doFinal();
        return (new BigInteger(1, result).toString(16));
    }

    //获取对应secretKey，如不存在随机生成并放入缓存。
    private static byte[] getSecretyKey(String id) throws NoSuchAlgorithmException {
        byte[] skey = store.get(id);
        if(skey == null){
            KeyGenerator keyGen = KeyGenerator.getInstance("HmacMD5");
            SecretKey key = keyGen.generateKey();
            skey = key.getEncoded();
            store.put(id, skey);
        }
        return skey;
    }
}
