package ch02.browser.views;


import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.jface.action.*;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;

import ch02.browser.internal.BrowserActivator;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class BookMarksView extends ViewPart 
{
	static public String ID = BookMarksView.class.getName();
	
	private TableViewer viewer;
	private Action actionRemoveBM;
	private Action doubleClickAction;

	/*
	 * The content provider class is responsible for
	 * providing objects to the view. It can wrap
	 * existing objects in adapters or simply return
	 * objects as-is. These objects may be sensitive
	 * to the current input of the view, or ignore
	 * it and always show the same content 
	 * (like Task List, for example).
	 */
	 
	class ViewContentProvider implements IStructuredContentProvider {
		Viewer v;
		public ViewContentProvider(Viewer v) {
			this.v = v;
		}
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public Object[] getElements(Object parent) {
			return new String[] {}; // "One", "Two", "Three" };
		}
	}
	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}
		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}
		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().
					getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
		}
	}
	class NameSorter extends ViewerSorter {
	}

	/**
	 * The constructor.
	 */
	public BookMarksView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) 
	{
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		
		viewer.setContentProvider(new ViewContentProvider(viewer));
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());
		
		makeActions();
		hookContextMenu();
		hookDoubleClickAction();
		contributeToActionBars();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				BookMarksView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(actionRemoveBM);
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(actionRemoveBM);
		
		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(actionRemoveBM);

		// Other plug-ins can contribute there actions here
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void makeActions() {
		actionRemoveBM = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				
				removeBookmark(obj);
			}
		};
		actionRemoveBM.setText("Remove Bookmark");
		actionRemoveBM.setToolTipText("Remove Bookmark");
		actionRemoveBM.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		
		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				
				// Navigate to url
				WebBrowserView v = (WebBrowserView)BrowserActivator.getView(getViewSite().getWorkbenchWindow(), WebBrowserView.ID);

				if ( v != null )
					v.navigateTo(obj.toString());
			}
		};
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}
	
	@SuppressWarnings("unused")
	private void showMessage(String message) {
		MessageDialog.openInformation(
			viewer.getControl().getShell(),
			"Bookmarks",
			message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	/**
	 * Add book-mark
	 * @param url
	 */
	public void addBookmark(final String url) 
	{
		viewer.add(url);
	}

	public void removeBookmark(Object element) 
	{
		if ( element == null ) return;
		
		viewer.remove(element);
	}
	
}