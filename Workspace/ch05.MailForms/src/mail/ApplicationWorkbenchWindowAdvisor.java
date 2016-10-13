package mail;

import mail.actions.OpenViewAction;
import mail.views.MailView;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchWindowAdvisor 
 extends WorkbenchWindowAdvisor 
{
    // Main Window header
    private Composite header;

    private Control page;

    // Main Window banner image
    private static Image bannerMain = Activator
            .imageDescriptorFromPlugin(Activator.PLUGIN_ID,
                    "icons/banner.png").createImage(); //$NON-NLS-1$
    
    // Window fill image
    private static Image bannerFill = Activator.imageDescriptorFromPlugin(
            Activator.PLUGIN_ID, "icons/fill.jpg").createImage(); 

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
        configurer.setInitialSize(new Point(700, 500));
        
        // hide coolbar, status line, and menu bar
        configurer.setShowCoolBar(false);
        configurer.setShowStatusLine(false);
        configurer.setShowMenuBar(false);
    }

    @Override
    public void createWindowContents(Shell shell) {
        /**
         * Define custom window contents 
         */
        // Menu bar
        final IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
//        final Menu menu = configurer.createMenuBar();
//        shell.setMenuBar(menu);

        // Control the position and size of the children of a composite 
        // control by using FormAttachments to optionally configure the 
        // left, top, right and bottom edges of each child. 
        final FormLayout layout = new FormLayout();
        layout.marginWidth = 0;
        layout.marginHeight = 0;
        shell.setLayout(layout);

        this.header = new Composite(shell, SWT.NONE);
        this.header.setBackground(Display.getDefault().getSystemColor(
                SWT.COLOR_WHITE));

        // 2 column grid layout for the main header
        final GridLayout gLayout = new GridLayout(2, false);
        gLayout.horizontalSpacing = 0;
        gLayout.marginHeight = 0;
        gLayout.marginWidth = 0;
        gLayout.marginWidth = 0;
        gLayout.verticalSpacing = 0;

        this.header.setLayout(gLayout);

        // 1st composite in the header is a toolbar
        final Composite toolbar = new Composite(this.header, SWT.NONE);
        
        // fill the background of the toolbar
        toolbar.setBackgroundImage(bannerFill);
        toolbar.setLayout(new GridLayout(1, false));

        GridData gd = new GridData(SWT.BEGINNING, SWT.CENTER, true, true);
        gd.horizontalIndent = 0;

        // toolbar has 2 actions: Open View, and about
        final ToolBarManager tbm = new ToolBarManager(new ToolBar(toolbar,
                SWT.FLAT));
        tbm.getControl().setBackgroundImage(bannerFill);
        tbm.getControl().setLayoutData(gd);

        // add toolbar actions
        // Open View action
        tbm.add(new OpenViewAction(configurer.getWindow(),
                "Open Another Message MailView", MailView.ID));
        
        // about
        tbm.add(new Action("About", Activator
                .getImageDescriptor("icons/about32.png")) {
            @Override
            public void run() {
                ActionFactory.ABOUT.create(
                        PlatformUI.getWorkbench()
                                .getActiveWorkbenchWindow()).run();
            }
        });

        tbm.update(true);

        gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        toolbar.setLayoutData(gd);

        // Main header image
        final Label mainImage = new Label(this.header, SWT.NONE);
        mainImage.setImage(bannerMain);

        gd = new GridData(SWT.END, SWT.BEGINNING, false, false);
        mainImage.setLayoutData(gd);

        /**
         * Creates the page composite, in which the window's pages, 
         * and their views and editors, appear. 
         */
        this.page = configurer.createPageComposite(shell);

        doLayout();
    }

    /**
     * If the receiver has a layout, asks the layout to lay out 
     * (that is, set the size and location of) the receiver's children. 
     */
    private void doLayout() {
        // layout header
        FormData data = new FormData();
        data.top = new FormAttachment(0, 0);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        this.header.setLayoutData(data);

        // layout custom page
        data = new FormData();
        data.top = new FormAttachment(this.header, 0, SWT.BOTTOM);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.bottom = new FormAttachment(100, 0);
        this.page.setLayoutData(data);
        
        getWindowConfigurer().getWindow().getShell().layout(true);
        if (this.page != null) {
            ((Composite) this.page).layout(true);
        }
    }

}
