package com.yale.test.io.imooc.serializable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DungeonTest {
	public static void main(String[] args) {
		DungeonGame d = new DungeonGame();
		System.out.println("序列化之前X的值:" + d.getX() + ",Y（注意Y这个字段是用transient关键字修饰的,不会被序列化）:" + d.getY() + ",Z:" + d.getZ());
		try {
			FileOutputStream fos = new FileOutputStream("dg.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(d);
			oos.close();
			FileInputStream fis = new FileInputStream("dg.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			d = (DungeonGame)ois.readObject();
			ois.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("解序列化之后(那个瞬时的变量没有了)X的值:" + d.getX() + ",Y（注意Y这个字段是用transient关键字修饰的,不会被序列化）:" + d.getY() + ",Z的值:" + d.getZ());
	}
}
