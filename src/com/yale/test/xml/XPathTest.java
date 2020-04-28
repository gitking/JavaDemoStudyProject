package com.yale.test.xml;

import java.io.InputStream;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

/**
 * XPath 是一门在 XML 文档中查找信息的语言。XPath 用于在 XML 文档中通过元素和属性进行导航。
 * 为什么我们需要用到XPATH
 * 上面我们使用dom4j的时候，要获取某个节点，都是通过根节点开始，一层一层地往下寻找，这就有些麻烦了！
 * 如果我们用到了XPATH这门语言，要获取得到XML的节点，就非常地方便了！
 * 使用XPATH需要导入开发包jaxen-1.1-beta-7，我们来看官方的文档来入门吧。
 * XPATH官网:http://www.zvon.org/xxl/XPathTutorial/General_chi/examples.html
 * @author dell
 */
public class XPathTest {

	public static void main(String[] args) throws DocumentException {
		
		//org.jdom.xpath.XPath xpath = XPath..newInstance("//REQUEST_TYPE");
		//Element elementRequestType = (Element) xpath.selectSingleNode(root);
		
		//https://mp.weixin.qq.com/s/A54bBHUZ_NCmneEzMXzr1Q
		SAXReader saxreadr = new SAXReader();
		InputStream is = XmlDom4j.class.getClassLoader().getResourceAsStream("xPath.xml");
		Document document = saxreadr.read(is);
		System.out.println("/这个符号表示从根节点开始选取");
		System.out.println("//这个符号表示选择从任意位置的某个节点,而不考虑他们的位置");

		Node node = document.selectSingleNode("/AAA");
		String val = node.getText();
		System.out.println("xPath找到的节点值:" + node);
		System.out.println("xPath找到的节点值:" + node.getName());
		
		List<Node> nodeList = document.selectNodes("/AAA/CCC");
		for (Node nodeT: nodeList) {
			System.out.println("xPath找到的节点值:" + nodeT);
			String temp = nodeT.getText();
			System.out.println("xPath找到的节点值:" + temp);
		}
		
		List<Node> nodeLi = document.selectNodes("/AAA/DDD/BBB");
		for (Node nodeT: nodeLi) {
			System.out.println("xPath找到的节点值:" + nodeT);
			String temp = nodeT.getText();
			System.out.println("xPath找到的节点值:" + temp);
		}
		
		List<Node> node01 = document.selectNodes("//BBB");
		for (Node nodeT: node01) {
			System.out.println("//BBB选择所有BBB元素:" + nodeT);
			String temp = nodeT.getText();
			System.out.println("xPath找到的节点值:" + temp);
		}

		List<Node> nodeDD = document.selectNodes("//DDD/BBB");
		for (Node nodeT: nodeDD) {
			System.out.println("//DDD/BBB选择所有父元素是DDD的BBB元素:" + nodeT);
			String temp = nodeT.getText();
			System.out.println("xPath找到的节点值:" + temp);
		}
		
		examples();
		examples04();
		examples05();
		examples06();
		examples07();
		examples08();
		examples09();
		examples10();
		examples11();
		examples12();
		examples13();
		examples14();
		examples15();
		examples16();
		examples17();
		examples18();
		examples19();
		examples20();
		examples21();
		examples22();
	}
	
	public static void examples() throws DocumentException {
		 String text = 
				 "<AAA>\n" +
						 "         <XXX>\n" + 
						 "              <DDD>\n" + 
						 "                   <BBB/>\n" + 
						 "                   <BBB/>\n" + 
						 "                   <EEE/>\n" + 
						 "                   <FFF/>\n" + 
						 "              </DDD>\n" + 
						 "         </XXX>\n" + 
						 "         <CCC>\n" + 
						 "              <DDD>\n" + 
						 "                   <BBB/>\n" + 
						 "                   <BBB/>\n" + 
						 "                   <EEE/>\n" + 
						 "                   <FFF/>\n" + 
						 "              </DDD>\n" + 
						 "         </CCC>\n" + 
						 "         <CCC>\n" + 
						 "              <BBB>\n" + 
						 "                   <BBB>\n" + 
						 "                        <BBB/>\n" + 
						 "                   </BBB>\n" + 
						 "              </BBB>\n" + 
						 "         </CCC>\n" + 
						 "    </AAA>";
		 Document docu = DocumentHelper.parseText(text);
		 List<Node> nodeList = docu.selectNodes("/AAA/CCC/DDD/*");
		 for (Node nt : nodeList) {
			 System.out.println("/AAA/CCC/DDD/*:选择所有路径依附于/AAA/CCC/DDD的元素" + nt.getName());
		 }
		 
		 List<Node> node2 = docu.selectNodes("/*/*/*/BBB");
		 for (Node nt : node2) {
			 System.out.println("/*/*/*/BBB:选择所有的有3个祖先元素的BBB元素" + nt.getPath());
		 }
		 
		 
		 List<Node> node3 = docu.selectNodes("//*");
		 for (Node nt : node3) {
			 System.out.println("//*:选择所有元素" + nt.getPath());
		 }
	}
	
	public static void examples04() throws DocumentException {
		 String text =
				 "<AAA>\n" +
						 "          <BBB/>\n" + 
						 "          <BBB/>\n" + 
						 "          <BBB/>\n" + 
						 "          <BBB/>\n" + 
						 "     </AAA>";

		 Document docu = DocumentHelper.parseText(text);
		 List<Node> nodeList = docu.selectNodes("/AAA/BBB[1]");
		 for (Node nt : nodeList) {
			 System.out.println("/AAA/BBB[1]:选择AAA的第一个BBB子元素" + nt.getName());
			 System.out.println("/AAA/BBB[1]:选择AAA的第一个BBB子元素" + nt.getPath());
		 }
		 
		 List<Node> node2 = docu.selectNodes("/AAA/BBB[last()]");
		 for (Node nt : node2) {
			 System.out.println("/AAA/BBB[last()]:选择AAA的最后一个BBB子元素" + nt.getName());
			 System.out.println("/AAA/BBB[last()]:选择AAA的最后一个BBB子元素" + nt.getPath());
		 }
	}
	
