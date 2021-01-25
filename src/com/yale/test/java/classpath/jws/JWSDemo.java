package com.yale.test.java.classpath.jws;
/*
 * 还有打War包的命令:
 * jar包和war包的介绍和区别 - 简书  这个写到java文档里面去
 * ---------------------------- Head First Java 第584页
 * 假如源代码(.java)存储在source目录下。在编译时动点手脚让输出(.class)产生在classes目录。有个编译器选项能够这么搞
 * 编译时加上-d(directory)选项。
 * javac -d ../classess MyApp.java
 * ../classess 的意思是要求编译器将编译后的class文件放在当前目录的上级目录下面的classes目录里面(前提是要确定"."配置在环境变量classpath中。)
 * MyApp.java 是要编译的java类
 * 使用-d选项,你就可以指定编译过的程序要放在那里,而不会默认放在同一个目录下.如果编译全部的.java文件
 * javac -d ../classes *.java 代表目前目录所有的源文件
 * 执行程序要切换到classes目录里面:cd MyProject/classess
 * java MyApp //从classes目录来运行
 * -------------------------
 * 把程序包进JAR,JAR就是Java ARchive。这种文件是个pkzip格式的文件,如果你很熟悉UNIX上的tar命令的话,你就知道jar这工具要怎么使用。
 * 创建可执行的JAR,秘诀在于创建出manifest文件,它会带有JAR的信息,告诉java虚拟机哪个类含有main()这个方法。
 * 1,确定所有的class文件都在classes目录下
 * 2,创建manifest.txt来描述哪个类带有main()方法,manifest.txt文件里面有这么一行
 * Main-class: MyApp
 * 在此行后面要有换行,否则有可能出错。将此文件放在classes目录下面
 * 3,执行jar工具来创建带有所有类以及manifest的JAR文件,首先要进入到classes文件
 * jar -cvmf manifest.txt app1.jar *.class
 * 或
 * jar -cvmf manifest.txt app1.jar MyApp.class
 * 大部分完全在本机的Java应用程序都是以可执行的JAR来部署的。
 * 执行JAR
 * java虚拟机能够从JAR中载入类,并调用该类的main()方法。事实上,整个应用程序都可以包在JAR中。一旦main()方法开始执行,
 * Java虚拟机就不会在乎类是从哪里来的,只要能够找到就行。其中一个来源就是classpath指定位置的所有JAR文件。如果看到某个JAR，
 * 则Java虚拟机就会在需要类的时候查询此JAR。
 * java -jar app1.jar 运行JAR文件,java虚拟机会检查JAR的manifest寻找入口,如果没有就会发生运行期间异常。
 * 根据操作系统如何动态设定,有可能直接双击JAR就可以开始执行,Windows与Mac OS X大致是这样。你可以通过点选JAR并要求OS以"Open with.."这一类的方式来打开。
 * 编译与执行包
 * 当类在包中,编译与执行都要有点技巧。主要的问题来自于编译器和Java虚拟机都得要能够找到你的类以及所用到的其他类。
 * javac -d ../classes com/headfirstjava/PackageExercise.java(完整包路径)
 * 编译com.headfirstjava这个包的所有.java文件
 * javac -d ../classes com/headfirstjava/*.java
 * 执行程序:
 * cd MyProject/classes 从classes目录执行
 * java com.headfirstjava.PackageExercise(必须指定完整的名称)
 * 创建可执行的JAR
 * 创建manifest.txt文件来描述哪个类带有main()必须使用完成的包路径
 * 在manifest.txt文件里面写入一行:
 * Main-Class: com.headfirstjava.PackageExercise
 * 然后把manifest.txt文件放到classes目录下
 * jar -cvmf manifest.txt packEx.jar com(只要从com目录开始就行,其下整个包的类都会被包进去JAR)
 * 你也可以把JAR的内容解压出来(就像unzip或untar一样)
 * 将JAR内容列出
 * jar -tf packEx.jar
 * -tf代表Table File,也就是列出文件列表
 * jar -xf packEx.jar
 * xf代表eXtract File,就像unzip一样,如果把packEx.jar解开,你会在当前目录之下看到META-JNF和com目录
 * jar工具会自动创建META-INF目录,META-INF代表META INFormation,jar工具会自动创建这个目录和MANIFEST.MF文件
 * 你的manifest.txt不会被带进JAR中,但它的内容会放进真正的manifest.mf中。
 * 问:如果用户没有按安装java,如何让用户安装java?
 * 答:理想情况是自定义安装程序和应用程序一并发布。有很多厂商提供简单或高等的工具来创建installer.有些安装程序能够检测用户的计算机是否
 * 有安装合适版本的Java,如果没有的话就会帮用户安装并设定Java。Installshield,InstallAnyWhere与DeployDirector等都提供
 * 安装程序工具。有些工具还能够制作安装光盘给各种主要的Java平台使用,因此一张CD ROM就能搞定。比如在Solaris上它就会安装Solaris版本的Java,
 * 在Windows上就会安装Windows版的Java。如果预算足够,你可以来上一段耗资千万的动画片头。
 * Java Web Start(JWS)
 * 运用Java Web Start(JWS),你的应用程序可以从浏览器上执行首次启动(从web来start,懂了吗?)但他运行起来几乎像是个独立的应用程序
 * 而不受浏览器的束缚。一旦它被下载到使用者的计算机之后(首次从浏览网址来下载),它就会被保存下来。
 * Java Web Start是个工作上如同浏览器plug-in的小java程序(就像ActiveX组件或用浏览器打开.pdf文件出现的Acrobat Reader)。
 * 这个程序被称为Java Web Startd的helper app,主要目的是用来管理下载,更新和启动JWS程序。
 * 当JWS下载你的程序(可执行的JAR)时,它会调用程序的main().然后用户就可以通过JWS helper app启动应用程序而不需要回到当初的网页。
 * 这还不是最棒的,JWS还能够检测服务器上应用程序局部(例如说某个类文件)的更新--在不需要用户介入的情况下,下载与整合更新过的程序。
 * 当然这还有点问题,比如用户要如何取得Java以及JWS。但这个问题也可以解决:从sun下载JWS。如果装了JWS但java版本不是最新的,
 * Java 2 Standard Edition也会被下载到用户计算机上。
 * 最棒的是这一切都很简单,你可以把JWS应用程序当作HTML网页或.jpg图文件一样的网络资源,设置一个链接到你的JWS应用程序上,然后就可以工作了。
 * 反正JWS应用程序就跟从网络上下载的可以执行JAR一样。
 * 总结:用户能通过点选网页上的某个链接来启动Java Web Start的应用程序。一旦程序下载后,它就能独立于浏览器之外来执行。事实上,Java Web Start应用程序只不过
 * 是通过网络来发布的应用程序而已。
 * Java Web Start的工作方式
 * 1,客户端点击某个网页上JWS应用程序的链接(.jnlp文件)<a href="MyApp.jnlp">Click</a>
 * 2,Web服务器收到请求发出.jnlp文件(不是JAR)给客户端的浏览器,.jnlp文件是个描述应用程序可执行JAR文件的XML文件。
 * 3,浏览器启动Java Web Start, JWS的helper app读取.jnlp文件,然后向服务器请求MyApp.jar
 * 4,Web服务器发送.jar文件
 * 5,JWS取得JAR并调用指定的main()来启动应用程序。然后用户就可以在离线的情况下通过JWS来启动应用程序。
 * .jnlp文件
 * 你需要.jnlp文件(Java Network Lanuch Protocol)来制作Java Web Start的应用程序。JWS会读取这个文件来寻找JAR并启动应用程序(调用JAR里面的main()).
 * .jnlp文件是个简约的XML文件,里面可以做许多设置,但至少会有下面几项:
 * <?xml version="1.0" encoding="utf-8"?>
 * codebase用来指定相关文件的起始目录,我们使用127.0.0.1来测试本机上的程序
 * href相对于codebase的位置路径,它也可以放在某个目录下
 * <jnlp spec="0.2 1.0" codebase="http://127.0.0.1/~kathy" href="MyApp.jnlp">
 * <information>
 * 这些tag都一定要加进去,不然程序可能会有问题,.information是给helper app用的,可供显示程序的信息
 * 		<title>kathy App</title>
 * 		<vendor>Wickedly Smart</vendor>
 * 		<homepage href="index.html"/>
 * 	    <description>Head First WebStart demo</description>
 *  	<icon href="kathys.gif"/>
 *  	<offline-allowed/>设定成离线时也可以执行
 * </information>
 * <resources>
 * 		<j2ee version="1.3+"/>指定需要1.3或之后版本的java
 * 		<jar href="MyApp.jar"/>可执行JAR的名称
 * </resources>
 * <application-desc main-class="HelloWebStart"/>就和manifest一样描述哪个类带有main()
 * </jnlp>
 * 创建于部署Java Web Start的步骤
 * 1,将程序制作成可执行的JAR
 * 2,编写.jnlp文件
 * 3,把.jnlp文件与JAR文件放到WEB服务器
 * 4,对web服务器设定新的mime类型application/x-java-jnlp-file,这会让Web服务器以正确的header送出.jnlp数据,如此才能让浏览器知道所接收的是什么。
 * 5,设定网页链接到.jnlp文件
 * <HTML>
 * 		<BODY>
 * 			<a href="MyApp2.jnlp">Launch My Application</a>
 * 		</BODY>
 * </HTML>
 * 问:Java Web Start与applet有什么不同?
 * 答:applet无法独立于浏览器之外。applet是网页的一部分而不是单独的.浏览器会使用Java的plug-in来执行applet.applet没有类似程度的自动更新等功能,
 * 且一定得从浏览器上面执行.对JWS应用程序而言,一旦从网站上面下载后,用户不必通过浏览器就可以离线执行程序。
 * 问:JWS有什么安全性的限制?
 * 答:JWS有包括用户硬盘的读写等好几项限制。但JWS自有一套API可操作特殊的对话框来打开和存储文件,因此应用程序可以在用户同意的情况下存取硬盘上特定受限区域的文件。
 * 要点:
 * JavaWebStart技术让你能够从网站来部署独立的客户端程序。
 * Java Web Start有个必须要安装在客户端的helper app(当然也需要Java)
 * 当浏览器从服务器上取得.jnlp文件时,浏览器就会启动JWS的helper app.
 * JWS的helper app会读取.jnlp来判断要从服务器上下载的可以执行JAR。
 * 取得JAR之后它就会调用.jnlp指定的main()
 */
public class JWSDemo {
	public static void main(String[] args) {

	}
}
