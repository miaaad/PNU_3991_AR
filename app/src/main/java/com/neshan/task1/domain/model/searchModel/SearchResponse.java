package com.neshan.task1.domain.model.searchModel;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class SearchResponse{

	@SerializedName("count")
	private int count;

	@SerializedName("items")
	private ArrayList<ItemsItem> items;

	public void setCount(int count){
		this.count = count;
	}

	public int getCount(){
		return count;
	}

	public void setItems(ArrayList<ItemsItem> items){
		this.items = items;
	}

	public ArrayList<ItemsItem> getItems(){
		return items;
	}
}