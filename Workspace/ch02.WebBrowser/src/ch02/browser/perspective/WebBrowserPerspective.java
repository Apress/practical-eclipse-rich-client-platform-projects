package ch02.browser.perspective;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import ch02.browser.views.BookMarksView;
import ch02.browser.views.WebBrowserView;

public class WebBrowserPerspective implements IPerspectiveFactory 
{
	public static final String ID = WebBrowserPerspective .class.getName();
	
	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.setEditorAreaVisible(false);

		layout.addView(BookMarksView.ID, IPageLayout.LEFT, 0.2f, editorArea);
		layout.addView(WebBrowserView.ID, IPageLayout.LEFT, 0.8f, editorArea);
		
		
	}

}
