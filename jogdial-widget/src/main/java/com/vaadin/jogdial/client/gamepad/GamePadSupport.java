package com.vaadin.jogdial.client.gamepad;

import com.google.gwt.animation.client.AnimationScheduler;
import com.google.gwt.animation.client.AnimationScheduler.AnimationCallback;

public class GamePadSupport  {
	private GamepadObserver gamepadObserver;
	
	private final GamePadSupport gamePadSupport = this;
	
	private GamePadUpdateCallback schedulerCallback;
	
	public void start(GamepadObserver gamepadObserver) {
		this.gamepadObserver = gamepadObserver;
		
		schedulerCallback = new GamePadUpdateCallback();
		
		pollGamePads();
	}

	public void pollGamePads() {
		pollGamePadsNative(gamePadSupport);
		scheduleNextUpdateNative(schedulerCallback);
	}
	
	public static native boolean isGamePadSupportAvailable() /*-{ 
		var gamepadSupportAvailable = navigator.getGamepads || 
		!!navigator.webkitGetGamepads || 
		!!navigator.webkitGamepads;
		
		if(gamepadSupportAvailable) {
			return true;
		}
		
		return false;
	}-*/;
	
	private native void pollGamePadsNative(GamePadSupport gamePadSupport) /*-{
		 var rawGamepads = (navigator.getGamepads && navigator.getGamepads()) ||
						(navigator.webkitGetGamepads && navigator.webkitGetGamepads()); 
				
		if(rawGamepads) {
			for (var i = 0; i < rawGamepads.length; i++) {
				if (rawGamepads[i]) {
					gamePadSupport.@com.vaadin.jogdial.client.gamepad.GamePadSupport::notifyGamePadStatusToConnector(Lcom/vaadin/jogdial/client/gamepad/GamePad;)(rawGamepads[i]);
				}
			}
		}
	}-*/;
	
	private void notifyGamePadStatusToConnector(GamePad pads) {
		gamepadObserver.onGamepadStatusChanged(pads);
	}
	
	private  void scheduleNextUpdateNative(GamePadUpdateCallback callback) {
		AnimationScheduler.get().requestAnimationFrame(callback);
	}
	
	private class GamePadUpdateCallback implements AnimationCallback {
		@Override
		public void execute(double timestamp) {
			pollGamePads();			
		}
	}
}
