/*******************************************************************************
 * Copyright (c) 2004, 2005 Elias Volanakis and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Elias Volanakis - initial API and implementation
 *******************************************************************************/
package org.eclipse.zest.examples.ui;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.zest.examples.views.TreeGraphView;

public class TreeGraphCreationWizard extends Wizard implements INewWizard {

    private static int fileCount = 1;
    private IWorkbench workbench;

    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.workbench = workbench;
    }

    @Override
    public boolean canFinish() {
        return true;
    }

    @Override
    public boolean performFinish() {
        // create a new file, result != null if successful
        fileCount++;

        // open newly created file in the editor
        IWorkbenchPage page = workbench.getActiveWorkbenchWindow()
                .getActivePage();
        try {
            page.showView(TreeGraphView.ID, "treeView" + fileCount,
                    IWorkbenchPage.VIEW_CREATE);

        } catch (PartInitException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
