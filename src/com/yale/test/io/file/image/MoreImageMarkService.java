package com.yale.test.io.file.image;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 第6章 实现图片添加多个文字水印
 * 本章将介绍如何实现在图片上添加多个文字水印 
 * https://www.imooc.com/video/10603
 * 视频： 6-1 java 实现添加多个文字水印（上） (04:07)
 * 视频： 6-2 java 实现添加多个文字水印（下） (03:58)
 * @author issuser
 */
public class MoreImageMarkService {
	public static final int FONT_SIZE = 120;
	public static final String MARK_TEXT = "慕课网";//文字水印内容
	public void watermark() {
		try {
			Image image2 = ImageIO.read(new File("C:\\Users\\issuser\\Desktop\\17cfd97aea9_2c1.jpeg"));
			int width = image2.getWidth(null);//原图片的宽度
			int height = image2.getHeight(null);//原图片的高度
			
			//第一步:创建图片缓存对象
			BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			
			//第二步:创建Java绘图工具对象,绘图工具对象,二维
			Graphics2D g = bufferedImage.createGraphics();
			//第三步:使用绘图工具对象将原图绘制到缓存图片对象
			g.drawImage(image2, 0, 0, width, height, null);
			
			//读取我们指定的水印图片
			File logo = new File("C:\\Users\\issuser\\Desktop\\a22b237bd7b7cbfe0da62f0de6d47288.jpeg");
			Image logoImage = ImageIO.read(logo);
			int width1 = logoImage.getWidth(null);//获得水印图片的宽度
			int height1 = logoImage.getHeight(null);//获得水印图片的高度
			
			//设置文字水印的透明度,值为0到1之间，做一个淡化处理。
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, 0.3F));
			
			//旋转水印图片,第一个参数是旋转度数,需要注意的是这里的度数单位是弧度,而不是我们习惯的角度,这里我们将图片旋转30度,
			//Math.toRadians(30)这个方法就是将我们习惯的30度转换为30弧度
			//接下来俩个参数指定旋转的中心,我们这里指定我们原图的中心就行了
			g.rotate(Math.toRadians(30), bufferedImage.getWidth()/2, bufferedImage.getHeight()/2);
			
			int x = -width/2;
			int y = -height/2;
			while (x<width*1.5) {
				y= -height/2;
				while (y < height *1.5) {
					g.drawImage(logoImage, x, y, null);//绘制图片水印
					y+=height1 + 300;
				}
				x+= width1 + 300;
			}
			//第四步：使用绘图工具将水印(文字/图片)绘制到缓存图片
			//开始绘制文字水印"慕课网",并设置纵坐标和横坐标的值,x和y的值是经过我们计算的，是一个准确的，可以保证我们文字内容全部正常显示的值
			//这里要注意纵坐标y的值要加上文字的大小，这是因为纵坐标的值它指向的位置是文字水印文本内容的下方，所以如果不增加文字大小的值，会导致文字水印的靠上显示
			//最后文字水印显示的不全
			g.dispose();//释放绘图工具
			//将带水印的图片写到指定的路径里面
			OutputStream os = new FileOutputStream(new File("C:\\Users\\issuser\\Desktop\\17cfd97aea9_2c1_moretupianshuiyin.jpeg"));
			//第五步:创建图形编码工具类
			JPEGImageEncoder en = JPEGCodec.createJPEGEncoder(os);
			//第六步:使用图像编码工具类，输出缓存图像到目标文件
			en.encode(bufferedImage);
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//计算水印的长度,因为水印有可能包含中文和英文
	public int getTextLength(String text) {
		int length = text.length();
		for (int i=0;i<text.length(); i++) {
			String s = String.valueOf(text.charAt(i));
			if (s.getBytes().length>1) {//字符的字节长度大于1说明不是英文字符，是中文
				length++;
			}//英文不做任何处理
		}
		//这里是算文字水印的宽度呢,具体咋算没听懂，要折半，不知道为啥
		length = length % 2 == 0 ? length / 2 : length/2 + 1;
		return length;
	}
	
	public static void main(String[] args) {
		MoreImageMarkService tms = new MoreImageMarkService();
		tms.watermark();
		System.out.println("添加多个图片水印设置成功了");
	}
}
