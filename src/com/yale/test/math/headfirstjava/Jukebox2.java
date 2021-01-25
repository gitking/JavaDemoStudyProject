package com.yale.test.math.headfirstjava;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/*
 * 因为老板很抠,所以公司没买数据库应用程序,你只能靠内存上的数据集合,还有记录用的文本文件。
 * 你已经知道如何读取与解析文件,并且也用ArrayList来排序。
 * 但是ArrayList没有sort()这个方法。ArrayList不是唯一的集合
 * TreeSet以有序状态保持并可防止重复
 * HashMap可用成对的name/value来保持与取出
 * LinkedList针对经常插入或删除中间元素所设计的高效率集合(实际上还是ArrayList比较好用)
 * HashSet防止重复的集合,可快速的找寻相符的元素
 * LinkedHashMap类似HashMap,但可记住元素插入的顺序,也可以设定成依照元素上次存取的先后来排序。
 * 你可以使用TreeSet或Collections.sort()方法
 * 如果你把字符串放进TreeSet而不是ArrayList,这些String会自动地按照字母顺序排在正确的位置。
 * Collections.sort(List list)这个方法,参数是List,而ArrayList有实现List这个接口,所以ArrayList是一个List
 * 感谢多态机制,你确实可以把ArrayList传给用到List的方法
 * 如果你把字符串放进TreeSet而不是ArrayList,这些String会自动地按照字母顺序排在正确的位置。每当你想要列出清单时,元素总是会以字母顺序出现。
 * 当你需要set集合或总是会依照字母顺序排列的清单时,它会很好用。
 * 另外一方面,如果你没有需要让清单保持有序的状态,TreeSet的成本会比你想付出的还多--每当插入新项目时,它都必须要花时间找出适当的位置。
 * 而ArrayList只要把项目放在最后面就好。
 * 问:但你可以用指定的索引来添加新项目到ArrayList中,而不是放到最后面add()有个重载的方法可以指定int值。这样会比较慢吗？
 * 答:是的.插到指定位置会比直接加到最后面要慢。所以add(index, element)不会像add(element)这么快。但通常你不会对
 * ArrayList加上指定索引。
 * 问：使用LinkedList这个类会不会比较好?
 * 答：没错.LinkedList对于在中间的插入或删除会比较快,但对大多数的应用程序而言ArrayList与LinkedList的差异有限,除非元素量真的很大。
 */
public class Jukebox2 {
	ArrayList<Song> songList = new ArrayList<Song>();
	public void go() {
		getSongs();
		System.out.println(songList);
		/**
		 * 编译不通过,因为Collections.sort()方法不知道要按照什么规则来给SongList排序
		 * 原因是为什么?来看一下Collections.sort()的方法签名,这是因为sort使用了泛型(generic)功能
		 * public static <T extends Comparable<? super T>> void sort(List<T> list)
		 * 我们就这么说吧,几乎所有你会以泛型写的程序都与处理集合有关。虽然泛型可以用在其他地方,但它主要目的还是让你能够写出有类型安全性的集合。
		 * public class ArrayList<E> extends AbstractList<E> ...{
		 * 	public boolean add(E o);只能在此使用E,因为它已经被定义成类的一部分。
		 * }
		 * 会被编译器这样看待:
		 * public class ArrayList<String> extends AbstractList<String> ... {
		 * 	public boolean add(String o);
		 * }
		 * 也就是说,E会被所指定的真正类型所取代(又被称为类型参数)。
		 * 
		 * 问:只能用E吗？因为排序的文件上面用的是T....
		 * 答:你可以使用任何合法的Java标识字符串。这代表不管用什么都会被当作是类型参数。但习惯用法是以单一的字母表示(你也应该这么做),
		 * 除非与集合有关,否则都是用T,因为E很清楚地指明是元素(Element)。
		 * 
		 * public <T extends Animal> void takeThing(ArrayList<T> list);
		 * 因为在前面声明T所以这里就可以使用<T>
		 * 如果类本身没有使用类型参数,你还可以通过在一个不寻常但可行的位置上指定给定方法--在返回类型之前。
		 * 这个方法意味着T可以是"任何一种Animal"
		 * 怪异之处:
		 * public <T extends Animal> void takeThing(ArrayList<T> list);
		 * 跟这个是不一样的:
		 * public void takeThing(ArrayList<Animal> list)
		 * 这俩个方法的定义都合法,但意义不同
		 * 首先<T extends Animal> 是方法声明的一部分,表示任何被声明为Animal或Animal的子型(像是Cat或Dog)的ArrayList是合法的。
		 * 因此你可以使用ArrayList<Dog>,ArrayList<Cat>或ArrayList<Animal>来调用上面的方法。
		 * 但是,下面方法的参数是ArrayList<Animal> list,代表只有ArrayList<Animal>是合法的,也就是说第一个可以使用任何一种Animal的ArrayList,
		 * 而第二个方法只能使用Animal的ArrayList。
		 * 
		 * public static <T extends Comparable<? super T>> void sort(List<T> list)
		 * <T extends Comparable<? super T>>表示T必须是Comparable或Comparable的子类
		 * Comparable<? super T>表示Comparable的类型参数必须是T或T的父类
		 * List<T> list 表示只能传入Comparable的参数化类型的List
		 * 问:等等,我看了String类的源码,String类没有继承Comparable,String类只是实现了Comparable接口,
		 * 所以上面的extends不太合理吧？
		 * 答:以泛型的观点来说,extends代表extend或implement,对泛型来说,extends这个关键词代表"是一个...",且适用于类和接口
		 * Java设计团队有给你一种对参数化类型加上限制的方法,因此你可以加上只能使用Animal的子类之类的限制。但你也会需要限制只允许有实现某特定接口的类。
		 * 因此现在的状况需要对俩种情形都适用的语法--继承和实现。也就是说使用于extends和implements。
		 * 答案揭晓:extends。它确实代表"是一个...",且不管是接口或类都能适用。
		 */
		Collections.sort(songList);
		System.out.println("排序后的:" + songList);
	}
	void getSongs() {
		try {
			File file = new File("SongList.txt");
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = reader.readLine()) != null) {
				addSong(line);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	void addSong(String lineToParse) {
		String[] tokens = lineToParse.split("/");
		Song song = new Song(tokens[0],tokens[1],tokens[2],tokens[3]);
		songList.add(song);
		
	}
	public static void main(String[] args) {

	}
}
