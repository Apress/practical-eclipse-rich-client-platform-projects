package ch08.glscenes;

import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

public class Perspective implements IPerspectiveFactory {

    public void createInitialLayout(IPageLayout layout) {
        String editorArea = layout.getEditorArea();
        layout.setEditorAreaVisible(false);
        layout.setFixed(true);

        /*
         *  addStandaloneView(String viewId, boolean showTitle, int relationship, float ratio, String refId) 
         * Parameters:
            viewId - the compound view id
            showTitle - true to show the title and related controls, false to hide them
            relationship - the position relative to the reference part; one of TOP, BOTTOM, LEFT, or RIGHT
            ratio - a ratio specifying how to divide the space currently occupied by the reference part, in the range 0.05f to 0.95f. Values outside this range will be clipped to facilitate direct manipulation. For a vertical split, the part on top gets the specified ratio of the current space and the part on bottom gets the rest. Likewise, for a horizontal split, the part at left gets the specified ratio of the current space and the part at right gets the rest.
            refId - the id of the reference part; either a view id, a folder id, or the special editor area id returned by getEditorArea
         */
        
        layout.addStandaloneView("ch08.glscenes.views.CubesView", true,
                IPageLayout.LEFT, 0.5f, editorArea);
        layout.addStandaloneView("ch08.glscenes.views.ChartView", true,
                IPageLayout.RIGHT, 0.5f, editorArea);
    }
}
