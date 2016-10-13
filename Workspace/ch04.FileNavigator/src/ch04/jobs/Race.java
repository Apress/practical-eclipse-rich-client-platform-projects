package ch04.jobs;

import java.util.ArrayList;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;

/**
 * Simple race Race to demonstrate concurrency concepts
 */
public class Race {

    static ArrayList<RaceRunner> results = new ArrayList<RaceRunner>();

    /**
     * The Referee monitors the race
     * 
     * @author Owner
     * 
     */
    static class Referee extends Job {
        int numRunners = 6;
        boolean done = false;

        public Referee(String name) {
            super(name);
        }

        @Override
        protected IStatus run(IProgressMonitor monitor) {
            while (!done) {
                if (results.size() >= numRunners)
                    done = true;
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                    System.err.println(e);
                }
            }

            System.out.println("Race results");

            // print race results
            int place = 1;
            for (int i = 0; i < results.size(); i++) {
                RaceRunner runner = results.get(i);

                // if the runner completes successfully
                if (runner.getResult().getCode() == IStatus.OK)
                    System.out.println((place++) + " " + runner);
            }
            return Status.OK_STATUS;
        }

    }

    public Race() {
    }

    /**
     * Start the race
     */
    public void start() {
        RaceRunner bob = new RaceRunner("Bob", "US");
        RaceRunner john = new RaceRunner("John", "US");
        RaceRunner hans = new RaceRunner("Hans", "GER");
        RaceRunner lars = new RaceRunner("Lars", "GER");
        RaceRunner harry = new RaceRunner("Harry", "UK");
        RaceRunner ron = new RaceRunner("Ron", "UK");

        // race referee...monitors results
        Referee ref = new Referee("Referee");
        ref.setSystem(true);
        ref.schedule();

        JobChangeAdapter oneKatNY = new JobChangeAdapter() {
            @Override
            public synchronized void done(IJobChangeEvent event) {
                results.add((RaceRunner) event.getJob());
            }
        };

        bob.register(oneKatNY);
        john.register(oneKatNY);
        hans.register(oneKatNY);
        lars.register(oneKatNY);
        harry.register(oneKatNY);
        ron.register(oneKatNY);

        bob.race();
        john.race();
        hans.race();
        lars.race();
        harry.race();
        ron.race();
    }
}
