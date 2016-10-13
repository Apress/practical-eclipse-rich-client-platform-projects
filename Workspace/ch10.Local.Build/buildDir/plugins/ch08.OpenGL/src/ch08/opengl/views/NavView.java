package ch08.opengl.views;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.part.ViewPart;

import ch08.opengl.internal.Activator;
import ch08.opengl.views.YGeoSearch.YResult;

public class NavView extends ViewPart implements Listener {
    public static final String ID = NavView.class.getName();

    private TableViewer viewer;
    private Text searchText;

    private CheckboxTableViewer layers;

    /**
     * This is a callback that will allow us to create the viewer and
     * initialize it.
     */
    public void createPartControl(Composite parent) {
        parent.setLayout(new GridLayout(2, true));

        Label l1 = new Label(parent, SWT.NONE);
        l1.setText("Y! GeoSearch");
        l1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2,
                1));

        // search box
        searchText = new Text(parent, SWT.BORDER);
        searchText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
                false));

        // serach buton
        Button b1 = new Button(parent, SWT.PUSH);

        b1.setText("Search");
        b1.addListener(SWT.Selection, this);
        b1.addListener(SWT.DefaultSelection, this);

        // results table viewer
        viewer = new TableViewer(parent, SWT.BORDER | SWT.H_SCROLL
                | SWT.V_SCROLL);

        viewer.getTable().setLayoutData(
                new GridData(SWT.FILL, SWT.FILL, true, false, 2, 1));
        viewer.getTable().addListener(SWT.Selection, this);

        // Layers label
        Label l2 = new Label(parent, SWT.NONE);
        l2.setText("Globe Layers");
        l2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 2,
                1));

        Table tableLayers = new Table(parent, SWT.CHECK | SWT.BORDER
                | SWT.H_SCROLL | SWT.V_SCROLL);

        // Layers tableviewer
        layers = new CheckboxTableViewer(tableLayers);
        layers.getTable().setLayoutData(
                new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));

        // fires when a layer on the table viewer is checked.
        layers.addCheckStateListener(new ICheckStateListener() {
            @Override
            public void checkStateChanged(CheckStateChangedEvent event) {
                // Enable/disable globe layer based on the check status
                // of the table
                Layer layer = (Layer) event.getElement();
                layer.setEnabled(event.getChecked());

                // repaint globe
                GlobeView view = (GlobeView) Activator.getView(
                        getViewSite().getWorkbenchWindow(), GlobeView.ID);
                view.repaint();
            }
        });

        init();
    }

    /**
     * Load layers from the globe and add them to the able viewer
     */
    private void init() {
        GlobeView view = (GlobeView) Activator.getView(getViewSite()
                .getWorkbenchWindow(), GlobeView.ID);
        if (view != null) {
            LayerList list = view.getLayers();

            for (Layer layer : list) {
                layers.add(layer);
                layers.setChecked(layer, layer.isEnabled());
            }
        }
    }

    /**
     * Passing the focus request to the viewer's control.
     */
    public void setFocus() {
        viewer.getControl().setFocus();
    }

    public void addLayer(Layer layer) {
        layers.add(layer.getName());
    }

    /**
     * Handle GUI events: Mouse clicks, button presses, etc
     */
    @Override
    public void handleEvent(Event event) {
        Widget w = event.widget;

        if (w instanceof Button) {
            final String text = searchText.getText();

            YGeoSearch search = new YGeoSearch(text);
            try {
                YResult[] results = search.getLocations();

                // clear prev results
                if (results != null && results.length > 0)
                    viewer.getTable().removeAll();

                // add results
                for (int i = 0; i < results.length; i++) {
                    viewer.add(results[i]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (w instanceof Table) {
            IStructuredSelection sel = (IStructuredSelection) viewer
                    .getSelection();
            YResult result = (YResult) sel.getFirstElement();

            // fly to location
            GlobeView view = (GlobeView) Activator.getView(getViewSite()
                    .getWorkbenchWindow(), GlobeView.ID);
            if (view != null) {
                view.flyTo(new LatLon(Angle.fromDegrees(result.latitude),
                        Angle.fromDegrees(result.longitude)));
            }
        }
    }
}