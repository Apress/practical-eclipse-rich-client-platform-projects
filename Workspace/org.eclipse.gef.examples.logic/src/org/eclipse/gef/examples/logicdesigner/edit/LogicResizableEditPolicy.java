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

import java.util.Iterator;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Rectangle;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;

import org.eclipse.gef.examples.logicdesigner.figures.AndGateFeedbackFigure;
import org.eclipse.gef.examples.logicdesigner.figures.CircuitFeedbackFigure;
import org.eclipse.gef.examples.logicdesigner.figures.GroundFeedbackFigure;
import org.eclipse.gef.examples.logicdesigner.figures.LEDFeedbackFigure;
import org.eclipse.gef.examples.logicdesigner.figures.LabelFeedbackFigure;
import org.eclipse.gef.examples.logicdesigner.figures.LiveOutputFeedbackFigure;
import org.eclipse.gef.examples.logicdesigner.figures.LogicColorConstants;
import org.eclipse.gef.examples.logicdesigner.figures.LogicFlowFeedbackFigure;
import org.eclipse.gef.examples.logicdesigner.figures.OrGateFeedbackFigure;
import org.eclipse.gef.examples.logicdesigner.figures.XOrGateFeedbackFigure;
import org.eclipse.gef.examples.logicdesigner.model.AndGate;
import org.eclipse.gef.examples.logicdesigner.model.Circuit;
import org.eclipse.gef.examples.logicdesigner.model.GroundOutput;
import org.eclipse.gef.examples.logicdesigner.model.LED;
import org.eclipse.gef.examples.logicdesigner.model.LiveOutput;
import org.eclipse.gef.examples.logicdesigner.model.LogicFlowContainer;
import org.eclipse.gef.examples.logicdesigner.model.LogicLabel;
import org.eclipse.gef.examples.logicdesigner.model.OrGate;
import org.eclipse.gef.examples.logicdesigner.model.XORGate;


/**
 * 
 */
public class LogicResizableEditPolicy
	extends ResizableEditPolicy
{

/**
 * Creates the figure used for feedback.
 * @return the new feedback figure
 */
protected IFigure createDragSourceFeedbackFigure() {
	IFigure figure = createFigure((GraphicalEditPart)getHost(), null);
	
	figure.setBounds(getInitialFeedbackBounds());
	addFeedback(figure);
	return figure;
}

protected IFigure createFigure(GraphicalEditPart part, IFigure parent) {
	IFigure child = getCustomFeedbackFigure(part.getModel());
		
	if (parent != null)
		parent.add(child);

	Rectangle childBounds = part.getFigure().getBounds().getCopy();
	
	IFigure walker = part.getFigure().getParent();
	
	while (walker != ((GraphicalEditPart)part.getParent()).getFigure()) {
		walker.translateToParent(childBounds);
		walker = walker.getParent();
	}
	
	child.setBounds(childBounds);
	
	Iterator i = part.getChildren().iterator();
	
	while (i.hasNext())
		createFigure((GraphicalEditPart)i.next(), child);
	
	return child;
}

protected IFigure getCustomFeedbackFigure(Object modelPart) {
	IFigure figure; 
	
	if (modelPart instanceof Circuit)
		figure = new CircuitFeedbackFigure();
	else if (modelPart instanceof LogicFlowContainer)
		figure = new LogicFlowFeedbackFigure();		
	else if (modelPart instanceof LogicLabel)
		figure = new LabelFeedbackFigure();
	else if (modelPart instanceof LED) 
		figure = new LEDFeedbackFigure();
	else if (modelPart instanceof OrGate)
		figure = new OrGateFeedbackFigure();
	else if (modelPart instanceof XORGate)
		figure = new XOrGateFeedbackFigure();
	else if (modelPart instanceof GroundOutput)
		figure = new GroundFeedbackFigure();
	else if (modelPart instanceof LiveOutput)
		figure = new LiveOutputFeedbackFigure();
	else if (modelPart instanceof AndGate) 
		figure = new AndGateFeedbackFigure();
	else {
		figure = new RectangleFigure();
		((RectangleFigure)figure).setXOR(true);
		((RectangleFigure)figure).setFill(true);
		figure.setBackgroundColor(LogicColorConstants.ghostFillColor);
		figure.setForegroundColor(ColorConstants.white);
	}
	
	return figure;
}

/**
 * Returns the layer used for displaying feedback.
 *  
 * @return the feedback layer
 */
protected IFigure getFeedbackLayer() {
	return getLayer(LayerConstants.SCALED_FEEDBACK_LAYER);
}

/**
 * @see org.eclipse.gef.editpolicies.NonResizableEditPolicy#getInitialFeedbackBounds()
 */
protected Rectangle getInitialFeedbackBounds() {
	return getHostFigure().getBounds();
}

}
