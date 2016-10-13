package ch08.opengl.views;

import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.render.WWIcon;

import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.WorldWind;

import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.examples.BasicDragger;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.TerrainProfileLayer;
import gov.nasa.worldwind.layers.WorldMapLayer;
import gov.nasa.worldwind.pick.PickedObjectList;
import gov.nasa.worldwind.view.FlyToOrbitViewStateIterator;
import gov.nasa.worldwind.view.OrbitView;
import gov.nasa.worldwind.View;

import java.awt.BorderLayout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class GlobeView extends ViewPart {
    public static final String ID = GlobeView.class.getName();

    private static final WorldWindowGLCanvas world = new WorldWindowGLCanvas();

    /**
     * Initialize the default WW layers
     */
    static {
        initWorldWindLayerModel();
    }

    /**
     * Globe Selection listener
     */
    public class GlobeSelectionListener implements SelectListener {
        private WWIcon lastToolTipIcon = null;
        private BasicDragger dragger = null;
        private WorldWindowGLCanvas world;
        private WWIcon lastPickedIcon;

        public GlobeSelectionListener(WorldWindowGLCanvas canvas) {
            dragger = new BasicDragger(canvas);
            world = canvas;
        }

        private void highlight(Object o) {
            if (this.lastPickedIcon == o)
                return; // same thing selected

            if (this.lastPickedIcon != null) {
                this.lastPickedIcon.setHighlighted(false);
                this.lastPickedIcon = null;
            }

            if (o != null && o instanceof WWIcon) {
                this.lastPickedIcon = (WWIcon) o;
                this.lastPickedIcon.setHighlighted(true);
            }
        }

        /**
         * Select
         */
        public void selected(SelectEvent event) {
            if (event.getEventAction().equals(SelectEvent.LEFT_CLICK)) {
                if (event.hasObjects()) {
                    if (event.getTopObject() instanceof WorldMapLayer) {
                        // Left click on World Map : iterate view to target
                        // position
                        Position targetPos = event.getTopPickedObject()
                                .getPosition();
                        OrbitView view = (OrbitView) world.getView();
                        Globe globe = world.getModel().getGlobe();

                        // Use a PanToIterator
                        view.applyStateIterator(FlyToOrbitViewStateIterator
                               .createPanToIterator(
                                  view,
                                  globe,
                                     new Position(
                                        new LatLon(targetPos.getLatitude()
                                           , targetPos.getLongitude()),0)
                                           , Angle.ZERO,Angle.ZERO
                                           , targetPos.getElevation()));
                    } 
                    else if (event.getTopObject() instanceof WWIcon) {
                    }
                }
            } else if (event.getEventAction().equals(SelectEvent.HOVER)) {
                if (lastToolTipIcon != null) {
                    lastToolTipIcon.setShowToolTip(false);
                    this.lastToolTipIcon = null;
                    world.repaint();
                }

                if (event.hasObjects() && !this.dragger.isDragging()) {
                    if (event.getTopObject() instanceof WWIcon) {
                        this.lastToolTipIcon = (WWIcon) event
                                .getTopObject();
                        lastToolTipIcon.setShowToolTip(true);
                        world.repaint();
                    }
                }
            } else if (event.getEventAction().equals(SelectEvent.ROLLOVER)
                    && !this.dragger.isDragging()) {
                highlight(event.getTopObject());
            } else if (event.getEventAction().equals(SelectEvent.DRAG_END)
                    || event.getEventAction().equals(SelectEvent.DRAG)) {
                // Delegate dragging computations to a dragger.
                this.dragger.selected(event);
                if (event.getEventAction().equals(SelectEvent.DRAG_END)) {
                    PickedObjectList pol = world
                            .getObjectsAtCurrentPosition();
                    if (pol != null)
                        highlight(pol.getTopObject());
                }
            }
        }
    }

    /**
     * This is a callback that will allow us to create the viewer and
     * initialize it.
     */
    public void createPartControl(Composite parent) {
        // Build GUI:
        // top(SWT)->Frame(AWT)->Panel(AWT)->WorldWindowGLCanvas(AWT)
        Composite top = new Composite(parent, SWT.EMBEDDED);
        top.setLayoutData(new GridData(GridData.FILL_BOTH));

        java.awt.Frame worldFrame = SWT_AWT.new_Frame(top);
        java.awt.Panel panel = new java.awt.Panel(
                new java.awt.BorderLayout());

        worldFrame.add(panel);
        panel.add(world, BorderLayout.CENTER);

        // max parent widget
        parent.setLayoutData(new GridData(GridData.FILL_BOTH));

        GlobeSelectionListener listener1 = new GlobeSelectionListener(
                world);
        world.addSelectListener(listener1);

    }

    /*
     * Initialize WW model with default layers
     */
    static void initWorldWindLayerModel() {
        Model m = (Model) WorldWind
                .createConfigurationComponent(AVKey.MODEL_CLASS_NAME);

        m.setShowWireframeExterior(false);
        m.setShowWireframeInterior(false);
        m.setShowTessellationBoundingVolumes(false);

        world.setModel(m);

        // Add Terrain Profiler
        TerrainProfileLayer tp = new TerrainProfileLayer();

        tp.setEventSource(world);
        tp.setStartLatLon(LatLon.fromDegrees(0, -10));
        tp.setEndLatLon(LatLon.fromDegrees(0, 65));
        tp.setFollow(TerrainProfileLayer.FOLLOW_CURSOR);

        world.getModel().getLayers().add(tp);

    }

    /**
     * Passing the focus request to the viewer's control.
     */
    public void setFocus() {
    }

    public void repaint() {
        world.repaint();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    public void flyTo(LatLon latlon) {
        View view = world.getView();
        Globe globe = world.getModel().getGlobe();

        view.applyStateIterator(FlyToOrbitViewStateIterator
                .createPanToIterator((OrbitView) view, globe,
                        new Position(latlon, 0) // bbox
                        , Angle.ZERO // Heading
                        , Angle.ZERO // Pitch
                        , 3e3) // Altitude/Zoom (m) Angle.ZERO.degrees)
                );
    }

    public LayerList getLayers() {
        return world.getModel().getLayers();
    }
}