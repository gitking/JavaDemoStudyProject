package com.yale.test.xml.jackson.demo4;

/*
 * 将海量动态数据以 JSON 格式导出 | Java Debug 笔记
 * https://juejin.cn/post/6961029395825819661?share_token=8aefa481-1a07-4499-a721-c62e41d92bf7
 * 如果想一次性导出海量数据为 JSON 文件（不要问我为什么有这种需求），一次性全部查询出来然后通过 JSON 库去序列化，一定是不现实的。因为如果一次导出的数据有 1GB，那么有 10 个人同时导出，实时内存占用就会达到 10 GB，而且还是不能回收的那种。
 * 再大的内存也扛不住这个玩法，所以需要换一种方式，增量导出。每次只获取一部分数据，写出到 JSON 串里，完成 JSON 串的写入，最后再进行输出（到文件或者其他 OutputStream）。
 * 但这样还是会有一个问题，虽说获取的数据是增量的，每次内存中只会存在这部分获取的数据；但写到 JSON 串这个操作，还是会导致所有数据堆积在内存中……​
 * 好在有足够强大的 Jackson，对于 JSON 序列化这个操作，它支持“增量”的模式。基于 SequenceWriter 来写入的话，可以做到每次只 write 一部分数据，在最后才执行 end 操作，完成结束的写入
 * // 记得维护单例
 * ObjectMapper jsonMapper = new ObjectMapper();
 * // 记得维护单例
 * ObjectWriter writer = jsonMapper.writer().withDefaultPrettyPrinter();
 * // 使用 writer.writeValues(OutputStream); 的方式，来创建 SequenceWriter
 * SequenceWriter sequenceWriter = writer.writeValues(outputstream);
 * for (int i = 0; i < 100; i++) {
	    // 然后对 sequenceWriter 执行 writeAll，即可完成“增量”创建 json 串
		sequenceWriter.writeAll(createData(100));
	}
 * // 最后一定要对 sequenceWriter close，才可以完成写入
 * sequenceWriter.close();
 * private List<Map<String,Object>> createData(int size){
    	List<Map<String,Object>> dataList = new ArrayList<>();
	    for (int i = 0; i < size; i++) {
	        Map<String,Object> dataMap = new HashMap<>();
	        dataMap.put("A", RandomStringUtils.randomNumeric(10000));
	        dataMap.put("B", RandomStringUtils.randomNumeric(10000));
	        dataMap.put("C", RandomStringUtils.randomNumeric(10000));
	        dataMap.put("D", RandomStringUtils.randomNumeric(10000));
	        dataMap.put("E", RandomStringUtils.randomNumeric(10000));
	        dataList.add(dataMap);
	    }
	    return dataList;
	}
 * 这样一来就避免了内存中存储全量数据的问题，增量导出的时候只有一组数据会驻留在内存中，再多的数据也没问题。
 * 配合 Servlet/Spring MVC 中下载
 * 基于上面的增量导出逻辑，如果在 Servlet 场景中完成这个增量数据导出 JSON 文件的功能呢？​
 * 由于数据是增量的，无法一次保存至内存中，所以根本不知道数据的总大小，Content-Length更是无法设置了。不过 HTTP 协议中还有一个 Chunked 编码格式，
 * 在 Chunked 下是不需要 Content-Length 的（关于 Chunked 编码可以参考https://juejin.cn/post/6844903956460601357#heading-8），客户端只需要解析每个 Chunk 部分，即可获取全部报文。
 * 细说 Http 中的 Keep-Alive 和 Java Http 中的 Keep-Alive 机制https://juejin.cn/post/6844903956460601357#heading-8
 * Jackson 可不止 JSON
 * Jackson 可能有些人认为它只是个 JSON 库，其实不止。它除了支持 JSON 序列化以外，还支持 XML/YAML/Properties/CSV 等等，而且 Jackson 是将基础功能进行了抽象，每一种序列化都是扩展的形式存在；所以上面的增量输出 SequenceWriter，也是支持其他格式的！
	作者：空无
	链接：https://juejin.cn/post/6961029395825819661
	来源：掘金
	著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
public class JacksonExportTest {
	public static void main(String[] args) {

	}
}
