/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.examples.flow.parts;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Text;

import org.eclipse.jface.viewers.CellEditor;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.tools.CellEditorLocator;

/**
 * CellEditorLocator for Activities.
 * @author Daniel Lee
 */
public class ActivityCellEditorLocator implements CellEditorLocator {
	private Label label;

/**
 * Creates a new ActivityCellEditorLocator for the given Label
 * @param label the Label
 */	
public ActivityCellEditorLocator(Label label) {
	setLabel(label);
}

/**
 * @see CellEditorLocator#relocate(org.eclipse.jface.viewers.CellEditor)
 */
public void relocate(CellEditor celleditor) {
	Text text = (Text)celleditor.getControl();
	Point pref = text.computeSize(-1, -1);
	Rectangle rect = label.getTextBounds().getCopy();
	label.translateToAbsolute(rect);
	text.setBounds(rect.x - 1, rect.y - 1, pref.x + 1, pref.y + 1);	
}

/**
 * Returns the Label figure.
 * @return the Label
 */
protected Label getLabel() {
	return label;
}

/**
 * Sets the label.
 * @param label The label to set
 */
protected void setLabel(Label label) {
	this.label = label;
}

}
