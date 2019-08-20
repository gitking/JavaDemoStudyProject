package com.yale.test.design.iterator;

import java.util.ArrayList;

import com.yale.test.design.iterator.before.MenuItem;

public class PancakeHouseMenuIteratory implements Iterator {
	ArrayList menuItemList;
	int position = 0;
	public PancakeHouseMenuIteratory(ArrayList menuItemList){
		this.menuItemList = menuItemList;
	}
	
	@Override
	public boolean hasNext() {
		if(position >= menuItemList.size()){
			return false;
		} else {
			return true;
		}
	}

	@Override
	public Object next() {
		MenuItem menuItem = (MenuItem)menuItemList.get(position);
		position = position + 1;
		return menuItem;
	}
}
