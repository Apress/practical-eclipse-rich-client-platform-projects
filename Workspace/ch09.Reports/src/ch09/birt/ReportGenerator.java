package ch09.birt;

import java.util.HashMap;

import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.RenderOption;

public class ReportGenerator {
    IReportEngine engine;

    public ReportGenerator() {
        final EngineConfig config = new EngineConfig();

        IReportEngineFactory factory = (IReportEngineFactory) Platform
            .createFactoryObject(
                    IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);

        engine = factory.createReportEngine(config);
    }

    /**
     * Run and render report task
     * @param designDocPath
     *            Design document
     * @param params
     *            Report params as a query string name1=val1&name2=val2,...
     * @param outFormat
     *            pdf, html, etc.
     * @param out
     *            Output file
     * @throws EngineException
     */
    @SuppressWarnings("unchecked")
    public void runAndRender(String designDocPath, String outFileName,
            String outFormat, String params) throws EngineException {
        // Open the report design
        IReportRunnable design = engine.openReportDesign(designDocPath);

        // Create task to run and render the report,
        IRunAndRenderTask task = engine.createRunAndRenderTask(design);

        // Set parent classloader for engine
        task.getAppContext().put(
                EngineConstants.APPCONTEXT_CLASSLOADER_KEY,
                ReportGenerator.class.getClassLoader());

        if (params != null) {
            task.setParameterValues(splitParams(params));
            task.validateParameters();
        }

        // render options
        IRenderOption options = new RenderOption();
        options.setOutputFormat(outFormat);
        options.setOutputFileName(outFileName);

        task.setRenderOption(options);

        // run and render report
        task.run();
        task.close();
    }

    public void destory() {
        engine.destroy();
    }

    /**
     * Extract report params from a query string. Values must be integer
     * @param queryString
     *            name1=val1&name2=val2&....
     * @return
     */
    private static HashMap<String, Object> splitParams(String queryString) {
        String[] pairs = queryString.split("&");
        HashMap<String, Object> params = new HashMap<String, Object>();

        if (pairs == null)
            return null;

        for (int i = 0; i < pairs.length; i++) {
            String[] keyVal = pairs[i].split("=");

            if (keyVal == null)
                throw new IllegalArgumentException(
                        "Invalid params quer string:" + queryString);
            // vals must be integers
            params.put(keyVal[0], new Integer(keyVal[1]));
        }
        return params;
    }

}
