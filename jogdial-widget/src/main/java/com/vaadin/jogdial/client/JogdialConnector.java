package com.vaadin.jogdial.client;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
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
		TouchEndHandler, MouseMoveHandler, MouseDownHandler, MouseUpHandler,
		GamepadObserver {
	private static final long serialVersionUID = -4509343276799655227L;

	private JogDialServerRpc serverRPC = RpcProxy.create(
			JogDialServerRpc.class, this);

	private boolean mouseDown;

	private Point centerPoint;
	private Point previousPoint;

	private GamePadSupport support = new GamePadSupport();

	private GamePad latestGamepadStatus;

	private JogDialClientRpc clientRpc = new JogDialClientRpc() {

		@Override
		public void moveCapTo(float x, float y) {
			JogdialConnector.this.setCapTo(x, y);
		}
	};

	private static final Logger logger = Logger
			.getLogger(JogdialConnector.class.getSimpleName());

	@Override
	protected void init() {
		super.init();

		getLayoutManager().registerDependency(this, getWidget().getElement());

		getWidget().addTouchMoveHandler(this);
		getWidget().addTouchStartHandler(this);
		getWidget().addTouchEndHandler(this);

		centerPoint = new Point(0, 0);

		support.start(this);
	}

	protected void setCapTo(float x, float y) {
		int capX = calculateXPositionFromDecimal(x);
		int capY = calculateYPositionFromDecimal(y);

		getWidget().drawCap(new Point(capX, capY));
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
	}

	@Override
	public void layout() {
		int outerWidth = getLayoutManager().getOuterWidth(
				getWidget().getElement());
		int outerHeight = getLayoutManager().getOuterHeight(
				getWidget().getElement());

		getWidget().adjustSize(outerWidth, outerHeight);

		centerPoint = new Point(outerWidth / 2, outerHeight / 2);
		previousPoint = centerPoint;
	}

	@Override
	public void onTouchMove(TouchMoveEvent event) {
		getLogger().info(
				getConnectorId() + " got " + event.getTouches().length()
						+ " touches and " + event.getTargetTouches().length()
						+ " targeted touches");

		Touch touch = event.getTargetTouches().get(0);
		int relativeX = touch.getRelativeX(getWidget().getElement());
		int relativeY = touch.getRelativeY(getWidget().getElement());

		updateCapPosition(new Point(relativeX, relativeY));

		event.getNativeEvent().preventDefault();
	}

	@Override
	public void onMouseMove(MouseMoveEvent event) {
		if (mouseDown) {
			updateCapPosition(new Point(event.getX(), event.getY()));
		}
	}

	private void updateCapPosition(Point newPosition) {
		int distanceToCenter = newPosition.distanceTo(centerPoint);

		if (isInAllowedRadius(distanceToCenter)) {
			int distanceToPreviousPoint = newPosition.distanceTo(previousPoint);

			if (isChangedSignificantly(distanceToPreviousPoint)) {
				previousPoint = newPosition;
				float xToCenter = calculateXFromCenter(newPosition.getX());
				float yToCenter = calculateYFromCenter(newPosition.getY());

				serverRPC.positionMoved(xToCenter, yToCenter);
			}

			getWidget().drawCap(newPosition);
		} else {
			setCapToCenter();
		}
	}

	private void setCapToCenter() {
		getWidget().drawCap(centerPoint);
		serverRPC.positionMoved(0, 0);
	}

	private boolean isChangedSignificantly(int distanceToPreviousPoint) {
		return distanceToPreviousPoint > 10;
	}

	private boolean isInAllowedRadius(int distanceToCenter) {
		return distanceToCenter < getWidget().getOffsetWidth() / 2;
	}

	private float calculateXFromCenter(int x) {
		int halfWidth = getWidget().getOffsetWidth() / 2;
		int xToCenter = x - halfWidth;

		return (float) xToCenter / (float) halfWidth;
	}

	private int calculateXPositionFromDecimal(float x) {
		float halfWidth = (getWidget().getOffsetWidth() / 2);
		halfWidth *= x;

		return (int) (centerPoint.getX() + halfWidth);
	}

	private int calculateYPositionFromDecimal(float y) {
		float halfHeight = (getWidget().getOffsetHeight() / 2);
		halfHeight *= y;

		return (int) (centerPoint.getY() + halfHeight);
	}

	private float calculateYFromCenter(int y) {
		int halfHeight = getWidget().getOffsetHeight() / 2;
		int yToCenter = y - halfHeight;

		return (float) yToCenter / (float) halfHeight;
	}

	@Override
	public void onMouseUp(MouseUpEvent event) {
		event.preventDefault();
		setCapToCenter();

		mouseDown = false;
	}

	@Override
	public void onMouseDown(MouseDownEvent event) {
		event.preventDefault();

		updateCapPosition(new Point(event.getX(), event.getY()));

		mouseDown = true;
	}

	@Override
	public void onTouchStart(TouchStartEvent event) {
		Touch touch = event.getTargetTouches().get(0);
		int relativeX = touch.getRelativeX(getWidget().getElement());
		int relativeY = touch.getRelativeY(getWidget().getElement());

		updateCapPosition(new Point(relativeX, relativeY));
	}

	private Logger getLogger() {
		return logger;
	}

	@Override
	public void onTouchEnd(TouchEndEvent event) {
		setCapToCenter();
	}

	@Override
	public void onGamepadStatusChanged(GamePad gamepad) {
		double leftX = gamepad.getAxes().get(0);
		double leftY = gamepad.getAxes().get(1);

		setCapTo((float) leftX, (float) leftY);
	}
}
