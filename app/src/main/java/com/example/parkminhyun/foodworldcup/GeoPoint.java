package com.example.parkminhyun.foodworldcup;

public class GeoPoint {
	double x;
	double y;
	double z;

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
