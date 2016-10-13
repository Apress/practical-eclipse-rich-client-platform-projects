package ch09.internal;

import java.util.Hashtable;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.osgi.framework.BundleContext;

import ch09.birt.ReportGenerator;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends Plugin implements CommandProvider {

    // The plug-in ID
    public static final String PLUGIN_ID = "ch09.Reports";

    // The shared instance
    private static Activator plugin;

    /**
     * The constructor
     */
    public Activator() {
    }

    @SuppressWarnings("unchecked")
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;

        Hashtable properties = new Hashtable();
        context.registerService(CommandProvider.class.getName(), this,
                properties);

        System.out.println("REPORT GENERATOR STARTED");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext
     * )
     */
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     * 
     * @return the shared instance
     */
    public static Activator getDefault() {
        return plugin;
    }

    @Override
    public String getHelp() {
        return "---BIRT Console Commands---"
                + "\n\treport <input_file> <output_file> <format> <user_params>"
                + "\n\t\tuser params: name1=value1&name2=value2&...";
    }

    /**
     * Report command
     * 
     * @param ci
     * @throws Exception
     */
    public void _report(CommandInterpreter ci) throws Exception {
        // Arguments:
        // In C:/Documents/Books/EclipseRCP/Workspace/ch09.Reports/Reports/
        // ClassicCarsInc/TopNPercent.rptdesign
        // Out c:/tmp/junk/rep.pdf
        // Fmt pdf
        // User params: "Top Percentage=10&Top Count=3"
        String in = ci.nextArgument();
        String out = ci.nextArgument();
        String fmt = ci.nextArgument();
        String params = ci.nextArgument();

        if (in == null) {
            ci.println("Invalid arguments.\n" + getHelp());
            return;
        }

        try {
            ReportGenerator generator = new ReportGenerator();
            generator.runAndRender(in, out, fmt, params);
            generator.destory();

            ci.println(out);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
