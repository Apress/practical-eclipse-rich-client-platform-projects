package ch07.graph.editor;

import org.eclipse.gef.ui.views.palette.PaletteView;
import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

    public void createInitialLayout(IPageLayout layout) {
        String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);

        IFolderLayout topLeft = layout.createFolder("topLeft",
                IPageLayout.LEFT, 0.5f, editorArea);
        topLeft.addView("eclipse.navigator.view");

        IFolderLayout botLeft = layout.createFolder("bottomLeft",
                IPageLayout.BOTTOM, 0.5f, "topLeft");
        botLeft.addView(PaletteView.ID);

    }
}
