package com.vaadin.jogdial.client;

import com.vaadin.shared.communication.ServerRpc;

public interface JogDialServerRpc extends ServerRpc {

	/**
	 * Communicates the current axis position for X and Y with range [-1, 1] as
	 * float.
	 * 
	 * @param x
	 * @param y
	 */
	public void positionMoved(float x, float y);
}
