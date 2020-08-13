package com.yale.test.ps;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/*
 * 口令加密算法
 * 上一节我们讲的AES加密，细心的童鞋可能会发现，密钥长度是固定的128/192/256位，而不是我们用WinZip/WinRAR那样，随便输入几位都可以。
 * 这是因为对称加密算法决定了口令必须是固定长度，然后对明文进行分块加密。又因为安全需求，口令长度往往都是128位以上，即至少16个字符。
 * 但是我们平时使用的加密软件，输入6位、8位都可以，难道加密方式不一样？
 * 实际上用户输入的口令并不能直接作为AES的密钥进行加密（除非长度恰好是128/192/256位），并且用户输入的口令一般都有规律，安全性远远不如安全随机数产生的随机口令。
 * 因此，用户输入的口令，通常还需要使用PBE算法，采用随机数杂凑计算出真正的密钥，再进行加密。
 * PBE就是Password Based Encryption的缩写，它的作用如下：
 * key = generate(userPassword, secureRandomPassword);
 * PBE的作用就是把用户输入的口令和一个安全随机的口令采用杂凑后计算出真正的密钥。以AES密钥为例，我们让用户输入一个口令，然后生成一个随机数，通过PBE算法计算出真正的AES口令，再进行加密，代码如下：
 * 理解原理，PBE假定用户的口令很简单不安全，所以需要一个高强度密码和用户口令混在一起加密
 */
public class AesPbeDemo {
	public static void main(String[] args) {
		//PBE的作用就是把用户输入的口令和一个安全随机的口令采用杂凑后计算出真正的密钥。以AES密钥为例，我们让用户输入一个口令，然后生成一个随机数，通过PBE算法计算出真正的AES口令，再进行加密，代码如下：
		Security.addProvider(new BouncyCastleProvider());//把BouncyCastle作为Provider添加到java.security
		String message = "Hello,world!";//明文相当于文件
		String password = "hello12345";//加密口令,相当于你输入的密码
		
		try {
			byte[] salt = SecureRandom.getInstanceStrong().generateSeed(16);//16byte随机Salt,密钥
			System.out.printf("salt:%032x\n", new BigInteger(1, salt));
			
			byte[] data = message.getBytes("UTF-8");
			byte[] encrypted = encrypt(password, salt, data);//加密
			System.out.println("加密encrypted:" + Base64.getEncoder().encodeToString(encrypted));
			
			//解密
			byte[] decrypted = decrypt(password, salt, encrypted);
			System.out.println("解密decrypted:" + new String(decrypted, "UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * PBE加密
	 * 使用PBE时，我们还需要引入BouncyCastle，并指定算法是PBEwithSHA1and128bitAES-CBC-BC。观察代码，实际上真正的AES密钥是调用Cipher的init()方法时同时传入SecretKey和PBEParameterSpec实现的。
	 * 在创建PBEParameterSpec的时候，我们还指定了循环次数1000，循环次数越多，暴力破解需要的计算量就越大。
	 * 如果我们把salt和循环次数固定，就得到了一个通用的“口令”加密软件。如果我们把随机生成的salt存储在U盘，就得到了一个“口令”加USB Key的加密软件，它的好处在于，即使用户使用了一个非常弱的口令，
	 * 没有USB Key仍然无法解密，因为USB Key存储的随机数密钥安全性非常高。 
	 * 这个其实主要用于加密文件和解密文件,比如你把一个文件加密了,但是这个加密的文件被黑客拿走了,并且黑客知道你的密码(口令),但是黑客不知道你的salt,黑客仍然打不开这个加密的文件
	 * 我自己用的时候把salt拿出来就行了。
	 * 小结
	 * PBE算法通过用户口令和安全的随机salt计算出Key，然后再进行加密；
	 * Key通过口令和安全的随机salt计算得出，大大提高了安全性；
	 * PBE算法内部使用的仍然是标准对称加密算法（例如AES）。
	 * 理解原理，PBE假定用户的口令很简单不安全，所以需要一个高强度密码和用户口令混在一起加密
	 */
	public static byte[] encrypt(String password, byte[] salt, byte[] input) 
	throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
		PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());//口令
		SecretKeyFactory skeyFactory = SecretKeyFactory.getInstance("PBEwithSHA1and128bitAES-CBC-BC");
		SecretKey skey = skeyFactory.generateSecret(keySpec);
		PBEParameterSpec pbeps = new PBEParameterSpec(salt, 1000);
		Cipher cipher = Cipher.getInstance("PBEwithSHA1and128bitAES-CBC-BC");
		cipher.init(Cipher.ENCRYPT_MODE, skey, pbeps);
		return cipher.doFinal(input);//加密明文
	}
	
	/**
	 * 解密
	 * @param password 口令
	 * @param salt 
	 * @param input 加密后的明文
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static byte[] decrypt(String password, byte[] salt, byte[] input)
	throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
		SecretKeyFactory skeyFactory = SecretKeyFactory.getInstance("PBEwithSHA1and128bitAES-CBC-BC");
		SecretKey skey = skeyFactory.generateSecret(keySpec);
		PBEParameterSpec pbeps = new PBEParameterSpec(salt, 1000);
		Cipher cipher = Cipher.getInstance("PBEwithSHA1and128bitAES-CBC-BC");
		cipher.init(Cipher.DECRYPT_MODE, skey, pbeps);
		return cipher.doFinal(input);//解密
	}
}
