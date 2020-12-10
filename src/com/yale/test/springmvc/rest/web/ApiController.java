package com.yale.test.springmvc.rest.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yale.test.springmvc.rest.entity.User;
import com.yale.test.springmvc.rest.service.UserService;

/*
 * 直接用Spring的Controller配合一大堆注解写REST太麻烦了，因此，Spring还额外提供了一个@RestController注解，
 * 使用@RestController替代@Controller后，每个方法自动变成API接口方法。我们还是以实际代码举例，编写ApiController如下：
 * 编写REST接口只需要定义@RestController，然后，每个方法都是一个API接口，输入和输出只要能被Jackson序列化或反序列化为JSON就没有问题。
 * 我们用浏览器测试GET请求，可直接显示JSON响应：
 * 要测试POST请求，可以用curl命令：
 * curl -v -H "Content-Type: application/json" -d '{"email":"bob@example.com","password":"bob123"}' http://localhost:8080/api/signin
	> POST /api/signin HTTP/1.1
	> Host: localhost:8080
	> User-Agent: curl/7.64.1
	> Accept: *\/*
	> Content-Type: application/json
	> Content-Length: 47
	> 
	< HTTP/1.1 200 
	< Content-Type: application/json
	< Transfer-Encoding: chunked
	< Date: Sun, 10 May 2020 08:14:13 GMT
	< 
	{"user":{"id":1,"email":"bob@example.com","password":"bob123","name":"Bob",...
 * 注意观察上述JSON的输出，User能被正确地序列化为JSON，但暴露了password属性，这是我们不期望的。要避免输出password属性，可以把User复制到另一个UserBean对象，该对象只持有必要的属性，
 * 但这样做比较繁琐。另一种简单的方法是直接在User的password属性定义处加上@JsonIgnore表示完全忽略该属性：
 * 但是这样一来，如果写一个register(User user)方法，那么该方法的User对象也拿不到注册时用户传入的密码了。如果要允许输入password，但不允许输出password，即在JSON序列化和反序列化时，
 * 允许写属性，禁用读属性，可以更精细地控制如下：
 * public class User {
	    ...
	
	    @JsonProperty(access = Access.WRITE_ONLY)
	    public String getPassword() {
	        return password;
	    }
	    ...
	}
 * 同样的，可以使用@JsonProperty(access = Access.READ_ONLY)允许输出，不允许输入。
 * 小结
 * 使用@RestController可以方便地编写REST服务，Spring默认使用JSON作为输入和输出。
 * 要控制序列化和反序列化，可以使用Jackson提供的@JsonIgnore和@JsonProperty注解。
 * 问:廖老师：请教一下：一定要映入jakson的依赖com.fasterxml.jackson.core:jackson-databind:2.11.0，
 * 廖:建议你用默认的jackson,如果一定要用其他的，只能自己去读spring文档
 * 
 * JSON里面到处都是引号，所以-d要用单引号：
 * -d ' xxxx '
 */
@RestController
@RequestMapping("/api")
public class ApiController {
	
	@Autowired
	UserService userService;
	
	@GetMapping("/users")
	public List<User> users() {
		return userService.getUsers();
	}
	
	@GetMapping("/users/{id}")
	public User user(@PathVariable("id")long id){
		return userService.getUserById(id);
	}
	
	@PostMapping("/signin")
	public Map<String, Object> signin(@RequestBody SignInRequest signinRequest) {
		try {
			User user = userService.signin(signinRequest.email, signinRequest.password);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("user", user);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("error", "SIGNIN_FAILED");
			result.put("message", e.getMessage());
			return result;
		}
	}
	
	/*
	 * 为什么SignInRequest必须是一个静态内部类？如果去掉static，jackson会报错无法实例化它？
	 * 非static类隐式地引用了主类实例，不能独立于主类使用
	 */
	public static class SignInRequest {
		public String email;
		public String password;
	}
}
