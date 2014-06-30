package com.vaadin.jogdial;

import java.io.Serializable;
import java.lang.reflect.Method;

import com.vaadin.jogdial.client.JogDialClientRpc;
import com.vaadin.jogdial.client.JogDialServerRpc;
import com.vaadin.jogdial.client.JogDialState;
import com.vaadin.jogdial.client.Position;
import com.vaadin.ui.Component;
import com.vaadin.util.ReflectTools;

public class JogDial extends com.vaadin.ui.AbstractComponent {
	private static final long serialVersionUID = 4502072695024900096L;

	private JogDialServerRpc rpc = new JogDialServerRpc() {
		private static final long serialVersionUID = -1573247303469542425L;

		@Override
		public void positionMoved(float x, float y) {
			fireEvent(new AxesMoveEvent(JogDial.this, x, y));
		}
	};

	private JogDialClientRpc clientRPC;

	public JogDial(Position position, int radius) {
		clientRPC = getRpcProxy(JogDialClientRpc.class);

		registerRpc(rpc);

		getState().position = position;
		getState().radius = radius;

		setWidth(radius * 2, Unit.PIXELS);
		setHeight(radius * 2, Unit.PIXELS);
	}

	@Override
	public JogDialState getState() {
		return (JogDialState) super.getState();
	}

	/**
	 * Adds given axis move lister to this analog. The listener will be notified
	 * when axises of this analog are moved.
	 * 
	 * @param listener
	 */
	public void addAxesMoveListener(AxesMoveListener listener) {
		addListener(AxesMoveEvent.class, listener,
				AxesMoveListener.AXES_MOVE_METHOD);
	}

	/**
	 * Removes the given axis move listener.
	 * 
	 * @param listener
	 */
	public void removeAxisMoveListener(AxesMoveListener listener) {
		removeListener(AxesMoveEvent.class, listener,
				AxesMoveListener.AXES_MOVE_METHOD);
	}

	public interface AxesMoveListener extends Serializable {
		public void onAxesMoved(AxesMoveEvent event);

		public static final Method AXES_MOVE_METHOD = ReflectTools.findMethod(
				AxesMoveListener.class, "onAxesMoved", AxesMoveEvent.class);
	}

	public class AxesMoveEvent extends Component.Event {
		private static final long serialVersionUID = 684896302630632837L;
		private final float x;
		private final float y;

		public AxesMoveEvent(Component source, float x, float y) {
			super(source);
			this.x = x;
			this.y = y;
		}

		public float getX() {
			return x;
		}

		public float getY() {
			return y;
		}
	}

	public void addUpShortcutKey(int key, int... modifiers) {
		addShortcutListener(new JogDialShortcutListener(this, key, 0, -1,
				modifiers));
	}

	public void addDownShortcutKey(int key, int... modifiers) {
		addShortcutListener(new JogDialShortcutListener(this, key, 0, 1,
				modifiers));
	}

	public void addLeftShortcutKey(int key, int... modifiers) {
		addShortcutListener(new JogDialShortcutListener(this, key, -1, 0,
				modifiers));
	}

	public void addRightShortcutKey(int key, int... modifiers) {
		addShortcutListener(new JogDialShortcutListener(this, key, 1, 0,
				modifiers));
	}

	public void setCapPosition(float x, float y) {
		clientRPC.moveCapTo(x, y);
	}
}
