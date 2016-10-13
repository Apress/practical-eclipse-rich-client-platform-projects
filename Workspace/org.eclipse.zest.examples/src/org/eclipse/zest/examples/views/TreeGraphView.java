package org.eclipse.zest.examples.views;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.Filter;
import org.eclipse.zest.layouts.LayoutItem;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

public class TreeGraphView extends ViewPart {
    public static final String ID = TreeGraphView.class.getName();

    public TreeGraphView() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public void createPartControl(Composite parent) {
        Image imgInfo = Display.getDefault().getSystemImage(
                SWT.ICON_INFORMATION);

        parent.setLayout(new FillLayout());

        Graph graph = new Graph(parent, SWT.NONE);
        graph.setConnectionStyle(ZestStyles.CONNECTIONS_DIRECTED);

        GraphNode a = new GraphNode(graph, SWT.NONE, "Root", imgInfo);
        GraphNode b = new GraphNode(graph, SWT.NONE, "B");
        GraphNode c = new GraphNode(graph, SWT.NONE, "C");
        GraphNode d = new GraphNode(graph, SWT.NONE, "D");
        GraphNode e = new GraphNode(graph, SWT.NONE, "E");
        GraphNode f = new GraphNode(graph, SWT.NONE, "F");
        GraphNode g = new GraphNode(graph, SWT.NONE, "G");
        GraphNode h = new GraphNode(graph, SWT.NONE, "H");

        GraphConnection connection = new GraphConnection(graph, SWT.NONE,
                a, b);
        connection.setData(Boolean.FALSE);

        connection = new GraphConnection(graph, SWT.NONE, a, c);
        connection.setData(Boolean.FALSE);

        connection = new GraphConnection(graph, SWT.NONE, a, c);
        connection.setData(Boolean.FALSE);

        connection = new GraphConnection(graph, SWT.NONE, a, d);
        connection.setData(Boolean.FALSE);

        connection = new GraphConnection(graph, SWT.NONE, b, e);
        connection.setData(Boolean.FALSE);

        connection = new GraphConnection(graph, SWT.NONE, b, f);
        connection.setData(Boolean.FALSE);

        connection = new GraphConnection(graph, SWT.NONE, c, g);
        connection.setData(Boolean.FALSE);

        connection = new GraphConnection(graph, SWT.NONE, d, h);
        connection.setData(Boolean.FALSE);

        connection = new GraphConnection(graph,
                ZestStyles.CONNECTIONS_DIRECTED, b, c);
        connection.setLineColor(ColorConstants.blue);
        connection.setLineWidth(3);

        connection = new GraphConnection(graph,
                ZestStyles.CONNECTIONS_DIRECTED, c, d);
        connection.setLineColor(ColorConstants.blue);
        connection.setLineWidth(3);

        connection = new GraphConnection(graph,
                ZestStyles.CONNECTIONS_DIRECTED, e, f);
        connection.setLineColor(ColorConstants.blue);
        connection.setLineWidth(3);

        connection = new GraphConnection(graph,
                ZestStyles.CONNECTIONS_DIRECTED, f, g);
        connection.setLineColor(ColorConstants.blue);
        connection.setLineWidth(3);

        connection = new GraphConnection(graph,
                ZestStyles.CONNECTIONS_DIRECTED, h, e);
        connection.setLineColor(ColorConstants.red);
        connection.setLineWidth(3);

        TreeLayoutAlgorithm treeLayoutAlgorithm = new TreeLayoutAlgorithm(
                LayoutStyles.NO_LAYOUT_NODE_RESIZING);
        Filter filter = new Filter() {
            public boolean isObjectFiltered(LayoutItem item) {

                // Get the "Connection" from the Layout Item
                // and use this connection to get the "Graph Data"
                Object object = item.getGraphData();
                if (object instanceof GraphConnection) {
                    GraphConnection connection = (GraphConnection) object;
                    if (connection.getData() != null
                            && connection.getData() instanceof Boolean) {
                        // If the data is false, don't filter, otherwise,
                        // filter.
                        return ((Boolean) connection.getData())
                                .booleanValue();
                    }
                    return true;
                }
                return false;
            }

        };
        
        treeLayoutAlgorithm.setFilter(filter);
        graph.setLayoutAlgorithm(treeLayoutAlgorithm, true);

    }

    @Override
    public void setFocus() {
    }

}
