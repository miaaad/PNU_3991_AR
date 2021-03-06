package com.neshan.task1.domain.model.searchModel;

import com.google.gson.annotations.SerializedName;

public class ItemsItem{

	@SerializedName("address")
	private String address;

	@SerializedName("location")
	private Location location;

	@SerializedName("category")
	private String category;

	@SerializedName("type")
	private String type;

	@SerializedName("region")
	private String region;

	@SerializedName("title")
	private String title;

	@SerializedName("neighbourhood")
	private String neighbourhood;

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	public void setLocation(Location location){
		this.location = location;
	}

	public Location getLocation(){
		return location;
	}

	public void setCategory(String category){
		this.category = category;
	}

	public String getCategory(){
		return category;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public void setRegion(String region){
		this.region = region;
	}

	public String getRegion(){
		return region;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setNeighbourhood(String neighbourhood){
		this.neighbourhood = neighbourhood;
	}

	public String getNeighbourhood(){
		return neighbourhood;
	}
}