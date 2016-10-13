package org.eclipse.gef.examples.shapes.parts;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import org.eclipse.gef.examples.shapes.model.Connection;
import org.eclipse.gef.examples.shapes.model.ModelElement;
import org.eclipse.gef.examples.shapes.model.commands.ConnectionDeleteCommand;

/**
 * Edit part for Connection model elements.
 * <p>
 * This edit part must implement the PropertyChangeListener interface, so
 * it can be notified of property changes in the corresponding model
 * element.
 * </p>
 * 
 */
class ConnectionEditPart extends AbstractConnectionEditPart implements
        PropertyChangeListener {

    /**
     * Upon activation, attach to the model element as a property change
     * listener.
     */
    public void activate() {
        if (!isActive()) {
            super.activate();
            ((ModelElement) getModel()).addPropertyChangeListener(this);
        }
    }

    protected void createEditPolicies() {
        // Selection handle edit policy.
        // Makes the connection show a feedback, when selected by the user.
        installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE,
                new ConnectionEndpointEditPolicy());

        // Allows the removal of the connection model element
        installEditPolicy(EditPolicy.CONNECTION_ROLE,
                new ConnectionEditPolicy() {
                    protected Command getDeleteCommand(GroupRequest request) {
                        return new ConnectionDeleteCommand(
                                getCastedModel());
                    }
                });
    }

    protected IFigure createFigure() {
        PolylineConnection connection = (PolylineConnection) super
                .createFigure();
        
        // arrow at target endpoint
        connection.setTargetDecoration(new PolygonDecoration());
        
        // line drawing style
        connection.setLineStyle(getCastedModel().getLineStyle()); 
        return connection;
    }

    /**
     * Upon deactivation, detach from the model element as a property
     * change listener.
     */
    public void deactivate() {
        if (isActive()) {
            super.deactivate();
            ((ModelElement) getModel()).removePropertyChangeListener(this);
        }
    }

    private Connection getCastedModel() {
        return (Connection) getModel();
    }

    public void propertyChange(PropertyChangeEvent event) {
        String property = event.getPropertyName();
        if (Connection.LINESTYLE_PROP.equals(property)) {
            ((PolylineConnection) getFigure())
                    .setLineStyle(getCastedModel().getLineStyle());
        }
    }

}