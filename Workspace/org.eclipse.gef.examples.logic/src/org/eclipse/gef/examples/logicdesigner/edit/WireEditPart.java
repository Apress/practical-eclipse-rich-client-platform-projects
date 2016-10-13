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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ManhattanConnectionRouter;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.RelativeBendpoint;

import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;

import org.eclipse.gef.examples.logicdesigner.LogicMessages;
import org.eclipse.gef.examples.logicdesigner.figures.FigureFactory;
import org.eclipse.gef.examples.logicdesigner.model.Wire;
import org.eclipse.gef.examples.logicdesigner.model.WireBendpoint;

/**
 * Implements a Connection Editpart to represnt a Wire like
 * connection.
 *
 */
public class WireEditPart
	extends AbstractConnectionEditPart
	implements PropertyChangeListener
{

AccessibleEditPart acc;

public static final Color
	alive = new Color(Display.getDefault(),0,74,168),
	dead  = new Color(Display.getDefault(),0,0,0);

public void activate(){
	super.activate();
	getWire().addPropertyChangeListener(this);
}

public void activateFigure(){
	super.activateFigure();
	/*Once the figure has been added to the ConnectionLayer, start listening for its
	 * router to change.
	 */
	getFigure().addPropertyChangeListener(Connection.PROPERTY_CONNECTION_ROUTER, this);
}

/**
 * Adds extra EditPolicies as required. 
 */
protected void createEditPolicies() {
	installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new WireEndpointEditPolicy());
	//Note that the Connection is already added to the diagram and knows its Router.
	refreshBendpointEditPolicy();
	installEditPolicy(EditPolicy.CONNECTION_ROLE,new WireEditPolicy());
}

/**
 * Returns a newly created Figure to represent the connection.
 *
 * @return  The created Figure.
 */
protected IFigure createFigure() {
	Connection connx = FigureFactory.createNewBendableWire(getWire());
	return connx;
}

public void deactivate(){
	getWire().removePropertyChangeListener(this);
	super.deactivate();
}

public void deactivateFigure(){
	getFigure().removePropertyChangeListener(Connection.PROPERTY_CONNECTION_ROUTER, this);
	super.deactivateFigure();
}

public AccessibleEditPart getAccessibleEditPart(){
	if (acc == null)
		acc = new AccessibleGraphicalEditPart(){
			public void getName(AccessibleEvent e) {
				e.result = LogicMessages.Wire_LabelText;
			}
		};
	return acc;
}

/**
 * Returns the model of this represented as a Wire.
 * 
 * @return  Model of this as <code>Wire</code>
 */
protected Wire getWire() {
	return (Wire)getModel();
}

/**
 * Returns the Figure associated with this, which draws the
 * Wire.
 *
 * @return  Figure of this.
 */
protected IFigure getWireFigure() {
	return (PolylineConnection) getFigure();
}

/**
 * Listens to changes in properties of the Wire (like the
 * contents being carried), and reflects is in the visuals.
 *
 * @param event  Event notifying the change.
 */
public void propertyChange(PropertyChangeEvent event) {
	String property = event.getPropertyName();
	if (Connection.PROPERTY_CONNECTION_ROUTER.equals(property)){
		refreshBendpoints();
		refreshBendpointEditPolicy();
	}
	if ("value".equals(property))   //$NON-NLS-1$
		refreshVisuals();
	if ("bendpoint".equals(property))   //$NON-NLS-1$
		refreshBendpoints();       
}

/**
 * Updates the bendpoints, based on the model.
 */
protected void refreshBendpoints() {
	if (getConnectionFigure().getConnectionRouter() instanceof ManhattanConnectionRouter)
		return;
	List modelConstraint = getWire().getBendpoints();
	List figureConstraint = new ArrayList();
	for (int i=0; i<modelConstraint.size(); i++) {
		WireBendpoint wbp = (WireBendpoint)modelConstraint.get(i);
		RelativeBendpoint rbp = new RelativeBendpoint(getConnectionFigure());
		rbp.setRelativeDimensions(wbp.getFirstRelativeDimension(),
									wbp.getSecondRelativeDimension());
		rbp.setWeight((i+1) / ((float)modelConstraint.size()+1));
		figureConstraint.add(rbp);
	}
	getConnectionFigure().setRoutingConstraint(figureConstraint);
}

private void refreshBendpointEditPolicy(){
	if (getConnectionFigure().getConnectionRouter() instanceof ManhattanConnectionRouter)
		installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE, null);
	else
		installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE, new WireBendpointEditPolicy());
}

/**
 * Refreshes the visual aspects of this, based upon the
 * model (Wire). It changes the wire color depending on
 * the state of Wire.
 * 
 */
protected void refreshVisuals() {
	refreshBendpoints();
	if (getWire().getValue())
		getWireFigure().setForegroundColor(alive);
	else
		getWireFigure().setForegroundColor(dead);
}

}