	public static void examples05() throws DocumentException {
		 String text =
				 "<AAA>\n" +
						 "          <BBB id = \"b1\"/>\n" + 
						 "          <BBB id = \"b2\"/>\n" + 
						 "          <BBB name = \"bbb\"/>\n" + 
						 "          <BBB/>\n" + 
						 "     </AAA>";

		 Document docu = DocumentHelper.parseText(text);
		 List<Node> nodeList = docu.selectNodes("//@id");
		 for (Node nt : nodeList) {
			 System.out.println("//@id:选择所有的id属性,属性的名字:" + nt.getName());
			 System.out.println("//@id:选择所有的id属性,属性本身也是一个Node:" + nt.getPath());
			 System.out.println("//@id:选择所有的id属性,属性的值为:" + nt.getText());
		 }
		 
		 List<Node> node2 = docu.selectNodes("//BBB[@id]");
		 for (Node nt : node2) {
			 System.out.println("//BBB[@id]:选择有id属性的BBB元素" + nt.getName());
			 System.out.println("//BBB[@id]:选择有id属性的BBB元素" + nt.getPath());
		 }
		 
		 List<Node> node3 = docu.selectNodes("//BBB[@name]");
		 for (Node nt : node3) {
			 System.out.println("//BBB[@name]:选择有name属性的BBB元素" + nt.getName());
			 System.out.println("//BBB[@name]:选择有name属性的BBB元素" + nt.getPath());
		 }
		 
		 
		 List<Node> node4 = docu.selectNodes("//BBB[@*]");
		 for (Node nt : node4) {
			 System.out.println("//BBB[@*]:选择有任意属性的BBB元素" + nt.getName());
			 System.out.println("//BBB[@*]:选择有任意属性的BBB元素" + nt.getPath());
		 }
		 
		 List<Node> node5 = docu.selectNodes("//BBB[not(@*)]");
		 for (Node nt : node5) {
			 System.out.println("//BBB[not(@*)]:选择没有属性的BBB元素" + nt.getName());
			 System.out.println("//BBB[not(@*)]:选择没有属性的BBB元素" + nt.getPath());
		 }
	}
	
	public static void examples06() throws DocumentException {
		 String text = "<AAA>\n" +
				 "          <BBB id = \"b1\"/>\n" + 
				 "          <BBB name = \" bbb \"/>\n" + 
				 "          <BBB name = \"bbb\"/>\n" + 
				 "     </AAA>";

		 Document docu = DocumentHelper.parseText(text);
		 List<Node> nodeList = docu.selectNodes("//BBB[@id='b1']");
		 for (Node nt : nodeList) {
			 System.out.println("//BBB[@id='b1']:选择含有属性id且其值为'b1'的BBB元素:" + nt.getName());
			 System.out.println("//BBB[@id='b1']:选择含有属性id且其值为'b1'的BBB元素:" + nt.getPath());
			 System.out.println("//BBB[@id='b1']:选择含有属性id且其值为'b1'的BBB元素:" + nt.getText());
		 }
		 
		 List<Node> node2 = docu.selectNodes("//BBB[@name='bbb']");
		 for (Node nt : node2) {
			 System.out.println("//BBB[@name='bbb']:选择含有属性name且其值为'bbb'的BBB元素" + nt.getName());
			 System.out.println("//BBB[@name='bbb']:选择含有属性name且其值为'bbb'的BBB元素" + nt.getPath());
		 }
		 
		 List<Node> node3 = docu.selectNodes("//BBB[@name]");
		 for (Node nt : node3) {
			 System.out.println("//BBB[@name]:选择有name属性的BBB元素" + nt.getName());
			 System.out.println("//BBB[@name]:选择有name属性的BBB元素" + nt.getPath());
		 }
		 
		 List<Node> node4 = docu.selectNodes("//BBB[normalize-space(@name)='bbb']");
		 for (Node nt : node4) {
			 System.out.println("//BBB[normalize-space(@name)='bbb']:选择含有属性name且其值(在用normalize-space函数去掉前后空格后)为'bbb'的BBB元素" + nt.getName());
			 System.out.println("//BBB[normalize-space(@name)='bbb']:选择含有属性name且其值(在用normalize-space函数去掉前后空格后)为'bbb'的BBB元素" + nt.getPath());
		 }
	}
	
	public static void examples07() throws DocumentException {
		 String text = 
				 "<AAA>\n" +
						 "    <CCC>\n" + 
						 "         <BBB/>\n" + 
						 "         <BBB/>\n" + 
						 "         <BBB/>\n" + 
						 "    </CCC>\n" + 
						 "    <DDD>\n" + 
						 "         <BBB/>\n" + 
						 "         <BBB/>\n" + 
						 "    </DDD>\n" + 
						 "    <EEE>\n" + 
						 "         <CCC/>\n" + 
						 "         <DDD/>\n" + 
						 "    </EEE>\n" + 
						 "</AAA>";

		 Document docu = DocumentHelper.parseText(text);
		 List<Node> nodeList = docu.selectNodes("//*[count(BBB)=2]");
		 for (Node nt : nodeList) {
			 System.out.println("//*[count(BBB)=2]:选择含有2个BBB子元素的元素:" + nt.getName());
			 System.out.println("//*[count(BBB)=2]:选择含有2个BBB子元素的元素:" + nt.getPath());
			 System.out.println("//*[count(BBB)=2]:选择含有2个BBB子元素的元素:" + nt.getText());
		 }
		 
		 List<Node> node2 = docu.selectNodes("//*[count(*)=2]");
		 for (Node nt : node2) {
			 System.out.println("//*[count(*)=2]:选择含有2个子元素的元素" + nt.getName());
			 System.out.println("//*[count(*)=2]:选择含有2个子元素的元素" + nt.getPath());
		 }
		 
		 List<Node> node3 = docu.selectNodes("//*[count(*)=3]");
		 for (Node nt : node3) {
			 System.out.println("//*[count(*)=3]:选择含有3个子元素的元素" + nt.getName());
			 System.out.println("//*[count(*)=3]:选择含有3个子元素的元素" + nt.getPath());
		 }
	}
	
