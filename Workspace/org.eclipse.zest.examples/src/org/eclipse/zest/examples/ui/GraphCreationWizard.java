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

/**
 * Create new new .shape-file. Those files can be used with the GraphEditor
 * (see plugin.xml).
 * 
 * @author Elias Volanakis
 */
public class GraphCreationWizard extends Wizard implements INewWizard {

    private static int fileCount = 1;
    private IWorkbench workbench;
    

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
     * org.eclipse.jface.viewers.IStructuredSelection)
     */
    public void init(IWorkbench workbench, IStructuredSelection selection) {
        this.workbench = workbench;
    }

    @Override
    public boolean canFinish() {
        return true;
    }
    
    public boolean performFinish() {

//        return page2.finish();
        // create a new file, result != null if successful
        fileCount++;

        // open newly created file in the editor
        IWorkbenchPage page = workbench.getActiveWorkbenchWindow()
                .getActivePage();
        try {
            page.showView(
                    "org.eclipse.zest.examples.views.Graph1View",
                    "view" + fileCount, IWorkbenchPage.VIEW_CREATE);

        } catch (PartInitException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
/*
    private class CreationPage1 extends WizardPage {
        private final IWorkbench workbench;

        protected CreationPage1(IWorkbench workbench) {
            super("graphCreationPage1");
            this.workbench = workbench;
            setTitle("Create a new Zest graph");
            setDescription("Create a new Zest Graph");
        }

        @Override
        public void createControl(Composite parent) {
            Composite container = new Composite(parent, SWT.NULL);
            container.setLayout(new GridLayout(1, false));
            
            GridData data = new GridData(GridData.FILL_HORIZONTAL);
            
            Label l = new Label(container, SWT.NONE);
            l.setText("Click finish to display the graph.");
            
            //setPageComplete(validatePage());
            setControl(container);
            setPageComplete(true);
            
        }

        boolean finish() {
            // create a new file, result != null if successful
            fileCount++;

            // open newly created file in the editor
            IWorkbenchPage page = workbench.getActiveWorkbenchWindow()
                    .getActivePage();
            try {
                page.showView(
                        "org.eclipse.zest.examples.views.Graph1View",
                        "view" + fileCount, IWorkbenchPage.VIEW_CREATE);

            } catch (PartInitException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        protected boolean validatePage() {
            return true;
        }
    }
*/
}
