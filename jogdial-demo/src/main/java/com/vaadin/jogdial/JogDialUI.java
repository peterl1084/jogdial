package com.vaadin.jogdial;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.jogdial.JogDial.AxisMoveEvent;
import com.vaadin.jogdial.JogDial.AxisMoveListener;
import com.vaadin.jogdial.client.Position;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;

@SuppressWarnings("serial")
public class JogDialUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = JogDialUI.class, widgetset = "com.vaadin.jogdial.JogDialDemoWidgetset")
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSizeFull();

		JogDial rotationalAnalog = new JogDial(Position.LEFT);
		rotationalAnalog.addAxisMoveListener(new AxisMoveListener() {

			@Override
			public void onAxisMoved(AxisMoveEvent event) {
				System.out.println("Rotational " + event.getX() + ","
						+ event.getY());
			}
		});

		JogDial movementAnalog = new JogDial(Position.RIGHT);
		movementAnalog.addAxisMoveListener(new AxisMoveListener() {

			@Override
			public void onAxisMoved(AxisMoveEvent event) {
				System.out.println("Movement " + event.getX() + ","
						+ event.getY());
			}
		});

		layout.addComponent(rotationalAnalog);
		layout.addComponent(movementAnalog);

		layout.setExpandRatio(movementAnalog, 1);
		layout.setComponentAlignment(rotationalAnalog, Alignment.BOTTOM_LEFT);
		layout.setComponentAlignment(movementAnalog, Alignment.BOTTOM_RIGHT);

		setContent(layout);
	}
}