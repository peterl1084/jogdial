package com.vaadin.jogdial.client;

import com.vaadin.shared.communication.ClientRpc;

public interface JogDialClientRpc extends ClientRpc {

	/**
	 * Positions the cap to jogdial between [-1, 1 ; -1, 1]
	 * 
	 * @param x
	 * @param y
	 */
	public void moveCapTo(float x, float y);
}
