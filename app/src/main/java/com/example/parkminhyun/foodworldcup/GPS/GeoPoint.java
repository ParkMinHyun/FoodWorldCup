package com.example.parkminhyun.foodworldcup.GPS;

public class GeoPoint {
	public double x;
	public double y;
	public double z;

	public GeoPoint() {
		super();
	}

	public GeoPoint(double x, double y) {
		super();
		this.x = x;
		this.y = y;
		this.z = 0;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

}
