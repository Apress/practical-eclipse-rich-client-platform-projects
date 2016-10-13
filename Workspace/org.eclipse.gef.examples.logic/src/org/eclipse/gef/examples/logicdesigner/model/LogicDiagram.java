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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Image;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import org.eclipse.draw2d.PositionConstants;

import org.eclipse.gef.examples.logicdesigner.LogicMessages;

public class LogicDiagram
	extends LogicSubpart
{
static final long serialVersionUID = 1;

public static String ID_ROUTER = "router";	//$NON-NLS-1$
public static Integer ROUTER_MANUAL = new Integer(0);
public static Integer ROUTER_MANHATTAN = new Integer(1);
public static Integer ROUTER_SHORTEST_PATH = new Integer(2);
private static int count;
private static Image LOGIC_ICON = createImage(LogicDiagram.class, "icons/circuit16.gif"); //$NON-NLS-1$


protected List children = new ArrayList();
protected LogicRuler leftRuler, topRuler;
protected Integer connectionRouter = null;
private boolean rulersVisibility = false;
private boolean snapToGeometry = false;
private boolean gridEnabled = false;
private double zoom = 1.0;

public LogicDiagram() {
	size.width = 100;
	size.height= 100;
	location.x = 20;
	location.y = 20;
	createRulers();
}

public void addChild(LogicElement child){
	addChild(child, -1);
}

public void addChild(LogicElement child, int index){
	if (index >= 0)
		children.add(index,child);
	else
		children.add(child);
	fireChildAdded(CHILDREN, child, new Integer(index));
}

protected void createRulers() {
	leftRuler = new LogicRuler(false);
	topRuler = new LogicRuler(true);
}

public List getChildren(){
	return children;
}

public Integer getConnectionRouter(){
	if(connectionRouter==null)
		connectionRouter = ROUTER_MANUAL;
	return connectionRouter;
}

public Image getIconImage() {
	return LOGIC_ICON;
}

public String getNewID() {
	return Integer.toString(count++);
}

public double getZoom() {
	return zoom;
}

/**
 * Returns <code>null</code> for this model. Returns
 * normal descriptors for all subclasses.
 *
 * @return  Array of property descriptors.
 */
public IPropertyDescriptor[] getPropertyDescriptors() {
	if(getClass().equals(LogicDiagram.class)){
		ComboBoxPropertyDescriptor cbd = new ComboBoxPropertyDescriptor(
				ID_ROUTER, 
				LogicMessages.PropertyDescriptor_LogicDiagram_ConnectionRouter,
				new String[]{
					LogicMessages.PropertyDescriptor_LogicDiagram_Manual,
					LogicMessages.PropertyDescriptor_LogicDiagram_Manhattan,
					LogicMessages.PropertyDescriptor_LogicDiagram_ShortestPath});
		cbd.setLabelProvider(new ConnectionRouterLabelProvider());
		return new IPropertyDescriptor[]{cbd};
	}
	return super.getPropertyDescriptors();
}

public Object getPropertyValue(Object propName) {
	if(propName.equals(ID_ROUTER))
		return connectionRouter;
	return super.getPropertyValue(propName);
}

public LogicRuler getRuler(int orientation) {
	LogicRuler result = null;
	switch (orientation) {
		case PositionConstants.NORTH :
			result = topRuler;
			break;
		case PositionConstants.WEST :
			result = leftRuler;
			break;
	}
	return result;
}

public boolean getRulerVisibility() {
	return rulersVisibility;
}

public boolean isGridEnabled() {
	return gridEnabled;
}

public boolean isSnapToGeometryEnabled() {
	return snapToGeometry;
}

private void readObject(java.io.ObjectInputStream s)
		throws IOException, ClassNotFoundException {
	s.defaultReadObject();
}
public void removeChild(LogicElement child){
	children.remove(child);
	fireChildRemoved(CHILDREN, child);
}

public void setConnectionRouter(Integer router){
	Integer oldConnectionRouter = connectionRouter;
	connectionRouter = router;
	firePropertyChange(ID_ROUTER, oldConnectionRouter, connectionRouter);
}

public void setPropertyValue(Object id, Object value){
	if(ID_ROUTER.equals(id))
		setConnectionRouter((Integer)value);
	else super.setPropertyValue(id,value);
}

public void setRulerVisibility(boolean newValue) {
	rulersVisibility = newValue;
}

public void setGridEnabled(boolean isEnabled) {
	gridEnabled = isEnabled;
}

public void setSnapToGeometry(boolean isEnabled) {
	snapToGeometry = isEnabled;
}

public void setZoom(double zoom) {
	this.zoom = zoom;
}

public String toString(){
	return LogicMessages.LogicDiagram_LabelText;
}

private class ConnectionRouterLabelProvider 
	extends org.eclipse.jface.viewers.LabelProvider{

	public ConnectionRouterLabelProvider(){
		super();
	}
	public String getText(Object element){
		if(element instanceof Integer){
			Integer integer = (Integer)element;
			if(ROUTER_MANUAL.intValue()==integer.intValue())
				return LogicMessages.PropertyDescriptor_LogicDiagram_Manual;
			if(ROUTER_MANHATTAN.intValue()==integer.intValue())
				return LogicMessages.PropertyDescriptor_LogicDiagram_Manhattan;
			if(ROUTER_SHORTEST_PATH.intValue()==integer.intValue())
				return LogicMessages.PropertyDescriptor_LogicDiagram_ShortestPath;
		}
		return super.getText(element);
	}
}

}
