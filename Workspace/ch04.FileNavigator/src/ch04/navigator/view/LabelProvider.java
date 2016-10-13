package ch04.navigator.view;

import java.util.Hashtable;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.program.Program;
import org.eclipse.ui.ISharedImages;

import ch04.Activator;
import ch04.NavigatorApplication;
import ch04.navigator.model.FileBean;

public class LabelProvider implements ILabelProvider {
    // Cached icons
    private Hashtable<Program, Image> iconCache
        = new Hashtable<Program, Image>();

    @Override
    public Image getImage(Object element) {
        FileBean file = (FileBean) element;
        String nameString = file.toString();
        Image image = null;

        int dot = nameString.lastIndexOf('.');

        // Get icon from the file system
        if (dot != -1) {
            // Find the program using the file extension
            String extension = nameString.substring(dot);
            Program program = Program.findProgram(extension);

            // get icon based on extension
            if (program != null) {
                image = getIconFromProgram(program);
            }
        }

        if (image == null)
            image = Activator.getSharedImage(ISharedImages.IMG_OBJ_FILE);

        return file.isDirectory() ? Activator
                .getSharedImage(ISharedImages.IMG_OBJ_FOLDER) : image;
    }

    @Override
    public String getText(Object element) {
        return element.toString();
    }

    @Override
    public void addListener(ILabelProviderListener listener) {
    }

    @Override
    public void dispose() {
    }

    @Override
    public boolean isLabelProperty(Object element, String property) {
        return false;
    }

    @Override
    public void removeListener(ILabelProviderListener listener) {
    }

    /**
     * Gets an image for a file associated with a given program
     * 
     * @param program
     *            the Program
     */
    public Image getIconFromProgram(Program program) {
        Image image = (Image) iconCache.get(program);
        if (image == null) {
            ImageData imageData = program.getImageData();
            if (imageData != null) {
                image = new Image(NavigatorApplication.getDefault()
                        .getDisplay(), imageData);
                iconCache.put(program, image);
            }
        }
        return image;
    }

}