	public static void examples08() throws DocumentException {
		System.out.println("name()函数返回元素的名称, start-with()函数在该函数的第一个参数字符串是以第二个参数字符开始的情况返回true, contains()函数当其第一个字符串参数包含有第二个字符串参数时返回true.");
		 String text = 
				 "<AAA>\n" +
						 "    <BCC>\n" + 
						 "         <BBB/>\n" + 
						 "         <BBB/>\n" + 
						 "         <BBB/>\n" + 
						 "    </BCC>\n" + 
						 "    <DDB>\n" + 
						 "         <BBB/>\n" + 
						 "         <BBB/>\n" + 
						 "    </DDB>\n" + 
						 "    <BEC>\n" + 
						 "         <CCC/>\n" + 
						 "         <DBD/>\n" + 
						 "    </BEC>\n" + 
						 "</AAA>";

		 Document docu = DocumentHelper.parseText(text);
		 List<Node> nodeList = docu.selectNodes("//*[name()='BBB']");
		 for (Node nt : nodeList) {
			 System.out.println("//*[name()='BBB']:选择所有名称为BBB的元素(这里等价于//BBB):" + nt.getName());
			 System.out.println("//*[name()='BBB']:选择所有名称为BBB的元素(这里等价于//BBB):" + nt.getPath());
			 System.out.println("//*[name()='BBB']:选择所有名称为BBB的元素(这里等价于//BBB):" + nt.getText());
		 }
		 
		 List<Node> node2 = docu.selectNodes("//*[starts-with(name(),'B')]");
		 for (Node nt : node2) {
			 System.out.println("//*[starts-with(name(),'B')]:选择所有名称以\"B\"起始的元素" + nt.getName());
			 System.out.println("//*[starts-with(name(),'B')]:选择所有名称以\"B\"起始的元素" + nt.getPath());
		 }
		 
		 List<Node> node3 = docu.selectNodes("//*[contains(name(),'C')]");
		 for (Node nt : node3) {
			 System.out.println("//*[contains(name(),'C')]:选择所有名称包含\"C\"的元素:" + nt.getName());
			 System.out.println("//*[contains(name(),'C')]:选择所有名称包含\"C\"的元素:" + nt.getPath());
		 }
	}
	
	public static void examples09() throws DocumentException {
		System.out.println("string-length函数返回字符串的字符数,你应该用&lt;替代<, 用&gt;代替>");
		String text =
				 "<AAA>\n" +
						 "          <Q/>\n" + 
						 "          <SSSS/>\n" + 
						 "          <BB/>\n" + 
						 "          <CCC/>\n" + 
						 "          <DDDDDDDD/>\n" + 
						 "          <EEEE/>\n" + 
						 "     </AAA>";


		 Document docu = DocumentHelper.parseText(text);
		 List<Node> nodeList = docu.selectNodes("//*[string-length(name()) = 3]");
		 for (Node nt : nodeList) {
			 System.out.println("//*[string-length(name()) = 3]:选择名字长度为3的元素:" + nt.getName());
			 System.out.println("//*[string-length(name()) = 3]:选择名字长度为3的元素:" + nt.getPath());
			 System.out.println("//*[string-length(name()) = 3]:选择名字长度为3的元素:" + nt.getText());
		 }
		 
		 List<Node> node2 = docu.selectNodes("//*[string-length(name()) < 3]");
		 for (Node nt : node2) {
			 System.out.println("//*[string-length(name()) < 3]:选择名字长度小于3的元素" + nt.getName());
			 System.out.println("//*[string-length(name()) < 3]:选择名字长度小于3的元素" + nt.getPath());
		 }
		 
		 List<Node> node3 = docu.selectNodes("//*[string-length(name()) > 3]");
		 for (Node nt : node3) {
			 System.out.println("//*[string-length(name()) > 3]:选择名字长度大于3的元素:" + nt.getName());
			 System.out.println("//*[string-length(name()) > 3]:选择名字长度大于3的元素:" + nt.getPath());
		 }
	}
	
	public static void examples10() throws DocumentException {
		System.out.println("多个路径可以用分隔符 | 合并在一起");
		String text = "<AAA>\n" +
						"          <BBB/>\n" + 
						"          <CCC/>\n" + 
						"          <DDD>\n" + 
						"               <CCC/>\n" + 
						"          </DDD>\n" + 
						"          <EEE/>\n" + 
						"     </AAA>";



		 Document docu = DocumentHelper.parseText(text);
		 List<Node> nodeList = docu.selectNodes("//CCC | //BBB");
		 for (Node nt : nodeList) {
			 System.out.println("//CCC | //BBB:选择所有的CCC和BBB元素:" + nt.getName());
			 System.out.println("//CCC | //BBB:选择所有的CCC和BBB元素:" + nt.getPath());
			 System.out.println("//CCC | //BBB:选择所有的CCC和BBB元素:" + nt.getText());
		 }
		 
		 List<Node> node2 = docu.selectNodes("/AAA/EEE | //BBB");
		 for (Node nt : node2) {
			 System.out.println("/AAA/EEE | //BBB:选择所有的BBB元素和所有是AAA的子元素的EEE元素:" + nt.getName());
			 System.out.println("/AAA/EEE | //BBB:选择所有的BBB元素和所有是AAA的子元素的EEE元素:" + nt.getPath());
		 }
		 
		 List<Node> node3 = docu.selectNodes("/AAA/EEE | //DDD/CCC | /AAA | //BBB");
		 for (Node nt : node3) {
			 System.out.println("/AAA/EEE | //DDD/CCC | /AAA | //BBB:可以合并的路径数目没有限制:" + nt.getName());
			 System.out.println("/AAA/EEE | //DDD/CCC | /AAA | //BBB:可以合并的路径数目没有限制:" + nt.getPath());
		 }
	}
	
