package com.yale.test.ps;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.crypto.KeyAgreement;

/*
 * 密钥交换算法
 * 对称加密算法解决了数据加密的问题。我们以AES加密为例，在现实世界中，小明要向路人甲发送一个加密文件，他可以先生成一个AES密钥，对文件进行加密，然后把加密文件发送给对方。因为对方要解密，就必须需要小明生成的密钥。
 * 现在问题来了：如何传递密钥？
 * 在不安全的信道上传递加密文件是没有问题的，因为黑客拿到加密文件没有用。但是，如何如何在不安全的信道上安全地传输密钥？
 * 要解决这个问题，密钥交换算法即DH算法：Diffie-Hellman算法应运而生。
 * DH算法解决了密钥在双方不直接传递密钥的情况下完成密钥交换，这个神奇的交换原理完全由数学理论支持。
 * 我们来看DH算法交换密钥的步骤。假设甲乙双方需要传递密钥，他们之间可以这么做：
 * 甲首选选择一个素数p，例如509，底数g，任选，例如5，随机数a，例如123，然后计算A=g^a mod p，结果是215，然后，甲发送p＝509，g=5，A=215给乙；
 * 乙方收到后，也选择一个随机数b，例如，456，然后计算B=g^b mod p，结果是181，乙再同时计算s=A^b mod p，结果是121；
 * 乙把计算的B=181发给甲，甲计算s＝B^a mod p的余数，计算结果与乙算出的结果一样，都是121。
 * 所以最终双方协商出的密钥s是121。注意到这个密钥s并没有在网络上传输。而通过网络传输的p，g，A和B是无法推算出s的，因为实际算法选择的素数是非常大的。
 * 所以，更确切地说，DH算法是一个密钥协商算法，双方最终协商出一个共同的密钥，而这个密钥不会通过网络传输。
 * 如果我们把a看成甲的私钥，A看成甲的公钥，b看成乙的私钥，B看成乙的公钥，DH算法的本质就是双方各自生成自己的私钥和公钥，私钥仅对自己可见，然后交换公钥，并根据自己的私钥和对方的公钥，生成最终的密钥secretKey，DH算法通过数学定律保证了双方各自计算出的secretKey是相同的。
 * 但是DH算法并未解决中间人攻击，即甲乙双方并不能确保与自己通信的是否真的是对方。消除中间人攻击需要其他方法。
 * 小结
 * DH算法是一种密钥交换协议，通信双方通过不安全的信道协商密钥，然后进行对称加密传输。
 * DH算法没有解决中间人攻击。
 * 问:先各自生成公钥和私钥，然后交换各自的公钥，根据对方的公钥和自己的私钥得出密钥，要是得出的密钥相同，就可以把这个密钥当作AES的密钥进行加密了
 * 答:不是“要是得出的密钥相同”，而是“肯定要得出相同的密钥”，不然就不是密钥交换算法了
 * 使用Java实现DH算法的代码如下：
 */
