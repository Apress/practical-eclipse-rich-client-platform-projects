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

import java.nio.FloatBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * A 3D cylinder chart.
 */
public class ChartScene extends GLScene {
    public static final int ROW_LENGTH = 6;
    public static final int CHART_COUNT = 4;

    private static final float[][] COLOR = { { 1.0f, 1.0f, 0.0f, 0.7f },
            { 0.0f, 1.0f, 0.0f, 0.7f }, { 0.0f, 0.0f, 1.0f, 0.7f },
            { 1.0f, 0.0f, 1.0f, 0.7f }, };

    private BarValue[][] chart;
    private Axis axis;
    private SceneGrip grip;

    static GLU glu = new GLU();

    public ChartScene(Composite parent) {
        super(parent);

        this.grip = new SceneGrip(context);
        this.grip.setOffsets(-3.25f, 3.25f, -30.5f);
        this.grip.setRotation(45.0f, -30.0f);

        this.getCanvas().addMouseListener(this.grip);
        this.getCanvas().addMouseMoveListener(this.grip);
        this.getCanvas().addListener(SWT.MouseWheel, this.grip);
        this.getCanvas().addKeyListener(this.grip);
    }

    protected void initGL() {
        super.initGL();

        context.makeCurrent();
        GL gl = context.getGL();

        BarValue.QUADRIC = glu.gluNewQuadric();
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);
        gl.glEnable(GL.GL_BLEND);
        gl.glEnable(GL.GL_LINE_SMOOTH);
        glu.gluQuadricNormals(BarValue.QUADRIC, GLU.GLU_SMOOTH);

        gl.glLightfv(GL.GL_LIGHT1, GL.GL_DIFFUSE, new float[] { 1.0f,
                1.0f, 1.0f, 1.0f }, 0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT, new float[] { 0.5f,
                0.5f, 0.5f, 1.0f }, 0);
        gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, new float[] { -50.f,
                50.0f, 100.0f, 1.0f }, 0);
        
        gl.glEnable(GL.GL_LIGHT1);
        gl.glEnable(GL.GL_LIGHTING);
        gl.glEnable(GL.GL_COLOR_MATERIAL);
        gl.glColorMaterial(GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE);

        this.axis = new Axis(context.getGL(), 15.0f, 9.0f, 11.0f);

        this.chart = new BarValue[CHART_COUNT][ROW_LENGTH];

        double slice = Math.PI / ROW_LENGTH;

        // initialize chart values
        for (int i = 0; i < this.chart.length; ++i) {
            BarValue[] value = this.chart[i];
            double shift = i * Math.PI / 4.0;

            for (int j = 1; j <= value.length; ++j) {
                value[j - 1] = new BarValue(context.getGL(),
                        (float)(8.0 * Math.abs(
                                Math.sin(slice * j - shift))));
            }
        }
        context.release();
    }

    protected void drawScene() {
        context.makeCurrent();
        GL gl = context.getGL();

        super.drawScene();
        this.grip.adjust();

        gl.glLineWidth(1.0f);
        
        // draw axis
        this.axis.draw();
        
        gl.glTranslatef(BarValue.RADIUS, 0.0f, BarValue.RADIUS);
        
        // draw bar values
        for (int i = 0; i < this.chart.length; ++i) {
            BarValue[] value = this.chart[i];
            gl.glColor4fv(COLOR[i % COLOR.length], 0);

            for (int j = 0; j < value.length; ++j) {
                value[j].draw();
                gl.glTranslatef(2.0f * BarValue.RADIUS, 0.0f, 0.0f);
            }

            gl.glTranslatef(-2.0f * BarValue.RADIUS * value.length
                    , 0.0f ,2.0f * BarValue.RADIUS + 0.5f);
        }

        context.release();
    }

    public void dispose() {
        glu.gluDeleteQuadric(BarValue.QUADRIC);

        for (int i = 0; i < this.chart.length; ++i) {
            BarValue[] value = this.chart[i];

            for (int j = 0; j < value.length; ++j) {
                value[j].dispose();
                value[j] = null;
            }
        }

        this.axis.dispose();
        super.dispose();
    }

    private static class Axis extends CompiledShape {
        private static float[] COLOR1 = new float[] { 0.6f, 0.6f, 0.6f,
                0.3f };
        private static float[] COLOR2 = new float[] { 1.0f, 1.0f, 1.0f,
                1.0f };
        private static float[] COLOR3 = new float[] { 0.6f, 0.0f, 0.0f,
                1.0f };

        public Axis(GL gl, float x, float y, float z) {
            super(gl);
            gl.glNewList(this.getListIndex(), GL.GL_COMPILE);

            gl.glBegin(GL.GL_QUADS);
            gl.glColor4fv(FloatBuffer.wrap(COLOR1));

            // 2 intersecting 4 sided polygons (GRAY)
            gl.glVertex3f(0.0f, y, z);
            gl.glVertex3f(0.0f, -1.0f, z);
            gl.glVertex3f(0.0f, -1.0f, -1.0f);
            gl.glVertex3f(0.0f, y, -1.0f);

            // 2nd polygon
            gl.glVertex3f(-1.0f, y, 0.0f);
            gl.glVertex3f(-1.0f, -1.0f, 0.0f);
            gl.glVertex3f(x, -1.0f, 0.0f);
            gl.glVertex3f(x, y, 0.0f);
            gl.glEnd();

            gl.glColor4fv(FloatBuffer.wrap(COLOR2));
            
            // polygon panel divider lines (WHITE)
            for (float a = 1.0f; a < y; a += 1.0f) {
                gl.glBegin(GL.GL_LINE_STRIP);
                gl.glVertex3f(0.1f, a, z);
                gl.glVertex3f(0.1f, a, 0.1f);
                gl.glVertex3f(x, a, 0.1f);
                gl.glEnd();
            }

            // X, Y, Z axis lines (RED)
            gl.glColor4fv(FloatBuffer.wrap(COLOR3));
            gl.glBegin(GL.GL_LINE_STRIP);
            gl.glVertex3f(0.1f, 0.0f, z);
            gl.glVertex3f(0.1f, 0.0f, 0.1f);
            gl.glVertex3f(x, 0.0f, 0.1f);
            gl.glEnd();
            
            gl.glBegin(GL.GL_LINES);
            gl.glVertex3f(0.1f, -1.0f, 0.1f);
            gl.glVertex3f(0.1f, y, 0.1f);
            gl.glEnd();
            
            gl.glEndList();
        }
    }

    private static class BarValue extends CompiledShape {
        public static final float RADIUS = 1.0f;
        public static GLUquadric QUADRIC;

        public BarValue(GL gl, float value) {
            super(gl);

            gl.glNewList(this.getListIndex(), GL.GL_COMPILE);
            gl.glRotatef(-90.0f, 1.0f, 0.0f, 0.0f);

            glu.gluCylinder(BarValue.QUADRIC, RADIUS, RADIUS, value,
                            32, 1);
            glu.gluDisk(BarValue.QUADRIC, 0.0, RADIUS, 32, 32);

            gl.glTranslatef(0.0f, 0.0f, value);
            glu.gluDisk(BarValue.QUADRIC, 0.0, RADIUS, 32, 32);

            gl.glTranslatef(0.0f, 0.0f, -value);
            gl.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
            gl.glEndList();
        }
    }
}
