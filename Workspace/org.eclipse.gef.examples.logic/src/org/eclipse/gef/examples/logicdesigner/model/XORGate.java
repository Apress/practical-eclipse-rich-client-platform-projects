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
package org.eclipse.gef.examples.logicdesigner.model;

import org.eclipse.swt.graphics.Image;

import org.eclipse.gef.examples.logicdesigner.LogicMessages;

public class XORGate
	extends Gate 
{

static final long serialVersionUID = 1;
private static Image XOR_ICON = createImage(XORGate.class, "icons/xor16.gif");  //$NON-NLS-1$

public Image getIconImage() {
	return XOR_ICON;
}

public boolean getResult() {
	return getInput(TERMINAL_A) & !getInput(TERMINAL_B) ||
		!getInput(TERMINAL_A) & getInput(TERMINAL_B);
}

public String toString() {
	return LogicMessages.XORGate_LabelText + " #" + getID();//$NON-NLS-1$
}

}
