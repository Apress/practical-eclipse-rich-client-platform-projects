package ch04.navigator.view;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import ch04.navigator.model.FileBean;
import ch04.navigator.model.NavigatorRoot;

public class TreeContentProvider implements ITreeContentProvider {

    @Override
    public Object[] getChildren(Object parentElement) {
        FileBean parent = (FileBean) parentElement;
        return parent.getChildren();
    }

    @Override
    public Object getParent(Object element) {
        return null;
    }

    @Override
    public boolean hasChildren(Object element) {
        FileBean file = (FileBean) element;
        return file.hasChildren();
    }

    @Override
    public Object[] getElements(Object inputElement) {
        NavigatorRoot root = ((NavigatorRoot) inputElement);

        return root.getParentBeans();
    }

    @Override
    public void dispose() {
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput,
            Object newInput) {
    }

}
