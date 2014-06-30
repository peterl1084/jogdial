package com.vaadin.jogdial.client.gamepad;

public class GamePadSupport {
	private GamepadObserver gamepadObserver;
	
	public void start(GamepadObserver gamepadObserver) {
		this.gamepadObserver = gamepadObserver;
		startGamepadPolling();
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
	
	private native void startGamepadPolling() /*-{
		var self = this;
		
		var pollGamepadsFunction = function() {
			var rawGamepads =
							(navigator.getGamepads && navigator.getGamepads()) ||
							(navigator.webkitGetGamepads && navigator.webkitGetGamepads()); 
			
			if(rawGamepads) {
				for (var i = 0; i < rawGamepads.length; i++) {
					if (rawGamepads[i]) {
						self.@com.vaadin.jogdial.client.gamepad.GamePadSupport::notifyGamePadStatusToConnector(Lcom/vaadin/jogdial/client/gamepad/GamePad;)(rawGamepads[i]);
					}
				}
			}
			
			$wnd.scheduleGamepadUpdate();
		};
		
		var scheduleNextUpdateFunction = function() {
			if (window.requestAnimationFrame) {
				window.requestAnimationFrame($wnd.pollGamepads);
			} else if (window.mozRequestAnimationFrame) {
				window.mozRequestAnimationFrame($wnd.pollGamepads);
			} else if (window.webkitRequestAnimationFrame) {
				window.webkitRequestAnimationFrame($wnd.pollGamepads);
			} 
		}
		
		$wnd.pollGamepads = pollGamepadsFunction;
		$wnd.scheduleGamepadUpdate = scheduleNextUpdateFunction;
		
		$wnd.pollGamepads();
	}-*/;
	
	public void notifyGamePadStatusToConnector(GamePad pads) {
		gamepadObserver.onGamepadStatusChanged(pads);
	}
}
