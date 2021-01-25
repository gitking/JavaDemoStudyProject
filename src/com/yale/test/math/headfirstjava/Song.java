package com.yale.test.math.headfirstjava;

/**
 * 你的主要任务就是决定如何判断Song的大小或先后,然后以compareTo()方法的实现来反映出这个逻辑。
 * 返回负数值表示传入的Song大于执行的Song,正数刚好相反。而返回0代表相等(以排序的目的来说,并不代表 倆对象真的相等)
 * Collections.sort()有重载版本,可以使用Comparator类型的参数。
 * 使用Comparable接口的compareTo()方法时,只能讲比较的规则写死,只有一种可以比较的方法。
 * 但Comparator是独立于所比较元素类型之外的,Comparator是独立的类。因此你可以实现多个类来实现多个不同的比较规则。
 * 注意,如果你调用的是Collections.sort()的Comparator的重载版本,那么sort方法就不会调用元素的compareTo()方法,
 * 而会去调用Comparator的compare()方法
 * 问:是否这代表如果类没有实现Comparable,且你也没有拿到源码,还是能通过Comparator来排序?
 * 答:没错,另外一种方法就是子类化该元素并实现出Comparable.
 * 问：为什么不是每个类都默认实现Comparable?
 * 答:不是每种东西都可以拿来比较的
 * @author dell
 */
public class Song implements Comparable<Song>{
	String title;
	String artist;
	String rating;
	String bpm;
	public Song(String title, String artist, String rating, String bpm) {
		super();
		this.title = title;
		this.artist = artist;
		this.rating = rating;
		this.bpm = bpm;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
	public String getBpm() {
		return bpm;
	}
	public void setBpm(String bpm) {
		this.bpm = bpm;
	}
	@Override
	public String toString() {
		return title;
	}
	
	@Override
	public int compareTo(Song s) {
		return title.compareTo(s.getTitle());
	}
	
	/*
	 * 俩个对象怎么才算相等一样,其实应该这样问俩个引用地址不一样的对象,怎么才算相等。
	 * 这里的相等要分俩种:1,引用相等 2,对象相等
	 * 1,引用相等,引用到堆上同一个对象的俩个引用是相等。这个不用说了,因为你的引用指向的是堆上同一个对象。
	 * hashCode()默认的行为会返回每个对象特有的序号(大部分的Java版本是依据内存位置计算此序号,所以不会有相同的hashCode)
	 * 如果想要知道俩个引用是否相等,可以使用==来比较变量上的字节组合。如果引用到相同的对象,字节组合也会一样。
	 * 2,对象相等：堆上的俩个不同对象在意义上是相同的。
	 * 如果你想要把俩个不同的Song对象视为相等的,就必须覆盖hashCode方法与equals方法。
	 * 当你把对象加入HashSet时,它会使用对象的hashCode值来判断对象加入的位置。但同时也会与其他已经加入的对象的hashCode作比对。
	 * 如果没有一样的hashCode,HashSet就会假设新对象没有重复出现。但是,即使hashCode相等,俩个对象也不一定相等。如果俩个对象的hashCode
	 * 相等,HashSet还会调用对象的equals方法来判断俩个对象是否真的相等。如果相等HashSet就不会重复添加。
	 */
	@Override
	public boolean equals(Object aSong) {
		Song s = (Song)aSong;
		return getTitle().equals(s.getTitle());
	}
	
	/**
	 * 重写equals和hashCode是为了能让HashSet正确识别俩个Song是否相等
	 * 
	 */
	@Override
	public int hashCode() {
		/*
		 * 注意hashCode方法和equals方法都是用title这个属性来比较的,这个很重要.
		 * 因为Java规定:如果俩个对象的equals方法相等,那么他们的hashCode也必须一样。
		 * 但是俩个对象的hashCode相等,则他们的equals方法不必一定相等。
		 * 因此如果重写equals方法,则必须重新hashCode方法。
		 * hashCode的默认返回值是对在heap上的对象产生独特的值。如果你没有重写过hashCode()方法,则该class的俩个对象
		 * 的hashCode怎么样都不会一样。
		 * equals的默认行为是用==比较的,也就是说默认会去测试俩个引用是否对上heap上同一个对象。如果equals没有被重写过
		 * 俩个对象永远都不会相等。因为不同的对象有不同的字节组合。
		 * HashSet使用hashCode来表达成存取速度较快的存储方法。HashSet使用hashCode来寻找符合条件的元素。因此当你想要寻找某个对象时,
		 * 通过hashCode就可以很快地算出该对象所在的位置,而不必从头一个一个找起。越糟糕的算法越容易碰撞,但这也与数据值域分布的特性有关,
		 * 总而言之,hashCode是用来缩小寻找成本,但最后还是要用equals()才能认定是否真的找到相同的项目。 
		 */
		return title.hashCode();
	}
}
