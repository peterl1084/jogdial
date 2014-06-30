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
import com.google.gwt.event.dom.client.TouchCancelHandler;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;

public class JogDialWidget extends Composite implements HasAllTouchHandlers,
		HasClickHandlers, HasMouseMoveHandlers, HasMouseDownHandlers,
		HasMouseUpHandlers {

	private DrawingArea drawingArea;
	private Circle cap;

	private FocusPanel panel;

	public JogDialWidget() {
		panel = new FocusPanel();
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

	private void drawBackground(int radius) {
		drawingArea.clear();

		Circle circle = new Circle(radius, radius, radius - 2);
		circle.setFillColor("#3b3b3b");
		circle.setStrokeColor("#2f2f2f");
		circle.setStrokeWidth(2);

		drawingArea.add(circle);
		drawingArea.add(cap);
	}

	public void drawCap(Point position) {
		cap.setX(position.getX());
		cap.setY(position.getY());
	}

	public void adjustSize(int radius) {
		drawingArea.setWidth(radius * 2);
		drawingArea.setHeight(radius * 2);

		drawBackground(radius);
	}

	@Override
	public HandlerRegistration addTouchStartHandler(TouchStartHandler handler) {
		return panel.addTouchStartHandler(handler);
	}

	@Override
	public HandlerRegistration addTouchMoveHandler(TouchMoveHandler handler) {
		return panel.addTouchMoveHandler(handler);
	}

	@Override
	public HandlerRegistration addTouchEndHandler(TouchEndHandler handler) {
		return panel.addTouchEndHandler(handler);
	}

	@Override
	public HandlerRegistration addTouchCancelHandler(TouchCancelHandler handler) {
		return panel.addTouchCancelHandler(handler);
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return panel.addClickHandler(handler);
	}

	@Override
	public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
		return panel.addMouseMoveHandler(handler);
	}

	@Override
	public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
		return panel.addMouseUpHandler(handler);
	}

	@Override
	public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
		return panel.addMouseDownHandler(handler);
	}
}