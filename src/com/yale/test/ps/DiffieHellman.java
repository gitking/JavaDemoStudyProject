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
		bob.generateKeyPair();//各自生成KeyPair:
		
		Person alice = new Person("Alice");
		alice.generateKeyPair();//各自生成KeyPair:
		
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
	
	//生成本地keyPair:
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