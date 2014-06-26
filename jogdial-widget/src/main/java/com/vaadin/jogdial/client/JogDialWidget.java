package com.vaadin.jogdial.client;

import org.vaadin.gwtgraphics.client.DrawingArea;
import org.vaadin.gwtgraphics.client.shape.Circle;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasAllTouchHandlers;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.HasMouseDownHandlers;
import com.google.gwt.event.dom.client.HasMouseMoveHandlers;
import com.google.gwt.event.dom.client.HasMouseUpHandlers;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.dom.client.TouchCancelEvent;
import com.google.gwt.event.dom.client.TouchCancelHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;

public class JogDialWidget extends Composite implements HasAllTouchHandlers,
		HasClickHandlers, HasMouseMoveHandlers, HasMouseDownHandlers,
		HasMouseUpHandlers {
	private DrawingArea drawingArea;

	private Circle cap;

	private SimplePanel panel;

	public JogDialWidget() {
		panel = new SimplePanel();
		panel.setStyleName("v-analog-panel");

		drawingArea = new DrawingArea(0, 0);
		drawingArea.setStyleName("v-analog-drawingarea");
		panel.setWidget(drawingArea);

		cap = new Circle(0, 0, 20);
		cap.setFillColor("#6600ff");
		cap.setStrokeColor("#000066");
		cap.setStrokeWidth(2);

		initWidget(panel);
	}

	private void drawAnalogsBackground() {
		drawingArea.clear();

		int centerX = (drawingArea.getWidth() / 2);
		int centerY = (drawingArea.getHeight() / 2);

		Circle circle = new Circle(centerX, centerY, (centerX - 4));
		circle.setFillColor("#3b3b3b");
		circle.setStrokeColor("#2f2f2f");
		circle.setStrokeWidth(2);

		drawCap(new Point(centerX, centerY));

		drawingArea.add(circle);
		drawingArea.add(cap);
	}

	public void drawCap(Point position) {
		cap.setX(position.getX());
		cap.setY(position.getY());
	}

	public void adjustAnalogSize(int outerWidth, int outerHeight) {
		drawingArea.setWidth(outerWidth);
		drawingArea.setHeight(outerHeight);

		drawAnalogsBackground();
	}

	@Override
	public HandlerRegistration addTouchStartHandler(TouchStartHandler handler) {
		return panel.addDomHandler(handler, TouchStartEvent.getType());
	}

	@Override
	public HandlerRegistration addTouchMoveHandler(TouchMoveHandler handler) {
		return panel.addDomHandler(handler, TouchMoveEvent.getType());
	}

	@Override
	public HandlerRegistration addTouchEndHandler(TouchEndHandler handler) {
		return panel.addDomHandler(handler, TouchEndEvent.getType());
	}

	@Override
	public HandlerRegistration addTouchCancelHandler(TouchCancelHandler handler) {
		return panel.addDomHandler(handler, TouchCancelEvent.getType());
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return drawingArea.addClickHandler(handler);
	}

	@Override
	public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
		return drawingArea.addMouseMoveHandler(handler);
	}

	@Override
	public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
		return drawingArea.addMouseUpHandler(handler);
	}

	@Override
	public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
		return drawingArea.addMouseDownHandler(handler);
	}
}