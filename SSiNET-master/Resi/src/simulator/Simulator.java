package simulator;

import java.util.ArrayList;

import infrastructure.event.Event;
import network.Topology;

public abstract class Simulator extends umontreal.ssj.simevents.Simulator{
	protected long currentTime = 0;
    protected boolean stopped = true;
    protected boolean simulating = false;
    
    public Topology topology;
    
    public ArrayList<Event> currentEvents = new ArrayList<Event>();
    
    public double time() {
        return this.currentTime;
    }

    public void init() {
        this.currentTime = 0;
        this.stopped = false;
        this.simulating = false;
    }
    
    public boolean isSimulating() {
        return this.simulating;
    }

    public boolean isStopped() {
        return this.stopped;
    }
    
    public void stop() {
        this.stopped = true;
    }
    
    public void start () {
    
    }
    
    
    
    /*public void addLocationOfEvents(Device device)
    {
    	if(device instanceof Host)
    	{
    		addLocation((Host)device);
    	}
    	if(device instanceof Switch)
    	{
    		
    	}
    }
    
    public void addLocation(Host host)
    {
    	
    }*/
    
}
