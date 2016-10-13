/*******************************************************************************
 * Copyright (c) 2005 Bo Majewski 
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/org/documents/epl-v10.html
 * 
 * Contributors:
 *     Bo Majewski - initial API and implementation
 *     Vladimir Silva - Eclipse RCP fixes
 *******************************************************************************/
package ch08.opengl.gl;

import javax.media.opengl.GL;
import javax.media.opengl.GLContext;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

/**
 * Implements a scene grip, capable of rotating and moving a GL scene with
 * the help of the mouse and keyboard.
 */
public class SceneGrip extends MouseAdapter implements MouseMoveListener,
        Listener, KeyListener {
    private float xrot;
    private float yrot;
    private float zoff;
    private float xoff;
    private float yoff;
    private float xcpy;
    private float ycpy;
    private boolean move;
    private int xdown;
    private int ydown;
    private int mouseDown;

    private GLContext context;

    public SceneGrip(final GLContext context) {
        this.init();
        this.context = context;
    }

    protected void init() {
        this.xrot = this.yrot = 0.0f;
        this.xoff = this.yoff = 0.0f;
        this.zoff = -8.0f;
    }

    public void mouseDown(MouseEvent e) {
        if (++this.mouseDown == 1) {
            if ((this.move = e.button == 3)) {
                this.xcpy = xoff;
                this.ycpy = yoff;
                ((Control) e.widget).setCursor(e.widget.getDisplay()
                        .getSystemCursor(SWT.CURSOR_HAND));
            } else {
                this.xcpy = xrot;
                this.ycpy = yrot;
                ((Control) e.widget).setCursor(e.widget.getDisplay()
                        .getSystemCursor(SWT.CURSOR_SIZEALL));
            }

            this.xdown = e.x;
            this.ydown = e.y;
        }
    }

    public void mouseUp(MouseEvent e) {
        if (--this.mouseDown == 0) {
            ((Control) e.widget).setCursor(e.widget.getDisplay()
                    .getSystemCursor(SWT.CURSOR_ARROW));
        }
    }

    public void mouseMove(MouseEvent e) {
        Point p = ((Control) e.widget).getSize();

        if (this.mouseDown > 0) {
            int dx = e.x - this.xdown;
            int dy = e.y - this.ydown;

            if (this.move) {
                yoff = this.ycpy + ((zoff + 1.0f) * dy) / (2.0f * p.y);
                xoff = this.xcpy - ((zoff + 1.0f) * dx) / (2.0f * p.x);
            } else {
                xrot = this.xcpy + dy / 2.0f;
                yrot = this.ycpy + dx / 2.0f;
            }
        }
    }

    public void handleEvent(Event event) {
        this.zoff += event.count / 6.0f;
    }

    public void keyPressed(KeyEvent e) {
        switch (e.keyCode) {
        case SWT.ARROW_UP:
            if ((e.stateMask & SWT.CTRL) != 0) {
                this.xrot -= 0.5f;
            } else {
                this.yoff += 0.05f;
            }
            break;
        case SWT.ARROW_DOWN:
            if ((e.stateMask & SWT.CTRL) != 0) {
                this.xrot += 0.5f;
            } else {
                this.yoff -= 0.05f;
            }
            break;
        case SWT.ARROW_LEFT:
            if ((e.stateMask & SWT.CTRL) != 0) {
                this.yrot -= 0.5f;
            } else {
                this.xoff -= 0.05f;
            }
            break;
        case SWT.ARROW_RIGHT:
            if ((e.stateMask & SWT.CTRL) != 0) {
                this.yrot += 0.5f;
            } else {
                this.xoff += 0.05f;
            }
            break;
        case SWT.PAGE_UP:
            this.zoff += 0.05f;
            break;
        case SWT.PAGE_DOWN:
            this.zoff -= 0.05f;
            break;
        case SWT.HOME:
            this.init();
            break;
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void adjust() {
        GL gl = context.getGL();
        gl.glTranslatef(this.xoff, this.yoff, this.zoff);
        gl.glRotatef(this.xrot, 1.0f, 0.0f, 0.0f);
        gl.glRotatef(this.yrot, 0.0f, 1.0f, 0.0f);
    }

    public void setOffsets(float x, float y, float z) {
        this.xoff = x;
        this.yoff = y;
        this.zoff = z;
    }

    public void setRotation(float x, float y) {
        this.xrot = x;
        this.yrot = y;
    }
}
