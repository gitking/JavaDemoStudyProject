package com.yale.test.design.graphic.builder;

/*
 * 生成器
 * 将一个复杂对象的构建与它的表示分离，使得同样的构建过程可以创建不同的表示。
 * 生成器模式（Builder）是使用多个“小型”工厂来最终创建出一个完整对象。
 * 当我们使用Builder的时候，一般来说，是因为创建这个对象的步骤比较多，每个步骤都需要一个零部件，最终组合成一个完整的对象。
 * 我们仍然以Markdown转HTML为例，因为直接编写一个完整的转换器比较困难，但如果针对类似下面的一行文本：# this is a heading转换成HTML就很简单：<h1>this is a heading</h1>
 * 因此，我们把Markdown转HTML看作一行一行的转换，每一行根据语法，使用不同的转换器：
 * 如果以#开头，使用HeadingBuilder转换；
 * 如果以>开头，使用QuoteBuilder转换；
 * 如果以---开头，使用HrBuilder转换；
 * 其余使用ParagraphBuilder转换。
 * 这个HtmlBuilder写出来如下：
 */
public class HtmlBuilder {
	private HeadingBuilder headingBuilder = new HeadingBuilder();
	private HrBuilder hrBuilder = new HrBuilder();
	private ParagraphBuilder paragraphBuilder = new ParagraphBuilder();
	private QuoteBuilder quoteBuilder = new QuoteBuilder();
	
	/*
	 * 注意观察下面的代码，HtmlBuilder并不是一次性把整个Markdown转换为HTML，而是一行一行转换，并且，它自己并不会将某一行转换为特定的HTML，
	 * 而是根据特性把每一行都“委托”给一个XxxBuilder去转换，最后，把所有转换的结果组合起来，返回给客户端。
	 * 这样一来，我们只需要针对每一种类型编写不同的Builder。例如，针对以#开头的行，需要HeadingBuilder：
	 * 注意：实际解析Markdown是带有状态的，即下一行的语义可能与上一行相关。这里我们简化了语法，把每一行视为可以独立转换。
	 * 可见，使用Builder模式时，适用于创建的对象比较复杂，最好一步一步创建出“零件”，最后再装配起来。
	 * JavaMail的MimeMessage就可以看作是一个Builder模式，只不过Builder和最终产品合二为一，都是MimeMessage：
	 * 很多时候，我们可以简化Builder模式，以链式调用的方式来创建对象。例如，我们经常编写这样的代码：
	 * StringBuilder builder = new StringBuilder();
		builder.append(secure ? "https://" : "http://")
		       .append("www.liaoxuefeng.com")
		       .append("/")
		       .append("?t=0");
		String url = builder.toString(); 
	 * 小结
	 * Builder模式是为了创建一个复杂的对象，需要多个步骤完成创建，或者需要多个零件组装的场景，且创建过程中可以灵活调用不同的步骤或组件。
	 */
	public String toHtml(String markdown) {
		StringBuilder buffer = new StringBuilder();
//		markdown.lines().forEach(line -> {
//            if (line.startsWith("#")) {
//                buffer.append(headingBuilder.buildHeading(line)).append('\n');
//            } else if (line.startsWith(">")) {
//                buffer.append(quoteBuilder.buildQuote(line)).append('\n');
//            } else if (line.startsWith("---")) {
//                buffer.append(hrBuilder.buildHr(line)).append('\n');
//            } else {
//                buffer.append(paragraphBuilder.buildParagraph(line)).append('\n');
//            }
//        });
		
		if (markdown.startsWith("#")){
			buffer.append(headingBuilder.buildHeading(markdown)).append('\n');
		} else if (markdown.startsWith(">")) {
			buffer.append(quoteBuilder.buildQuote(markdown)).append('\n');
		} else if (markdown.startsWith("---")) {
			buffer.append(hrBuilder.buildHr(markdown)).append('\n');
		} else {
			buffer.append(paragraphBuilder.buildParagraph(markdown)).append('\n');
		}
        return buffer.toString();
	}
}
