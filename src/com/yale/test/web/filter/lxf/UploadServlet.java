package com.yale.test.web.filter.lxf;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.output.ByteArrayOutputStream;

@WebServlet(urlPatterns="/upload/file")
public class UploadServlet extends HttpServlet{
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		InputStream input = req.getInputStream();
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		for(;;) {
			int len = input.read(buffer);
			if (len == -1) {
				break;
			}
			output.write(buffer, 0, len);
		}
		String uploadedText = output.toString(StandardCharsets.UTF_8);
		System.out.println("upload: " + uploadedText);
		PrintWriter pw = resp.getWriter();
		pw.write("<h1>Uploaded;</h1>");
		pw.write("<pre><code>");
		pw.write(uploadedText);
		pw.write("</code></pre>");
		pw.flush();
	}
}