public class DiffieHellman {
	public static void main(String[] args) {
		// Bob和Alice:
		Person bob = new Person("Bob");
		bob.generateKeyPair();//各自生成KeyPair:生成私钥和公钥
		
		Person alice = new Person("Alice");
		alice.generateKeyPair();//各自生成KeyPair:生成私钥和公钥
		
		//双方交换各自的PublicKey:
		//Bob根据Alice的PublicKey生成自己的本地密钥:
		bob.generateSecretKey(alice.publicKey.getEncoded());
		
		//Alice根据Bob的PublicKey生成自己的本地密钥:
		alice.generateSecretKey(bob.publicKey.getEncoded());
		
		//检查双方的本地密钥是否相同:
		bob.printKeys();
		System.out.println("---------------------------------------");
		alice.printKeys();
		
		//双方的SecretKey相同,后续通信将使用SecretKey作为密钥进行AES加密
		
		/**
		 * MOD(求余函数),mod运算符做求余运算，15625mod23=8
		 * A=g^a mod p 这个表达式,请问是乘方再取模吗？ Math.pow(5,123)%p我算出来不是215这个值.
		 * 你算错了,必须用BigInteger整数计算。
		 * 5的123次方，是一个非常非常大的数字。long都表示不了。
		 * 
		 * 问：在一次运行时，模数和底数应当是由系统自己决定的公开参数，因此一个程序内两个person对象各自生成公私密钥用的是同一对模数和底数。当在一台电脑上获取了公私密钥，然后在另一台电脑上获取公私密钥，该如何保证这两台电脑用的是同一对模数和底数呢？
		 * 答：从理论到实际应用，还要有算法、标准，p和g是rfc3526定死的，大家都用固定的p和g，算出来的共享密钥就是一样的。https://www.ietf.org/rfc/rfc3526.txt
		 * 
		 * 问：为什么实际代码不用传递信息（g，p）。 实际代码测试确实如老师所言，但是理论上是需要传递信息（g，p），
		 * 这是因为KeyPairGenerator.getInstance(512)已经内置了相同的（g,p）；还是因为 KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
		 * keyAgreement.init(this.privateKey); // 自己的PrivateKey
		 * keyAgreement.doPhase(receivedPublicKey, true); // 对方的PublicKey，做了什么操作
		 * 自问自答：KeyPairGenerator.getInstance(512)。这个调用后p和g就变成了相同值，但是为什么这样呢？不同的机器运行也会是同一个值？
		 * https://www.liaoxuefeng.com/wiki/1252599548343744/1304227905273889
		 * 量子论的出现彻底颠覆了传统的物理世界，现在有的物理规律在量子世界里全军覆没了。这就是《三体》中说的物理学即将不复存在的意思。在量子世界里，薛定谔的猫可以即是死的同时又是活的，
		 * 在MWI理论中一个人也是杀不死的，你在这个世界死了，在另外一个世界却活的好好的，这就是电影中说的平行世界。平行世界不是一个科幻概念，而是一个真实的量子理论。
		 * 量子理论这个东西科学家讨论了几百年也没有一个大一统的解释，每个科学家拉帮结派搞出无数个理论，超弦万能理论，MWI多宇宙理论，退相干历史理论，系综理论，等等，这么多理论把科学家都搞疯了。
		 * 关于量子的物理实验也非常不好做，因为人一旦参与进去就会干扰量子世界，比如那个猫，你在没观测它的时候，它处于死活叠加的状态，人一看，它就立马随机挑一个确定状态展现出来，人看到的猫要么是死的要么是活的。
		 * 不同的观测手段，观测到的是不一样的结果。这一切一切的都是因为，在几百年前科学家的一场争论，光到底是一个粒子还是一个波。最后说一下量子计算机，普通的电脑计算机在同一时刻只能计算一个数字要么是0要么是1，
		 * 但是量子计算机同一时刻可以计算1和0俩个数字，就是说1和0叠加在一起了，这个特性使得量子计算机的计算速度比传统计算机的计算能力大了几百万倍，量子计算机的出现让现有的加密学瞬间土崩瓦解。
		 * 解密本质上就是对一个很大的数字做质数分解，比如15等于3*5。但是你要对一个256位的数字坐质数分解，把全世界的计算机集合起来，算到地球爆炸也算不出来，量子计算机可能一秒就计算出来了。不过也不用担心，量子计算机出来之后，量子加密算法跟着就出来了。
		 * 目前流行的加密算法不少，很多都是依赖于这样一个靠山，也即所谓的“大数不可分解性”。大家中学里都苦练过因式分解，也做过质因数分解的练习，比如把15这个数字分解成它的质因数的乘积，我们就会得到15=3x5这样一个
		 * 唯一的答案。问题是，分解15看起来很简单，但如果要分解一个很大很大的数，我们所遭遇到的困难就变得几乎不可克服了。比如,把10949769651859分解成它的质因数乘积，我们该怎么做？槽糕的是，
		 * 在解决这种问题上，我们还没有发现一种有效的算法。一种笨办法就是用所有已知的质数去一个一个地试，最后我们会发现10949769651859=4220851x2594209（数字取自德义奇的著作The Fabric of Reality）
		 * 但这是异常低效的。更遗憾的是，随着数字的加大,这种方法所费的时间呈现出几何式的增长。每当它增加一位数，我们就要多费3倍多的时间来分解它，很快我们就会发现，就算计算时间超过宇宙的年龄，我们也无法完成这个任务。
		 * 当然我们可以改进我们的算法，但目前所知最好的算法(我想应该是GNFS)所需的复杂性也只不过比指数增长稍好，仍未达到多项式的要求(所谓多项式，指的是当处理数字的位数n增大时，算法所费时间按照多项式的形式，
		 * 也就是n的k次方的速度增长)。大数分解加密的安全性2019383291023828921092383= ? x ?，这就是大数分解加密的安全性。
		 * 所以，如果我们用一个大数来保护我们的密码，只有当这个大数被成功分解时才会泄密，我们应当是可以感觉非常安全的。因为从上面的分析可以看出，想使用“暴力”方法，也就是穷举法来破解这样的密码几乎是不可能的。虽然我们
		 * 的处理器速度每隔18个月就翻倍，但也远远追不上安全性的增长：只要给我们的大数增加一俩位数，就可以保好几十年的平安。目前最流行的一些加密术，比如公钥的RSA算法正是建筑在这个基础之上的。
		 * 但量子计算机实现的可能使得所有的这些算法在瞬间人人自危。量子计算机的并行机制使得它可以同时处理多个计算，这使得大数不再成为障碍！1994年，贝尔实验室的彼得·肖(Peter Shor)创造了一种利用量子计算机
		 * 的算法，可以有效地分解大数(复杂性符合多项式！)。比如我们要分解一个250位的数字，如果用传统计算机的话，就算我们利用最有效的算法，把全世界所有的计算机都联网到一起联合工作，也要花上几百万年的漫长时间。
		 * 但如果要用量子计算机在分解250位数的时候，同时处理了10的500次方个不同的计算！
		 * 更糟的事情接踵而来。在肖发明了他的算法之后，1996年贝尔实验室的另一位科学家洛佛·各路佛(Lov Grover)很快发现了另一种算法，可以有效地搜索未排序的数据库。如果我们想从一个
		 * 有n个记录但未排序的数据库中找出一个特定的记录的话，大概只好靠随机地碰运气，平均试n/2次才会得到结果,但如果用格鲁佛的算法，复杂性则下降到根号n次方。这使得另一种著名的非公钥系统加密算法，DES面临崩溃。
		 * 现在几乎所有的人都开始关注量子计算，更多的量子算法肯定会接连不断地被创造出来，如果真的能够造出量子计算机，那么对于现在所有的加密算法，不管是RSA,DES，或者别的什么椭圆曲线，都可以看成是末日的来临。
		 * 最可怕的是，因为量子并行运算内在的机制，即使我们不断增加密码的位数，也只不过给破解者增加很小的代价罢了，这些加密术实际上都破产了。
		 * 2001年,IBM的一个小组演示了肖的算法，他们利用7个量子比特把15分解成了3和5的乘积。当然，这只是非常初步的进展，我们还不知道，是否真的可以造出有实际价值的量子计算机，量子态的纠缠非常容易退相干，
		 * 这使得我们面临着技术上的严重困难。虽然2002年，斯坦福和日本的科学家声称，一台硅量子计算机时可以利用现在的技术实现的，2003年，马里兰大学的科学家们成功地实现了相距0.7毫米的俩个量子比特的
		 * 互相纠缠，一切都在向好的方向发展，但也许量子计算机真正的运用还要过好几十年才会实现。这个项目是目前最为热门的话题之一，让我们且拭目以待。
		 * 《上帝掷骰子吗？》
		 * 质数是指在大于1的自然数中，除了1和它本身以外不再有其他因数的自然数。
		 * 质数又称素数。一个大于1的自然数，除了1和它自身外，不能被其他自然数整除的数叫做质数；否则称为合数（规定1既不是质数也不是合数）。2,3,5,7,11,13,19,23都是质数，质数的个数是无穷的。
		 * 因数是指整数a除以整数b(b≠0) 的商正好是整数而没有余数，我们就说b是a的因数。
		 * 每个合数都可以写成几个质数相乘的形式，其中每个质数都是这个合数的因数，把一个合数用质因数相乘的形式表示出来，叫做分解质因数。如30=2×3×5 。分解质因数只针对合数。
		 * 合数:除1和它本身以外，还有其他因数。（因数个数至少有3个），最小的质数是2，最小的合数是4，一位数中，最大的质数是7，最大的合数是9.
		 * 问：请写出36的因数有哪些，有多少个，质因数有哪些？注意因数和质因数是俩个意思。因数包含质因数。
		 * 把一个合数分解成若干个质因数的乘积的形式，即求质因数的过程叫做分解质因数。
		 * 分解质因数只针对合数。（分解质因数也称分解素因数）求一个数分解质因数，要从最小的质数除起，一直除到结果为质数为止。分解质因数的算式叫短除法，和除法的性质相似，还可以用来求多个数的公因式。
		 * 自然数是指用以计量事物的件数或表示事物次序的数。即用数码0，1，2，3，4……所表示的数。自然数由0开始，一个接一个，组成一个无穷的集体。自然数有有序性，无限性。分为偶数和奇数，合数和质数等。
		 * DH的本质好像就是质数分解把？？？
		 * 15能被3和5整除，所以不是质数，是合数。
		 * 100 以内的素数(质数)有 2、3、5、7 、11、13、17、19 、23、29、 31、37 、41、43、47 、53、59 、61、67、 71、73、79、 83、89 、97 
		 * 质数是不能被1和自身以外的整数整除的数，奇数是不能被2整除的数。
		 * 2是质数，质数除了2以外都是奇数，但奇数不一定是质数 。
		 * 0和1既不是质数，也不是合数 
		 * https://baike.baidu.com/item/%E5%88%86%E8%A7%A3%E8%B4%A8%E5%9B%A0%E6%95%B0/2253749?fr=aladdin
		 */
		BigInteger pow = BigInteger.valueOf(5).pow(123);
		System.out.println(pow.mod(BigInteger.valueOf(509)));
		
		
		System.out.println("2的256次方:->" + new BigInteger("2").pow(256));
		System.out.println("2的3次方为(2x2x2):->" + new BigInteger("2").pow(3));
		System.out.println("2的4次方为(2x2x2x2):->" + new BigInteger("2").pow(4));
		
		System.out.println(pow.longValue());
		System.out.println("Long能表示的最大整数为:" + Long.MAX_VALUE);
		System.out.println("Long能表示的最大整数为:" + (Long.MAX_VALUE + 1));
		System.out.println((long)(Math.pow(5,123)));
		System.out.println((long)(Math.pow(5,123))%509);
		System.out.println((long)(Math.pow(5,123))/509);
		
		BigInteger bigInt = new BigInteger("2019383291023828921092383");
		System.out.println("大数分解加密的安全性来自《上帝掷骰子吗》这本书:" + bigInt.toString());
		
		System.out.println("如果返回为true代表可能为质数(素数)，如果为false它肯定是合数,23是一个质数:-->" + bigInt.isProbablePrime(23));
		System.out.println("返回大于这个 {@code BigInteger} 的第一个整数，它可能是素数。 " + bigInt.nextProbablePrime());
		
		System.out.println("是否为质数:" + isPrimeNumber(new BigInteger("2")));
		System.out.println("是否为质数:" + isPrimeNumber(new BigInteger("3")));
		System.out.println("是否为质数:" + isPrimeNumber(new BigInteger("5")));
		System.out.println("是否为质数:" + isPrimeNumber(new BigInteger("7")));
		System.out.println("是否为质数:" + isPrimeNumber(new BigInteger("11")));
		System.out.println("是否为质数:" + isPrimeNumber(new BigInteger("19")));
		System.out.println("是否为质数:" + isPrimeNumber(new BigInteger("23")));
		System.out.println("是否为质数:" + isPrimeNumber(new BigInteger("100")));
		System.out.println("是否为质数:" + isPrimeNumber(new BigInteger("2019383291023828921092383")));
		System.out.println("2019383291023828921092383除以131等于多少:" + new BigInteger("2019383291023828921092383").divide(new BigInteger("131")));
		System.out.println("15415139626136098634293乘以131等于多少:" + new BigInteger("15415139626136098634293").multiply(new BigInteger("131")));
		System.out.println("分解质因数:10949769651859:--->");
		H6();
		H6Plus(new BigInteger("10949769651859"));//计算大数:10949769651859的质因数总共耗时596毫秒
		//2019383291023828921092383=131*39581*701863*12804578845,2019383291023828921092383共有4个质因数
		H6Plus(new BigInteger("2019383291023828921092383"));//计算大数:2019383291023828921092383的质因数总共耗时2672652毫秒
		//H6Plus方法比primeFactorization方法效率提高了几百万倍
		//primeFactorization(new BigInteger("10949769651859"));
	}
	