	public static void examples11() throws DocumentException {
		System.out.println("child轴(axis)包含上下文节点的子元素, 作为默认的轴,可以忽略不写.");
		String text = "<AAA>\n" +
						"      <BBB/>\n" + 
						"      <CCC/>\n" + 
						" </AAA>";

		 Document docu = DocumentHelper.parseText(text);
		 List<Node> nodeList = docu.selectNodes("/AAA");
		 for (Node nt : nodeList) {
			 System.out.println("/AAA:等价于 /child::AAA:" + nt.getName());
			 System.out.println("/AAA:等价于 /child::AAA:" + nt.getPath());
			 System.out.println("/AAA:等价于 /child::AAA:" + nt.getText());
		 }
		 
		 List<Node> node2 = docu.selectNodes("/child::AAA");
		 for (Node nt : node2) {
			 System.out.println("/child::AAA:等价于/AAA:" + nt.getName());
			 System.out.println("/child::AAA:等价于/AAA:" + nt.getPath());
		 }
		 
		 List<Node> node3 = docu.selectNodes("/AAA/BBB");
		 for (Node nt : node3) {
			 System.out.println("/AAA/BBB:等价于/child::AAA/child::BBB:" + nt.getName());
			 System.out.println("/AAA/BBB:等价于/child::AAA/child::BBB:" + nt.getPath());
		 }
		 
		 List<Node> node4 = docu.selectNodes("/child::AAA/child::BBB");
		 for (Node nt : node4) {
			 System.out.println("/child::AAA/child::BBB:等价于/AAA/BBB:" + nt.getName());
			 System.out.println("/child::AAA/child::BBB:等价于/AAA/BBB:" + nt.getPath());
		 }
		 
		 List<Node> node5 = docu.selectNodes("/child::AAA/BBB");
		 for (Node nt : node5) {
			 System.out.println("/child::AAA/BBB:二者都可以被合并:" + nt.getName());
			 System.out.println("/child::AAA/BBB:二者都可以被合并:" + nt.getPath());
		 }
	}
	
	public static void examples12() throws DocumentException {
		System.out.println("descendant (后代)轴包含上下文节点的后代,一个后代是指子节点或者子节点的子节点等等, 因此descendant轴不会包含属性和命名空间节点..");
		String text = "<AAA>\n" +
						"    <BBB>\n" + 
						"         <DDD>\n" + 
						"              <CCC>\n" + 
						"                   <DDD/>\n" + 
						"                   <EEE/>\n" + 
						"              </CCC>\n" + 
						"         </DDD>\n" + 
						"    </BBB>\n" + 
						"    <CCC>\n" + 
						"         <DDD>\n" + 
						"              <EEE>\n" + 
						"                   <DDD>\n" + 
						"                        <FFF/>\n" + 
						"                   </DDD>\n" + 
						"              </EEE>\n" + 
						"         </DDD>\n" + 
						"    </CCC>\n" + 
						"</AAA>";

		 Document docu = DocumentHelper.parseText(text);
		 List<Node> nodeList = docu.selectNodes("/descendant::*");
		 for (Node nt : nodeList) {
			 System.out.println("/descendant::*:选择文档根元素的所有后代.即所有的元素被选择" + nt.getName());
			 System.out.println("/descendant::*:选择文档根元素的所有后代.即所有的元素被选择" + nt.getPath());
			 System.out.println("/descendant::*:选择文档根元素的所有后代.即所有的元素被选择" + nt.getText());
		 }
		 
		 List<Node> node2 = docu.selectNodes("/AAA/BBB/descendant::*");
		 for (Node nt : node2) {
			 System.out.println("/AAA/BBB/descendant::*:选择/AAA/BBB的所有后代元素:" + nt.getName());
			 System.out.println("/AAA/BBB/descendant::*:选择/AAA/BBB的所有后代元素:" + nt.getPath());
		 }
		 
		 List<Node> node3 = docu.selectNodes("//CCC/descendant::*");
		 for (Node nt : node3) {
			 System.out.println("//CCC/descendant::*:选择在祖先元素中有CCC的所有元素:" + nt.getName());
			 System.out.println("//CCC/descendant::*:选择在祖先元素中有CCC的所有元素:" + nt.getPath());
		 }
		 
		 List<Node> node4 = docu.selectNodes("//CCC/descendant::DDD");
		 for (Node nt : node4) {
			 System.out.println("//CCC/descendant::DDD:选择所有以CCC为祖先元素的DDD元素:" + nt.getName());
			 System.out.println("//CCC/descendant::DDD:选择所有以CCC为祖先元素的DDD元素:" + nt.getPath());
		 }
	}
	public static void examples13() throws DocumentException {
		System.out.println("parent轴(axis)包含上下文节点的父节点, 如果有父节点的话");
		String text = 
				"<AAA>\n" +
						"    <BBB>\n" + 
						"         <DDD>\n" + 
						"              <CCC>\n" + 
						"                   <DDD/>\n" + 
						"                   <EEE/>\n" + 
						"              </CCC>\n" + 
						"         </DDD>\n" + 
						"    </BBB>\n" + 
						"    <CCC>\n" + 
						"         <DDD>\n" + 
						"              <EEE>\n" + 
						"                   <DDD>\n" + 
						"                        <FFF/>\n" + 
						"                   </DDD>\n" + 
						"              </EEE>\n" + 
						"         </DDD>\n" + 
						"    </CCC>\n" + 
						"</AAA>";


		 Document docu = DocumentHelper.parseText(text);
		 List<Node> nodeList = docu.selectNodes("//DDD/parent::*");
		 for (Node nt : nodeList) {
			 System.out.println("//DDD/parent::*:选择DDD元素的所有父节点:" + nt.getName());
			 System.out.println("//DDD/parent::*:选择DDD元素的所有父节点:" + nt.getPath());
			 System.out.println("//DDD/parent::*:选择DDD元素的所有父节点:" + nt.getText());
		 }
	}
	
