package com.vaadin.jogdial.client.gamepad;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayNumber;

public class GamePad extends JavaScriptObject {

	protected GamePad() {

	}

	public final native float[] getButtons() /*-{ return this.buttons; }-*/;

	public final native JsArrayNumber getAxes() /*-{ return this.axes; }-*/;

	public final native String getId() /*-{ return this.id; }-*/;

	public final native int getIndex() /*-{ return this.index; }-*/;
}
