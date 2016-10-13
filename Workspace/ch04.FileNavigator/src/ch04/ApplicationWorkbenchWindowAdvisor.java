package ch04;

import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import ch04.jobs.Race;

public class ApplicationWorkbenchWindowAdvisor extends
        WorkbenchWindowAdvisor {

    public ApplicationWorkbenchWindowAdvisor(
            IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(
            IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }

    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setShowCoolBar(false);
        configurer.setShowStatusLine(false);
    }

    @Override
    public void postWindowOpen() {
        super.postWindowOpen();
        new Race().start();
    }
}
