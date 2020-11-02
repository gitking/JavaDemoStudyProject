package com.yale.test.design.graphic.factory.af;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Html文档接口
 * 注意到上面的抽象工厂AbstractFactory仅仅是一个接口，没有任何代码。同样的，因为HtmlDocument和WordDocument都比较复杂，现在我们并不知道如何实现它们，所以只有接口：
 * 这样，我们就定义好了抽象工厂（AbstractFactory）以及两个抽象产品（HtmlDocument和WordDocument）。因为实现它们比较困难，我们决定让供应商来完成。
 * 现在市场上有两家供应商：FastDoc Soft的产品便宜，并且转换速度快，而GoodDoc Soft的产品贵，但转换效果好。我们决定同时使用这两家供应商的产品，以便给免费用户和付费用户提供不同的服务。
 * 我们先看看FastDoc Soft的产品是如何实现的。首先，FastDoc Soft必须要有实际的产品，即FastHtmlDocument和FastWordDocument：
 * 然后，FastDoc Soft必须提供一个实际的工厂来生产这两种产品，即FastFactory：
 * 这样，我们就可以使用FastDoc Soft的服务了。客户端编写代码如下：
 * @author dell
 */
public interface HtmlDocument {
	String toHtml();
	void save(Path path) throws IOException;
}
