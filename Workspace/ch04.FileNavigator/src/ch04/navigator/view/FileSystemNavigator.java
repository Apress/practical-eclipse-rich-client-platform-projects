package ch04.navigator.view;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.navigator.CommonNavigator;

import ch04.jobs.Race;
import ch04.navigator.model.NavigatorRoot;

public class FileSystemNavigator extends CommonNavigator {
    private Action action1;

    @Override
    protected IAdaptable getInitialInput() {
        return new NavigatorRoot();
    }

    @Override
    public void createPartControl(Composite parent) {
        super.createPartControl(parent);

        makeActions();
        hookContextMenu();
        contributeToActionBars();
    }

    private void makeActions() {
        action1 = new Action() {
            public void run() {
                new Race().start();
            }
        };
        action1.setText("Start a new Race");
        action1.setToolTipText("Start a new Race");
        action1.setImageDescriptor(PlatformUI.getWorkbench()
                .getSharedImages().getImageDescriptor(
                        ISharedImages.IMG_TOOL_FORWARD));

    }

//    private void showMessage(String message) {
//        MessageDialog.openInformation(getCommonViewer().getControl()
//                .getShell(), "Sample View", message);
//    }

    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#PopupMenu");
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager manager) {
                FileSystemNavigator.this.fillContextMenu(manager);
            }
        });
        Menu menu = menuMgr.createContextMenu(getCommonViewer()
                .getControl());
        getCommonViewer().getControl().setMenu(menu);
        getSite().registerContextMenu(menuMgr, getCommonViewer());
    }

    private void contributeToActionBars() {
        IActionBars bars = getViewSite().getActionBars();
        fillLocalPullDown(bars.getMenuManager());
        fillLocalToolBar(bars.getToolBarManager());
    }

    private void fillContextMenu(IMenuManager manager) {
        manager.add(action1);
        manager.add(new Separator());
        
        // Other plug-ins can contribute there actions here
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }

    private void fillLocalToolBar(IToolBarManager manager) {
        manager.add(action1);
        manager.add(new Separator());
    }

    private void fillLocalPullDown(IMenuManager manager) {
        manager.add(action1);
        manager.add(new Separator());
    }
}
