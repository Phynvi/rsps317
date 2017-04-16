package com.bclaus.rsps.server.vd.event;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles all of our cycle based events
 * 
 * @author Stuart <RogueX>
 * 
 */
public class CycleEventHandler {

	/**
	 * The instance of this class
	 */
	private static CycleEventHandler instance;

	/**
	 * Returns the instance of this class
	 * 
	 * @return
	 */
	public static CycleEventHandler getSingleton() {
		if (instance == null) {
			instance = new CycleEventHandler();
		}
		return instance;
	}

	/**
	 * Holds all of our events currently being ran
	 */
	private static List<CycleEventContainer> events;

	/**
	 * Creates a new instance of this class
	 */
	public static void addEvent(final Object owner, final CycleEvent event, final int cycles) {
        events.add(new CycleEventContainer(owner, event, cycles));
    }
	public CycleEventHandler() {
		this.events = new ArrayList<CycleEventContainer>();
	}

	/**
	 * Add an event to the list
	 * 
	 * @param owner
	 * @param event
	 * @param cycles
	 */
	public static void addEvent(int id, Object owner, CycleEvent event, int cycles) {
		events.add(new CycleEventContainer(id, owner, event, cycles));
	}

    public static void addEvent(Object owner, CycleEvent event, int cycles, String name) {
        events.add(new CycleEventContainer(owner, event, cycles, name));
    }
	/**
	 * Execute and remove events
	 */
	public void process() {
		List<CycleEventContainer> eventsCopy = new ArrayList<CycleEventContainer>(events);
		List<CycleEventContainer> remove = new ArrayList<CycleEventContainer>();
		for (CycleEventContainer c : eventsCopy) {
			if (c != null) {
				if(c.needsExecution() && c.isRunning()) {
					c.execute();
				if (!c.isRunning()) {
					remove.add(c);
				}
			}
		}
	}
		for (CycleEventContainer c : remove) {
			events.remove(c);
		}
	}
	
	/**
	 * Returns the amount of events currently running
	 * @return amount
	 */
	public int getEventsCount() {
		return this.events.size();
	}
	
	/**
	 * Stops all events which are being ran by the given owner
	 * @param owner
	 */
	public void stopEvents(Object owner) {
		for (CycleEventContainer c : events) {
			if(c.getOwner() == owner) {
				c.stop();
			}
		}
	}

}