package com.yale.test.design.structural.decorator;

/*
 * 装饰器
 * 动态地给一个对象添加一些额外的职责。就增加功能来说，相比生成子类更为灵活。
 * 装饰器（Decorator）模式，是一种在运行期动态给某个对象的实例增加功能的方法。
 * 我们在IO的Filter模式一节中其实已经讲过装饰器模式了。在Java标准库中，InputStream是抽象类，FileInputStream、ServletInputStream、Socket.getInputStream()这些InputStream都是最终数据源。 
 * 现在，如果要给不同的最终数据源增加缓冲功能、计算签名功能、加密解密功能，那么，3个最终数据源、3种功能一共需要9个子类。如果继续增加最终数据源，或者增加新功能，子类会爆炸式增长，这种设计方式显然是不可取的。
 * Decorator模式的目的就是把一个一个的附加功能，用Decorator的方式给一层一层地累加到原始数据源上，最终，通过组合获得我们想要的功能。
 * 例如：给FileInputStream增加缓冲和解压缩功能，用Decorator模式写出来如下：
 * // 创建原始的数据源:
 * InputStream fis = new FileInputStream("test.gz");
 * // 增加缓冲功能:
 * InputStream bis = new BufferedInputStream(fis);
 * // 增加解压缩功能:
 * InputStream gis = new GZIPInputStream(bis);
 * 或者一次性写成这样：
 * InputStream input = new GZIPInputStream( // 第二层装饰
                        new BufferedInputStream( // 第一层装饰
                            new FileInputStream("test.gz") // 核心功能
                        ));
 * 观察BufferedInputStream和GZIPInputStream，它们实际上都是从FilterInputStream继承的，这个FilterInputStream就是一个抽象的Decorator。我们用图把Decorator模式画出来如下：
 *   		 ┌───────────┐
             │ Component │
             └───────────┘
                   ▲
      ┌────────────┼─────────────────┐
      │            │                 │
┌───────────┐┌───────────┐     ┌───────────┐
│ComponentA ││ComponentB │...  │ Decorator │
└───────────┘└───────────┘     └───────────┘
                                     ▲
                              ┌──────┴──────┐
                              │             │
                        ┌───────────┐ ┌───────────┐
                        │DecoratorA │ │DecoratorB │...
                        └───────────┘ └───────────┘
 * 最顶层的Component是接口，对应到IO的就是InputStream这个抽象类。ComponentA、ComponentB是实际的子类，对应到IO的就是FileInputStream、ServletInputStream这些数据源。
 * Decorator是用于实现各个附加功能的抽象装饰器，对应到IO的就是FilterInputStream。而从Decorator派生的就是一个一个的装饰器，它们每个都有独立的功能，对应到IO的就是BufferedInputStream、GZIPInputStream等。
 * Decorator模式有什么好处？它实际上把核心功能和附加功能给分开了。核心功能指FileInputStream这些真正读数据的源头，附加功能指加缓冲、压缩、解密这些功能。如果我们要新增核心功能，就增加Component的子类，例如ByteInputStream。
 * 如果我们要增加附加功能，就增加Decorator的子类，例如CipherInputStream。两部分都可以独立地扩展，而具体如何附加功能，由调用方自由组合，从而极大地增强了灵活性。
 * 如果我们要自己设计完整的Decorator模式，应该如何设计？
 * 我们还是举个栗子：假设我们需要渲染一个HTML的文本，但是文本还可以附加一些效果，比如加粗、变斜体、加下划线等。为了实现动态附加效果，可以采用Decorator模式。
 * 首先，仍然需要定义顶层接口TextNode：
 * 对于核心节点，例如<span>，它需要从TextNode直接继承：
 * 紧接着，为了实现Decorator模式，需要有一个抽象的Decorator类：
 * 这个NodeDecorator类的核心是持有一个TextNode，即将要把功能附加到的TextNode实例。接下来就可以写一个加粗功能：
 * 类似的，可以继续加ItalicDecorator、UnderlineDecorator等。客户端可以自由组合这些Decorator：
 * 小结
 * 使用Decorator模式，可以独立增加核心功能，也可以独立增加附加功能，二者互不影响；
 * 可以在运行期动态地给核心功能增加任意个附加功能。
 */
public interface TextNode {
	//设置Text
	void setText(String text);
	String getText();//获取Text
}
