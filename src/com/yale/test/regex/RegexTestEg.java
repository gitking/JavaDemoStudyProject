package com.yale.test.regex;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 正则表达式练习
 * https://www.cnblogs.com/deerchao/archive/2006/08/24/zhengzhe30fengzhongjiaocheng.html
 * 【强制】在使用正则表达式时，利用好其预编译功能，可以有效加快正则匹配速度。 
 * 说明：不要在方法体内定义：Pattern pattern = Pattern.compile(“规则”);《阿里巴巴Java开发手册（泰山版）.
 * 正则表达式的匹配规则是从左到右按规则匹配。我们首先来看如何使用正则表达式来做精确匹配。
 * 如果正则表达式有特殊字符，那就需要用\转义。例如，正则表达式a\&c，其中\&是用来匹配特殊字符&的，它能精确匹配字符串"a&c"，但不能匹配"ac"、"a-c"、"a&&c"等。
 * 要注意正则表达式在Java代码中也是一个字符串，所以，对于正则表达式a\&c来说，对应的Java字符串是"a\\&c"，因为\也是Java字符串的转义字符，两个\\实际上表示的是一个\：
 * 如果想匹配非ASCII字符，例如中文，那就用\\u####的十六进制表示，例如：a\u548cc匹配字符串"a和c"，中文字符和的Unicode编码是548c。
 * 精确匹配实际上用处不大，因为我们直接用String.equals()就可以做到。大多数情况下，我们想要的匹配规则更多的是模糊匹配。我们可以用.匹配一个任意字符。
 * @author lenovo
 */
