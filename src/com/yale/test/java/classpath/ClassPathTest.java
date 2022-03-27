package com.yale.test.java.classpath;
/*
 * javac -d /myWebApplication/WEB-INF/classes myServlet.java
 * 如果我们需要在src目录下执行javac命令：
 * javac -d ../bin ming/Person.java hong/Person.java mr/jun/Arrays.java
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1260466914339296
 * 
 * 在Java中，我们经常听到classpath这个东西。网上有很多关于“如何设置classpath”的文章，但大部分设置都不靠谱。
到底什么是classpath？
classpath是JVM用到的一个环境变量，它用来指示JVM如何搜索class。
因为Java是编译型语言，源码文件是.java，而编译后的.class文件才是真正可以被JVM执行的字节码。因此，JVM需要知道，如果要加载一个abc.xyz.Hello的类，应该去哪搜索对应的Hello.class文件。
所以，classpath就是一组目录的集合，它设置的搜索路径与操作系统相关。例如，在Windows系统上，用;分隔，带空格的目录用""括起来，可能长这样：
C:\work\project1\bin;C:\shared;"D:\My Documents\project1\bin"
在Linux系统上，用:分隔，可能长这样：
/usr/shared:/usr/local/bin:/home/liaoxuefeng/bin
现在我们假设classpath是.;C:\work\project1\bin;C:\shared，当JVM在加载abc.xyz.Hello这个类时，会依次查找：
    <当前目录>\abc\xyz\Hello.class
    C:\work\project1\bin\abc\xyz\Hello.class
    C:\shared\abc\xyz\Hello.class
注意到.代表当前目录。如果JVM在某个路径下找到了对应的class文件，就不再往后继续搜索。如果所有路径下都没有找到，就报错。
classpath的设定方法有两种：
在系统环境变量中设置classpath环境变量，不推荐；
在启动JVM时设置classpath变量，推荐。
我们强烈不推荐在系统环境变量中设置classpath，那样会污染整个系统环境。在启动JVM时设置classpath才是推荐的做法。实际上就是给java命令传入-classpath或-cp参数：
java -classpath .;C:\work\project1\bin;C:\shared abc.xyz.Hello
或者使用-cp的简写：
java -cp .;C:\work\project1\bin;C:\shared abc.xyz.Hello
没有设置系统环境变量，也没有传入-cp参数，那么JVM默认的classpath为.，即当前目录：
java abc.xyz.Hello
上述命令告诉JVM只在当前目录搜索Hello.class。
在IDE中运行Java程序，IDE自动传入的-cp参数是当前工程的bin目录和引入的jar包。
通常，我们在自己编写的class中，会引用Java核心库的class，例如，String、ArrayList等。这些class应该上哪去找？
有很多“如何设置classpath”的文章会告诉你把JVM自带的rt.jar放入classpath，但事实上，根本不需要告诉JVM如何去Java核心库查找class，JVM怎么可能笨到连自己的核心库在哪都不知道？
不要把任何Java核心库添加到classpath中！JVM根本不依赖classpath加载核心库！
更好的做法是，不要设置classpath！默认的当前目录.对于绝大多数情况都够用了。
jar包
如果有很多.class文件，散落在各层目录中，肯定不便于管理。如果能把目录打一个包，变成一个文件，就方便多了。
jar包就是用来干这个事的，它可以把package组织的目录层级，以及各个目录下的所有文件（包括.class文件和其他文件）都打成一个jar文件，这样一来，无论是备份，还是发给客户，就简单多了。
jar包实际上就是一个zip格式的压缩文件，而jar包相当于目录。如果我们要执行一个jar包的class，就可以把jar包放到classpath中：
java -cp ./hello.jar abc.xyz.Hello
这样JVM会自动在hello.jar文件里去搜索某个类。
那么问题来了：如何创建jar包？
因为jar包就是zip包，所以，直接在资源管理器中，找到正确的目录，点击右键，在弹出的快捷菜单中选择“发送到”，“压缩(zipped)文件夹”，就制作了一个zip文件。然后，把后缀从.zip改为.jar，一个jar包就创建成功。
假设编译输出的目录结构是这样：
见jar.png
package_sample
└─ bin
   ├─ hong
   │  └─ Person.class
   │  ming
   │  └─ Person.class
   └─ mr
      └─ jun
         └─ Arrays.class
这里需要特别注意的是，jar包里的第一层目录，不能是bin，而应该是hong、ming、mr。如果在Windows的资源管理器中看，应该长这样：
说明打包打得有问题，JVM仍然无法从jar包中查找正确的class，原因是hong.Person必须按hong/Person.class存放，而不是bin/hong/Person.class。
jar包还可以包含一个特殊的/META-INF/MANIFEST.MF文件，MANIFEST.MF是纯文本，可以指定Main-Class和其它信息。JVM会自动读取这个MANIFEST.MF文件，如果存在Main-Class，我们就不必在命令行指定启动的类名，而是用更方便的命令：
java -jar hello.jar
jar包还可以包含其它jar包，这个时候，就需要在MANIFEST.MF文件里配置classpath了。
在大型项目中，不可能手动编写MANIFEST.MF文件，再手动创建zip包。Java社区提供了大量的开源构建工具，例如Maven，可以非常方便地创建jar包。
打jar包的命令 jar -cvf test.jar javalist.txt 
javalist.txt文件放你要要打成jar包的class文件
还有打War包的命令:
jar包和war包的介绍和区别 - 简书  这个写到java文档里面去
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
 * 
 * 然后用javac编译Main.java，编译的时候要指定classpath，不然编译器找不到我们引用的org.apache.commons.logging包。编译命令如下：
 * javac -cp commons-logging-1.2.jar Main.java
 * 
 * java -cp .:commons-logging-1.2.jar Main
 * 注意到传入的classpath有两部分：一个是.，一个是commons-logging-1.2.jar，用;分割。.表示当前目录，如果没有这个.，JVM不会在当前目录搜索Main.class，就会报错。
 * 如果在Linux或macOS下运行，注意classpath的分隔符不是;，而是:：
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1264738932870688
 */
public class ClassPathTest {
	public static void main(String[] args) {
		/**
		 * CLASSPATH就是JVM去哪找你的class文件
		 * CLASSPATH的值是.就代表去当前目录中找
		 * CLASSPATH的值也可以设置为别的路径:d:\demo这种路径,那JVM就去这找路径
		 */
	}
}
