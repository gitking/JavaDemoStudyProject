package com.yale.test.math;

/**
 * https://www.liaoxuefeng.com/article/895884925598720
 * @author issuser
 */
public class BooleanDemo {
	/**
	 * 另外注意到Boolean类实际上只有两种不同状态的实例：一个包装true，一个包装false，Boolean又是immutable class，所以在内存中相同状态的Boolean实例完全可以共享，
	 * 不必用new创建很多实例。因此Boolean class还提供两个静态变量：
	 * public static final Boolean TRUE = new Boolean(true);
	 * public static final Boolean FALSE = new Boolean(false);
	 * 这两个变量在Class Loader装载时就被实例化，并且申明为final，不能再指向其他实例。
	 * 提供这两个静态变量是为了让开发者直接使用这两个变量而不是每次都new一个Boolean，这样既节省内存又避免了创建一个新实例的时间开销。
	 * 因此，用 Boolean b = Boolean.TRUE; 比 Boolean b = new Boolean(true);要好得多。
	 * 如果遇到下面的情况：
	 * Boolean b = new Boolean(var);
	 * 一定要根据一个boolean变量来创建Boolean实例怎么办？推荐使用Boolean提供的静态工厂方法：
	 * Boolean b = Boolean.valueOf(var);
	 * 这样就可以避免创建新的实例。看看valueOf()静态方法：
	 * public static Boolean valueOf(boolean b) {
		    return (b ? TRUE : FALSE);
		}
	 * 这个静态工厂方法返回的仍然是两个静态变量TRUE和FALSE之一，而不是new一个Boolean出来。虽然Boolean非常简单，占用的内存也很少，
	 * 但是一个复杂的类用new创建实例的开销可能非常大，而且，使用工厂方法可以方便的实现缓存实例，这对客户端是透明的。所以，能用工厂方法就不要使用new。
	 * 和Boolean只有两种状态不同，Integer也是immutable class，但是状态上亿种，不可能用静态实例缓存所有状态。不过，SUN的工程师还是作了一点优化，Integer类缓存了-128到127这256个状态的Integer，如果使用Integer.valueOf(int i)，传入的int范围正好在此内，就返回静态实例。
	 * hashCode()方法很奇怪，两种Boolean的hash code分别是1231和1237。估计写Boolean.java的人对这两个数字有特别偏好：
	 * public int hashCode() {
		    return value ? 1231 : 1237;
	 * }
	 * 很多人写equals()总是在第一行写一个null判断：
	 * if (obj==null)
	 * 		return false;
	 * 其实完全没有必要，因为如果obj==null，下一行的if (obj instanceof Type)就肯定返回false，因为(null instanceof AnyType)永远是false。
	 * 详细内容请参考《Effective Java》第7条：Obey the general contract when overriding equals。
	 * 总结
	 * 如果一个类只有有限的几种状态，考虑用几个final的静态变量来表示不同状态的实例。例如编写一个Weekday类，状态只有7个，就不要让用户写new Weekday(1)，直接提供Weekday.MONDAY即可。
	 * 要防止用户使用new生成实例，就取消public构造方法，用户要获得静态实例的引用有两个方法：如果申明了public static var，就可以直接访问，比如Boolean.TRUE，第二个方法是通过静态工厂方法：Boolean.valueOf(?)。
	 * 如果不提供public构造方法，让用户只能通过上面的方法获得静态变量的引用，还可以大大简化equals()方法：
	 * public boolean equals(Object obj) {
		    return this==obj;
		}
	 */
}
