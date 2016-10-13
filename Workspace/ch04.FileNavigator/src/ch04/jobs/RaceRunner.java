package ch04.jobs;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;

/**
 * To cancel a team IJobManager jobMan = Platform.getJobManager();
 * jobMan.cancel(MY_FAMILY);
 * 
 * @author Owner
 * 
 */
public class RaceRunner extends Job {
    int maxDistance = 1000;
    private String team;
    private String name;

    public RaceRunner(final String name, String team) {
        super(name);
        this.team = team;
        this.name = name;
    }

    public void register(JobChangeAdapter adapter) {
        addJobChangeListener(adapter);
    }

    @Override
    public boolean belongsTo(Object family) {
        return family == team;
    }

    @Override
    protected IStatus run(IProgressMonitor monitor) {
        int count = 0;

        try {
            monitor.beginTask("From team " + team, maxDistance);

            while (count++ < maxDistance) {
                if (monitor.isCanceled())
                    return Status.CANCEL_STATUS;

                int stamina = (int) (Math.random() * 100) + 1;
                final long sleep = (long) (Math.random() * 1000) / stamina;

                monitor.subTask("Elapsed distance " + count + "/"
                        + maxDistance + " stamina " + stamina);

                // ... do some work ...
                monitor.worked(1);
                Thread.sleep(sleep);
            }

        } catch (InterruptedException e) {
            System.err.println(e);
        } finally {
            monitor.done();
        }
        return Status.OK_STATUS;
    }

    public void race() {
        schedule();
    }

    @Override
    public String toString() {
        return name + " from team " + team;
    }
}