	/**
	 * 判断一个整数是否为质数
	 * 质数是指在大于1的自然数中，除了1和它本身以外不再有其他因数的自然数。
	 * 质数又称素数。一个大于1的自然数，除了1和它自身外，不能被其他自然数整除的数叫做质数；否则称为合数（规定1既不是质数也不是合数）。2,3,5,7,11,13,19,23都是质数，质数的个数是无穷的。
	 * @param bigInt
	 * @return
	 */
	public static boolean isPrimeNumber(BigInteger bigInt) {
		//bigInt的数字越大,isPrimeNumber这个执行所耗费的时间就越慢,当bigInt等于9万的时候,这个方法平均要耗费10毫秒左右的时间
		long beginTime = System.nanoTime();
		boolean result = false;
		BigInteger begin = new BigInteger("1");
		while (true) {
			begin = begin.add(new BigInteger("1"));//从2开始除
			if (bigInt.compareTo(new BigInteger("1")) < 1) {
				//质数必须比1大
				continue;
			}
			if (bigInt.compareTo(begin) == 0) {
				//除了本身，等于0说明俩个数字相等
				continue;
			}
			if (bigInt.compareTo(begin) < 0) {
				result = true;
				//begin比bigInt大的时候就不再比了
				break;
			}
			BigInteger mod = bigInt.mod(begin);
			if (mod.compareTo(BigInteger.ZERO) == 0) {
				//余数=0说明被整数了，肯定不是质数
				break;
			}
			//BigInteger remainder = b1.remainder(b2);//取余
		}
		long takeTime = System.nanoTime() - beginTime;
		long endTime = TimeUnit.NANOSECONDS.toMillis(takeTime);
		if (endTime > 1000) {
			System.out.println("耗时" + endTime + "毫秒，begin的从1加到" + begin + "，大数" + bigInt + "是否为质数:" + result);
		}
		return result;
	}
	
