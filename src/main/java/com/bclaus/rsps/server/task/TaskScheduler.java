package com.bclaus.rsps.server.task;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ScheduledExecutorService;

/**
 * A class which schedules the execution of {@link Task}'s.
 * 
 * @author Graham
 * @author lare96
 */
public final class TaskScheduler {

	/**
	 * A queue of pending tasks.
	 */
	private static final Queue<ScheduledTask> pendingTasks = new LinkedList<>();

	/**
	 * A list of active tasks.
	 */
	private static final List<ScheduledTask> tasks = new LinkedList<>();

	/**
	 * Schedules the specified task. If this scheduler has been stopped with the
	 * {@link #terminate()} method the task will not be executed or
	 * garbage-collected.
	 * 
	 * @param task
	 *            The task to schedule.
	 */
	public void schedule(final ScheduledTask task) {
		if (task.isImmediate()) {
			task.execute();
		}
		pendingTasks.add(task);
	}

	public void submit(final ScheduledTask task) {
		schedule(task);
	}

	/**
	 * This method is automatically called every cycle by the
	 * {@link ScheduledExecutorService} and executes, adds and removes
	 * {@link Task}s. It should not be called directly as this will lead to
	 * concurrency issues and inaccurate time-keeping.
	 */
	public void run() {
		ScheduledTask task;

		// add all of the pending tasks
		while ((task = pendingTasks.poll()) != null) {
			if (task.isRunning()) {
				tasks.add(task);
			}
		}

		// process all existing tasks
		Iterator<ScheduledTask> it = tasks.iterator();
		while (it.hasNext()) {
			task = it.next();
			if (task.isStopped()) {
				it.remove();
				continue;
			}
			task.tick();
		}
	}

	/**
	 * Stops all tasks with the specified attachment.
	 * 
	 * @param attachment
	 *            the attachment to stop all tasks with.
	 */
	public void stopAll(Object attachment) {

		// stop all pending tasks
		for (ScheduledTask t : pendingTasks) {
			if (t.getAttachment().equals(attachment)) {
				t.stop();
			}
		}

		// stop all active tasks
		for (ScheduledTask t : tasks) {
			if (t.getAttachment().equals(attachment)) {
				t.stop();
			}
		}
	}
}
