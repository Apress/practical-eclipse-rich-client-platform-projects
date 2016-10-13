package ch08.opengl.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import ch08.opengl.gl.ChartScene;
import ch08.opengl.gl.GLScene;
import ch08.opengl.gl.Refresher;

public class GLChartView extends ViewPart {
	public static final String ID = GLChartView.class.getName();

    private GLScene scene;

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {
        this.scene = new ChartScene(parent);
        new Refresher(this.scene).run();
	    
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
	    this.scene.setFocus();
	}
}