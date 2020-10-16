package com.yale.test.net.server.mail;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.tomcat.util.http.fileupload.util.mime.MimeUtility;

import com.sun.mail.pop3.POP3SSLStore;

/*
 * 接收Email
 * 发送Email的过程我们在上一节已经讲过了，客户端总是通过SMTP协议把邮件发送给MTA。
 * 接收Email则相反，因为邮件最终到达收件人的MDA服务器，所以，接收邮件是收件人用自己的客户端把邮件从MDA服务器上抓取到本地的过程。
 * 接收邮件使用最广泛的协议是POP3：Post Office Protocol version 3，它也是一个建立在TCP连接之上的协议。POP3服务器的标准端口是110，如果整个会话需要加密，那么使用加密端口995。
 * 另一种接收邮件的协议是IMAP：Internet Mail Access Protocol，它使用标准端口143和加密端口993。
 * IMAP和POP3的主要区别是，IMAP协议在本地的所有操作都会自动同步到服务器上，并且，IMAP可以允许用户在邮件服务器的收件箱中创建文件夹。
 * JavaMail也提供了IMAP协议的支持。因为POP3和IMAP的使用方式非常类似，因此我们只介绍POP3的用法。
 * 使用POP3收取Email时，我们无需关心POP3协议底层，因为JavaMail提供了高层接口。首先需要连接到Store对象：
 * 一个Store对象表示整个邮箱的存储，要收取邮件，我们需要通过Store访问指定的Folder（文件夹），通常是INBOX表示收件箱：
 * 当我们获取到一个Message对象时，可以强制转型为MimeMessage，然后打印出邮件主题、发件人、收件人等信息：
 * 比较麻烦的是获取邮件的正文。一个MimeMessage对象也是一个Part对象，它可能只包含一个文本，也可能是一个Multipart对象，即由几个Part构成，因此，需要递归地解析出完整的正文：
 * 最后记得关闭Folder和Store：
 * folder.close(true); // 传入true表示删除操作会同步到服务器上（即删除服务器收件箱的邮件）
 * store.close();
 * 小结
 * 使用Java接收Email时，可以用POP3协议或IMAP协议。
 * 使用POP3协议时，需要用Maven引入JavaMail依赖，并确定POP3服务器的域名／端口／是否使用SSL等，然后，调用相关API接收Email。
 * 设置debug模式可以查看通信详细内容，便于排查错误。
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1319099948859426
 */
public class Pop3 {
	final String popHost;
	final String username;
	final String password;
	final boolean debug;
	
	public Pop3(String popHost, String username, String password) {
		this.popHost = popHost;
		this.username = username;
		this.password = password;
		this.debug = true;
	}
	
