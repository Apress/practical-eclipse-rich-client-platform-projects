package ch02.browser.internal;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class BrowserActivator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "ch02.WebBrowser";

	// The shared instance
	private static BrowserActivator plugin;
	
	/**
	 * The constructor
	 */
	public BrowserActivator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static BrowserActivator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	private static String retrieveMessage(String property, String bundle)
    {
        ResourceBundle res = ResourceBundle.getBundle(bundle, Locale.getDefault());
        return (String) res.getObject(property);
    }
	
	static public String getString(String key) {
		return retrieveMessage(key, BrowserActivator.class.getName()); // BUNDLE_NAME);
	}
	
	/**
	 * Returns a string from the resource bundle and binds it
	 * with the given arguments. If the key is not found,
	 * return the key.
	 */
	public static String getString(String key, Object[] args) {
		try {
			return MessageFormat.format(getString(key), args);
		} catch (MissingResourceException e) {
			return key;
		} catch (NullPointerException e) {
			return "!" + key + "!";
		}
	}

	public static void showInfoMessage(Shell shell, String message) {
		MessageDialog.openInformation(shell, getString("info.dialog.title"), message);
	}
	
	public static void showErrorMessage(Shell shell, String message) {
		MessageDialog.openError(shell, getString("err.dialog.title"), message);
	}
	

	public static IViewPart getView (IWorkbenchWindow window,  String ViewID) 
	{
		IViewReference[] refs = window.getActivePage().getViewReferences();
		for (IViewReference viewReference : refs) {
			if ( viewReference.getId().equals(ViewID) )
				return viewReference.getView(true);
		}
		return null;
	}
	
}
