package ch02.browser.views;

import java.io.IOException;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.LocationEvent;
import org.eclipse.swt.browser.LocationListener;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.CoolItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import ch02.browser.internal.BrowserActivator;
import ch02.browser.util.SimpleHTTPClient;

public class WebBrowserView extends ViewPart {
    static public String ID = WebBrowserView.class.getName();

    private Combo urlCombo;
    private Browser browser;

    // Local View Actions
    private Action actionBack;
    private Action actionForward;
    private Action actionHome;
    private Action actionAddBookmark;

    private final String startUrl = "http://www.google.com/";

    public static ImageDescriptor ICON_HOME = BrowserActivator
            .getImageDescriptor("icons/16x16-home.gif");

    private IStatusLineManager statusLine;

    public WebBrowserView() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void createPartControl(Composite parent) {
        Composite comp = new Composite(parent, SWT.NONE);
        comp.setLayout(new GridLayout(1, true));

        CoolBar coolbar = new CoolBar(comp, SWT.NONE);
        coolbar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        // Create a cool item with a URL combo
        CoolItem item = new CoolItem(coolbar, SWT.NONE);
        item.setControl(createComboView(coolbar, new GridData(
                GridData.FILL_HORIZONTAL))); // gridData));
        calcSize(item);

        /*
         * Use IE on Win32. MOZILLA in Linux/OSX. See:
         * http://www.eclipse.org/swt/faq.php#howusemozilla Mozilla
         * requires XulRunner:
         * http://releases.mozilla.org/pub/mozilla.org/xulrunner
         * /releases/1.8.1.3/contrib/ XR install:
         * http://developer.mozilla.org
         * /en/docs/XULRunner_1.8.0.1_Release_Notes#Installing_XULRunner
         */
        try {
            browser = new Browser(comp, SWT.BORDER); // MOZILLA);
        } catch (SWTError e) {
            BrowserActivator.showErrorMessage(getViewSite().getShell(),
                    "Error creating browser:" + e);
            return;
        }

        browser.setLayoutData(new GridData(GridData.FILL_BOTH));
        browser.setUrl(startUrl);

        browser.addLocationListener(new LocationListener() {
            public void changed(LocationEvent event) {
                locChanged(event);
            }

            public void changing(LocationEvent event) {
                locChanging(event);
            }
        });

        // Progress listener
        browser.addProgressListener(new ProgressListener() {
            public void changed(ProgressEvent event) {
                onProgress(event);
            }

            public void completed(ProgressEvent event) {
            }
        });

        makeActions();
        contributeToActionBars();

        // Status line
        statusLine = getViewSite().getActionBars().getStatusLineManager();
    }

    @Override
    public void setFocus() {
        browser.setFocus();
    }

    /**
     * Creates the urlCombo box view.
     * 
     * @param parent
     *            the parent control
     */
    private Control createComboView(Composite parent, Object layoutData) {
        urlCombo = new Combo(parent, SWT.NONE);
        urlCombo.setLayoutData(layoutData);
        urlCombo.addSelectionListener(new SelectionListener() {

            public void widgetDefaultSelected(SelectionEvent e) {

                final String url = ((Combo) e.getSource()).getText();
                browser.setUrl(url);
                urlCombo.add(url);
            }

            public void widgetSelected(SelectionEvent e) {
                browser.setUrl(((Combo) e.getSource()).getText());
            }

        });

        return urlCombo;
    }

    /**
     * Helper method to calculate the size of the cool item
     * 
     * @param item
     *            the cool item
     */
    private void calcSize(CoolItem item) {
        Control control = item.getControl();
        org.eclipse.swt.graphics.Point pt = control.computeSize(
                SWT.DEFAULT, SWT.DEFAULT);
        pt = item.computeSize(pt.x, pt.y);
        item.setSize(pt);
    }

