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
package org.eclipse.gef.examples.logicdesigner.edit;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.eclipse.swt.accessibility.AccessibleEvent;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;

import org.eclipse.gef.AccessibleAnchorProvider;
import org.eclipse.gef.AccessibleEditPart;

import org.eclipse.gef.examples.logicdesigner.figures.GroundFigure;
import org.eclipse.gef.examples.logicdesigner.figures.LiveOutputFigure;
import org.eclipse.gef.examples.logicdesigner.figures.OutputFigure;
import org.eclipse.gef.examples.logicdesigner.model.GroundOutput;
import org.eclipse.gef.examples.logicdesigner.model.LiveOutput;
import org.eclipse.gef.examples.logicdesigner.model.SimpleOutput;

/**
 * EditPart for Output types in Logic Example
 */
public class OutputEditPart
	extends LogicEditPart
{

protected AccessibleEditPart createAccessible() {
	return new AccessibleGraphicalEditPart(){
		public void getName(AccessibleEvent e) {
			e.result = getSimpleOutput().toString();
		}
	};
}

/**
 * Returns a newly created Figure.
 */
protected IFigure createFigure() {
	OutputFigure figure;
	if (getModel() == null)
		return null;
	else if (getModel() instanceof LiveOutput)
		figure =  new LiveOutputFigure();
	else if (getModel() instanceof GroundOutput)
		figure = new GroundFigure();
	else
		figure = new OutputFigure();
	return figure;
}

public Object getAdapter(Class key) {
	if (key == AccessibleAnchorProvider.class)
		return new DefaultAccessibleAnchorProvider() { 
			public List getSourceAnchorLocations() {
				List list = new ArrayList();
				Vector sourceAnchors = getNodeFigure().getSourceConnectionAnchors();
				for (int i=0; i<sourceAnchors.size(); i++) {
					ConnectionAnchor anchor = (ConnectionAnchor)sourceAnchors.get(i);
					list.add(anchor.getReferencePoint().getTranslated(0, -3));
				}
				return list;
			}

		};
	return super.getAdapter(key);
}

/**
 * Returns the Figure for this as an OutputFigure.
 * @return  Figure of this as a OutputFigure.
 */
protected OutputFigure getOutputFigure() {
	return (OutputFigure)getFigure();
}

/**
 * Returns the model of this as a SimpleOutput.
 * @return  Model of this as a SimpleOutput.
 */
protected SimpleOutput getSimpleOutput() {
	return (SimpleOutput)getModel();
}

}
