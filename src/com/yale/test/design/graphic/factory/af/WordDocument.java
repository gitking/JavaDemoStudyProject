package com.yale.test.design.graphic.factory.af;

import java.io.IOException;
import java.nio.file.Path;

//Word文档接口
public interface WordDocument {
	void save(Path path) throws  IOException;
}
