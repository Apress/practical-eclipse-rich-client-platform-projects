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
package org.eclipse.gef.examples.flow.model.commands;

import org.eclipse.gef.commands.Command;

import org.eclipse.gef.examples.flow.model.Activity;
import org.eclipse.gef.examples.flow.model.StructuredActivity;
import org.eclipse.gef.examples.flow.model.Transition;

/**
 * @author Daniel Lee
 */
public class AddAndAssignSourceCommand extends Command {

private StructuredActivity parent;
private Activity child;
private Activity source;
private Transition transition;

/**
 * @see org.eclipse.gef.commands.Command#canExecute()
 */
public boolean canExecute() {
	for(int i = 0; i < source.getOutgoingTransitions().size(); i++) {
		Activity target = ((Transition)source.getOutgoingTransitions().get(i)).target;
		if (target.equals(child))
			return false;
	}
	return true;
}

/**
 * @see org.eclipse.gef.commands.Command#execute()
 */
public void execute() {
	parent.addChild(child);
	transition = new Transition(source, child);
}

/**
 * @see org.eclipse.gef.commands.Command#redo()
 */
public void redo() {
	source.addOutput(transition);
	child.addInput(transition);
	parent.addChild(child);
}


/**
 * Sets the parent ActivityDiagram
 * @param sa the parent
 */
public void setParent(StructuredActivity sa) {
	parent = sa;
}

/**
 * Sets the Activity to create
 * @param activity the Activity to create
 */
public void setChild(Activity activity) {
	child = activity;
}

/**
 * Sets the source to the passed activity
 * @param activity the source
 */
public void setSource(Activity activity) {
	source = activity;
}

/**
 * @see org.eclipse.gef.commands.Command#undo()
 */
public void undo() {
	source.removeOutput(transition);
	child.removeInput(transition);
	parent.removeChild(child);
}

}