	public static void examples14() throws DocumentException {
		System.out.println("ancestor轴(axis)包含上下节点的祖先节点, 该祖先节点由其上下文节点的父节点以及父节点的父节点等等诸如此类的节点构成,所以ancestor轴总是包含有根节点,除非上下文节点就是根节点本身.");
		String text = 
				"<AAA>\n" +
						"          <BBB>\n" + 
						"               <DDD>\n" + 
						"                    <CCC>\n" + 
						"                         <DDD/>\n" + 
						"                         <EEE/>\n" + 
						"                    </CCC>\n" + 
						"               </DDD>\n" + 
						"          </BBB>\n" + 
						"          <CCC>\n" + 
						"               <DDD>\n" + 
						"                    <EEE>\n" + 
						"                         <DDD>\n" + 
						"                              <FFF/>\n" + 
						"                         </DDD>\n" + 
						"                    </EEE>\n" + 
						"               </DDD>\n" + 
						"          </CCC>\n" + 
						"     </AAA>";



		 Document docu = DocumentHelper.parseText(text);
		 List<Node> nodeList = docu.selectNodes("/AAA/BBB/DDD/CCC/EEE/ancestor::*");
		 for (Node nt : nodeList) {
			 System.out.println("/AAA/BBB/DDD/CCC/EEE/ancestor::*:选择一个绝对路径上的所有节点:" + nt.getName());
			 System.out.println("/AAA/BBB/DDD/CCC/EEE/ancestor::*:选择一个绝对路径上的所有节点:" + nt.getPath());
			 System.out.println("/AAA/BBB/DDD/CCC/EEE/ancestor::*:选择一个绝对路径上的所有节点:" + nt.getText());
		 }
	}
	
	public static void examples15() throws DocumentException {
		System.out.println("following-sibling轴(axis)包含上下文节点之后的所有兄弟节点.");
		String text = 
				"<AAA>\n" +
						"          <BBB>\n" + 
						"               <CCC/>\n" + 
						"               <DDD/>\n" + 
						"          </BBB>\n" + 
						"          <XXX>\n" + 
						"               <DDD>\n" + 
						"                    <EEE/>\n" + 
						"                    <DDD/>\n" + 
						"                    <CCC/>\n" + 
						"                    <FFF/>\n" + 
						"                    <FFF>\n" + 
						"                         <GGG/>\n" + 
						"                    </FFF>\n" + 
						"               </DDD>\n" + 
						"          </XXX>\n" + 
						"          <CCC>\n" + 
						"               <DDD/>\n" + 
						"          </CCC>\n" + 
						"     </AAA>";

		 Document docu = DocumentHelper.parseText(text);
		 List<Node> nodeList = docu.selectNodes("/AAA/BBB/following-sibling::*");
		 for (Node nt : nodeList) {
			 System.out.println("/AAA/BBB/following-sibling::*:注意是所有兄弟节点:" + nt.getName());
			 System.out.println("/AAA/BBB/following-sibling::*:注意是所有兄弟节点:" + nt.getPath());
			 System.out.println("/AAA/BBB/following-sibling::*:注意是所有兄弟节点:" + nt.getText());
		 }
		 
		 List<Node> nodeList1 = docu.selectNodes("//CCC/following-sibling::*");
		 for (Node nt : nodeList1) {
			 System.out.println("//CCC/following-sibling::*:注意是所有兄弟节点:" + nt.getName());
			 System.out.println("//CCC/following-sibling::*:注意是所有兄弟节点:" + nt.getPath());
			 System.out.println("//CCC/following-sibling::*:注意是所有兄弟节点:" + nt.getText());
		 }
	}
	
	public static void examples16() throws DocumentException {
		System.out.println("preceding-sibling 轴(axis)包含上下文节点之前的所有兄弟节点");
		String text = 
				"<AAA>\n" +
						"          <BBB>\n" + 
						"               <CCC/>\n" + 
						"               <DDD/>\n" + 
						"          </BBB>\n" + 
						"          <XXX>\n" + 
						"               <DDD>\n" + 
						"                    <EEE/>\n" + 
						"                    <DDD/>\n" + 
						"                    <CCC/>\n" + 
						"                    <FFF/>\n" + 
						"                    <FFF>\n" + 
						"                         <GGG/>\n" + 
						"                    </FFF>\n" + 
						"               </DDD>\n" + 
						"          </XXX>\n" + 
						"          <CCC>\n" + 
						"               <DDD/>\n" + 
						"          </CCC>\n" + 
						"     </AAA>";


		 Document docu = DocumentHelper.parseText(text);
		 List<Node> nodeList = docu.selectNodes("/AAA/XXX/preceding-sibling::*");
		 for (Node nt : nodeList) {
			 System.out.println("/AAA/XXX/preceding-sibling::*:注意是之前的所有兄弟节点:" + nt.getName());
			 System.out.println("/AAA/XXX/preceding-sibling::*:注意是之前的所有兄弟节点:" + nt.getPath());
			 System.out.println("/AAA/XXX/preceding-sibling::*:注意是之前的所有兄弟节点:" + nt.getText());
		 }
		 
		 List<Node> nodeList1 = docu.selectNodes("//CCC/preceding-sibling::*");
		 for (Node nt : nodeList1) {
			 System.out.println("//CCC/preceding-sibling::*:注意是之前的所有兄弟节点:" + nt.getName());
			 System.out.println("//CCC/preceding-sibling::*:注意是之前的所有兄弟节点:" + nt.getPath());
			 System.out.println("//CCC/preceding-sibling::*:注意是之前的所有兄弟节点:" + nt.getText());
		 }
	}
	
