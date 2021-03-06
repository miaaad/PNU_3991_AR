package com.neshan.task1.domain.model.searchModel;

import com.google.gson.annotations.SerializedName;

public class Location{

	@SerializedName("x")
	private double X;

	@SerializedName("y")
	private double Y;

	@SerializedName("z")
	private String Z;

	public void setX(double X){
		this.X = X;
	}

	public double getX(){
		return X;
	}

	public void setY(double Y){
		this.Y = Y;
	}

	public double getY(){
		return Y;
	}

	public void setZ(String Z){
		this.Z = Z;
	}

	public String getZ(){
		return Z;
	}
}