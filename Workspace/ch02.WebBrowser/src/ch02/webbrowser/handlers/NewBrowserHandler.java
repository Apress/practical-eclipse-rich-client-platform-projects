package ch02.webbrowser.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.dialogs.MessageDialog;

import ch02.browser.views.WebBrowserView;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class NewBrowserHandler extends AbstractHandler {
	private int instanceNum = 0;
	
	/**
	 * The constructor.
	 */
	public NewBrowserHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
    	try {
    		window.getActivePage().showView(WebBrowserView.ID
    				, Integer.toString(instanceNum++)
    				, IWorkbenchPage.VIEW_ACTIVATE);
		} 
    	catch (PartInitException e) {
    		MessageDialog.openError(
    				window.getShell(),
    				"WebBrowser Plug-in",
    				e.getMessage());
		}
		return null;
	}
}
