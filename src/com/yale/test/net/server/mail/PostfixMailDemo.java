package com.yale.test.net.server.mail;

/**
 * 3分钟安装配置Postfix邮件服务器
 * Linux邮件服务器通常使用Sendmail，在网上Google了Sendmail的教程后，我决定知难而退，改用Postfix。
 * Postfix是用来替代Sendmail的，它的配置文件比Sendmail简单得多，配置相当容易。
 * 在配置邮件服务器之前，先解释几个概念。
 * 我们通常使用Email都很容易，但是Internet的邮件系统是通过几个复杂的部分连接而成的，
 * 对于最终用户而言，我们熟悉的Outlook，Foxmail等都是用来收信和发信的，称之为MUA：Mail User Agent，邮件用户代理。
 * MUA并非直接将邮件发送至收件人手中，而是通过MTA：Mail Transfer Agent，邮件传输代理代为传递，Sendmail和Postfix就是扮演MTA的角色。
 * 一封邮件从MUA发出后，可能通过一个或多个MTA传递，最终到达MDA：Mail Delivery Agent，邮件投递代理，邮件到达MDA后，就存放在某个文件或特殊的数据库里，我们将这个长期保存邮件的地方称之为邮箱。
 * 一旦邮件到达邮箱，就原地不动了，等用户再通过MUA将其取走，就是用Outlook，Foxmail等软件收信的过程。
 * 所以一封邮件的流程是：发件人：MUA --发送--> MTA -> 若干个MTA... -> MTA -> MDA <--收取-- MUA：收件人
 * MUA到MTA，以及MTA到MTA之间使用的协议就是SMTP协议，而收邮件时，MUA到MDA之间使用的协议最常用的是POP3或IMAP。
 * 需要注意的是，专业邮件服务商都有大量的机器来为用户服务，所以通常MTA和MDA并不是同一台服务器，因此，在Outlook等软件里，我们需要分别填写SMTP发送服务器的地址和POP3接收服务器的地址。
 * 下面开始用Postfix搭建Linux下的邮件服务器。目标服务器是RedHat Enterprise Linux 4，首先需要停止Sendmail：
 * # /etc/init.d/sendmail stop
 * 并从启动组里移除：
 * # chkconfig sendmail off
 * 然后，通过rpm包安装Postfix：
 * # rpm -Uvh postfix-2.x.x.xxx.rpm
 * Postfix只有一个/etc/postfix/main.cf需要修改，其他配置文件可以直接使用默认设置。
 * 第一个需要修改的参数是myhostname，指向真正的域名，例如：
 * myhostname = mail.example.com
 * mydomain参数指向根域：
 * mydomain = example.com
 * myorigin和mydestination都可以指向mydomain：
 * Postfix默认只监听本地地址，如果要与外界通信，就需要监听网卡的所有IP：
 * inet_interfaces = all
 * Postfix默认将子网内的机器设置为可信任机器，如果只信任本机，就设置为host：
 * mynetworks_style = host
 * 配置哪些地址的邮件能够被Postfix转发，当然是mydomain的才能转发，否则其他人都可以用这台邮件服务器转发垃圾邮件了：
 * relay_domains = $mydomain
 * 现在，Postfix已经基本配置完成，我们需要对邮件的发送进行控制：
 * 	1.对于外域到本域的邮件，必须接收，否则，收不到任何来自外部的邮件；
 * 	2.对于本域到外域的邮件，只允许从本机发出，否则，其他人通过伪造本域地址就可以向外域发信；
 * 	3.对于外域到外域的邮件，直接拒绝，否则我们的邮件服务器就是Open Relay，将被视为垃圾邮件服务器。
 * 先设置发件人的规则：
 * smtpd_sender_restrictions = permit_mynetworks, check_sender_access hash:/etc/postfix/sender_access, permit
 * 以上规则先判断是否是本域地址，如果是，允许，然后再从sender_access文件里检查发件人是否存在，拒绝存在的发件人，最后允许其他发件人。
 * 然后设置收件人规则：
 * smtpd_recipient_restrictions = permit_mynetworks, check_recipient_access hash:/etc/postfix/recipient_access, reject
 * 以上规则先判断是否是本域地址，如果是，允许，然后再从recipient_access文件里检查收件人是否存在，允许存在的收件人，最后拒绝其他收件人。
 * /etc/postfix/sender_access的内容：
 * example.com REJECT
 * 目的是防止其他用户从外部以xxx@example.com身份发送邮件，但登录到本机再发送则不受影响，因为第一条规则permit_mynetworks允许本机登录用户发送邮件。
 * /etc/postfix/recipient_access的内容：
 * postmaster@example.com OK
 * webmaster@example.com OK
 * 因此，外域只能发送给以上两个Email地址，其他任何地址都将被拒绝。但本机到本机发送不受影响。
 * 最后用postmap生成hash格式的文件：
 * # postmap sender_access
 * # postmap recipient_access
 * 启动Postfix：
 * # /etc/init.d/postfix start
 * 设置到启动组里：
 * # chkconfig postfix on
 * 现在就可以通过telnet来测试了：（红色是输入的命令）
 * 220 mail.example.com ESMTP Postfix
 * helo localhost(命令)
 * 250 mail.example.com
 * mail from:test@gmail.com(命令)
 * 250 Ok
 * rcpt to:webmaster@example.com(命令)
 * 250 Ok
 * data(命令)
 * 354 End data with<CR><LF>.<CR><LF>
 * hello!!!!!!(命令)
 * .
 * 250 Ok: queued as D68E41407D0
 * mail from:test@gmail.com(命令)
 * 250 Ok
 * rcpt to:haha@example.com(命令)
 * 554<haha@example.com>: Recipient address rejected: Access denied
 * quit(命令)
 * 221 Bye
 * 如果配置了SMTP认证，就可以让用户远程发送时能通过认证后再发送邮件，目前还没有配置，准备继续研究后再配置。需要注意的是，配置SMTP认证后，设置规则应该是：
 * 	1.外域->本域：不需认证，允许，否则将接受不到任何外部邮件；
 * 	2.本域->外域：需要认证，否则拒绝。
 * 因为我们作为发送服务器的MTA和转发的MTA实际上是由一个MTA完成的，所以需要以上规则。
 * 对于大型邮件服务商，发送服务器的MTA和转发的MTA是分别部署的，例如，sina的发送服务器是smtp.sina.com，需要经过用户认证，而转发服务器是mx???.sina.com，不需要认证，否则无法转发邮件。
 * 最后不要忘了在DNS的MX记录中将域名mail.example.com添上。
 * https://www.liaoxuefeng.com/article/895886450140288
 * @author issuser
 */
public class PostfixMailDemo {
}