	/**
	 * 分解质因数,分解质因子 
	 * Prime factor decomposition, Prime factorization 
	 * 比如把15这个数字分解成它的质因数的乘积，我们就会得到15=3x5这样一个唯一的答案。
	 * 比如462=2x3x7x11
	 */
	public static void primeFactorization (BigInteger bigInt) {
		//从最小的质因数2开始计算
		BigInteger beginInt = new BigInteger("1");
		long beginTime = System.nanoTime();
		while (true) {
			beginInt = beginInt.add(new BigInteger("1"));//从最小的质因数2开始计算
			if (beginInt.intValue() == 80000) {
				beginTime = System.nanoTime();
			}
			if (beginInt.mod(new BigInteger("10000")).compareTo(BigInteger.ZERO) == 0) {
				System.out.println("我还正在运行:" + beginInt);
			}
			if (beginInt.intValue() == 90000) {
				long takeTime = System.nanoTime() - beginTime;
				long endTime = TimeUnit.NANOSECONDS.toMillis(takeTime);
				if (endTime > 1000) {
					System.out.println("+++++++++++++++++++耗时" + endTime + "毫秒，beginInt的从1加到" + beginInt + "，大数" + bigInt);
				}
			}
			if (isPrimeNumber(beginInt)) {//是否为质数,这个方法没循环一次都会调用一次,一次就算耗费2毫秒,5万次循环都需要花费10万毫秒=100秒了。
				BigInteger res = bigInt.mod(beginInt);
				if (res.compareTo(BigInteger.ZERO) == 0) {
					BigInteger resOther = bigInt.divide(beginInt);
					System.out.println("分解质因数的结果为:" + bigInt + " = " + beginInt + " x " + resOther);
					break;
				}
			} else {
				continue;
			}
			long takeTime = System.nanoTime() - beginTime;
			long endTime = TimeUnit.NANOSECONDS.toMillis(takeTime);
			if (endTime > 60000) {
				System.out.println("==================耗时" + endTime + "毫秒，beginInt的从1加到" + beginInt + "，大数" + bigInt);
			}
		}
	}
	
