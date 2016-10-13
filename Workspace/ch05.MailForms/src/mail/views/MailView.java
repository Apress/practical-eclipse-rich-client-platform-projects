package mail.views;

import java.util.Date;

import mail.Activator;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.ViewPart;

public class MailView extends ViewPart {

    /**
     * Eclipse forms support.
     */
    private FormToolkit toolkit;
    private Form form;

    public static final String ID = "Mail.view";

    // Form header icon
    private final ImageDescriptor FORM_ICON = Activator
            .getImageDescriptor("icons/sample3.gif");

    
//    final Font boldFont = JFaceResources.getFontRegistry().getBold(
//            JFaceResources.DEFAULT_FONT);

    /**
     * Creates the SWT controls for this workbench part.
     */
    public void createPartControl(Composite parent) {
        // Create a Form API toolkit
        toolkit = new FormToolkit(parent.getDisplay());

        /**
         * Create a form widget in the provided parent. Note that this
         * widget does not scroll its content. If you require scrolling,
         * use 'createScrolledForm' instead
         */
        form = toolkit.createForm(parent);
        form.setText("user@aol.com");
        form.setImage(FORM_ICON.createImage());

        // Takes advantage of the gradients and other capabilities
        // to decorate the form heading
        toolkit.decorateFormHeading(form);

        // flat look
        toolkit.paintBordersFor(form.getBody());

        // Add a 1 column layout to the scrolled form contents
        TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 1;

        form.getBody().setLayout(layout);

        // form toolbar
        createToolBar();

        Composite body = form.getBody();

        // Message subject widgets
        toolkit.createLabel(body, "Subject:");
        toolkit.createLabel(body,
                "This is a message about the cool Eclipse RCP!");
        toolkit.createLabel(body, "From:");

        // hyper link with listener
        Hyperlink link = toolkit.createHyperlink(body, "user@aol.com",
                SWT.WRAP);

        link.addHyperlinkListener(new HyperlinkAdapter() {
            @Override
            public void linkActivated(
                    org.eclipse.ui.forms.events.HyperlinkEvent e) {
                MessageDialog.openInformation(getSite().getShell(),
                  "Not Implemented",
                  "Open the address book or a new message being created.");
            }
        });

        // Date
        toolkit.createLabel(body, "Date:");
        toolkit.createLabel(body, new Date().toString());

        // Text widget
        Text text = new Text(body, SWT.MULTI | SWT.WRAP);
        text.setText("This RCP Application was generated from the PDE "
                + "Plug-in Project wizard. This sample shows how to:\n"
                + "- add a top-level menu and toolbar with actions\n"
                + "- add keybindings to actions\n"
                + "- create views that can't be closed and\n"
                + "  multiple instances of the same view\n"
                + "- perspectives with placeholders for new views\n"
                + "- use the default about dialog\n"
                + "- create a product definition\n");

        text.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));
    }

    /**
     * Create a form tool bar
     */
    private void createToolBar() {
        // tool bar icons
        ImageDescriptor TB_ICON_1 = Activator
                .getImageDescriptor("icons/close16.png");

        // Action #1: Hide view
        Action toolBtn1 = new Action() {
            public void run() {
                MailView.this.getViewSite().getPage().hideView(
                        MailView.this);
            }
        };
        toolBtn1.setToolTipText("Hide this View");
        toolBtn1.setText("Hide this View");
        toolBtn1.setImageDescriptor(TB_ICON_1);

        // Add tool bar actions
        form.getToolBarManager().add(toolBtn1);
        form.getToolBarManager().add(new Separator());

        form.getToolBarManager().update(true);

        // Sets the tool bar vertical alignment relative to the header.
        // Can be useful when there is more free space at the second row
        form.setToolBarVerticalAlignment(SWT.LEFT);
    }

    public void setFocus() {
        form.setFocus();
    }

    /**
     * Form Message Handling
     * 
     * @param text
     *            Message to display
     * @param type
     *            One of IMessageProvider.ERROR , NONE, WRNING, INFORMATION
     */
    @SuppressWarnings("unused")
    private void setFormMessage(String text, int type) {
        /**
         * Adds a message hyperlink listener. messages will be rendered as
         * hyperlinks.
         */
        form.addMessageHyperlinkListener(new HyperlinkAdapter());
        form.setMessage(text, type);
    }

}