	public static void main(String[] args) throws MessagingException, IOException {
		/*
		 * QQ邮箱这里的密码应该填写QQ邮箱的授权码,不能填写QQ邮箱的登录密码,填QQ邮箱的登录密码会报如下错误,访问异常中的链接,就知道怎么获取授权码了.
		 * 可以去QQ邮箱的设置里面看QQ邮箱的pop服务器等相关信息
		 * Exception in thread "main" javax.mail.AuthenticationFailedException: 
		 * Please using authorized code to login. More information at http://service.mail.qq.com/cgi-bin/help?subtype=1&&id=28&&no=1001256
			at com.sun.mail.pop3.POP3Store.protocolConnect(POP3Store.java:215)
			at javax.mail.Service.connect(Service.java:366)
			at javax.mail.Service.connect(Service.java:246)
			at javax.mail.Service.connect(Service.java:195)
			at com.yale.test.net.server.mail.Pop3.createSSLStore(Pop3.java:110)
			at com.yale.test.net.server.mail.Pop3.main(Pop3.java:66)
		 */
		Pop3 pop = new Pop3("pop.qq.com", "937243987@qq.com", "你自己邮箱的QQ授权码");
		Folder folder = null;
		Store store = null;
		try {//使用POP3收取Email时，我们无需关心POP3协议底层，因为JavaMail提供了高层接口。首先需要连接到Store对象：
			store = pop.createSSLStore();//创建Store对象
			//一个Store对象表示整个邮箱的存储，要收取邮件，我们需要通过Store访问指定的Folder（文件夹），通常是INBOX表示收件箱：
			folder = store.getFolder("INBOX");
			folder.open(Folder.READ_WRITE);// 以读写方式打开:
			// 打印邮件总数/新邮件数量/未读数量/已删除数量:
			System.out.println("Total messages: " + folder.getMessageCount());
			System.out.println("New messages: " + folder.getNewMessageCount());
			System.out.println("Unread messages: " + folder.getUnreadMessageCount());
			System.out.println("Deleted messages: " + folder.getDeletedMessageCount());
			Message[] messages = folder.getMessages();// 获取每一封邮件:
			for (Message message : messages) {
				printMessage((MimeMessage)message); // 打印每一封邮件:
			}
		} finally {
			if (folder != null) {
				try {
					folder.close(true);//传入true表示删除操作会同步到服务器上（即删除服务器收件箱的邮件）
				} catch(MessagingException e) {
					e.printStackTrace();
				}
			}
			
			if (store != null) {
				try {
					store.close();
				} catch(MessagingException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public Store createSSLStore() throws MessagingException {
		Properties props = new Properties();
		props.setProperty("mail.store.protocol", "pop3");//
		props.setProperty("mail.pop3.port", "995");//主机端口号
		props.setProperty("mail.pop3.host", this.popHost);//POP3主机名
		
		//启动SSL：
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.socketFactory.port", "995");
		
		URLName url = new URLName("pop3", this.popHost, 995, "", this.username, this.password);
		Session session  = Session.getInstance(props, null);
		session.setDebug(this.debug);
		Store store = new POP3SSLStore(session, url);
		store.connect();
		return store;
	}
	
	/*
	 * 当我们获取到一个Message对象时，可以强制转型为MimeMessage，然后打印出邮件主题、发件人、收件人等信息：
	 */
	static void printMessage(MimeMessage msg) throws MessagingException, IOException {
		System.out.println("-----------------------");
		System.out.println("邮件主题Subject: " + MimeUtility.decodeText(msg.getSubject()));
		System.out.println("发件人From: " + getFrom(msg));
		System.out.println("To: " + getTo(msg));
		System.out.println("Sent: " + msg.getSentDate().toString());
		System.out.println("Seen: " + msg.getFlags().contains(Flags.Flag.SEEN));
		System.out.println("Priority: " + getPriority(msg));
		System.out.println("Size: " + msg.getSize() / 1024 + "kb");
		System.out.println("Body: " + getBody(msg) + "kb");
		System.out.println("-----------------------");
		System.out.println();
	}
	
	static String getFrom(MimeMessage msg) throws MessagingException, UnsupportedEncodingException {
		Address[] froms = msg.getFrom();
		return addressToString(froms[0]);
	}
	
	static String getTo(MimeMessage msg) throws MessagingException, UnsupportedEncodingException {
		Address[] tos = msg.getRecipients(RecipientType.TO);
		List<String> list = new ArrayList<String>();
		for (Address to: tos) {
			list.add(addressToString(to));
		}
		return String.join(", ", list);
	}
	
	static String addressToString(Address addr) throws UnsupportedEncodingException {
		InternetAddress address = (InternetAddress)addr;
		String personal = address.getPersonal();
		return personal == null ? address.getAddress() : (MimeUtility.decodeText(personal) + "<" + address.getAddress() + ">");
	}
	
	static String getPriority(MimeMessage msg) throws MessagingException {
		String priority = "Normal";
		String[] headers = msg.getHeader("X-Priority");
		if (headers != null) {
			String header = headers[0];
			if ("1".equals(header) || "high".equalsIgnoreCase(header)) {
				priority = "High";
			} else if ("5".equals(header) || "low".equalsIgnoreCase(header)) {
				priority = "Low";
			}
		}
		return priority;
	}
	
	/*
	 * 比较麻烦的是获取邮件的正文。一个MimeMessage对象也是一个Part对象，它可能只包含一个文本，也可能是一个Multipart对象，即由几个Part构成，因此，需要递归地解析出完整的正文：
	 */
	static String getBody(Part part) throws MessagingException, IOException {
		if (part.isMimeType("text/*")) {
			return part.getContent().toString();// Part是文本:
		} else if (part.isMimeType("multipart/*")){// Part是一个Multipart对象:
			Multipart multipart = (Multipart)part.getContent();
			for (int i=0;i<multipart.getCount(); i++) {// 循环解析每个子Part:
				BodyPart bodyPart = multipart.getBodyPart(i);
				String body = getBody(bodyPart);
				if(!body.isEmpty()) {
					return body;
				}
			}
		}
		return "";
	}
}
