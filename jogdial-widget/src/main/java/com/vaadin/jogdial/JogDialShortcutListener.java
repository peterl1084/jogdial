package com.vaadin.jogdial;

import com.vaadin.event.ShortcutListener;

public class JogDialShortcutListener extends ShortcutListener {
	private static final long serialVersionUID = -2013316193806832470L;
	private JogDial jogDial;

	private int x;
	private int y;

	public JogDialShortcutListener(JogDial jogDial, int keyCode, int x, int y,
			int... modifiers) {
		super(null, keyCode, modifiers);

		this.jogDial = jogDial;
		this.x = x;
		this.y = y;
	}

	@Override
	public void handleAction(Object sender, Object target) {
		jogDial.setCapPosition(x, y);
	}
}