	public static void examples17() throws DocumentException {
		System.out.println("following轴(axis)包含同一文档中按文档顺序位于上下文节点之后的所有节点, 除了祖先节点,属性节点和命名空间节点");
		String text = 
				"<AAA>\n" +
						"      <BBB>\n" + 
						"           <CCC/>\n" + 
						"           <ZZZ>\n" + 
						"                <DDD/>\n" + 
						"                <DDD>\n" + 
						"                     <EEE/>\n" + 
						"                </DDD>\n" + 
						"           </ZZZ>\n" + 
						"           <FFF>\n" + 
						"                <GGG/>\n" + 
						"           </FFF>\n" + 
						"      </BBB>\n" + 
						"      <XXX>\n" + 
						"           <DDD>\n" + 
						"                <EEE/>\n" + 
						"                <DDD/>\n" + 
						"                <CCC/>\n" + 
						"                <FFF/>\n" + 
						"                <FFF>\n" + 
						"                     <GGG/>\n" + 
						"                </FFF>\n" + 
						"           </DDD>\n" + 
						"      </XXX>\n" + 
						"      <CCC>\n" + 
						"           <DDD/>\n" + 
						"      </CCC>\n" + 
						" </AAA>";

		 Document docu = DocumentHelper.parseText(text);
		 List<Node> nodeList = docu.selectNodes("/AAA/XXX/following::*");
		 for (Node nt : nodeList) {
			 System.out.println("/AAA/XXX/following::*:文档中按文档顺序位于上下文节点之后的所有节点, 除了祖先节点,属性节点和命名空间节点:" + nt.getName());
			 System.out.println("/AAA/XXX/following::*:文档中按文档顺序位于上下文节点之后的所有节点, 除了祖先节点,属性节点和命名空间节点:" + nt.getPath());
			 System.out.println("/AAA/XXX/following::*:文档中按文档顺序位于上下文节点之后的所有节点, 除了祖先节点,属性节点和命名空间节点:" + nt.getText());
		 }
		 
		 List<Node> nodeList1 = docu.selectNodes("//ZZZ/following::*");
		 for (Node nt : nodeList1) {
			 System.out.println("//ZZZ/following::*:文档中按文档顺序位于上下文节点之后的所有节点, 除了祖先节点,属性节点和命名空间节点:" + nt.getName());
			 System.out.println("//ZZZ/following::*:文档中按文档顺序位于上下文节点之后的所有节点, 除了祖先节点,属性节点和命名空间节点:" + nt.getPath());
			 System.out.println("//ZZZ/following::*:文档中按文档顺序位于上下文节点之后的所有节点, 除了祖先节点,属性节点和命名空间节点:" + nt.getText());
		 }
	}
	
	public static void examples18() throws DocumentException {
		System.out.println("following轴(axis)包含同一文档中按文档顺序位于上下文节点之前的所有节点, 除了祖先节点,属性节点和命名空间节点");
		String text = 
				"<AAA>\n" +
						"    <BBB>\n" + 
						"         <CCC/>\n" + 
						"         <ZZZ>\n" + 
						"              <DDD/>\n" + 
						"         </ZZZ>\n" + 
						"    </BBB>\n" + 
						"    <XXX>\n" + 
						"         <DDD>\n" + 
						"              <EEE/>\n" + 
						"              <DDD/>\n" + 
						"              <CCC/>\n" + 
						"              <FFF/>\n" + 
						"              <FFF>\n" + 
						"                   <GGG/>\n" + 
						"              </FFF>\n" + 
						"         </DDD>\n" + 
						"    </XXX>\n" + 
						"    <CCC>\n" + 
						"         <DDD/>\n" + 
						"    </CCC>\n" + 
						"</AAA>";

		 Document docu = DocumentHelper.parseText(text);
		 List<Node> nodeList = docu.selectNodes("/AAA/XXX/preceding::*");
		 for (Node nt : nodeList) {
			 System.out.println("/AAA/XXX/preceding::*:包含同一文档中按文档顺序位于上下文节点之前的所有节点, 除了祖先节点,属性节点和命名空间节点:" + nt.getName());
			 System.out.println("/AAA/XXX/preceding::*:包含同一文档中按文档顺序位于上下文节点之前的所有节点, 除了祖先节点,属性节点和命名空间节点:" + nt.getPath());
			 System.out.println("/AAA/XXX/preceding::*:包含同一文档中按文档顺序位于上下文节点之前的所有节点, 除了祖先节点,属性节点和命名空间节点:" + nt.getText());
		 }
		 
		 List<Node> nodeList1 = docu.selectNodes("//GGG/preceding::*");
		 for (Node nt : nodeList1) {
			 System.out.println("//GGG/preceding::*:包含同一文档中按文档顺序位于上下文节点之前的所有节点, 除了祖先节点,属性节点和命名空间节点:" + nt.getName());
			 System.out.println("//GGG/preceding::*:包含同一文档中按文档顺序位于上下文节点之前的所有节点, 除了祖先节点,属性节点和命名空间节点:" + nt.getPath());
			 System.out.println("//GGG/preceding::*:包含同一文档中按文档顺序位于上下文节点之前的所有节点, 除了祖先节点,属性节点和命名空间节点:" + nt.getText());
		 }
	}
	
