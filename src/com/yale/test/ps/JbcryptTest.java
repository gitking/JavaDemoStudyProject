package com.yale.test.ps;

import com.yale.test.ps.source.BCrypt;

/**
 * com.yale.test.ps.source.BCrypt
 * 是BCrypt的源码,我把它下载下来了。
 * com.yale.test.ps.source.TestBCrypt
 * 是BCrypt的官方测试类
 * 官网地址:https://www.mindrot.org/projects/jBCrypt/
 * jbcrypt.jar包下载地址(maven仓库):https://maven.aliyun.com/mvn/search
 * @author dell
 */
public class JbcryptTest {
	public static void main(String[] args) {
		/**
		 * Jenkins的密码保存路径为(最好用jenkins用户登录服务器):/data/jenkins/.jenkins/users/dbhea_6297604982755738865
		 * Jenkins采用Java加密工具jBCrypt，所以我们在生成密码的时候需要添加相关的jar包。
		 * 关于 bcrypt：
		 * 1、bcrypt是不可逆的加密算法，无法通过解密密文得到明文。
		 * 2、bcrypt和其他对称或非对称加密方式不同的是，不是直接解密得到明文，也不是二次加密比较密文，而是把明文和存储的密文一块运算得到另一个密文，如果这两个密文相同则验证成功。
		 * 综上，Jenkins专有用户数据库使用了jbcrypt加密，jbcrypt加密是不可逆的，而且对于同一个明文的加密结果一般不同
         * 版权声明：本文为CSDN博主「John的博客」的原创文章，遵循CC 4.0 BY-SA版权协议，转载请附上原文出处链接及本声明。
		 * 原文链接：https://blog.csdn.net/John_z1/article/details/88972490
         */
		
		String admin = BCrypt.hashpw("admin", BCrypt.gensalt());
        System.out.println("综上，Jenkins专有用户数据库使用了jbcrypt加密，jbcrypt加密是不可逆的，而且对于同一个明文的加密结果一般不同。加密：" + admin);
 
		String admin12 = BCrypt.hashpw("admin", BCrypt.gensalt(12));
        System.out.println("综上，Jenkins专有用户数据库使用了jbcrypt加密，jbcrypt加密是不可逆的，而且对于同一个明文的加密结果一般不同。加密：" + admin12);
 
        //解密
        if(BCrypt.checkpw("admin", admin)){
            System.out.println("is match");
        } else {
            System.out.println("is not match");
        }
        
    	String admin1 = BCrypt.hashpw("admin", BCrypt.gensalt());
        System.out.println("综上，Jenkins专有用户数据库使用了jbcrypt加密，jbcrypt加密是不可逆的，而且对于同一个明文的加密结果一般不同。加密：" + admin);
 
        /**
         * jenkins的jbcrypt解密逻辑为:
         * jenkins会在你注册时根据你输入的密码用checkpw("admin", admin1)生成一个加密后的hash值,并把这个hash值永远都保存下来。
         * 然后在你下次登录的时候,拿到你输入的的明文密码,然后再和你注册时输入的那个密码加密后的hash值。
         * 然后再进行checkpw("admin", admin1)验证成功才行
         */
        if(BCrypt.checkpw("admin", admin1)){
            System.out.println("is match");
        } else {
            System.out.println("is not match");
        }
        
		String test = BCrypt.hashpw("123456", BCrypt.gensalt());
        System.out.println("综上，Jenkins专有用户数据库使用了jbcrypt加密，jbcrypt加密是不可逆的，而且对于同一个明文的加密结果一般不同。加密：" + test);
	}
}
