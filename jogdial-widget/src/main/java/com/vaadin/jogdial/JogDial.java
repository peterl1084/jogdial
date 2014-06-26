package com.vaadin.jogdial;

import java.io.Serializable;
import java.lang.reflect.Method;

import com.vaadin.jogdial.client.JogDialServerRpc;
import com.vaadin.jogdial.client.JogDialState;
import com.vaadin.ui.Component;
import com.vaadin.util.ReflectTools;

public class JogDial extends com.vaadin.ui.AbstractComponent {
	private static final long serialVersionUID = 4502072695024900096L;

	private JogDialServerRpc rpc = new JogDialServerRpc() {
		private static final long serialVersionUID = -1573247303469542425L;

		@Override
		public void positionMoved(float x, float y) {
			fireEvent(new AxisMoveEvent(JogDial.this, x, y));
		}
	};

	public JogDial() {
		setWidth(250, Unit.PIXELS);
		setHeight(250, Unit.PIXELS);

		registerRpc(rpc);
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
	public void addAxisMoveListener(AxisMoveListener listener) {
		addListener(AxisMoveEvent.class, listener,
				AxisMoveListener.AXIS_MOVE_METHOD);
	}

	/**
	 * Removes the given axis move listener.
	 * 
	 * @param listener
	 */
	public void removeAxisMoveListener(AxisMoveListener listener) {
		removeListener(AxisMoveEvent.class, listener,
				AxisMoveListener.AXIS_MOVE_METHOD);
	}

	public interface AxisMoveListener extends Serializable {
		public void onAxisMoved(AxisMoveEvent event);

		public static final Method AXIS_MOVE_METHOD = ReflectTools.findMethod(
				AxisMoveListener.class, "onAxisMoved", AxisMoveEvent.class);
	}

	public class AxisMoveEvent extends Component.Event {
		private static final long serialVersionUID = 684896302630632837L;
		private final float x;
		private final float y;

		public AxisMoveEvent(Component source, float x, float y) {
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
}
