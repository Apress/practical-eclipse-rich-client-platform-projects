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

public class Refresher implements Runnable {
    public static final int DELAY = 100;
    
    private GLScene scene;
    
    public Refresher(GLScene canvas) {
        this.scene = canvas;
    }
    
    public void run() {
        if (this.scene != null && !this.scene.isDisposed()) {
            this.scene.render();
            this.scene.getDisplay().timerExec(DELAY, this);
        }
    }
}
