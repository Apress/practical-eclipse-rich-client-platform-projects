/*******************************************************************************
 * Copyright (c) 2004, 2005 Elias Volanakis and others.
�* All rights reserved. This program and the accompanying materials
�* are made available under the terms of the Eclipse Public License v1.0
�* which accompanies this distribution, and is available at
�* http://www.eclipse.org/legal/epl-v10.html
�*
�* Contributors:
�*����Elias Volanakis - initial API and implementation
�*******************************************************************************/
package org.eclipse.gef.examples.shapes.model;

import org.eclipse.swt.graphics.Image;


/**
 * A rectangular shape.
 * @author Elias Volanakis
 */
public class RectangularShape extends Shape {
/** A 16x16 pictogram of a rectangular shape. */
private static final Image RECTANGLE_ICON = createImage("icons/rectangle16.gif");

private static final long serialVersionUID = 1;

public Image getIcon() {
	return RECTANGLE_ICON;
}

public String toString() {
	return "Rectangle " + hashCode();
}
}
