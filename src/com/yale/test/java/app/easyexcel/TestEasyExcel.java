package com.yale.test.java.app.easyexcel;

import java.io.IOException;

import com.yale.test.java.app.easyexcel.util.EasyExcelUtils;

public class TestEasyExcel {

	public static void main(String[] args) {
		EasyExcelUtils eeu = new EasyExcelUtils();
		try {
			eeu.writeExcelOneSheetOnceWriter();
			eeu.writeExcelOneSheetMoreWrite();
			eeu.writeExcelMoreSheetMoreWrite();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
