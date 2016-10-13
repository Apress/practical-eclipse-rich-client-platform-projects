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

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * Draws a few, colored, wire frame cubes.
 */
public class CubeScene extends GLScene {
    private SceneGrip grip;

    public CubeScene(Composite parent) {
        super(parent);

        this.grip = new SceneGrip(context);
        this.grip.setOffsets(0.0f, 0.0f, -15.0f);
        this.grip.setRotation(45.0f, -30.0f);

        // listen for mouse and keyboard events
        this.getCanvas().addMouseListener(this.grip);
        this.getCanvas().addMouseMoveListener(this.grip);
        this.getCanvas().addListener(SWT.MouseWheel, this.grip);
        this.getCanvas().addKeyListener(this.grip);
    }

    protected void initGL() {
        super.initGL();
        context.makeCurrent();
        GL gl = context.getGL();

        gl.glEnable(GL.GL_LINE_SMOOTH);
        gl.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_NICEST);
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

        context.release();
    }

    protected void drawScene() {
        context.makeCurrent();
        GL gl = context.getGL();

        super.drawScene();
        this.grip.adjust();

        // draw a white floor square
        gl.glColor3f(1.0f, 1.0f, 1.0f);

        gl.glBegin(GL.GL_LINE_LOOP);
        gl.glVertex3f(-6.0f, -1.0f, -9.0f);
        gl.glVertex3f(6.0f, -1.0f, -9.0f);
        gl.glVertex3f(6.0f, -1.0f, 3.0f);
        gl.glVertex3f(-6.0f, -1.0f, 3.0f);
        gl.glEnd();

        // red wire cube
        gl.glTranslatef(-3.0f, 0.0f, -6.0f);
        gl.glColor3f(1.0f, 0.0f, 0.0f);
        GLUT.wireCube(gl, 2.0f);

        // green wire cube
        gl.glTranslatef(3.0f, 0.0f, 6.0f);
        gl.glColor3f(0.0f, 1.0f, 0.0f);
        GLUT.wireCube(gl, 2.0f);

        // blue wire cube
        gl.glTranslatef(3.0f, 0.0f, -6.0f);
        gl.glColor3f(0.0f, 0.0f, 1.0f);
        GLUT.wireCube(gl, 2.0f);

        context.release();
    }
}
