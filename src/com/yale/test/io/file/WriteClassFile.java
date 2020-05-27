package com.yale.test.io.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * @author ipodao@qq.com
 * @date 2019/9/4 16:05
 */
public class WriteClassFile {
    private static final String EXPORT_PATH = "D:\\JavaDemo\\";

    public static void main(String[] args) throws IOException {
    	WriteClassFile apk = new WriteClassFile();
       //String infilePath = EXPORT_PATH + "test.zip";
        //String outfilePath = EXPORT_PATH + "hex.txt";
        //apk.fileToHex(infilePath, outfilePath);
        //hex val
        String val = "Classs";
        apk.hexToFile(val, "a.txt");
    	writeClass();
    }

    /**
     * 文件转成十六进制
     *
     * @param infilePath  转为16进制的文件
     * @param outfilePath 16进制 txt
     */
    public void fileToHex(String infilePath, String outfilePath) {
        try {
            StringBuffer sb = new StringBuffer();
            FileInputStream fis = new FileInputStream(infilePath);
            java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int read = 1024;
            int readSize = 1024;
            while (read == readSize) {
                read = fis.read(buffer, 0, readSize);
                bos.write(buffer, 0, read);
            }
            byte[] result = bos.toByteArray();
            // 字节数组转成十六进制
            String str = byte2HexStr(result);
            System.out.println("HexStr：" + str);
            /*
             * 将十六进制串保存到txt文件中
             */
            PrintWriter pw = new PrintWriter(new FileWriter(outfilePath));
            pw.println(str);
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 十六进制转成文件
     *
     * @param hex
     * @param filePath
     */
    public static void hexToFile(String hex, String filePath) {
        StringBuilder sb = new StringBuilder();
        sb.append(hex);
        saveToFile(sb.toString().toUpperCase(), EXPORT_PATH + filePath);
    }

    /**
     * hex 转为文件
     *
     * @param src
     * @param output
     */
    public static void saveToFile(String src, String output) {
        if (src == null || src.length() == 0) {
            return;
        }
        try {
            FileOutputStream out = new FileOutputStream(new File(output));
            byte[] bytes = src.getBytes();
            for (int i = 0; i < bytes.length; i += 2) {//一次写俩个字节
            	int temp = charToInt(bytes[i]) * 16 + charToInt(bytes[i + 1]);
            	System.out.println(temp);
                out.write(temp);
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int charToInt(byte ch) {
        int val = 0;
        if (ch >= 0x30 && ch <= 0x39) {//十六进制0x30就是十进制的0,十六进制0x39就是十进制的9
            val = ch - 0x30;
        } else if (ch >= 0x41 && ch <= 0x46) {//十六进制0x41就是ASCII码表中的字母A十进制65,十六进制0x46就是ASCII码表中的字母F十进制70
            val = ch - 0x41 + 10;
        }
        return val;
    }

    /*
     * 实现字节数组向十六进制的转换方法一
     */
    public static String byte2HexStr(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs.toUpperCase();
    }
    
    /**
     * 要想把字符变成16进制写进文件里面,需要先把字符变成16进制的形式往文件里面写
     * 如果是整形直接往文件里面写就行,写完就是十六进制的内容了
     * 如果要往文件里面写16进制的数据
     * @throws IOException
     */
    public static void writeClass() throws IOException {
    	FileOutputStream out = new FileOutputStream(new File(EXPORT_PATH + File.separator + "class.txt"));
    	String str = "public class WriteClass {";
        System.out.println("-------------");

    	int sdf = 10 >>> 24 & 0xFF;
        System.out.println(sdf);
        out.write(sdf);

        int sdf1 = 10 >>> 16 & 0xFF;
        System.out.println(0);

        out.write(sdf1);

        int sdf2 =10 >>> 8 & 0xFF;
        System.out.println(0);

        out.write(sdf2);

        int sdf3 =10 >>> 0 & 0xFF;
        System.out.println(10);

        out.write(sdf3);

//    	byte[] charArr = str.getBytes();
//    	for (int i=0; i<charArr.length; i++) {
//            out.write(charArr[i]);
//    	}
    	out.close();
    }
    
}