	public static void examples19() throws DocumentException {
		System.out.println("descendant-or-self 轴(axis)包含上下文节点本身和该节点的后代节点");
		String text = "<AAA>\n" +
						"          <BBB>\n" + 
						"               <CCC/>\n" + 
						"               <ZZZ>\n" + 
						"                    <DDD/>\n" + 
						"               </ZZZ>\n" + 
						"          </BBB>\n" + 
						"          <XXX>\n" + 
						"               <DDD>\n" + 
						"                    <EEE/>\n" + 
						"                    <DDD/>\n" + 
						"                    <CCC/>\n" + 
						"                    <FFF/>\n" + 
						"                    <FFF>\n" + 
						"                         <GGG/>\n" + 
						"                    </FFF>\n" + 
						"               </DDD>\n" + 
						"          </XXX>\n" + 
						"          <CCC>\n" + 
						"               <DDD/>\n" + 
						"          </CCC>\n" + 
						"     </AAA>";


		 Document docu = DocumentHelper.parseText(text);
		 List<Node> nodeList = docu.selectNodes("/AAA/XXX/descendant-or-self::*");
		 for (Node nt : nodeList) {
			 System.out.println("/AAA/XXX/descendant-or-self::*:包含上下文节点本身和该节点的后代节点:" + nt.getName());
			 System.out.println("/AAA/XXX/descendant-or-self::*:包含上下文节点本身和该节点的后代节点:" + nt.getPath());
			 System.out.println("/AAA/XXX/descendant-or-self::*:包含上下文节点本身和该节点的后代节点:" + nt.getText());
		 }
		 
		 List<Node> nodeList1 = docu.selectNodes("//GGG/preceding::*");
		 for (Node nt : nodeList1) {
			 System.out.println("//CCC/descendant-or-self::*:包含上下文节点本身和该节点的后代节点:" + nt.getName());
			 System.out.println("//CCC/descendant-or-self::*:包含上下文节点本身和该节点的后代节点:" + nt.getPath());
			 System.out.println("//CCC/descendant-or-self::*:包含上下文节点本身和该节点的后代节点:" + nt.getText());
		 }
	}
	
	public static void examples20() throws DocumentException {
		System.out.println("ancestor-or-self 轴(axis)包含上下文节点本身和该节点的祖先节点");
		String text = "<AAA>\n" +
						"    <BBB>\n" + 
						"         <CCC/>\n" + 
						"         <ZZZ>\n" + 
						"              <DDD/>\n" + 
						"         </ZZZ>\n" + 
						"    </BBB>\n" + 
						"    <XXX>\n" + 
						"         <DDD>\n" + 
						"              <EEE/>\n" + 
						"              <DDD/>\n" + 
						"              <CCC/>\n" + 
						"              <FFF/>\n" + 
						"              <FFF>\n" + 
						"                   <GGG/>\n" + 
						"              </FFF>\n" + 
						"         </DDD>\n" + 
						"    </XXX>\n" + 
						"    <CCC>\n" + 
						"         <DDD/>\n" + 
						"    </CCC>\n" + 
						"</AAA>";

		 Document docu = DocumentHelper.parseText(text);
		 List<Node> nodeList = docu.selectNodes("/AAA/XXX/DDD/EEE/ancestor-or-self::*");
		 for (Node nt : nodeList) {
			 System.out.println("/AAA/XXX/DDD/EEE/ancestor-or-self::*:包含上下文节点本身和该节点的祖先节点:" + nt.getName());
			 System.out.println("/AAA/XXX/DDD/EEE/ancestor-or-self::*:包含上下文节点本身和该节点的祖先节点:" + nt.getPath());
			 System.out.println("/AAA/XXX/DDD/EEE/ancestor-or-self::*:包含上下文节点本身和该节点的祖先节点:" + nt.getText());
		 }
		 
		 List<Node> nodeList1 = docu.selectNodes("//GGG/ancestor-or-self::*");
		 for (Node nt : nodeList1) {
			 System.out.println("//GGG/ancestor-or-self::*:包含上下文节点本身和该节点的祖先节点:" + nt.getName());
			 System.out.println("//GGG/ancestor-or-self::*:包含上下文节点本身和该节点的祖先节点:" + nt.getPath());
			 System.out.println("//GGG/ancestor-or-self::*:包含上下文节点本身和该节点的祖先节点:" + nt.getText());
		 }
	}
	
