package com.yale.test.springmvc.controller.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

@Controller
public class FileUploadController {
	
	/**
	 * @RequestParam("file")这个必须写,否则会报错
	 * at org.springframework.web.multipart.commons.CommonsMultipartFile.<init>(CommonsMultipartFile.java:63)
	 * @param file
	 * @param req
	 * @return
	 */
	@RequestMapping("/upload")
	public String fileUpload(@RequestParam("file")CommonsMultipartFile file, HttpServletRequest req){
		System.out.println("Spring文件批量上传:");
		//通过HttpServletRequest获取文件的保存路径,
		//String path = req.getRealPath("/fileupload");这个方法不建议使用了
		String path = req.getServletContext().getRealPath("/fileupload");
		try {
			InputStream is = file.getInputStream();
			String fileName = file.getOriginalFilename();//得到上传的文件名
			OutputStream os = new FileOutputStream(new File(path, fileName));
			int len = 0;
			byte[] buffer = new byte[1024];
			while ((len = is.read(buffer)) != -1) {
				os.write(buffer, 0, len);
			}
			os.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "index";
	}
	
	@RequestMapping("/uploadbatch")
	public String fileUploadBatch(@RequestParam("file")CommonsMultipartFile file[], HttpServletRequest req, String desc){
		System.out.println("SpringMVC文件批量上传:");
		System.out.println("上传文件的同时还传其他数据:" + desc);
		//通过HttpServletRequest获取文件的保存路径,
		//String path = req.getRealPath("/fileupload");这个方法不建议使用了
		String path = req.getServletContext().getRealPath("/fileupload");
		try {
			for (int i=0;i<file.length;i++) {
				InputStream is = file[i].getInputStream();
				String fileName = file[i].getOriginalFilename();//得到上传的文件名
				OutputStream os = new FileOutputStream(new File(path, fileName));
				int len = 0;
				byte[] buffer = new byte[1024];
				while ((len = is.read(buffer)) != -1) {
					os.write(buffer, 0, len);
				}
				os.close();
				is.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "index";
	}
}
