package org.eclipse.zest.examples.views;


import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.*;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.swt.SWT;



public class Graph1View extends ViewPart {
	/**
	 * The constructor.
	 */
	public Graph1View() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
	    
	    Image image1 = Display.getDefault().getSystemImage(SWT.ICON_INFORMATION);
        Image image2 = Display.getDefault().getSystemImage(SWT.ICON_WARNING);
        Image image3 = Display.getDefault().getSystemImage(SWT.ICON_ERROR);
        
        parent.setLayout(new FillLayout());
        
        Graph g = new Graph(parent, SWT.NONE);
        g.setConnectionStyle(ZestStyles.CONNECTIONS_DIRECTED );
        GraphNode n1 = new GraphNode(g, SWT.NONE, "Information", image1);
        GraphNode n2 = new GraphNode(g, SWT.NONE, "Warning", image2);
        GraphNode n3 = new GraphNode(g, SWT.NONE, "Error", image3);
        
        GraphConnection connection1 = new GraphConnection(g, SWT.NONE, n1, n2);
        GraphConnection connection2 = new GraphConnection(g, SWT.NONE, n2, n3);
        
        Image information2warningImage = mergeImages(image1, image2);
        Image warning2error = mergeImages(image2, image3);
        IFigure tooltip1 = new Label("Information to Warning", information2warningImage);
        IFigure tooltip2 = new Label("Warning to Error", warning2error);
        connection1.setTooltip(tooltip1);
        connection2.setTooltip(tooltip2);
        
        n1.setLocation(10, 10);
        n2.setLocation(200, 10);
        n3.setLocation(200, 200);

	    
	}

    /**
     * Merges 2 images so they appear beside each other
     * 
     * You must dispose this image!
     * @param image1
     * @param image2
     * @param result
     * @return
     */
    public static Image mergeImages(Image image1, Image image2) {
        Image mergedImage = new Image(Display.getDefault(), image1.getBounds().width + image2.getBounds().width, image1.getBounds().height);
        GC gc = new GC(mergedImage);
        gc.drawImage(image1, 0, 0);
        gc.drawImage(image2, image1.getBounds().width, 0);
        gc.dispose();
        return mergedImage;
    }

	public void setFocus() {
		//viewer.getControl().setFocus();
	}
}