package com.vaadin.jogdial.client;

public class Point {
	private final int x;
	private final int y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int distanceTo(Point other) {
		int xDifferenceSquared = (this.x - other.x) * (this.x - other.x);
		int yDifferenceSquared = (this.y - other.y) * (this.y - other.y);

		double distance = Math.sqrt(xDifferenceSquared + yDifferenceSquared);

		return (int) distance;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}
