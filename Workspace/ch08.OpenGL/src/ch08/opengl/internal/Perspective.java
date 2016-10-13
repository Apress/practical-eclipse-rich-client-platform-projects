package ch08.opengl.internal;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import ch08.opengl.views.GLCubesView;
import ch08.opengl.views.GlobeView;
import ch08.opengl.views.NavView;
import ch08.opengl.views.GLChartView;

public class Perspective implements IPerspectiveFactory {

    public void createInitialLayout(IPageLayout layout) {
        String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);
        layout.setFixed(true);

        IFolderLayout topLeft = layout.createFolder("topLeft",
                IPageLayout.LEFT, 0.22f, editorArea);

        topLeft.addView(NavView.ID);

        IFolderLayout botLeft = layout.createFolder("bottomLeft",
                IPageLayout.BOTTOM, 0.65f, "topLeft");

        botLeft.addView(GLCubesView.ID);

        IFolderLayout topRight = layout.createFolder("topRight",
                IPageLayout.RIGHT, 0.3f, editorArea);
        topRight.addView(GlobeView.ID);

        IFolderLayout botRight = layout.createFolder("bottomRight",
                IPageLayout.BOTTOM, 0.7f, "topRight");
        botRight.addView(GLChartView.ID);

    }
}
