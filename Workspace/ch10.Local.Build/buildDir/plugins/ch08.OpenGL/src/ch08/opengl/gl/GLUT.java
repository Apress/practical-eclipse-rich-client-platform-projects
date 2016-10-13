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

/**
 * Simulates some of the GLUT functions.
 * 
 */
public class GLUT {
    public static final void wireCube(GL gl, float size) {
        float neg = -0.5f * size;
        float pos = 0.5f * size;

        // front face
        gl.glBegin(GL.GL_LINE_LOOP);
        gl.glVertex3f(neg, neg, neg);
        gl.glVertex3f(pos, neg, neg);
        gl.glVertex3f(pos, pos, neg);
        gl.glVertex3f(neg, pos, neg);
        gl.glEnd();

        // back face
        gl.glBegin(GL.GL_LINE_LOOP);
        gl.glVertex3f(neg, neg, pos);
        gl.glVertex3f(pos, neg, pos);
        gl.glVertex3f(pos, pos, pos);
        gl.glVertex3f(neg, pos, pos);
        gl.glEnd();

        gl.glBegin(GL.GL_LINES);
        gl.glVertex3f(neg, neg, neg);
        gl.glVertex3f(neg, neg, pos);

        gl.glVertex3f(pos, neg, neg);
        gl.glVertex3f(pos, neg, pos);

        gl.glVertex3f(pos, pos, neg);
        gl.glVertex3f(pos, pos, pos);

        gl.glVertex3f(neg, pos, neg);
        gl.glVertex3f(neg, pos, pos);
        gl.glEnd();
    }

    public static final void solidCube(GL gl, float size) {
        float neg = -0.5f * size;
        float pos = 0.5f * size;

        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glNormal3f(0.0f, 0.0f, 1.0f);
        gl.glVertex3f(neg, neg, pos);
        gl.glVertex3f(pos, neg, pos);
        gl.glVertex3f(pos, pos, pos);
        gl.glVertex3f(neg, pos, pos);

        // Back Face
        gl.glNormal3f(0.0f, 0.0f, -1.0f);
        gl.glVertex3f(neg, neg, neg);
        gl.glVertex3f(neg, pos, neg);
        gl.glVertex3f(pos, pos, neg);
        gl.glVertex3f(pos, neg, neg);

        // Top Face
        gl.glNormal3f(0.0f, 1.0f, 0.0f);
        gl.glVertex3f(neg, pos, neg);
        gl.glVertex3f(neg, pos, pos);
        gl.glVertex3f(pos, pos, pos);
        gl.glVertex3f(pos, pos, neg);

        // Bottom Face
        gl.glNormal3f(0.0f, -1.0f, 0.0f);
        gl.glVertex3f(neg, neg, neg);
        gl.glVertex3f(pos, neg, neg);
        gl.glVertex3f(pos, neg, pos);
        gl.glVertex3f(neg, neg, pos);

        // Right face
        gl.glNormal3f(1.0f, 0.0f, 0.0f);
        gl.glVertex3f(pos, neg, neg);
        gl.glVertex3f(pos, pos, neg);
        gl.glVertex3f(pos, pos, pos);
        gl.glVertex3f(pos, neg, pos);

        // Left Face
        gl.glNormal3f(-1.0f, 0.0f, 0.0f);
        gl.glVertex3f(neg, neg, neg);
        gl.glVertex3f(neg, neg, pos);
        gl.glVertex3f(neg, pos, pos);
        gl.glVertex3f(neg, pos, neg);
        gl.glEnd();
    }

    public static final void texturedCube(GL gl, float size) {
        float neg = -0.5f * size;
        float pos = 0.5f * size;

        gl.glBegin(GL.GL_QUADS);
        // Front Face
        gl.glNormal3f(0.0f, 0.0f, 1.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(neg, neg, pos);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(pos, neg, pos);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(pos, pos, pos);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(neg, pos, pos);

        // Back Face
        gl.glNormal3f(0.0f, 0.0f, -1.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(neg, neg, neg);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(neg, pos, neg);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(pos, pos, neg);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(pos, neg, neg);

        // Top Face
        gl.glNormal3f(0.0f, 1.0f, 0.0f);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(neg, pos, neg);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(neg, pos, pos);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(pos, pos, pos);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(pos, pos, neg);

        // Bottom Face
        gl.glNormal3f(0.0f, -1.0f, 0.0f);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(neg, neg, neg);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(pos, neg, neg);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(pos, neg, pos);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(neg, neg, pos);

        // Right face
        gl.glNormal3f(1.0f, 0.0f, 0.0f);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(pos, neg, neg);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(pos, pos, neg);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(pos, pos, pos);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(pos, neg, pos);

        // Left Face
        gl.glNormal3f(-1.0f, 0.0f, 0.0f);
        gl.glTexCoord2f(0.0f, 0.0f);
        gl.glVertex3f(neg, neg, neg);
        gl.glTexCoord2f(1.0f, 0.0f);
        gl.glVertex3f(neg, neg, pos);
        gl.glTexCoord2f(1.0f, 1.0f);
        gl.glVertex3f(neg, pos, pos);
        gl.glTexCoord2f(0.0f, 1.0f);
        gl.glVertex3f(neg, pos, neg);
        gl.glEnd();
    }
}
