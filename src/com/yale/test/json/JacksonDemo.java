package com.yale.test.json;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/*
 * 使用JSON
 * 前面我们讨论了XML这种数据格式。XML的特点是功能全面，但标签繁琐，格式复杂。在Web上使用XML现在越来越少，取而代之的是JSON这种数据结构。
 * JSON是JavaScript Object Notation的缩写，它去除了所有JavaScript执行代码，只保留JavaScript的对象格式。一个典型的JSON如下：
 * {
	    "id": 1,
	    "name": "Java核心技术",
	    "author": {
	        "firstName": "Abc",
	        "lastName": "Xyz"
	    },
	    "isbn": "1234567",
	    "tags": ["Java", "Network"]
	}
 * JSON作为数据传输的格式，有几个显著的优点：
 * JSON只允许使用UTF-8编码，不存在编码问题；
 * JSON只允许使用双引号作为key，特殊字符用\转义，格式简单；
 * 浏览器内置JSON支持，如果把数据用JSON发送给浏览器，可以用JavaScript直接处理。
 * 因此，JSON适合表示层次结构，因为它格式简单，仅支持以下几种数据类型：
 * 键值对：{"key": value}
 * 数组：[1, 2, 3]
 * 字符串："abc"
 * 数值（整数和浮点数）：12.34
 * 布尔值：true或false
 * 空值：null
 * 浏览器直接支持使用JavaScript对JSON进行读写：
 * // JSON string to JavaScript object:
	jsObj = JSON.parse(jsonStr);
	// JavaScript object to JSON string:
	jsonStr = JSON.stringify(jsObj);
 * 所以，开发Web应用的时候，使用JSON作为数据传输，在浏览器端非常方便。因为JSON天生适合JavaScript处理，所以，绝大多数REST API都选择JSON作为数据传输格式。
 * 现在问题来了：使用Java如何对JSON进行读写？
 * 在Java中，针对JSON也有标准的JSR 353 API，但是我们在前面讲XML的时候发现，如果能直接在XML和JavaBean之间互相转换是最好的。类似的，
 * 如果能直接在JSON和JavaBean之间转换，那么用起来就简单多了。
 * 常用的用于解析JSON的第三方库有：Jackson,Gson,Fastjson
 * 注意到上一节提到的那个可以解析XML的浓眉大眼的Jackson也可以解析JSON！因此我们只需要引入以下Maven依赖：com.fasterxml.jackson.core:jackson-databind:2.10.0
 * 就可以使用下面的代码解析一个JSON文件：
 * 核心代码是创建一个ObjectMapper对象。关闭DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES功能使得解析时如果JavaBean不存在该属性时解析不会报错。
 * 把JSON解析为JavaBean的过程称为反序列化。如果把JavaBean变为JSON，那就是序列化。要实现JavaBean到JSON的序列化，只需要一行代码：
 * String json = mapper.writeValueAsString(book);
 * 要把JSON的某些值解析为特定的Java对象，例如LocalDate，也是完全可以的。例如：
 * {
	    "name": "Java核心技术",
	    "pubDate": "2016-09-01"
	}
	要解析为：
	public class Book {
	    public String name;
	    public LocalDate pubDate;
	}
 * 只需要引入标准的JSR 310关于JavaTime的数据格式定义至Maven：com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.10.0
 * 然后，在创建ObjectMapper时，注册一个新的JavaTimeModule：ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
 * 有些时候，内置的解析规则和扩展的解析规则如果都不满足我们的需求，还可以自定义解析。
 * 举个例子，假设Book类的isbn是一个BigInteger：
 * public class Book {
		public String name;
		public BigInteger isbn;
	}
 * 但JSON数据并不是标准的整形格式：
 * {
	    "name": "Java核心技术",
	    "isbn": "978-7-111-54742-6"
	}
 * 直接解析，肯定报错。这时，我们需要自定义一个IsbnDeserializer，用于解析含有非数字的字符串：
 * public class IsbnDeserializer extends JsonDeserializer<BigInteger> {
	    public BigInteger deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
	        // 读取原始的JSON字符串内容:
	        String s = p.getValueAsString();
	        if (s != null) {
	            try {
	                return new BigInteger(s.replace("-", ""));
	            } catch (NumberFormatException e) {
	                throw new JsonParseException(p, s, e);
	            }
	        }
	        return null;
	    }
	}
 * 然后，在Book类中使用注解标注：
 * public class Book {
	    public String name;
	    // 表示反序列化isbn时使用自定义的IsbnDeserializer:
	    @JsonDeserialize(using = IsbnDeserializer.class)
	    public BigInteger isbn;
	}
 * 类似的，自定义序列化时我们需要自定义一个IsbnSerializer，然后在Book类中标注@JsonSerialize(using = ...)即可。
 * 小结
 * JSON是轻量级的数据表示方式，常用于Web应用；
 * Jackson可以实现JavaBean和JSON之间的转换；
 * 可以通过Module扩展Jackson能处理的数据类型；
 * 可以自定义JsonSerializer和JsonDeserializer来定制序列化和反序列化。
 * 
 * 
{

    "id": 1,

    "name": "Java核心技术",

    "author": { "firstName": "Abc", "lastName": "Xyz" },

    "isbn": "1234567",

    "tags": ["Java", "Network"]}
    "firstName": "Abc", "lastName": "Xyz"是如何解析的？谢谢！
    很明显，需要再定义一个Author类：
    Class Author {
    public String firstName;
    public String lastName;
}
并在Book类添加成员属性：
public Author author;
就可以解析并通过book.author.firstName和book.author.lastName访问。
不知道有没有更好的处理方法。
public Map<String,String> author;我用map好像没问题的

fastJson与一起堆内存溢出'血案' https://club.perfma.com/article/1656271?from=timeline#/article/1656271?from=timeline
很简单，就是一定要记住fastjson序列化的时候要加上IgnoreNonFieldGetter就可以了。
SerializerFeature.IgnoreNonFieldGetter
首先getNextTrainCost这个getter中的nextTrainCost被当成了一个field，因为其返回值是一个JSONArray,其本身是可以作为setter用到的。其反序列化，用json中"nextTrainCost"相关反序列化
该字符串是[{".config.cost[0]"} 即使用了fastjson的循环引用,这个反序列化出来为[null] (因为本身config压根就不属于field,只是一个get方法而已)
然后调用setter(本身就是一个setter),得到cost,然后将这个[null] add到cost上
然后每反序列化一次都向cost中加入一个[null],进而使cost越来越大(JSONArray#底层数组还会自动expand)
 * 《定制 Jackson 解析器来完成对复杂格式 XML 的解析 ｜ Java Debug 笔记》https://juejin.cn/post/6961271701271216141?share_token=b126d332-8bd5-40df-9c7e-45089baca931
 * 《将海量动态数据以 JSON 格式导出 | Java Debug 笔记》https://juejin.cn/post/6961029395825819661?share_token=8aefa481-1a07-4499-a721-c62e41d92bf7
 */
