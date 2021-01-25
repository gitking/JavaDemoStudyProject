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
 * List：对付顺序的好帮手,是一种知道索引位置的集合。
 * Set：注重独一无二的性质。不允许重复的集合
 * Map：用Key来搜索的专家
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
public class Jukebox1 {
	ArrayList<String> songList = new ArrayList<String>();
	public void go() {
		getSongs();
		System.out.println(songList);
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
		songList.add(tokens[0]);
		
	}
	public static void main(String[] args) {

	}
}