public class RegexTestEg {
	public static void main(String[] args) throws ParseException {
		/*
		 * https://www.cnblogs.com/deerchao/archive/2006/08/24/zhengzhe30fengzhongjiaocheng.html
		 * 字符类
		 * 要想查找数字，字母或数字，空白是很简单的，因为已经有了对应这些字符集合的元字符，但是如果你想匹配没有预定义元字符的字符集合(比如元音字母a,e,i,o,u),应该怎么办？
		 * 很简单，你只需要在方括号里列出它们就行了，像[aeiou]就匹配任何一个英文元音字母，[.?!]匹配标点符号(.或?或!)。
		 * 下面是一个更复杂的表达式：\(?0\d{2}[) -]?\d{8}。“(”和“)”也是元字符，后面的分组节里会提到，所以在这里需要使用转义。
		 * 这个表达式可以匹配几种格式的电话号码，像(010)88886666，或022-22334455，或02912345678等。
		 * 我们对它进行一些分析吧：首先是一个转义字符\(,它能出现0次或1次(?),然后是一个0，后面跟着2个数字(\d{2})，然后是)或-或空格中的一个，它出现1次或不出现(?)，
		 * 最后是8个数字(\d{8})。
		 * 分枝条件
		 * 不幸的是，刚才那个表达式也能匹配010)12345678或(022-87654321这样的“不正确”的格式。要解决这个问题，我们需要用到分枝条件。正则表达式里的分枝条件指的是有几种规则，
		 * 如果满足其中任意一种规则都应该当成匹配，具体方法是用|把不同的规则分隔开。听不明白？没关系，看例子：
		 * 0\d{2}-\d{8}|0\d{3}-\d{7}这个表达式能匹配两种以连字号分隔的电话号码：一种是三位区号，8位本地号(如010-12345678)，一种是4位区号，7位本地号(0376-2233445)。
		 * \(0\d{2}\)[- ]?\d{8}|0\d{2}[- ]?\d{8}这个表达式匹配3位区号的电话号码，其中区号可以用小括号括起来，也可以不用，区号与本地号间可以用连字号或空格间隔，也可以没有间隔。你可以试试用分枝条件把这个表达式扩展成也支持4位区号的。
		 * \d{5}-\d{4}|\d{5}这个表达式用于匹配美国的邮政编码。美国邮编的规则是5位数字，或者用连字号间隔的9位数字。之所以要给出这个例子是因为它能说明一个问题：使用分枝条件时，
		 * 要注意各个条件的顺序。如果你把它改成\d{5}|\d{5}-\d{4}的话，那么就只会匹配5位的邮编(以及9位邮编的前5位)。原因是匹配分枝条件时，将会从左到右地测试每个条件，如果满足了某个分枝的话，就不会去再管其它的条件了。
		 * 分组
		 * 我们已经提到了怎么重复单个字符（直接在字符后面加上限定符就行了）；但如果想要重复多个字符又该怎么办？你可以用小括号来指定子表达式(也叫做分组)，然后你就可以指定这个子表达式的重复次数了，你也可以对子表达式进行其它一些操作(后面会有介绍)。
		 * (\d{1,3}\.){3}\d{1,3}是一个简单的IP地址匹配表达式。要理解这个表达式，请按下列顺序分析它：\d{1,3}匹配1到3位的数字，(\d{1,3}\.){3}匹配三位数字加上一个英文句号(这个整体也就是这个分组)重复3次，最后再加上一个一到三位的数字(\d{1,3})。
		 * 不幸的是，它也将匹配256.300.888.999这种不可能存在的IP地址。如果能使用算术比较的话，或许能简单地解决这个问题，但是正则表达式中并不提供关于数学的任何功能，所以只能使用冗长的分组，选择，字符类来描述一个正确的IP地址：
		 * ((2[0-4]\d|25[0-5]|[01]?\d\d?)\.){3}(2[0-4]\d|25[0-5]|[01]?\d\d?)。理解这个表达式的关键是理解2[0-4]\d|25[0-5]|[01]?\d\d?，这里我就不细说了，你自己应该能分析得出来它的意义。
		 * 反义
		 * 有时需要查找不属于某个能简单定义的字符类的字符。比如想查找除了数字以外，其它任意字符都行的情况，这时需要用到反义：
		 * \B 匹配不是单词开头或结束的位置
		 * [^x] 匹配除了x以外的任意字符
		 * [^aeiou] 匹配除了aeiou这几个字母以外的任意字符
		 * 例子：\S+匹配不包含空白符的字符串。
		 * <a[^>]+>匹配用尖括号括起来的以a开头的字符串。
		 * 后向引用
		 * 使用小括号指定一个子表达式后，匹配这个子表达式的文本(也就是此分组捕获的内容)可以在表达式或其它程序中作进一步的处理。
		 * 默认情况下，每个分组会自动拥有一个组号，规则是：从左向右，以分组的左括号为标志，第一个出现的分组的组号为1，第二个为2，以此类推。
		 * 后向引用用于重复搜索前面某个分组匹配的文本。例如，\1代表分组1匹配的文本。难以理解？请看示例：
		 * \b(\w+)\b\s+\1\b可以用来匹配重复的单词，像go go, 或者kitty kitty。这个表达式首先是一个单词，也就是单词开始处和结束处之间的多于一个的字母或数字(\b(\w+)\b)，
		 * 这个单词会被捕获到编号为1的分组中，然后是1个或几个空白符(\s+)，最后是分组1中捕获的内容（也就是前面匹配的那个单词）(\1)。
		 * 呃……其实,组号分配还不像我刚说得那么简单：1、分组0对应整个正则表达式2、实际上组号分配过程是要从左向右扫描两遍的：第一遍只给未命名组分配，第二遍只给命名组分配－－因此所有命名组的组号都大于未命名的组号
		 * 3、你可以使用(?:exp)这样的语法来剥夺一个分组对组号分配的参与权
		 * 你也可以自己指定子表达式的组名。要指定一个子表达式的组名，请使用这样的语法：(?<Word>\w+)(或者把尖括号换成'也行：(?'Word'\w+)),这样就把\w+的组名指定为Word了。
		 * 要反向引用这个分组捕获的内容，你可以使用\k<Word>,所以上一个例子也可以写成这样：\b(?<Word>\w+)\b\s+\k<Word>\b
		 * 使用小括号的时候，还有很多特定用途的语法。下面列出了最常用的一些：
		 * 分类                			代码/语法                                                  说明
		 * ----------------------------------------------------------------------
		 * 					(exp)			匹配exp,并捕获文本到自动命名的组里
		 * 捕获				(?<name>exp)    匹配exp,并捕获文本到名称为name的组里，也可以写成(?'name'exp)
		 * 					(?:exp)			匹配exp,不捕获匹配的文本，也不给此分组分配组号
		 * ------------------------------------------------------------------------
		 * 					(?=exp)			匹配exp前面的位置
		 * 					(?<=exp)		匹配exp后面的位置
		 * 零宽断言			(?!exp)			匹配后面跟的不是exp的位置
		 * 					(?<!exp)		匹配前面不是exp的位置
		 *-------------------------------------------------------------
		 * 注释				(?#comment)     这种类型的分组不对正则表达式的处理产生任何影响，用于提供注释让人阅读
		 * 我们已经讨论了前两种语法。第三个(?:exp)不会改变正则表达式的处理方式，只是这样的组匹配的内容不会像前两种那样被捕获到某个组里面，也不会拥有组号。“我为什么会想要这样做？”——好问题，你觉得为什么呢？
		 * 零宽断言
		 * 断言用来声明一个应该为真的事实。正则表达式中只有当断言为真时才会继续进行匹配。
		 * 接下来的四个用于查找在某些内容(但并不包括这些内容)之前或之后的东西，也就是说它们像\b,^,$那样用于指定一个位置，这个位置应该满足一定的条件(即断言)，因此它们也被称为零宽断言。
		 * 最好还是拿例子来说明吧：
		 * 接下来的四个用于查找在某些内容(但并不包括这些内容)之前或之后的东西，也就是说它们像\b,^,$那样用于指定一个位置，这个位置应该满足一定的条件(即断言)，因此它们也被称为零宽断言。最好还是拿例子来说明吧：
		 * (?=exp)也叫零宽度正预测先行断言，它断言自身出现的位置的后面能匹配表达式exp。比如\b\w+(?=ing\b)，匹配以ing结尾的单词的前面部分(除了ing以外的部分)，如查找I'm singing while you're dancing.时，它会匹配sing和danc。
		 * (?<=exp)也叫零宽度正回顾后发断言，它断言自身出现的位置的前面能匹配表达式exp。比如(?<=\bre)\w+\b会匹配以re开头的单词的后半部分(除了re以外的部分)，例如在查找reading a book时，它匹配ading。
		 * 假如你想要给一个很长的数字中每三位间加一个逗号(当然是从右边加起了)，你可以这样查找需要在前面和里面添加逗号的部分：((?<=\d)\d{3})+\b，用它对1234567890进行查找时结果是234567890。
		 * 下面这个例子同时使用了这两种断言：(?<=\s)\d+(?=\s)匹配以空白符间隔的数字(再次强调，不包括这些空白符)。
		 * 负向零宽断言
		 * 前面我们提到过怎么查找不是某个字符或不在某个字符类里的字符的方法(反义)。但是如果我们只是想要确保某个字符没有出现，但并不想去匹配它时怎么办？
		 * 例如，如果我们想查找这样的单词--它里面出现了字母q,但是q后面跟的不是字母u,我们可以尝试这样：
		 * \b\w*q[^u]\w*\b匹配包含后面不是字母u的字母q的单词。但是如果多做测试(或者你思维足够敏锐，直接就观察出来了)，你会发现，如果q出现在单词的结尾的话，像Iraq,Benq，
		 * 这个表达式就会出错。这是因为[^u]总要匹配一个字符，所以如果q是单词的最后一个字符的话，后面的[^u]将会匹配q后面的单词分隔符(可能是空格，或者是句号或其它的什么)，
		 * 后面的\w*\b将会匹配下一个单词，于是\b\w*q[^u]\w*\b就能匹配整个Iraq fighting。负向零宽断言能解决这样的问题，因为它只匹配一个位置，并不消费任何字符。
		 * 现在，我们可以这样来解决这个问题：\b\w*q(?!u)\w*\b。
		 * 零宽度负预测先行断言(?!exp)，断言此位置的后面不能匹配表达式exp。例如：\d{3}(?!\d)匹配三位数字，而且这三位数字的后面不能是数字；\b((?!abc)\w)+\b匹配不包含连续字符串abc的单词。
		 * 同理，我们可以用(?<!exp),零宽度负回顾后发断言来断言此位置的前面不能匹配表达式exp：(?<![a-z])\d{7}匹配前面不是小写字母的七位数字。
		 * 
		 * 请详细分析表达式(?<=<(\w+)>).*(?=<\/\1>)，这个表达式最能表现零宽断言的真正用途。
		 * 一个更复杂的例子：(?<=<(\w+)>).*(?=<\/\1>)匹配不包含属性的简单HTML标签内里的内容。(?<=<(\w+)>)指定了这样的前缀：被尖括号括起来的单词(比如可能是<b>)，然后是.*(任意的字符串),最后是一个后缀(?=<\/\1>)。
		 * 注意后缀里的\/，它用到了前面提过的字符转义；\1则是一个反向引用，引用的正是捕获的第一组，前面的(\w+)匹配的内容，这样如果前缀实际上是<b>的话，后缀就是</b>了。
		 * 整个表达式匹配的是<b>和</b>之间的内容(再次提醒，不包括前缀和后缀本身)。
		 * 
		 * 注释
		 * 小括号的另一种用途是通过语法(?#comment)来包含注释。例如：2[0-4]\d(?#200-249)|25[0-5](?#250-255)|[01]?\d\d?(?#0-199)。
		 * 要包含注释的话，最好是启用“忽略模式里的空白符”选项，这样在编写表达式时能任意的添加空格，Tab，换行，而实际使用时这些都将被忽略。启用这个选项后，在#后面到这一行结束的所有文本都将被当成注释忽略掉。例如，我们可以前面的一个表达式写成这样：
		 *    (?<=    # 断言要匹配的文本的前缀
		      <(\w+)> # 查找尖括号括起来的字母或数字(即HTML/XML标签)
		      )       # 前缀结束
		      .*      # 匹配任意文本
		      (?=     # 断言要匹配的文本的后缀
		      <\/\1>  # 查找尖括号括起来的内容：前面是一个"/"，后面是先前捕获的标签
		      )       # 后缀结束
		 * 贪婪与懒惰
		 * 当正则表达式中包含能接受重复的限定符时，通常的行为是（在使整个表达式能得到匹配的前提下）匹配尽可能多的字符。以这个表达式为例：a.*b，它将会匹配最长的以a开始，以b结束的字符串。如果用它来搜索aabab的话，它会匹配整个字符串aabab。这被称为贪婪匹配。
		 * 有时，我们更需要懒惰匹配，也就是匹配尽可能少的字符。前面给出的限定符都可以被转化为懒惰匹配模式，只要在它后面加上一个问号?。
		 * 这样.*?就意味着匹配任意数量的重复，但是在能使整个匹配成功的前提下使用最少的重复。现在看看懒惰版的例子吧：
		 * a.*?b匹配最短的，以a开始，以b结束的字符串。如果把它应用于aabab的话，它会匹配aab（第一到第三个字符）和ab（第四到第五个字符）。
		 * 为什么第一个匹配是aab（第一到第三个字符）而不是ab（第二到第三个字符）？简单地说，因为正则表达式有另一条规则，比懒惰／贪婪规则的优先级更高：最先开始的匹配拥有最高的优先权——The match that begins earliest wins。
		 * 					懒惰限定符-表
		 * 			代码/语法				说明
		 * 			*?				重复任意次，但尽可能少重复
		 * 			+?				重复1次或更多次，但尽可能少重复
		 * 			??				重复0次或1次，但尽可能少重复
		 * 			{n,m}?			重复n到m次，但尽可能少重复
		 * 			{n,}?			重复n次以上，但尽可能少重复
		 * 处理选项
		 * 上面介绍了几个选项如忽略大小写，处理多行等，这些选项能用来改变处理正则表达式的方式。下面是.Net中常用的正则表达式选项：
		 * 在C#中，你可以使用Regex(String, RegexOptions)构造函数来设置正则表达式的处理选项。如：Regex regex = new Regex(@"\ba\w{6}\b", RegexOptions.IgnoreCase);
		 * 				常用的处理选项-表
		 * 		名称							说明
		 * 	 IgnoreCase(忽略大小写)			匹配时不区分大小写。
		 *   Multiline(多行模式)  			更改^和$的含义，使它们分别在任意一行的行首和行尾匹配，而不仅仅在整个字符串的开头和结尾匹配。(在此模式下,$的精确含意是:匹配\n之前的位置以及字符串结束前的位置.)
		 *   Singleline(单行模式)			更改.的含义，使它与每一个字符匹配（包括换行符\n）。
		 * IgnorePatternWhitespace(忽略空白)		忽略表达式中的非转义空白并启用由#标记的注释。
		 *   ExplicitCapture(显式捕获)			仅捕获已被显式命名的组。
		 * 一个经常被问到的问题是：是不是只能同时使用多行模式和单行模式中的一种？答案是：不是。这两个选项之间没有任何关系，除了它们的名字比较相似（以至于让人感到疑惑）以外。
		 * 平衡组/递归匹配
		 * 这里介绍的平衡组语法是由.Net Framework支持的；其它语言／库不一定支持这种功能，或者支持此功能但需要使用不同的语法。
		 * 有时我们需要匹配像( 100 * ( 50 + 15 ) )这样的可嵌套的层次性结构，这时简单地使用\(.+\)则只会匹配到最左边的左括号和最右边的右括号之间的内容(这里我们讨论的是贪婪模式，懒惰模式也有下面的问题)。
		 * 假如原来的字符串里的左括号和右括号出现的次数不相等，比如( 5 / ( 3 + 2 ) ) )，那我们的匹配结果里两者的个数也不会相等。有没有办法在这样的字符串里匹配到最长的，配对的括号之间的内容呢？
		 * 为了避免(和\(把你的大脑彻底搞糊涂，我们还是用尖括号代替圆括号吧。现在我们的问题变成了如何把xx <aa <bbb> <bbb> aa> yy这样的字符串里，最长的配对的尖括号内的内容捕获出来？
		 * 这里需要用到以下的语法构造：
		 * 1、(?'group') 把捕获的内容命名为group,并压入堆栈(Stack)
		 * 2、(?'-group') 从堆栈上弹出最后压入堆栈的名为group的捕获内容，如果堆栈本来为空，则本分组的匹配失败
		 * 3、(?(group)yes|no) 如果堆栈上存在以名为group的捕获内容的话，继续匹配yes部分的表达式，否则继续匹配no部分
		 * 4、(?!) 零宽负向先行断言，由于没有后缀表达式，试图匹配总是失败
		 * 我们需要做的是每碰到了左括号，就在压入一个"Open",每碰到一个右括号，就弹出一个，到了最后就看看堆栈是否为空－－如果不为空那就证明左括号比右括号多，那匹配就应该失败。正则表达式引擎会进行回溯(放弃最前面或最后面的一些字符)，尽量使整个表达式得到匹配。
		 * 如果你不是一个程序员（或者你自称程序员但是不知道堆栈是什么东西），你就这样理解上面的三种语法吧：第一个就是在黑板上写一个"group"，第二个就是从黑板上擦掉一个"group"，第三个就是看黑板上写的还有没有"group"，如果有就继续匹配yes部分，否则就匹配no部分。
		 * <                         #最外层的左括号
			    [^<>]*                #最外层的左括号后面的不是括号的内容
			    (
			        (
			            (?'Open'<)    #碰到了左括号，在黑板上写一个"Open"
			            [^<>]*       #匹配左括号后面的不是括号的内容
			        )+
			        (
			            (?'-Open'>)   #碰到了右括号，擦掉一个"Open"
			            [^<>]*        #匹配右括号后面不是括号的内容
			        )+
			    )*
			    (?(Open)(?!))         #在遇到最外层的右括号前面，判断黑板上还有没有没擦掉的"Open"；如果还有，则匹配失败
			
			>                         #最外层的右括号
		 * 平衡组的一个最常见的应用就是匹配HTML,下面这个例子可以匹配嵌套的<div>标签：<div[^>]*>[^<>]*(((?'Open'<div[^>]*>)[^<>]*)+((?'-Open'</div>)[^<>]*)+)*(?(Open)(?!))</div>.
		 * 还有些什么东西没提到
		 * 上边已经描述了构造正则表达式的大量元素，但是还有很多没有提到的东西。下面是一些未提到的元素的列表，包含语法和简单的说明。你可以在网上找到更详细的参考资料来学习它们--当你需要用到它们的时候。如果你安装了MSDN Library,你也可以在里面找到.net下正则表达式详细的文档。
		 * 这里的介绍很简略，如果你需要更详细的信息，而又没有在电脑上安装MSDN Library,可以查看关于正则表达式语言元素的MSDN在线文档(https://docs.microsoft.com/zh-cn/dotnet/standard/base-types/regular-expression-language-quick-reference?redirectedfrom=MSDN)。
		 * 			表7.尚未详细讨论的语法
		 * 		代码/语法				说明
		 * 		  \a			报警字符(打印它的效果是电脑嘀一声)
		 * 		  \b			通常是单词分界位置，但如果在字符类里使用代表退格
		 * 		  \t			制表符，Tab
		 * 		  \r			回车
		 * 		  \v	                       竖向制表符
		 * 		  \f			换页符
		 * 		  \n		           换行符
		 * 		  \e	        Escape
		 * 	      \0nn          ASCII代码中八进制代码为nn的字符
		 * 		  \xnn          ASCII代码中十六进制代码为nn的字符
		 * 		  \\unnnn		Unicode代码中十六进制代码为nnnn的字符
		 * 		  \cN			ASCII控制字符。比如\cC代表Ctrl+C
		 * 		  \A 			字符串开头(类似^，但不受处理多行选项的影响)
		 * 	      \Z			字符串结尾或行尾(不受处理多行选项的影响)
		 * 		  \z			字符串结尾(类似$，但不受处理多行选项的影响)
		 * 		  \G			当前搜索的开头
		 * 		  \p{name}		Unicode中命名为name的字符类，例如\p{IsGreek}
		 * 		  (?>exp)		贪婪子表达式
		 * 		  (?<x>-<y>exp)  平衡组
		 * 		  (?im-nsx:exp)  在子表达式exp中改变处理选项
		 * 		  (?im-nsx)      为表达式后面的部分改变处理选项
		 * 		  (?(exp)yes|no)  把exp当作零宽正向先行断言，如果在这个位置能匹配，使用yes作为此组的表达式；否则使用no
		 * 		  (?(exp)yes)     同上，只是使用空表达式作为no
		 * 	      (?(name)yes|no) 如果命名为name的组捕获到了内容，使用yes作为表达式；否则使用no
		 * 		  (?(name)yes)	      同上，只是使用空表达式作为no
		 * 网上的资源及本文参考文献
		 * 微软的正则表达式教程 https://docs.microsoft.com/zh-cn/
		 * System.Text.RegularExpressions.Regex类(MSDN) https://docs.microsoft.com/zh-cn/dotnet/api/system.text.regularexpressions.regex?redirectedfrom=MSDN&view=net-5.0
		 * 专业的正则表达式教学网站(英文)http://www.regular-expressions.info/
		 * 关于.Net下的平衡组的详细讨论（英文）https://weblogs.asp.net/whaggard/377025
		 * Mastering Regular Expressions (Second Edition) https://www.oreilly.com/library/view/regular-expression-pocket/9780596514273/
		 * https://weibo.com/deerchao?is_all=1#1610008184197
		 * 微博用户spincat问:另请问，零宽断言的正向和负向其实和可以相互转换的吧。比如匹配hellowordhelloword 中间的word：(?=w)\w{4}(?<!\b)也是可以的吧
		 */
		System.out.println("[]中括号表示范围,[abc]表示在abc这个中间就可以:" + "a".matches("[abc]"));
		System.out.println("[]中括号表示范围^表示取反,[^abc]表示不在abc这个中间就可以:" + "x".matches("[^abc]"));
		System.out.println("[0-9]表示只能是数字:" + "9".matches("[0-9]"));
		System.out.println("[0-9a-zA-Z]表示只能是数字或英文字母并且不区分大小写:" + "F".matches("[0-9a-zA-Z]"));
		System.out.println("************下面展示简化表达式,即上面的简写方式************");
		System.out.println(".点这个字符表示任意一个字符:" + "/".matches("."));
		System.out.println(".点这个字符表示任意一个字符:" + ".".matches("."));
		System.out.println(".点这个字符表示任意一个字符:" + ".".matches("\\."));


		System.out.println("\\d表示任意一个数字,等价于[0-9]:" + "9".matches("\\d"));
		System.out.println("\\D表示任意一个不是数字的字符,等价取反的[^0-9]:" + ",".matches("\\D"));
		
		System.out.println("\\s表示是一位空格,可能是空格或者是制表符\\t或者换行\\n:" + "\n".matches("\\s"));
		System.out.println("\\s表示是一位空格,可能是空格或者是制表符\\t或者换行\\n:" + "\t".matches("\\s"));
		System.out.println("\\s表示是一位空格,可能是空格或者是制表符\\t或者换行\\n:" + " ".matches("\\s"));
		
		System.out.println("\\S表示不是一位空格:" + "\t".matches("\\S"));
		System.out.println("\\w等价于[a-zA-Z0-9_]表示字母、数字、下划线:" + "_".matches("\\w"));
		System.out.println("\\W等价于[^a-zA-Z0-9_]就是\\w的取反操作:" + "_".matches("\\W"));
		System.out.println("************边界匹配,边界匹配JAVA用不到,js才能用到,js必须写边界字符即：^表示开始,$表示结束结尾************");
		/*
		 * \b 匹配单词的开始或结束
		 * 元字符^（和数字6在同一个键位上的符号）和$都匹配一个位置，这和\b有点类似。^匹配你要用来查找的字符串的开头，$匹配结尾。这两个代码在验证输入的内容时非常有用，
		 * 比如一个网站如果要求你填写的QQ号必须为5位到12位数字时，可以使用：^\d{5,12}$。
		 * 正则表达式引擎通常会提供一个“测试指定的字符串是否匹配一个正则表达式”的方法，如JavaScript里的RegExp.test()方法或.NET里的Regex.IsMatch()方法。
		 * 这里的匹配是指是字符串里有没有符合表达式规则的部分。如果不使用^和$的话，对于\d{5,12}而言，使用这样的方法就只能保证字符串里包含5到12连续位数字，而不是整个字符串就是5到12位数字。
		 * 和忽略大小写的选项类似，有些正则表达式处理工具还有一个处理多行的选项。如果选中了这个选项，^和$的意义就变成了匹配行的开始处和结束处
		 * 字符转义
		 * 如果你想查找元字符本身的话，比如你查找.,或者*,就出现了问题：你没办法指定它们，因为它们会被解释成别的意思。这时你就得使用\来取消这些字符的特殊意义。
		 * 因此，你应该使用\.和\*。当然，要查找\本身，你也得用\\.例如：unibetter\.com匹配unibetter.com，C:\\Windows匹配C:\Windows。
		 */
		System.out.println("\\W等价于[^a-zA-Z0-9_]就是\\w的取反操作:" + ",".matches("^\\W$"));
		System.out.println("************下面展示数量表达************");
		System.out.println("正则?,?问号表示前面的正则匹配0次或1次都行:" + "a".matches("\\w?"));
		System.out.println("正则+,+加号表示前面的正则匹配1次或多次都行:" + "11111111111a".matches("\\w+"));
		System.out.println("正则*,*星号表示前面的正则匹配0次或1次或多次都行,其实就是随便多少次都行,爱出现不出现:" + "".matches("\\w*"));
		System.out.println("正则{n},{n}表示前面的正则匹配正好出现n次:" + "666666".matches("\\d{6}"));
		System.out.println("正则{n,},{n}表示前面的正则匹配出现至少n次以上:" + "66666677".matches("\\d{6,}"));
		System.out.println("正则{n,m},{n,m}表示前面的正则匹配出现n到m次:" + "6666667557".matches("\\d{6,9}"));
		System.out.println("************逻辑匹配************");
		System.out.println("正则A正则B,表示并且的关系,在第一个匹配之后立即匹配第二个:" + "6qweasd".matches("\\d?\\w{6}"));
		System.out.println("正则A正则B,表示并且的关系,在第一个匹配之后立即匹配第二个:" + "qweasd".matches("\\d?\\w{6}"));
		System.out.println("正则A|正则B,表示满足任意一个正则就可以:" + "a123".matches("\\d{6,9}|\\w+\\d{3}"));
		System.out.println("正则A|正则B,表示满足任意一个正则就可以:" + "1234567".matches("\\d{6,9}|\\w+\\d{3}"));
		System.out.println("匹配一个斜杠\\" + "\\".matches("\\\\"));


		System.out.println("(正则):表示按照一组正则匹配处理");
		
		System.out.println("*******************String类的对正则的支持*************************");
		System.out.println("=====================String的正则替换=============");
		String strA = "adsfzcadfa()12zxc!@#5323412zxc12zxxcsadf@#$5s12zxc";
		String regexStr = "^[^a-zA-Z]$";
		System.out.println("找到strA里面的所有英文字母:" +  strA.replaceAll(regexStr, ""));
		String regexStr1 = "[^a-zA-Z]";
		System.out.println("找到strA里面的所有英文字母:" +  strA.replaceAll(regexStr1, ""));
		System.out.println("找到strA里面的所有英文字母:" +  strA.matches(regexStr1));
		System.out.println("=====================String的正则拆分=============");
		String strNum = "a1s21zxc12zxctqwezxc11^3ez";//以数字进行拆分
		String regexNum = "\\d+";
		String [] strNumArr = strNum.split(regexNum);
		for (int i=0; i<strNumArr.length; i++) {
			System.out.println(strNumArr[i]);
		}
		System.out.println("==================字符串的验证=======");
		System.out.println("验证字符串是否为数字(整数或小数或负数)");
		String str = "-1100.09";
		String regexDou = "(-)?\\d+(\\.\\d+)?";//这里用到了正则里面的括号了,括号表示一整块正则表达
		if (str.matches(regexDou)) {
			double dou = Double.parseDouble(str);
			System.out.println(dou);
		} else {
			System.out.println("不是数字");
		}
		System.out.println("验证一个字符是否为日期或者为日期时间");
		String strDate = "2016-09-12 15:09:59";
		String regexDate = "\\d{4}-\\d{2}-\\d{2}";
		String regexDateSec = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}";
		if (strDate.matches(regexDate)) {
			System.out.println(new SimpleDateFormat("yyyy-MM-dd").parse(strDate));
		} else if (strDate.matches(regexDateSec)) {
			System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strDate));
		}
		System.out.println("验证电话号码是否正确");
		String strPhoneNum = "51283346";
		String regPhoneNum = "\\d{7,8}";
		if (strPhoneNum.matches(regPhoneNum)) {
			System.out.println(strPhoneNum + "是合法的电话号码");
		} else {
			System.out.println(strPhoneNum + "不是合法的电话号码");
		}
		String strPhoneNum01 = "01051283346";
		String regPhoneNum01 = "(\\d{3,4})?\\d{7,8}";
		if (strPhoneNum01.matches(regPhoneNum01)) {
			System.out.println(strPhoneNum01 + "是合法的电话号码");
		} else {
			System.out.println(strPhoneNum01 + "不是合法的电话号码");
		}
		String strPhoneNum02 = "(010)-51283346";
		String regPhoneNum02 = "(\\(\\d{3,4}\\)-)?\\d{7,8}";
		if (strPhoneNum02.matches(regPhoneNum02)) {
			System.out.println(strPhoneNum02 + "是合法的电话号码");
		} else {
			System.out.println(strPhoneNum02 + "不是合法的电话号码");
		}
		System.out.println("验证字符串是否是一个合法的email地址");
		String emailStr = "amldn88-1.2@mldnjava.cn";
		String emailReg = "[a-zA-Z][a-zA-Z\\._\\-0-9]{5,14}@[a-zA-Z\\._\\-0-9]+\\.(cn|com|net|edu)";
		if (emailStr.matches(emailReg)) {
			System.out.println(emailStr + "是一个合法的email地址");
		} else {
			System.out.println(emailStr + "不是一个合法的email地址");
		}
		System.out.println("对开发者而言,正则的重大意义在于字符串的组成验证处理上");
		
		/*
		 * 我们在前面的代码中用到的正则表达式代码是String.matches()方法，而我们在分组提取的代码中用的是java.util.regex包里面的Pattern类和Matcher类。
		 * 实际上这两种代码本质上是一样的，因为String.matches()方法内部调用的就是Pattern和Matcher类的方法。
		 * 但是反复使用String.matches()对同一个正则表达式进行多次匹配效率较低，因为每次都会创建出一样的Pattern对象。
		 * 完全可以先创建出一个Pattern对象，然后反复使用，就可以实现编译一次，多次匹配：
		 */
		System.out.println("JDK1.4里面增加了一个开发包java.uti.regex,但是在这个包里面只有俩个类:Pattern类,Matcher类.Pattern类负责编译正则,"
				+ "而Matcher负责进行正则匹配.如果要做一般的处理,使用String类即可.");
		
		String strPat = "a|b|c";
		String regexPat = "\\|";
		Pattern pat = Pattern.compile(regexPat);
		String [] strPatArr = pat.split(strPat);
		for (int i=0; i < strPatArr.length; i++) {
			System.out.println(strPatArr[i]);
		}
		
		
		String strPatNum = "100";
		String regexPatNum = "\\d+";
		Pattern patNum = Pattern.compile(regexPatNum);
		Matcher mat = patNum.matcher(strPatNum);
		System.out.println("但是这么写还不如直接用String自带的方法:" + mat.matches());
		System.out.println("在进行一些复杂的正则操作里面,String类是完成不了的,必须通过Matcher类处理,因为这里面有一个分组的概念.");
		System.out.println("比如下面这个,String类就处理不了");
		
		String strMat  = "insert into table values (#{table.name},#{table.age},#{table.sex})";
		String regexMat = "#\\{[a-zA-Z\\._\\-]+\\}";
		Pattern patMat = Pattern.compile(regexMat);
		Matcher matcher = patMat.matcher(strMat);
		System.out.println("是否匹配:" + matcher.matches());
		while (matcher.find()) {
			System.out.println(matcher.group(0));
		}
		
		System.out.println("正则表达式匹配中文:");
		System.out.println("a\u548cc".equals("a和c"));
		
		//\b单词边界
		String strB = "(中文问号？123???英文)问号?我是华丽[的制表符\t]我是华丽{的空格符 我是华丽}的换行符\n";
		String rex = "\\b";
		Pattern pattern = Pattern.compile(rex);
		//Matcher matcherB = pattern.matcher(strB);
		String[] resultB = pattern.split(strB);
		for (String string : resultB) {
			//从这些分割的字符串中我们可以知道单词边界就是单词和符号之间的边界
			//这里的单词可以是中文字符,英文字符,数字；符号可以是中文符号,英文符号,空格,制表符,换行
			System.out.println("分割的字符串:->" + string);
		}
		String strSplit = "123;456";
		String[] strSpArr = strSplit.split(";");
		for (String string : strSpArr) {
			//从这些分割的字符串中我们可以知道单词边界就是单词和符号之间的边界
			//这里的单词可以是中文字符,英文字符,数字；符号可以是中文符号,英文符号,空格,制表符,换行
			System.out.println("split分割的字符串:->" + string);
		}
		
		String strC = " 2 ";
		String rexGsd = "\\b2\\b";
		Pattern patternGsd = Pattern.compile(rexGsd);
		Matcher matcherGsd = patternGsd.matcher(strC);
		if (matcherGsd.matches()) {
			System.out.println("匹配成功");
		} else {
			//经过分割的例子后就知道了 空格并不是边界 空格与数字2之间的那个才叫边界  所以运行结果不言而喻 肯定是匹配不成功
			System.out.println("匹配不成功");
		}
		
		
		//一般来说\b不用来判断当前字符串是否符合某种规则,一般我们都用\b来进行获取
		String strBu = ",,,,呵呵,,,,";
		String rexBu = "\\b呵呵\\b";
		Pattern patternBu = Pattern.compile(rexBu);
		Matcher matcherBu = patternBu.matcher(strBu);
		if (matcherBu.find()) {
			System.out.println(matcherBu.group());
		}
		
		/*
		 * \B的用法
		 * 了解了\b的用法 我们再来说说\B \B是非单词边界,也就说\B=[^\b]//符号^是非的意思1
		 * \b是单词与符号的边界 那非单词与符号的边界的其它都是\B,所以我们的猜想\B是符号与符号,单词与单词的边界
		 * 当然猜想需要认证！下面我们写一个例子来证明一个！
		 */
		String strBB = "1234A56我是JAVA{，、；‘asd";
		String rexBB = "\\B";
		Pattern patternBB = Pattern.compile(rexBB);
		String[] resultBB = patternBB.split(strBB);
		for (String string: resultBB) {
			//\B分割的字符串: A{  注意单词与符号之间的边界不算\B的边界
			System.out.println("\\B分割的字符串: " + string);
		}
		
		/*
		 * \B一般也是用来获取字符串的
		 * 深入浅析正则表达式中的\B和\b https://www.jb51.net/article/138235.htm
		 * JS正则表达式修饰符global(/g)用法分析 https://www.jb51.net/article/101408.htm
		 * python 正则表达式 re.sub & re.subn https://www.jb51.net/article/129582.htm
		 * Python的爬虫包Beautiful Soup中用正则表达式来搜索 https://www.jb51.net/article/78380.htm
		 */
		String strBbB = ",,,,,和呵呵,,,,,";//,,,,,和呵呵a,,,,,
		String rexBbB = "\\B呵\\B";
		Pattern patternBbB = Pattern.compile(rexBbB);
		Matcher matcherBbB = patternBbB.matcher(strBbB);
		while (matcherBbB.find()) {
			//注意单词与符号(呵,)之间的边界不算\B的边界,所以运行的结果是:呵
			//换成,,,,,和呵呵a,,,,,就会输出呵呵
			System.out.println(matcherBbB.group());
		}
		
		/*
		 * 关于java正则表达式中的 ^和$的使用
		 * https://blog.csdn.net/OUCFSB/article/details/80240423
		 * https://bbs.csdn.net/topics/391907864
		 * java正则表达式的边界匹配符中，有两个比较常用的字符：“ ^ ”和“ $ ”,这两个字符理解起来比较容易混淆。先说下这两个字符的含义：
		 * “ ^ ”：匹配输入字符串开始的位置。如果设置了 RegExp 对象的 Multiline 属性，^ 还会与”\n”或”\r”之后的位置匹配（即匹配每一行的开始）；
		 * “ $ ” ：匹配输入字符串结尾的位置。如果设置了 RegExp 对象的 Multiline 属性，$ 还会与”\n”或”\r”之前的位置匹配（即匹配每一行的结束）。
		 * 那很多初学的朋友，不理解这两个字符的用法，“^aa+$”和“aa+”在进行匹配的时候貌似没区别。我们结合代码来看，就容易理解了。 
		 * 字符串a和b，字符串和匹配的正则表达式基本相同，只是b中有“^”字符。那我们看输出的字符串结果，不难发现，a中，将两个数字的部分，均替换成了“Z”，而b中只是将开头的两个数字替换成了“Z”，因为“^”的要求是从开始进行匹配。这样中间的两个数字就匹配不到了，也就没有替换。而没有“^”的a来说，只要满足是两个数字的情况，就可以进行匹配和替换，不会区分是否是从字符串的开始进行匹配。同理，“$”判定是从最后面开始匹配，可结合c和d的结果，类比理解。
		 * 值得一提的一点是，不管是带有“ ^”和 ” \$” 的正则表达式，在进行matches方法匹配的时候，均返回false。因为matches（）匹配的是整个字符串，而不是某一部分，很明显文中使用的正则表达式并不能完全匹配输入的字符串，所以返回值均为false。
		 * https://blog.csdn.net/OUCFSB/article/details/80240423
		 */
		String a = "3131sasfasd".replaceAll("\\d{2}", "Z");
        String b = "3131sasfasd".replaceAll("^\\d{2}", "Z");//仅替换字符串开头的两个数字
        String c = "3131sdasfasd".replaceAll("sd", "Z");
        String d = "3131sdsfasd".replaceAll("sd$", "Z");//仅替换字符串结尾的两个数字
        System.out.println(a);//ZZZsasfasd
        System.out.println(b);//Z3131sasfasd
        System.out.println(c);//aa3131ZasfaZ
        System.out.println(d);//aa3131sdsfaZ
        
        String bb = "3131sasfasd".replaceAll("^\\d{2}$", "Z");
        
        System.out.println(bb);//aa3131sdsfaZ

        String str6 = "aa3131sasfasd";
        System.out.println(str6.matches("\\d{2}")); //false
        System.out.println(str6.matches("^\\d{2}"));//false
        
        String regex3 = "^[^9a]{1,}$";
        String str4 = "1234fsfse";
        String str5 = "a2343";
        System.out.println(str4.matches(regex3));//true
        System.out.println(str5.matches(regex3));//false
        
        //匹配固定电话  4位区号-7位号码 或者 3位区号-8位号码
        String regexPh = "^\\d{4}-\\d{7}|\\d{3}-\\d{8}$";
        String strPh = "020-13222113";
        String strPh1 = "0532-9989211";
        System.out.println(strPh.matches(regexPh));
        System.out.println(strPh1.matches(regexPh));
        //匹配邮箱
        String regexEmail = "^.+@.+(\\..+){1,}$";
        String strEm = "fushb@163.com";
        System.out.println(strEm.matches(regexEmail));
        
        
        String testFin = "3131sasfasd";
        String testFin1 = "3131";
        String testFin2 = "31";
        String testRes = "^\\d{2}$";
        String testRes1 = "\\d{2}";
        System.out.println(testFin.matches(testRes));
        System.out.println(testFin1.matches(testRes));
        System.out.println(testFin2.matches(testRes));
        
        System.out.println(testFin.matches(testRes1));
        System.out.println(testFin1.matches(testRes1));
        System.out.println(testFin2.matches(testRes1));
        
        
        
        String strDateTest = "a2016-12-a1sdfas2016-09-12 15:09:59";
		String regexDateT = "\\d{4}-\\d{2}-\\d{2}";
		String regexDateSetc = "\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}";
		
		System.out.println(strDateTest.replaceAll(regexDateT, ""));
		System.out.println(strDateTest.split(regexDateT)[0]);
		System.out.println(strDateTest.split(regexDateT)[1]);
		System.out.println(strDateTest.matches(regexDateT));
        
		/*
		 * http://cn.voidcc.com/question/p-wzcdkrft-eu.html
		 * www.javaregex.com
		 * Regex regexObj = new Regex("YOURREGEX"); 
		// search for a match within a string 
		regexObj.search("YOUR STRING YOUR STRING"); 
		if(regexObj.didMatch()){ 
			// Prints "true" -- r.didMatch() is a boolean function 
			// that tells us whether the last search was successful 
			// in finding a pattern. 
			// r.left() returns left String , string before the matched pattern 
			int index = regexObj.left().length(); 
		} 
		 */
	}
}
