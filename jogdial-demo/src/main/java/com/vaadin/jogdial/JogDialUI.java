package com.vaadin.jogdial;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.jogdial.JogDial.AxesMoveEvent;
import com.vaadin.jogdial.JogDial.AxesMoveListener;
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

		JogDial rotationDial = new JogDial(Position.LEFT, 200);
		rotationDial.addAxesMoveListener(new AxesMoveListener() {

			@Override
			public void onAxesMoved(AxesMoveEvent event) {
				System.out.println("Rotational " + event.getX() + ","
						+ event.getY());
			}
		});

		JogDial movementDial = new JogDial(Position.RIGHT, 300);
		movementDial.addAxesMoveListener(new AxesMoveListener() {

			@Override
			public void onAxesMoved(AxesMoveEvent event) {
				System.out.println("Movement " + event.getX() + ","
						+ event.getY());
			}
		});

		movementDial.addUpShortcutKey(KeyCode.ARROW_UP);
		movementDial.addDownShortcutKey(KeyCode.ARROW_DOWN);
		movementDial.addLeftShortcutKey(KeyCode.ARROW_LEFT);
		movementDial.addRightShortcutKey(KeyCode.ARROW_RIGHT);

		layout.addComponent(rotationDial);
		layout.addComponent(movementDial);

		layout.setExpandRatio(movementDial, 1);
		layout.setComponentAlignment(rotationDial, Alignment.BOTTOM_LEFT);
		layout.setComponentAlignment(movementDial, Alignment.BOTTOM_RIGHT);

		setContent(layout);
	}
}