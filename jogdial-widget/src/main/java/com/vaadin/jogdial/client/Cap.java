package com.vaadin.jogdial.client;

import org.vaadin.gwtgraphics.client.shape.Circle;

import com.google.gwt.user.client.Event;

public class Cap extends Circle {

	public Cap(int x, int y, int radius) {
		super(x, y, radius);
	}

	@Override
	public void onBrowserEvent(Event event) {
		super.onBrowserEvent(event);

		System.out.println(event.getType());
	}

}
