package com.yale.test.xml.jackson.demo4;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SequenceWriter;

//https://juejin.cn/post/6961029395825819661?share_token=8aefa481-1a07-4499-a721-c62e41d92bf7
public class JacksonExport {
	ObjectMapper jsonMapper = new ObjectMapper();//记得维护单例模式

	public void exportJsonData() throws IOException {
		ObjectWriter writer = jsonMapper.writer().withDefaultPrettyPrinter();//这个对象也记得需要维护成单例模式
		
		OutputStream outputStream = new FileOutputStream(new File(""));
		//使用wirter.wirterValues(OutputStream);的方式,来创建SequenceWriter
		SequenceWriter sequenceWriter = writer.writeValues(outputStream);
		
		for (int i=0;i<100; i++) {
			sequenceWriter.writeAll(createData(100));
		}
		sequenceWriter.close();//最后一定要对sequenceWriter进行close,才可以完成写入.
	}
	
	private List<Map<String, Object>> createData(int size) {
		List<Map<String, Object>> dataList = new ArrayList<>(size);
		for (int i=0; i<size; i++) {
			Map<String, Object> dataMap = new HashMap<>();
			//创建一个随机字符串，其长度为指定的字符数。将从数字字符集中选择字符。 
			dataMap.put("A", RandomStringUtils.randomNumeric(10000));
			dataMap.put("B", RandomStringUtils.randomNumeric(10000));
			dataMap.put("C", RandomStringUtils.randomNumeric(10000));
			dataMap.put("D", RandomStringUtils.randomNumeric(10000));
			dataMap.put("E", RandomStringUtils.randomNumeric(10000));
			dataList.add(dataMap);
		}
		return dataList;
	}
	
	/*
	 * 配合 Servlet/Spring MVC 中下载
	 * 配合 Servlet/Spring MVC 中下载
	 * 基于上面的增量导出逻辑，如果在 Servlet 场景中完成这个增量数据导出 JSON 文件的功能呢？​
	 * 由于数据是增量的，无法一次保存至内存中，所以根本不知道数据的总大小，Content-Length更是无法设置了。不过 HTTP 协议中还有一个 Chunked 编码格式，
	 * 在 Chunked 下是不需要 Content-Length 的（关于 Chunked 编码可以参考https://juejin.cn/post/6844903956460601357#heading-8），客户端只需要解析每个 Chunk 部分，即可获取全部报文。
	 * 细说 Http 中的 Keep-Alive 和 Java Http 中的 Keep-Alive 机制https://juejin.cn/post/6844903956460601357#heading-8
	 * 如果是在 Tomcat 下，当 OutputStream 写入的数据过多时（大于8K），会自动使用 Chunked 编码，无需手动设置
	 * Jackson 可不止 JSON
	 * Jackson 可能有些人认为它只是个 JSON 库，其实不止。它除了支持 JSON 序列化以外，还支持 XML/YAML/Properties/CSV 等等，而且 Jackson 是将基础功能进行了抽象，每一种序列化都是扩展的形式存在；所以上面的增量输出 SequenceWriter，也是支持其他格式的！
		作者：空无
		链接：https://juejin.cn/post/6961029395825819661
		来源：掘金
		著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
	 */
	@GetMapping("download")
	public void download(HttpServletResponse httpServletResponse) throws IOException {
		String fileName = "";
		//直接给个文件名,不需要设置Content-Length
		httpServletResponse.setHeader("Content-Disposition", "attchment;filename=" + fileName + "data.json");
		ObjectWriter writer = jsonMapper.writer().withDefaultPrettyPrinter();
		//使用servlet resp作为SequenceWriter outputStream
		SequenceWriter sequenceWriter = writer.writeValues(httpServletResponse.getOutputStream());
		for (int i=0; i<100; i++) {
			sequenceWriter.writeAll(createData(100));
		}
		sequenceWriter.close();
	}
}