	public static void examples21() throws DocumentException {
		System.out.println("ancestor, descendant, following, preceding 和self轴(axis)分割了XML文档(忽略属性节点和命名空间节点), 不能交迭, 而一起使用则包含所有节点");
		String text = "<AAA>\n" +
						"    <BBB>\n" + 
						"         <CCC/>\n" + 
						"         <ZZZ/>\n" + 
						"    </BBB>\n" + 
						"    <XXX>\n" + 
						"         <DDD>\n" + 
						"              <EEE/>\n" + 
						"              <FFF>\n" + 
						"                   <HHH/>\n" + 
						"                   <GGG>\n" + 
						"                        <JJJ>\n" + 
						"                             <QQQ/>\n" + 
						"                        </JJJ>\n" + 
						"                        <JJJ/>\n" + 
						"                   </GGG>\n" + 
						"                   <HHH/>\n" + 
						"              </FFF>\n" + 
						"         </DDD>\n" + 
						"    </XXX>\n" + 
						"    <CCC>\n" + 
						"         <DDD/>\n" + 
						"    </CCC>\n" + 
						"</AAA>";

		 Document docu = DocumentHelper.parseText(text);
		 List<Node> nodeList = docu.selectNodes("//GGG/ancestor::*");
		 for (Node nt : nodeList) {
			 System.out.println("//GGG/ancestor::*:不知道是啥看截图把:" + nt.getName());
			 System.out.println("//GGG/ancestor::*:不知道是啥看截图把:" + nt.getPath());
			 System.out.println("//GGG/ancestor::*:不知道是啥看截图把:" + nt.getText());
		 }
		 
		 List<Node> nodeList1 = docu.selectNodes("//GGG/descendant::*");
		 for (Node nt : nodeList1) {
			 System.out.println("//GGG/descendant::*:不知道是啥看截图把:" + nt.getName());
			 System.out.println("//GGG/descendant::*:不知道是啥看截图把:" + nt.getPath());
			 System.out.println("//GGG/descendant::*:不知道是啥看截图把:" + nt.getText());
		 }
		 
		 List<Node> nodeList2 = docu.selectNodes("//GGG/following::*");
		 for (Node nt : nodeList2) {
			 System.out.println("//GGG/following::*:不知道是啥看截图把:" + nt.getName());
			 System.out.println("//GGG/following::*:不知道是啥看截图把:" + nt.getPath());
			 System.out.println("//GGG/following::*:不知道是啥看截图把:" + nt.getText());
		 }
		 
		 List<Node> nodeList3 = docu.selectNodes("//GGG/preceding::*");
		 for (Node nt : nodeList3) {
			 System.out.println("//GGG/preceding::*:不知道是啥看截图把:" + nt.getName());
			 System.out.println("//GGG/preceding::*:不知道是啥看截图把:" + nt.getPath());
			 System.out.println("//GGG/preceding::*:不知道是啥看截图把:" + nt.getText());
		 }
		 
		 
		 List<Node> nodeList4 = docu.selectNodes("//GGG/self::*");
		 for (Node nt : nodeList4) {
			 System.out.println("//GGG/self::*:不知道是啥看截图把:" + nt.getName());
			 System.out.println("//GGG/self::*:不知道是啥看截图把:" + nt.getPath());
			 System.out.println("//GGG/self::*:不知道是啥看截图把:" + nt.getText());
		 }
		 
		 List<Node> nodeList5 = docu.selectNodes("//GGG/ancestor::* | //GGG/descendant::* | //GGG/following::* | //GGG/preceding::* | //GGG/self::*");
		 for (Node nt : nodeList5) {
			 System.out.println("//GGG/ancestor::* | //GGG/descendant::* | //GGG/following::* | //GGG/preceding::* | //GGG/self::*:不知道是啥看截图把:" + nt.getName());
			 System.out.println("//GGG/ancestor::* | //GGG/descendant::* | //GGG/following::* | //GGG/preceding::* | //GGG/self::*:不知道是啥看截图把:" + nt.getPath());
			 System.out.println("//GGG/ancestor::* | //GGG/descendant::* | //GGG/following::* | //GGG/preceding::* | //GGG/self::*:不知道是啥看截图把:" + nt.getText());
		 }
	}
	
	public static void examples22() throws DocumentException {
		System.out.println("div运算符做浮点除法运算, mod运算符做求余运算, floor函数返回不大于参数的最大整数(趋近于正无穷), ceiling返回不小于参数的最小整数(趋近于负无穷)");
		String text = "<AAA>\n" +
						"    <BBB/>\n" + 
						"    <BBB/>\n" + 
						"    <BBB/>\n" + 
						"    <BBB/>\n" + 
						"    <BBB/>\n" + 
						"    <BBB/>\n" + 
						"    <BBB/>\n" + 
						"    <BBB/>\n" + 
						"    <CCC/>\n" + 
						"    <CCC/>\n" + 
						"    <CCC/>\n" + 
						"</AAA>";


		 Document docu = DocumentHelper.parseText(text);
		 List<Node> nodeList = docu.selectNodes("//BBB[position() mod 2 = 0 ]");
		 for (Node nt : nodeList) {
			 System.out.println("//BBB[position() mod 2 = 0 ]:选择偶数位置的BBB元素:" + nt.getName());
			 System.out.println("//BBB[position() mod 2 = 0 ]:选择偶数位置的BBB元素:" + nt.getPath());
			 System.out.println("//BBB[position() mod 2 = 0 ]:选择偶数位置的BBB元素:" + nt.getText());
		 }
		 
		 List<Node> nodeList1 = docu.selectNodes("//BBB[ position() = floor(last() div 2 + 0.5) or position() = ceiling(last() div 2 + 0.5) ]");
		 for (Node nt : nodeList1) {
			 System.out.println("//BBB[ position() = floor(last() div 2 + 0.5) or position() = ceiling(last() div 2 + 0.5) ]:选择中间的BBB元素:" + nt.getName());
			 System.out.println("//BBB[ position() = floor(last() div 2 + 0.5) or position() = ceiling(last() div 2 + 0.5) ]:选择中间的BBB元素:" + nt.getPath());
			 System.out.println("//BBB[ position() = floor(last() div 2 + 0.5) or position() = ceiling(last() div 2 + 0.5) ]:选择中间的BBB元素:" + nt.getText());
		 }
		 
		 List<Node> nodeList2 = docu.selectNodes("//CCC[ position() = floor(last() div 2 + 0.5) or position() = ceiling(last() div 2 + 0.5) ]");
		 for (Node nt : nodeList2) {
			 System.out.println("//CCC[ position() = floor(last() div 2 + 0.5) or position() = ceiling(last() div 2 + 0.5) ]:选择中间的CCC元素" + nt.getName());
			 System.out.println("//CCC[ position() = floor(last() div 2 + 0.5) or position() = ceiling(last() div 2 + 0.5) ]:选择中间的CCC元素" + nt.getPath());
			 System.out.println("//CCC[ position() = floor(last() div 2 + 0.5) or position() = ceiling(last() div 2 + 0.5) ]:选择中间的CCC元素" + nt.getText());
		 }
	}
}
