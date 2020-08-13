package com.yale.test.ps;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

/*
 * 签名算法
 * 我们使用非对称加密算法的时候，对于一个公钥-私钥对，通常是用公钥加密，私钥解密。
 * 如果使用私钥加密，公钥解密是否可行呢？实际上是完全可行的。
 * 不过我们再仔细想一想，私钥是保密的，而公钥是公开的，用私钥加密，那相当于所有人都可以用公钥解密。这个加密有什么意义？
 * 这个加密的意义在于，如果小明用自己的私钥加密了一条消息，比如小明喜欢小红，然后他公开了加密消息，由于任何人都可以用小明的公钥解密，从而使得任何人都可以确认小明喜欢小红这条消息肯定是小明发出的，其他人不能伪造这个消息，小明也不能抵赖这条消息不是自己写的。
 * 因此，私钥加密得到的密文实际上就是数字签名，要验证这个签名是否正确，只能用私钥持有者的公钥进行解密验证。使用数字签名的目的是为了确认某个信息确实是由某个发送方发送的，任何人都不可能伪造消息，并且，发送方也不能抵赖。
 * 在实际应用的时候，签名实际上并不是针对原始消息，而是针对原始消息的哈希进行签名，即：
 * signature = encrypt(privateKey, sha256(message))
 * 对签名进行验证实际上就是用公钥解密
 * hash = decrypt(publicKey, signature)
 * 然后把解密后的哈希与原始消息的哈希进行对比。
 * 因为用户总是使用自己的私钥进行签名，所以，私钥就相当于用户身份。而公钥用来给外部验证用户身份。
 * 常用数字签名算法有：1,MD5withRSA 2,SHA1withRSA 3,SHA256withRSA 它们实际上就是指定某种哈希算法进行RSA签名的方式。
 * 使用其他公钥，或者验证签名的时候修改原始信息，都无法验证成功。
 * DSA签名
 * 除了RSA可以签名外，还可以使用DSA算法进行签名。DSA是Digital Signature Algorithm的缩写，它使用ElGamal数字签名算法。
 * DSA只能配合SHA使用，常用的算法有：
 *  SHA1withDSA
 *  SHA256withDSA
 *  SHA512withDSA
 * 和RSA数字签名相比，DSA的优点是更快。
 * ECDSA签名
 * 椭圆曲线签名算法ECDSA：Elliptic Curve Digital Signature Algorithm也是一种常用的签名算法，它的特点是可以从私钥推出公钥。
 * 比特币的签名算法就采用了ECDSA算法，使用标准椭圆曲线secp256k1。BouncyCastle提供了ECDSA的完整实现。
 * 小结
 * 数字签名就是用发送方的私钥对原始数据进行签名，只有用发送方公钥才能通过签名验证。
 * 数字签名用于：
    	防止伪造；
    	防止抵赖；
    	检测篡改。
 * 常用的数字签名算法包括：MD5withRSA／SHA1withRSA／SHA256withRSA／SHA1withDSA／SHA256withDSA／SHA512withDSA／ECDSA等。
 */
public class SignDemo {
	public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
		//生成RSA公钥和私钥:
		KeyPairGenerator kpGen = KeyPairGenerator.getInstance("RSA");
		kpGen.initialize(1024);
		KeyPair kp = kpGen.generateKeyPair();
		PrivateKey sk = kp.getPrivate();//私钥
		PublicKey pk = kp.getPublic();//公钥
		
		//待签名的消息
		byte[] message = "Hello, I'm Bob!".getBytes(StandardCharsets.UTF_8);
		
		//用私钥签名
		Signature sign = Signature.getInstance("SHA1withRSA");
		sign.initSign(sk);
		sign.update(message);
		byte[] signed = sign.sign();
		System.out.println(String.format("私钥签名后的信息:signature:%x", new BigInteger(1, signed)));
		
		//用公钥验证
		Signature v = Signature.getInstance("SHA1withRSA");
		v.initVerify(pk);
		v.update(message);
		boolean valid = v.verify(signed);
		System.out.println("公钥验证是否通过?valid:" + valid);
	}
}
