package ch04.navigator.model;

import java.io.File;

import org.eclipse.core.runtime.PlatformObject;

public class NavigatorRoot extends PlatformObject {
    private final String OSNAME = System.getProperty("os.name");
    private final boolean isWindows = OSNAME.indexOf("Windows") != -1;

    public FileBean[] getParentBeans() {
        File f = isWindows ? new File("c:/") : new File("/");

        FileBean top = new FileBean(f);
        return top.getChildren();
    }

}
