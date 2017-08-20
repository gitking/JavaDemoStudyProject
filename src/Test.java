import com.yale.test.run.RunntimeTest;

public class Test {
	public static void main(String[] args) {
		System.out.println(System.getProperty("user.dir"));
		String str = "abc";
		Test.isSame(str);
		Test.testStr(str);;
		System.out.println(str);
	}
	
	public static void isSame(String str){
		String strSec = "abc";
		if (strSec == str) {
			System.out.println(true);
		} else {
			System.out.println(false);
		}
	}
	
	public static void testStr (String str) {
		str += "追加";
		System.out.println(str);
	}
}
