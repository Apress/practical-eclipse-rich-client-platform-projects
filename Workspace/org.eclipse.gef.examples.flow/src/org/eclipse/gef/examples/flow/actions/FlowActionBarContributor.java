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
package org.eclipse.gef.examples.flow.actions;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.EditorActionBarContributor;

import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.DeleteRetargetAction;
import org.eclipse.gef.ui.actions.RedoRetargetAction;
import org.eclipse.gef.ui.actions.UndoRetargetAction;

/**
 * Contributes actions to the Editor.
 * @author Daniel Lee
 */
public class FlowActionBarContributor extends ActionBarContributor {

/**
 * @see org.eclipse.gef.ui.actions.ActionBarContributor#buildActions()
 */
protected void buildActions() {
	addRetargetAction(new UndoRetargetAction());
	addRetargetAction(new RedoRetargetAction());
	addRetargetAction(new DeleteRetargetAction());

}

/**
 * @see EditorActionBarContributor#contributeToToolBar(IToolBarManager)
 */
public void contributeToToolBar(IToolBarManager toolBarManager) {
	toolBarManager.add(getAction(IWorkbenchActionConstants.UNDO));
	toolBarManager.add(getAction(IWorkbenchActionConstants.REDO));
}


/**
 * @see org.eclipse.gef.ui.actions.ActionBarContributor#declareGlobalActionKeys()
 */
protected void declareGlobalActionKeys() {
	// TODO Auto-generated method stub

}

}
