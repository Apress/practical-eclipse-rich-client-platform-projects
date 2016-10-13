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
 * A conveninece class for creating compiled lists of commands.
 * 
 */
public abstract class CompiledShape {
    private int listIndex;
    private GL gl;
    
    public CompiledShape(GL gl) {
        this.listIndex = gl.glGenLists(1);
        this.gl = gl;
    }
    
    public int getListIndex() {
        return this.listIndex;
    }
    
    public void draw() {
        gl.glCallList(this.getListIndex());
    }
    
    public void dispose() {
        gl.glDeleteLists(this.getListIndex(), 1);
    }
}
