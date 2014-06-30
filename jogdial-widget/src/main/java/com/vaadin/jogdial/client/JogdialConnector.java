package com.vaadin.jogdial.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.client.communication.StateChangeEvent;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.client.ui.SimpleManagedLayout;
import com.vaadin.jogdial.JogDial;
import com.vaadin.jogdial.client.gamepad.GamePad;
import com.vaadin.jogdial.client.gamepad.GamePadSupport;
import com.vaadin.jogdial.client.gamepad.GamepadObserver;
import com.vaadin.shared.ui.Connect;

@Connect(JogDial.class)
public class JogdialConnector extends AbstractComponentConnector implements
		SimpleManagedLayout, TouchMoveHandler, TouchStartHandler,
		TouchEndHandler, GamepadObserver {
	private static final long serialVersionUID = -4509343276799655227L;

	private static final float MOVEMENT_THRESHOLD = 0.05f;

	private Point centerPoint;
	private Point previousPoint;

	private Position position = Position.LEFT;

	private int radius;

	private GamePadSupport support = new GamePadSupport();

	private JogDialClientRpc clientRpc = new JogDialClientRpc() {
		private static final long serialVersionUID = 2363280604346399389L;

		@Override
		public void moveCapTo(float x, float y) {
			setCapPosition(x, y, false);
		}
	};

	private JogDialServerRpc serverRpc = RpcProxy.create(
			JogDialServerRpc.class, this);

	@Override
	protected void init() {
		super.init();

		registerRpc(JogDialClientRpc.class, clientRpc);
		getLayoutManager().registerDependency(this, getWidget().getElement());

		getWidget().addTouchMoveHandler(this);
		getWidget().addTouchStartHandler(this);
		getWidget().addTouchEndHandler(this);

		centerPoint = new Point(0, 0);

		support.start(this);
	}

	private Point calculateRadiusCappedPosition(float x, float y) {
		float dX = 0 - x;
		float dY = 0 - y;

		double angle = Math.atan2(dY, dX);

		int newX = (int) (Math.cos(angle) * (radius * Math.abs(x)));
		int newY = (int) (Math.sin(angle) * (radius * Math.abs(y)));

		return new Point(centerPoint.getX() + newX, centerPoint.getY() + newY);
	}

	@Override
	protected JogDialWidget createWidget() {
		return GWT.create(JogDialWidget.class);
	}

	@Override
	public JogDialWidget getWidget() {
		return (JogDialWidget) super.getWidget();
	}

	@Override
	public JogDialState getState() {
		return (JogDialState) super.getState();
	}

	@Override
	public void onStateChanged(StateChangeEvent stateChangeEvent) {
		super.onStateChanged(stateChangeEvent);

		this.position = getState().position;
		this.radius = getState().radius;
	}

	@Override
	public void layout() {
		int outerWidth = getLayoutManager().getOuterWidth(
				getWidget().getElement());
		int outerHeight = getLayoutManager().getOuterHeight(
				getWidget().getElement());

		getWidget().adjustSize(Math.max(outerWidth / 2, outerHeight / 2));

		centerPoint = new Point(radius, radius);
		previousPoint = centerPoint;
	}

	@Override
	public void onTouchStart(TouchStartEvent event) {
		onTouchEvent(event.getTargetTouches().get(0));
	}

	@Override
	public void onTouchMove(TouchMoveEvent event) {
		onTouchEvent(event.getTargetTouches().get(0));
		event.getNativeEvent().preventDefault();
	}

	@Override
	public void onTouchEnd(TouchEndEvent event) {
		setCapToCenter(true);
	}

	protected void onTouchEvent(Touch touch) {
		float relativeX = (float) touch.getRelativeX(getWidget().getElement())
				/ (float) radius;
		float relativeY = (float) touch.getRelativeY(getWidget().getElement())
				/ (float) radius;

		setCapPosition(relativeX, relativeY, true);
	}

	@Override
	public void onGamepadStatusChanged(GamePad gamepad) {
		float x = 0;
		float y = 0;

		switch (position) {
		case LEFT:
			x = (float) gamepad.getAxes().get(0) * -1f;
			y = (float) gamepad.getAxes().get(1) * -1f;
			break;
		case RIGHT: {
			x = (float) gamepad.getAxes().get(2) * -1f;
			y = (float) gamepad.getAxes().get(3) * -1f;
			break;
		}
		}

		setCapPosition(x, y, true);
	}

	private void setCapPosition(float x, float y, boolean fireEvent) {
		x = Math.abs(x) < MOVEMENT_THRESHOLD ? 0 : x;
		y = Math.abs(y) < MOVEMENT_THRESHOLD ? 0 : y;

		Point capPosition = calculateRadiusCappedPosition(x, y);

		if (!isInAllowedRadius(capPosition)) {
			capPosition = calculateRadiusCappedPosition(
					((float) capPosition.getX() / (float) radius),
					((float) capPosition.getY() / radius));
		}

		int distanceToPreviousPoint = capPosition.distanceTo(previousPoint);

		getWidget().drawCap(capPosition);

		if (isChangedSignificantly(distanceToPreviousPoint)) {
			previousPoint = capPosition;

			if (fireEvent) {
				serverRpc.positionMoved(x, y);
			}
		}
	}

	private void setCapToCenter(boolean fireEvent) {
		getWidget().drawCap(centerPoint);

		if (fireEvent) {
			serverRpc.positionMoved(0, 0);
		}
	}

	private boolean isChangedSignificantly(int distanceToPreviousPoint) {
		return distanceToPreviousPoint > 10;
	}

	private boolean isInAllowedRadius(Point point) {
		return centerPoint.distanceTo(point) <= radius;
	}
}
