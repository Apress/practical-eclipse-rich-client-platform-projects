package dummy;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.ViewPart;

public class View extends ViewPart implements Listener
// Implementers of Listener provide a simple handleEvent()
// method that is used internally by SWT to dispatch events.
{
    public static final String ID = View.class.getName();

    /**
     * Eclipse forms support. Requires the plugin: org.eclipse.ui.forms
     */
    private FormToolkit toolkit;
    private ScrolledForm scrolledForm;

    private ImageDescriptor FORM_ICON = Activator
            .getImageDescriptor("icons/alt_window_16.gif");

    private TableViewer viewer;

    /**
     * The content provider class is responsible for providing objects to
     * the view. It can wrap existing objects in adapters or simply return
     * objects as-is. These objects may be sensitive to the current input
     * of the view, or ignore it and always show the same content (like
     * Task List, for example).
     */
    class ViewContentProvider implements IStructuredContentProvider {
        public void inputChanged(Viewer v, Object oldInput, Object newInput) {
        }

        public void dispose() {
        }

        public Object[] getElements(Object parent) {
            return new String[] { "One", "Two", "Three" };
        }
    }

    class ViewLabelProvider extends LabelProvider implements
            ITableLabelProvider {
        public String getColumnText(Object obj, int index) {
            return getText(obj);
        }

        public Image getColumnImage(Object obj, int index) {
            return getImage(obj);
        }

        public Image getImage(Object obj) {
            return PlatformUI.getWorkbench().getSharedImages().getImage(
                    ISharedImages.IMG_OBJ_ELEMENT);
        }
    }

    /**
     * This is a call back that will allow us to create the viewer and
     * initialize it.
     */
    public void createPartControl(Composite parent) {
        // Create a Form API toolkit
        toolkit = new FormToolkit(getFromColors(parent.getDisplay()));

        /**
         * Create a scrolled form widget in the provided parent. If you do
         * not require scrolling because there is already a scrolled
         * composite up the parent chain, use 'createForm' instead
         */
        scrolledForm = toolkit.createScrolledForm(parent);

        // Form title & image
        scrolledForm.setFont(new Font(null, "Times", 18, SWT.BOLD
                | SWT.ITALIC));
        scrolledForm.setText("Form Title");
        scrolledForm.setImage(FORM_ICON.createImage());

        /**
         * Takes advantage of the gradients and other capabilities to
         * decorate the form heading using colors computed based on the
         * current skin and operating system.
         */
        toolkit.decorateFormHeading(scrolledForm.getForm());

        // Add a 1 column layout to the scrolled form contents
        TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 1;

        scrolledForm.getBody().setLayout(layout);

        /**
         * Create form toolbar
         */
        createToolBar();

        /**
         * Add a form drop down menu
         */
        addFromDropDownMenu();

        /**
         * Add controls
         */
        addCommonControls();

        addComplexControls();

        addExpandableSectionWithTable();

        addFormTextControl();
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    public void setFocus() {
        scrolledForm.setFocus();
    }

    /**
     * TOOLBAR CONTROLS
     */

    /**
     * Create a form tool bar
     */
    private void createToolBar() {
        // 2 tool bar icons
        ImageDescriptor TB_ICON_1 = Activator
                .getImageDescriptor("icons/alt_window_16.gif");
        ImageDescriptor TB_ICON_2 = Activator
                .getImageDescriptor("icons/sample3.gif");

        Form form = scrolledForm.getForm();

        // Tool bar action #1:
        Action toolBtn1 = new Action() {
            public void run() {
                setFormMessage("Tool button 1",
                        IMessageProvider.INFORMATION);
            }
        };
        toolBtn1.setToolTipText("Tool button 1");
        toolBtn1.setText("Tool button 1");
        toolBtn1.setImageDescriptor(TB_ICON_1);

        // Tool bar action #2
        Action toolBtn2 = new Action() {
            public void run() {
                setFormMessage("Tool button 2", IMessageProvider.WARNING);
            }
        };
        toolBtn2.setToolTipText("Tool button 2");
        toolBtn2.setText("Tool button 2");
        toolBtn2.setImageDescriptor(TB_ICON_2);

        // Add toolbat actions
        form.getToolBarManager().add(toolBtn1);
        form.getToolBarManager().add(toolBtn2);
        form.getToolBarManager().add(new Separator());

        form.getToolBarManager().update(true);

        // Sets the tool bar vertical alignment relative to the header.
        // Can be useful when there is more free space at the second row
        form.setToolBarVerticalAlignment(SWT.LEFT);
    }

    /**
     * Form Drop-Down Menus
     */
    private void addFromDropDownMenu() {
        // drop down menu icon
        ImageDescriptor DD_ICON_1 = Activator
                .getImageDescriptor("icons/sample3.gif");

        Form form = scrolledForm.getForm();

        // drop down menu action #1 ith icon and message
        form.getMenuManager().add(new Action("Menu option 1", DD_ICON_1) {
            @Override
            public void run() {
                setFormMessage("Menu option1",
                        IMessageProvider.INFORMATION);
            }
        });

        // drop down action 2, no icon or message
        form.getMenuManager().add(new Action("Menu option 2") {
        });
    }

    /**
     * Form Message Handling
     * 
     * @param text
     *            Message to display
     * @param type
     *            : one of IMessageProvider.ERROR , NONE, WRNING,
     *            INFORMATION
     * 
     */
    private void setFormMessage(String text, int type) {
        Form form = scrolledForm.getForm();

        /**
         * Adds a message hyperlink listener. messages will be rendered as
         * hyperlinks.
         */
        form.addMessageHyperlinkListener(new HyperlinkAdapter());
        form.setMessage(text, type);
    }

    /**
     * COMMON CONTROLS
     */
    private void addCommonControls() {
        // tip: to have he background of controls match the background of
        // the form don't use:
        // Label label = new Label(form.getBody(), SWT.NULL);
        // label.setText("Label");
        // Text text = new Text(form.getBody(), SWT.BORDER);

        // Use
        // toolkit.createLabel(scrolledForm.getBody(), "Label");
        // Text text = toolkit.createText(scrolledForm.getBody(), "",
        // SWT.FILL);

        toolkit.createLabel(scrolledForm.getBody(), "Label");

        // Text box
        // Tip: To paint borders for the text box use:
        // toolkit.paintBordersFor(text.getParent());

        Text text = toolkit.createText(scrolledForm.getBody(), "",
                SWT.FILL);
        text.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB));

        // paint borders
        toolkit.paintBordersFor(text.getParent());

        Button b1 = toolkit.createButton(scrolledForm.getBody(),
                "Check Box", SWT.CHECK);
        Button b2 = toolkit.createButton(scrolledForm.getBody(),
                "Push Button", SWT.PUSH);

        b1.addListener(SWT.Selection, this);
        b2.addListener(SWT.Selection, this);
    }

    /**
     * COMPLEX CONTROLS
     */
    private void addComplexControls() {
        createHyperLink();
        createExpandableComposite();
    }

    private void createHyperLink() {
        // hyper link with listener
        Hyperlink link = toolkit.createHyperlink(scrolledForm.getBody(),
                "Hyperlink.", SWT.WRAP);

        // Tip: When the hyperlink has a focus rectangle painted around it.
        // it means the keyboard has focus is on the link, thus simply
        // pressing the 'Enter' key would activate it.
        link.addHyperlinkListener(new HyperlinkAdapter() {
            @Override
            public void linkActivated(
                    org.eclipse.ui.forms.events.HyperlinkEvent e) {
                System.out.println(e);
            }
        });

        // Image hyperlink
        ImageDescriptor ICON = Activator
                .getImageDescriptor("icons/alt_window_32.gif");

        ImageHyperlink ihl = toolkit.createImageHyperlink(scrolledForm
                .getBody(), SWT.WRAP);

        ihl.setImage(ICON.createImage());
        ihl.setToolTipText("Image Hyperlink");
        ihl.addHyperlinkListener(new HyperlinkAdapter() {
            @Override
            public void linkActivated(
                    org.eclipse.ui.forms.events.HyperlinkEvent e) {
                System.out.println(e);
            }
        });

    }

    private void createExpandableComposite() {
        ExpandableComposite ec = toolkit.createExpandableComposite(
                scrolledForm.getBody(), ExpandableComposite.TREE_NODE
                        | ExpandableComposite.CLIENT_INDENT);

        ec.setText("Expandable Composite");
        String text = "This composite is capable of expanding or collapsing "
                + "a single client that is its direct child. "
                + "The composite renders an expansion toggle "
                + "affordance (according to the chosen style), "
                + "and a title that also acts as a hyperlink.";

        Label client = toolkit.createLabel(ec, text, SWT.WRAP);
        ec.setClient(client);

        TableWrapData td = new TableWrapData();
        td.colspan = 1;
        ec.setLayoutData(td);

        ec.addExpansionListener(new ExpansionAdapter() {
            @Override
            public void expansionStateChanged(ExpansionEvent e) {
                scrolledForm.reflow(true);
            }
        });
    }

    /**
     * SECTIONS
     */
    private void addExpandableSectionWithTable() {
        /**
         * Add an expandable section with a table viewer with an expanded
         * style and column span of 1
         */
        int expanded = Section.DESCRIPTION | Section.TITLE_BAR
                | Section.TWISTIE | Section.EXPANDED;
        int colSpan = 1;

        viewer = createExpandableSectionWithTable("Expandable Section 1",
                "This is an expandable Section with a table viewer",
                expanded, colSpan);

        viewer.setContentProvider(new ViewContentProvider());
        viewer.setLabelProvider(new ViewLabelProvider());
        viewer.setInput(getViewSite());
    }

    /**
     * Create a search section with a text box button and results table
     * 
     * @param title
     * @param description
     * @param style
     * @return
     */
    private TableViewer createExpandableSectionWithTable(String title,
            String description, int style, int colSpan) {
        /**
         * Create an expandable section with title, description, style
         * (expanded) and column span
         */
        Section section = createSection(title, description, style, colSpan);

        // Create the composite as a part of the form.
        Composite sectionClient = toolkit.createComposite(section);

        /**
         * Give the widgets a flat look
         */
        sectionClient.setData(FormToolkit.KEY_DRAW_BORDER,
                FormToolkit.TEXT_BORDER);
        toolkit.paintBordersFor(sectionClient);

        /**
         * Use the forms HTML like table layout for the contents of the
         * expandable section
         */
        TableWrapLayout layout = new TableWrapLayout();
        layout.numColumns = 2;

        sectionClient.setLayout(layout);

        /**
         * Add contents to the section client. A table is wrapped on a
         * table viewer to use label and content providers
         */
        Table table = toolkit.createTable(sectionClient, SWT.FILL);

        TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.colspan = 2;
        td.heightHint = 100;

        table.setLayoutData(td);

        /**
         * Sets the client of this expandable composite. The client must
         * not be null and must be a direct child of this container.
         */
        section.setClient(sectionClient);

        return new TableViewer(table);
    }

    /**
     * Create a expandable form section. It uses a Layout data in
     * conjunction with HTMLTableLayout.
     */
    private Section createSection(String title, String description,
            int style, int colSpan) {
        Section section = toolkit.createSection(scrolledForm.getBody(),
                style);

        section.addExpansionListener(new ExpansionAdapter() {
            public void expansionStateChanged(ExpansionEvent e) {
                scrolledForm.reflow(true);
            }
        });

        if (title != null)
            section.setText(title);

        if (description != null)
            section.setDescription(description);

        TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
        td.colspan = colSpan;
        section.setLayoutData(td);

        return section;
    }

    /**
     * 1. Render plain wrapped text 2. Automatic conversion of URLs into
     * hyperlinks 3. Parses formatting markup as HTML-like tags
     */
    private void addFormTextControl() {
        ImageDescriptor ICON = Activator
                .getImageDescriptor("icons/sample3.gif");

        FormText formText = toolkit.createFormText(scrolledForm.getBody(),
                true);
        formText.setLayoutData(new TableWrapData(TableWrapData.FILL));

        StringBuffer html = new StringBuffer(
                "<form><p><b>Here is some HTML text</b> for the form to render "
                + "including this image <img href=\"image\"/>. "
                + "For more information see: http://www.eclipse.org</p>");

        html.append("<li>List item</li>");
        html.append("<li>List item</li>");
        html
                .append("<li style=\"image\" value=\"image\">Item with image</li>");
        html.append("</form>");

        // parseTags if true , formatting tags will be parsed. Otherwise,
        // text will be rendered as-is.
        // expandURLs if true , URLs found in the untagged text will be
        // converted into hyperlinks.
        boolean parseTags = true;
        boolean expandURLs = true;

        // 
        // TIP: When configured to use formatting XML (parseTags is true ),
        // the control requires the root element <form> to be used.
        // otherwise the exception java.lang.IllegalArgumentException:
        // Argument not valid
        // will be thrown
        formText.setText(html.toString(), parseTags, expandURLs);
        formText.setImage("image", ICON.createImage());

        formText.addHyperlinkListener(new HyperlinkAdapter() {
            @Override
            public void linkActivated(
                    org.eclipse.ui.forms.events.HyperlinkEvent e) {
                System.out.println(e.getHref());
            }
        });
    }

    @Override
    public void handleEvent(Event event) {
        System.out.println(event);
    }

    // Advanced Topics: Color And Toolkit Management
    public FormColors getFromColors(final Display display) {
        final Color COLOR_START = new Color(null, 128, 128, 128);
        final Color COLOR_END = new Color(null, 255, 255, 255);
        final Color COLOR_HEADING = new Color(null, 102, 102, 102);

        FormColors formColors;

        formColors = new FormColors(display);
        formColors.createColor(IFormColors.H_GRADIENT_START
                , COLOR_START.getRGB());
        formColors.createColor(IFormColors.H_GRADIENT_END
                , COLOR_END.getRGB());
        formColors.createColor(IFormColors.H_BOTTOM_KEYLINE1
                , COLOR_END.getRGB());
        formColors.createColor(IFormColors.H_BOTTOM_KEYLINE2
                , COLOR_START.getRGB());
        formColors.createColor(IFormColors.TITLE, COLOR_HEADING.getRGB());
        return formColors;
    }

}