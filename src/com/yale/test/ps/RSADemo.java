package com.yale.test.ps;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/*
 * 非对称加密算法
 * 从DH算法我们可以看到，公钥-私钥组成的密钥对是非常有用的加密方式，因为公钥是可以公开的，而私钥是完全保密的，由此奠定了非对称加密的基础。
 * 非对称加密就是加密和解密使用的不是相同的密钥：只有同一个公钥-私钥对才能正常加解密。
 * 因此，如果小明要加密一个文件发送给小红，他应该首先向小红索取她的公钥，然后，他用小红的公钥加密，把加密文件发送给小红，此文件只能由小红的私钥解开，因为小红的私钥在她自己手里，所以，除了小红，没有任何人能解开此文件。
 * 非对称加密的典型算法就是RSA算法，它是由Ron Rivest，Adi Shamir，Leonard Adleman这三个哥们一起发明的，所以用他们仨的姓的首字母缩写表示。
 * 非对称加密相比对称加密的显著优点在于，对称加密需要协商密钥，而非对称加密可以安全地公开各自的公钥，在N个人之间通信的时候：使用非对称加密只需要N个密钥对，每个人只管理自己的密钥对。而使用对称加密需要则需要N*(N-1)/2个密钥，因此每个人需要管理N-1个密钥，密钥管理难度大，而且非常容易泄漏。
 * 既然非对称加密这么好，那我们抛弃对称加密，完全使用非对称加密行不行？也不行。因为非对称加密的缺点就是运算速度非常慢，比对称加密要慢很多。
 * 所以，在实际应用的时候，非对称加密总是和对称加密一起使用。假设小明需要给小红需要传输加密文件，他俩首先交换了各自的公钥，然后：
 *  1.小明生成一个随机的AES口令，然后用小红的公钥通过RSA加密这个口令，并发给小红；
 *  2.小红用自己的RSA私钥解密得到AES口令；
 *  3.双方使用这个共享的AES口令用AES加密通信。
 * 可见非对称加密实际上应用在第一步，即加密“AES口令”。这也是我们在浏览器中常用的HTTPS协议的做法，即浏览器和服务器先通过RSA交换AES口令，接下来双方通信实际上采用的是速度较快的AES对称加密，而不是缓慢的RSA非对称加密。
 * Java标准库提供了RSA算法的实现，示例代码如下：
 * 以RSA算法为例，它的密钥有256/512/1024/2048/4096等不同的长度。长度越长，密码强度越大，当然计算速度也越慢。
 * 如果修改待加密的byte[]数据的大小，可以发现，使用512bit的RSA加密时，明文长度不能超过53字节，使用1024bit的RSA加密时，明文长度不能超过117字节，
 * 这也是为什么使用RSA的时候，总是配合AES一起使用，即用AES加密任意长度的明文，用RSA加密AES口令。
 * 此外，只使用非对称加密算法不能防止中间人攻击。
 * 非对称加密就是加密和解密使用的不是相同的密钥，只有同一个公钥-私钥对才能正常加解密；
 * 只使用非对称加密算法不能防止中间人攻击。
 * 同学笔记:
 * 1.mark 下，这节有个点还是很有收获的。使用非对称秘钥（可以减少密钥管理）生成一个公共的密钥（代替了密钥交换），然后利用此公共密钥做相应数据的对称加解密（提升性能）。
 * 2.因此，如果小明要寄一份机密文件给小红，他应该首先向小红要一把她的锁，然后，他用小红的锁把文件锁上，然后把上了锁的文件给小红，此文件只能由小红的钥匙解开，因为小红的钥匙在她自己手里。
 */
public class RSADemo {
	public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		//明文
		byte[] plain = "Hello, encrypt use RSA".getBytes("UTF-8");
		//创建公钥/私钥对:
		PersonRsa alice = new PersonRsa("Alice");
		//用Alice的公钥加密
		byte[] pk = alice.getPublicKey();
		System.out.println(String.format("public key:%x", new BigInteger(1, pk)));
		
		byte[] encrypted = alice.encrypt(plain);
		System.out.println(String.format("RSA加密后的字符串encrypted: %x", new BigInteger(1, encrypted)));
		
		//用Alice的私钥解密
		byte[] sk = alice.getPrivateKey();
		System.out.println(String.format("private key:%x", new BigInteger(1, sk)));
		
		byte[] decrypted = alice.decrypt(encrypted);
		System.out.println("RSA解密后的字符串:" + new String(decrypted, "UTF-8"));
		
		
		try {
			/*
			 * RSA的公钥和私钥都可以通过getEncoded()方法获得以byte[]表示的二进制数据，并根据需要保存到文件中。要从byte[]数组恢复公钥或私钥，可以这么写：
			 */
			byte[] pkData = new byte[10];//这里从文件里面读取数据
			byte[] skData = new byte[10];//这里从文件里面读取数据
			KeyFactory kf = KeyFactory.getInstance("RSA");
			
			//恢复公钥
			X509EncodedKeySpec pkSpec = new X509EncodedKeySpec(pkData);
			PublicKey pkHf = kf.generatePublic(pkSpec);
			
			//恢复私钥
			PKCS8EncodedKeySpec skPec = new PKCS8EncodedKeySpec(skData);
			PrivateKey skHf = kf.generatePrivate(skPec);
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
	}
}

class PersonRsa {
	String name;
	PrivateKey sk;
	PublicKey pk;
	 
	public PersonRsa(String name) throws NoSuchAlgorithmException {
		this.name = name;
		//生成公钥/私钥对
		KeyPairGenerator kpGen = KeyPairGenerator.getInstance("RSA");
		kpGen.initialize(1024);
		KeyPair kp = kpGen.generateKeyPair();
		this.sk = kp.getPrivate();
		this.pk = kp.getPublic();
	}
	
	//把私钥导出为字节
	public byte[] getPrivateKey() {
		return this.sk.getEncoded();
	}
	
	//把公钥导出为字节
	public byte[] getPublicKey() {
		return this.pk.getEncoded();
	}
	
	/*
	 * RSA用公钥加密
	 */
	public byte[] encrypt(byte[] message) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, this.pk);
		return cipher.doFinal(message);
	}
	
	/*
	 * RSA用私钥解密
	 */
	public byte[] decrypt(byte[] input) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, this.sk);
		return cipher.doFinal(input);
	}
	
}
