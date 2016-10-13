package ch03;

import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import ch02.browser.perspective.WebBrowserPerspective;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {


    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        return new ApplicationWorkbenchWindowAdvisor(configurer);
    }

	public String getInitialWindowPerspectiveId() {
		return  WebBrowserPerspective.ID; 
	}
}
