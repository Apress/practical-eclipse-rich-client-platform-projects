package ch06.infocenter;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {
    // Help actions
    private IWorkbenchAction showHelpAction;
    private IWorkbenchAction searchHelpAction;
    private IWorkbenchAction dynamicHelpAction;

    public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
        super(configurer);
    }

    protected void makeActions(IWorkbenchWindow window) {
        // Help Contents
        showHelpAction = ActionFactory.HELP_CONTENTS.create(window);
        register(showHelpAction);

        // Help Search
        searchHelpAction = ActionFactory.HELP_SEARCH.create(window);
        register(searchHelpAction);

        // Dynamic Help
        dynamicHelpAction = ActionFactory.DYNAMIC_HELP.create(window);
        register(dynamicHelpAction);

    }

    protected void fillMenuBar(IMenuManager menuBar) {
        MenuManager helpMenu = new MenuManager("&Help",
                IWorkbenchActionConstants.M_HELP);

        // Help menu options
        helpMenu.add(showHelpAction);
        helpMenu.add(searchHelpAction);
        helpMenu.add(dynamicHelpAction);
        helpMenu.add(new Separator());

        menuBar.add(helpMenu);
    }

}
