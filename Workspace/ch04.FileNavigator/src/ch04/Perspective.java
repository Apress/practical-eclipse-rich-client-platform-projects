package ch04;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import ch04.views.SampleView;

public class Perspective implements IPerspectiveFactory {
    public static final String ID = Perspective.class.getName();

    public void createInitialLayout(IPageLayout layout) {
        String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);

        IFolderLayout topLeft = layout.createFolder("topLeft", IPageLayout.LEFT, 0.50f, editorArea);
        topLeft.addView("navigator.view");
        
        IFolderLayout botLeft = layout.createFolder("bottomLeft", IPageLayout.BOTTOM, 0.75f,
                "topLeft");
        botLeft.addView("ch04.progress.view");

        IFolderLayout topRight = layout.createFolder("topRight", IPageLayout.RIGHT, 0.50f, editorArea);
        
        topRight.addView("eclipse.navigator.view"); 
        topRight.addView(SampleView.ID); 

    }
}
