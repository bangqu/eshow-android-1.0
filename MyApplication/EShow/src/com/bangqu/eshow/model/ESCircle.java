package com.bangqu.eshow.model;

public class ESCircle {
	
	public ESPoint point;
	public double r;
	
	public ESCircle() {
		super();
	}

	public ESCircle(ESPoint point, double r) {
		super();
		this.point = point;
		this.r = r;
	}

	@Override
	public String toString() {
		return "(" + point.x + "," + point.y + "),r="+r;
	}

}