    private void makeActions() {
        actionBack = new Action() {
            public void run() {
                browser.back();
            }
        };
        actionBack.setText("Back");
        actionBack.setToolTipText("Back");
        actionBack.setImageDescriptor(PlatformUI.getWorkbench()
                .getSharedImages().getImageDescriptor(
                        ISharedImages.IMG_TOOL_BACK));

        actionForward = new Action() {
            public void run() {
                browser.forward();
            }
        };
        actionForward.setText("Forward");
        actionForward.setToolTipText("Forward");
        actionForward.setImageDescriptor(PlatformUI.getWorkbench()
                .getSharedImages().getImageDescriptor(
                        ISharedImages.IMG_TOOL_FORWARD));

        actionHome = new Action() {
            public void run() {
                browser.setUrl(startUrl);
            }
        };
        actionHome.setText("Home");
        actionHome.setToolTipText("Home");
        actionHome.setImageDescriptor(ICON_HOME);

        actionAddBookmark = new Action() {
            public void run() {
                addBookmark(urlCombo.getText());
            }
        };
        actionAddBookmark.setText("Add Bookmark");
        actionAddBookmark.setToolTipText("Add Bookmark");
        actionAddBookmark.setImageDescriptor(PlatformUI.getWorkbench()
                .getSharedImages().getImageDescriptor(
                        ISharedImages.IMG_OBJ_FILE));

    }

    private void contributeToActionBars() {
        IActionBars bars = getViewSite().getActionBars();
        fillLocalPullDown(bars.getMenuManager());
        fillLocalToolBar(bars.getToolBarManager());

    }

    private void fillLocalToolBar(IToolBarManager manager) {
        // Fill local toobar
        manager.add(actionHome);
        manager.add(actionBack);
        manager.add(actionForward);

        // Other plugins can add actions here
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }

    private void fillLocalPullDown(IMenuManager manager) {
        manager.add(actionAddBookmark);

        // Other plugins can add actions here
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }

    /**
     * Add book-mark
     * 
     * @param url
     */
    private void addBookmark(final String url) {
        BookMarksView v = (BookMarksView) BrowserActivator.getView(
                getViewSite().getWorkbenchWindow(), BookMarksView.ID);
        if (v != null)
            v.addBookmark(url);

    }

    /**
     * Fires when the location in the web browser is changing
     * 
     * @param evt
     */
    void locChanging(LocationEvent evt) {
        String location = evt.location;

        // TODO: Status line
        statusLine.getProgressMonitor().beginTask(location, 100);
    }

    /**
     * TODO: Trap requests content types Fires when the location in the web
     * browser is changing
     * Don't forget to add a location listener to the browser widget
     * as follows: 
     *  browser.addLocationListener(new LocationListener() {
     *    public void changing(LocationEvent event) {
     *      locChanging(event);
     *    }
     * });
     */
    void locChanging1(LocationEvent evt) {
        String location = evt.location;

        if (!location.startsWith("http")) {
            System.out.println("WebBrowser::abort loc=" + location);
            return;
        }

        // TODO: handle custom content type
        try {
            SimpleHTTPClient client = new SimpleHTTPClient(location);
            String response = client.doGet();

            if (client.isContentTypeKML() || client.isContentTypeKMZ()) {
                evt.doit = false;

                // handle kml/kmz
                // handleKmlKmz( location);
                System.out.println(response);
            }
        } catch (IOException e) {
        }
    }

    /**
     * Fires after the web browser location has changed
     * 
     * @param event
     */
    void locChanged(LocationEvent event) {
        urlCombo.setText(event.location);
    }

    /**
     * 
     * @param event
     */
    private void onProgress(ProgressEvent event) {
        if (event.total == 0)
            return;
        int ratio = event.current * 100 / event.total;

        // TODO: Status line
        statusLine.getProgressMonitor().worked(ratio);

    }

    public void navigateTo(String url) {
        browser.setUrl(url);
    }
}
