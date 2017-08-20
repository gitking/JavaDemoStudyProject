package com.yale.design.iterator;

import com.yale.design.iterator.before.MenuItem;
import java.util.Iterator;
public class DinerMenuIterator implements Iterator {
	
	MenuItem [] items;
	int position = 0;//记录当前数组遍历的位置
	
	public DinerMenuIterator(MenuItem [] menuItem){
		this.items = menuItem;
	}
	
	public boolean hasNext() {
		if(position >= items.length || items[position] == null){
			return false;
		} else {
			return true;
		}
	}

	public Object next() {
		MenuItem menuItem = items[position];
		position = position + 1;
		return menuItem;
	}
	public void remove(){
		if(position < 0){
			throw new IllegalStateException("集合已被删空");
		}
		if(items[position -1] != null){
			for(int i = position -1; i < (items.length -1); i++){
				items[i] = items[i+1];
			}
			items[items.length -1] = null;
		}
	}
}