	/**
	 * 百度百科:分解质因数，短除法分解质因数
	 * https://baike.baidu.com/item/%E5%88%86%E8%A7%A3%E8%B4%A8%E5%9B%A0%E6%95%B0/2253749?fr=aladdin#3_3
	 * 比如462=2x3x7x11，如30=2×3×5，36=2x2x3x3,360=2x2x2x3x3x5分解质因数只针对合数
	 * 此方法最多可分解包含50个质数的合数.  @author寒鸦LMC
	 * 合数:除1和它本身以外，还有其他因数。（因数个数至少有3个），最小的质数是2，最小的合数是4，一位数中，最大的质数是7，最大的合数是9.
	 * 每个合数都可以写成几个质数相乘的形式，其中每个质数都是这个合数的因数，把一个合数用质因数相乘的形式表示出来，叫做分解质因数。如30=2×3×5 。分解质因数只针对合数。
	 * 因数是指整数a除以整数b(b≠0) 的商正好是整数而没有余数，我们就说b是a的因数。
	 */
	public static void H6() {
		System.out.println("请输入所求整数:");
		Scanner sc = new Scanner(System.in);
		long n = sc.nextLong();
		long m = n;
		int flag = 0;//质数只能被1和它本身整数
		String[] str = new String[50];
		for (long i=2; i<=n; i++) {//分解质因数，注意是质因数，必须从最小的质数2开始除,利用短除法开始，分解质因数的短除法的除数必须用质数
			if (n%i == 0) {
				str[flag] = Long.toString(i);
				flag ++;//质数只能被1和它本身整数,所以如果flag大于2说明这个数字不是质数,如果flag小于2说明他就是质数
				n = n / i;
				i--;//假如i=2能除尽，就一直用2除,
			}
		}
		System.out.println("n最后肯定变成1了:" + n);
		
		if (flag < 2) {//质数只能被1和它本身整数,所以如果flag大于2说明这个数字不是质数,如果flag小于2说明他就是质数
			System.out.println(m + "为质数");
		} else {
			System.out.print(m + "=" + str[0]);
			for (int k=1; k<flag; k++) {
				System.out.print("*" + str[k]);
			}
			System.out.println();
			System.out.println(m + "共有" + flag + "个质因数");
		}
		sc.close();
	}
	
