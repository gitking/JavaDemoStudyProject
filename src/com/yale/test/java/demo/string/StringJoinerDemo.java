package com.yale.test.java.demo.string;

import java.util.StringJoiner;

/*
 * 要高效拼接字符串，应该使用StringBuilder。
 */
public class StringJoinerDemo {
	public static void main(String[] args) {
		//很多时候，我们拼接的字符串像这样：Hello Bob, Alice, Grace!
		String[] names = {"Bob", "Alice", "Grace"};
		StringBuilder sb = new StringBuilder();
        sb.append("Hello ");
        for (String name : names) {
            sb.append(name).append(", ");
        }
        // 注意去掉最后的", ":
        sb.delete(sb.length() - 2, sb.length());
        sb.append("!");
        System.out.println(sb.toString());
        
        //类似用分隔符拼接数组的需求很常见，所以Java标准库还提供了一个StringJoiner来干这个事：
        //jdk1.8才有StringJoiner
        String[] nameJon = {"Bob", "Alice", "Grace"};
        StringJoiner sj = new StringJoiner(", ");
        for (String name : nameJon) {
            sj.add(name);
        }
        System.out.println(sj.toString());
        
        //慢着！用StringJoiner的结果少了前面的"Hello "和结尾的"!"！遇到这种情况，需要给StringJoiner指定“开头”和“结尾”：
        String[] nameJoin = {"Bob", "Alice", "Grace"};
        StringJoiner sjo = new StringJoiner(", ", "Hello ", "!");
        for (String name : nameJoin) {
        	sjo.add(name);
        }
        System.out.println(sjo.toString());
        
        //String还提供了一个静态方法join()1.8才有，这个方法在内部使用了StringJoiner来拼接字符串，
        //在不需要指定“开头”和“结尾”的时候，用String.join()更方便：
        String[] nameArr = new String[]{"Bob", "Alice", "Grace"};
        String sjoin = String.join(", ", nameArr);
        System.out.println("Stirng.join:" + sjoin);
	}
}
