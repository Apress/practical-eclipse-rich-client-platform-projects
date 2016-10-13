package ch04;

import java.net.URL;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.application.IWorkbenchConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.ide.IDEWorkbenchPlugin;
import org.eclipse.ui.internal.ide.model.WorkbenchAdapterBuilder;
import org.osgi.framework.Bundle;


public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {

    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(
            IWorkbenchWindowConfigurer configurer) {
        return new ApplicationWorkbenchWindowAdvisor(configurer);
    }

    public String getInitialWindowPerspectiveId() {
        return Perspective.ID;
    }

    @Override
    public IAdaptable getDefaultPageInput() {
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        return workspace.getRoot();
    }

    @Override
    public void initialize(IWorkbenchConfigurer configurer) {
        WorkbenchAdapterBuilder.registerAdapters();

        final String ICONS_PATH = "icons/full/";
        final String PATH_OBJECT = ICONS_PATH + "obj16/";
        Bundle ideBundle = Platform
                .getBundle(IDEWorkbenchPlugin.IDE_WORKBENCH);
        declareWorkbenchImage(configurer, ideBundle,
                IDE.SharedImages.IMG_OBJ_PROJECT, PATH_OBJECT
                        + "prj_obj.gif", true);
        declareWorkbenchImage(configurer, ideBundle,
                IDE.SharedImages.IMG_OBJ_PROJECT_CLOSED, PATH_OBJECT
                        + "cprj_obj.gif", true);
        
    }

    private void declareWorkbenchImage(IWorkbenchConfigurer configurer_p,
            Bundle ideBundle, String symbolicName, String path,
            boolean shared) {
        URL url = ideBundle.getEntry(path);
        ImageDescriptor desc = ImageDescriptor.createFromURL(url);
        configurer_p.declareImage(symbolicName, desc, shared);
    }

}