	/**
	 * 2019383291023828921092383=131*39581*701863*12804578845
	 * 2019383291023828921092383共有4个质因数
	 * 计算大数:2019383291023828921092383的质因数总共耗时2672652毫秒
	 * @param bigInt
	 */
	public static void H6Plus(BigInteger bigInt) {
		long beginTime = System.nanoTime();
		BigInteger temp = new BigInteger(bigInt.toString());
		int flag = 0;//质数只能被1和它本身整数
		String[] str = new String[50];
		BigInteger i;
		for (i=new BigInteger("2"); i.compareTo(bigInt)<=0; i=i.add(new BigInteger("1"))) {//分解质因数，注意是质因数，必须从最小的质数2开始除,利用短除法开始，分解质因数的短除法的除数必须用质数
			if (bigInt.mod(i).intValue() == 0) {
				str[flag] = i.toString();
				flag ++;//质数只能被1和它本身整数,所以如果flag大于2说明这个数字不是质数,如果flag小于2说明他就是质数
				bigInt = bigInt.divide(i);
				i = i.subtract(new BigInteger("1"));//假如i=2能除尽，就一直用2除,
			}
		}
		System.out.println("bigInt最后肯定变成1了:" + bigInt);
		
		if (flag < 2) {//质数只能被1和它本身整数,所以如果flag大于2说明这个数字不是质数,如果flag小于2说明他就是质数
			System.out.println(temp + "为质数");
		} else {
			System.out.print(temp + "=" + str[0]);
			for (int k=1; k<flag; k++) {
				System.out.print("*" + str[k]);
			}
			System.out.println();
			System.out.println(temp + "共有" + flag + "个质因数");
		}
		long takeTime = System.nanoTime() - beginTime;
		long endTime = TimeUnit.NANOSECONDS.toMillis(takeTime);
		System.out.println("计算大数:" + temp+ "的质因数总共耗时" + endTime + "毫秒");
	}
}

class Person {
	public final String name;
	
	public PublicKey publicKey;
	private PrivateKey privateKey;
	private byte[] secretKey;
	
	public Person(String name) {
		this.name = name;
	}
	
	//生成本地keyPair:生成私钥和公钥
	public void generateKeyPair() {
		try {
			KeyPairGenerator kpGen = KeyPairGenerator.getInstance("DH");
			kpGen.initialize(512);
			KeyPair kp = kpGen.generateKeyPair();
			this.privateKey = kp.getPrivate();
			this.publicKey = kp.getPublic();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	public void generateSecretKey(byte[] receivedPubKeyBytes) {
		try {
			//从byte[]恢复PublicKey
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(receivedPubKeyBytes);
			KeyFactory kf = KeyFactory.getInstance("DH");
			PublicKey receivedPublicKey = kf.generatePublic(keySpec);
			
			//生成本地密钥:
			KeyAgreement keyAgreement = KeyAgreement.getInstance("DH");
			keyAgreement.init(this.privateKey);//自己的PrivateKey
			keyAgreement.doPhase(receivedPublicKey, true);//对方的publicKey
			
			//生成的SecretKey密钥:
			this.secretKey = keyAgreement.generateSecret();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
	}
	
	public void printKeys() {
		System.out.printf("Name: %s\n", this.name);
		System.out.printf("Private key:%x\n", new BigInteger(1, this.privateKey.getEncoded()));
		System.out.printf("Public key:%x\n", new BigInteger(1, this.publicKey.getEncoded()));
		System.out.printf("Secret key:%x\n", new BigInteger(1, this.secretKey));
	}
}