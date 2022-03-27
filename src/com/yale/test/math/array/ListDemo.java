package com.yale.test.math.array;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;


/**
 * 本类和com.yale.test.math.map.PersonHashCode.java一起看
 * 本类和com.yale.test.math.map.MapDemo.java一起看
 * 本类和com.yale.test.math.array.SetDemo.java一起看
 * 因为List内部并不是通过==判断两个元素是否相等，而是使用equals()方法判断两个元素是否相等，例如contains()方法可以实现如下：
 * 因此，要正确使用List的contains()、indexOf()这些方法，放入的实例必须正确覆写equals()方法，否则，放进去的实例，查找不到。
 * 我们之所以能正常放入String、Integer这些对象，是因为Java标准库定义的这些类已经正确实现了equals()方法。
 * 我们以Person对象为例，测试一下：
 * 不出意外，虽然放入了new Person("Bob")，但是用另一个new Person("Bob")查询不到，原因就是Person类没有覆写equals()方法。
 * 编写equals
 * 如何正确编写equals()方法？equals()方法要求我们必须满足以下条件：
 *  自反性（Reflexive）：对于非null的x来说，x.equals(x)必须返回true；
    对称性（Symmetric）：对于非null的x和y来说，如果x.equals(y)为true，则y.equals(x)也必须为true；
    传递性（Transitive）：对于非null的x、y和z来说，如果x.equals(y)为true，y.equals(z)也为true，那么x.equals(z)也必须为true；
    一致性（Consistent）：对于非null的x和y来说，只要x和y状态不变，则x.equals(y)总是一致地返回true或者false；
    对null的比较：即x.equals(null)永远返回false。
 * 如果Person有好几个引用类型的字段，上面的写法就太复杂了。要简化引用类型的比较，我们使用java.util.Objects.equals()静态方法：
 * https://www.liaoxuefeng.com/wiki/1252599548343744/1265116446975264
 * @author issuser
 */
public class ListDemo {
	public static void main(String[] args) {
		List<String> list = List.of("A", "B", "C");
		System.out.println("list里面是否存在C这个字符:" + list.contains("C"));
		System.out.println("list里面是否存在X这个字符:" + list.contains("X"));
		System.out.println("list里面的C的下标索引:" + list.indexOf("C"));//2
		System.out.println("list里面的X的下标索引:" + list.contains("X"));//-1,如果元素不存在,就返回-1
		
		System.out.println("list里面是否存在C元素:" + list.contains(new String("C")));//true of false
		System.out.println("list里面的C的下标索引:" + list.indexOf(new String("C")));//2 or -1?
		
		/**
		 * 14. 【强制】不要在foreach循环里进行元素的remove/add操作。remove元素请使用Iterator方式，如果并发操作，需要对Iterator对象加锁。
		 * 正例如下:
		 * 《阿里巴巴Java开发手册嵩山版2020.pdf》
		 */
		List<String> listDemo = new ArrayList<String>();
		listDemo.add("1");
		listDemo.add("2");
		Iterator<String> iterator = listDemo.iterator();
		while (iterator.hasNext()) {
			String item = iterator.next();
			if (true) {
				iterator.remove();//安全删除
			}
		}
		
		//反例:说明：下面的代码的执行结果肯定会出乎大家的意料，那么试一下把“1”换成“2”，会是同样的结果吗？
		for (String item: list) {
			if ("1".equals(item)) {
				list.remove(item);
			}
		}
	}
}