public class JacksonDemo {
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		InputStream input = JacksonDemo.class.getResourceAsStream("/book.json");
		//核心代码是创建一个ObjectMapper对象。关闭DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES功能使得解析时如果JavaBean不存在该属性时解析不会报错。
		//要把JSON的某些值解析为特定的Java对象，例如LocalDate，也是完全可以的。例如：
		/*
		 * {
			    "name": "Java核心技术",
			    "pubDate": "2016-09-01"
			}
			要解析为：
			public class Book {
			    public String name;
			    public LocalDate pubDate;
			}
		 * 只需要引入标准的JSR 310关于JavaTime的数据格式定义至Maven：com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.10.0
		 * 然后，在创建ObjectMapper时，注册一个新的JavaTimeModule：
		 * ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
		 */
		ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());
		//反序列化时忽略不存在的javaBean属性
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		Book book = mapper.readValue(input, Book.class);
		System.out.println(book.id);
		System.out.println(book.name);
		System.out.println(book.author);
		System.out.println(book.isbn);
		System.out.println(book.tags);
		System.out.println(book.pubDate);
		System.out.println(book.price);
		
		//序列化为JSON
		//把JSON解析为JavaBean的过程称为反序列化。如果把JavaBean变为JSON，那就是序列化。要实现JavaBean到JSON的序列化，只需要一行代码：
		String json = mapper.writeValueAsString(book);
		System.out.println(json);
	}
}
