package simulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import common.StdOut;
import config.Constant;
import infrastructure.event.Event;
import network.elements.EntranceBuffer;
import network.elements.ExitBuffer;
import network.elements.UnidirectionalWay;
import network.entities.Host;
import network.entities.Link;
import network.entities.Switch;
import network.entities.TypeOfHost;

public class DiscreteEventSimulator extends Simulator {
	public int numReceived = 0;
    public long receivedPacketPerUnit[];
    public int numSent = 0;
    public int numLoss = 0;
    public long totalPacketTime = 0;
    public int numEvent = 0;
    private boolean isLimit;
    private double timeLimit;
    private boolean verbose;
    private boolean isAssigned = false;
    public long totalHop = 0;
    
    public List<Integer> sizeOfCurrEvents = new ArrayList<Integer>();
    
    private static final DiscreteEventSimulator des = new DiscreteEventSimulator(true, Constant.MAX_TIME, false);
    
    public int halfSizeOfEvents = 0;
    
    private static boolean IS_LIMIT = false, VERBOSE = false;
    private static double TIME_LIMIT = 0;
    
    public static void Initialize(boolean isLimit, double timeLimit, boolean verbose)
    {
    	IS_LIMIT = isLimit;
    	TIME_LIMIT = timeLimit;
    	VERBOSE = verbose;
    }

    public static DiscreteEventSimulator getInstance()
    {
    	if(!des.isAssigned)
    	{
	    	des.isLimit = IS_LIMIT;
	    	des.timeLimit = TIME_LIMIT;
	    	des.verbose = VERBOSE;
	    	des.receivedPacketPerUnit = new long[(int)(des.timeLimit/ Constant.EXPERIMENT_INTERVAL +1)];
	    	des.isAssigned = true;
    	}
    	return des;
    }
    
    private DiscreteEventSimulator(boolean isLimit, double timeLimit, boolean verbose) {
        super();
        this.isLimit = isLimit;
        this.verbose = verbose;
        this.timeLimit = timeLimit;
        this.receivedPacketPerUnit = new long[(int)(timeLimit/ Constant.EXPERIMENT_INTERVAL +1)];
        
    }

    public double getTime() {
        return currentTime;
    }

    public double getTimeLimit() {
        return timeLimit;
    }
    
    
    @Override
    public void start () {
    	
    	if (eventList.isEmpty())
            throw new IllegalStateException ("start() called with an empty event list");
    	
		stopped = false;
		simulating = true;
		umontreal.ssj.simevents.Event ev;
		int countEvent = 0;
		
		try {
			long startTime = System.currentTimeMillis();//remove redundant variable
			int lastPercentage = 0;
			
			while ((ev = removeFirstEvent()) != null && !stopped
                    && (!isLimit || currentTime < timeLimit)) {
				
				countEvent++;
				ev.actions();
	
				
				int percentage = (int) (currentTime ) / (int) Constant.EXPERIMENT_INTERVAL; 
				if (percentage > lastPercentage) 
				{ 
					lastPercentage = percentage;
					StdOut.printProgress("Progress", startTime, (long) timeLimit, currentTime); 
				}
				
				 
			}
			StdOut.print("\r");
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			stopped = true;
			simulating = false;
		}
		
		System.out.println("# of Events: " + countEvent);
	}
    
    
    @Override
    protected Event removeFirstEvent() {
        if (this.stopped) {
            return null;
        } else {
            Event ev = (Event) this.eventList.removeFirst();
            if (ev == null) {
                return null;
            } else {
                this.currentTime = //(long) ev.time();
                		ev.getEndTime();
                ev.setEndTime(-10);
                
                return ev;
            }
        }
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void log(String message) {
        if (this.verbose) {
            StdOut.printf("At %d: %s\n", (long) this.getTime(), message);
        }
    }
    
    public void addEvent(Event e) {
        this.getEventList().add(e);
    }

    public void initializeCollectionOfEvents()
    {
    	//long startTime = System.currentTimeMillis();
		// todo chu y kiem tra moi thu extends Element Class
		List<Host> allHosts = this.topology.getHosts();
		for (Host host : allHosts) {
			if (host.type != TypeOfHost.Destination) {//tuc no la source
				//soonestEndTime will be updated later as events are executed
				//host.physicalLayer.sourceQueue.sim = this;
				halfSizeOfEvents++;
				//soonestEndTime will be updated later as events are executed

				//add uniWay of host(way from it)
				UnidirectionalWay unidirectionalWay = host.physicalLayer.links.get(host.getId()).getWayToOtherNode(host);
				//unidirectionalWay.sim = this;
				halfSizeOfEvents++;

				int connectedNodeID = host.physicalLayer.links
						.get(host.getId()).getOtherNode(host).getId();
				//host.physicalLayer.exitBuffers.get(connectedNodeID).sim = this;
				halfSizeOfEvents++;
				
			}


		}
		//lay event tu tat ca ca link truoc
		List<Switch> allSwitches = this.topology.getSwitches();
		for (Switch aSwitch : allSwitches) {
			//add uniWay of switch(way from it)
			for (Link link : aSwitch.physicalLayer.links.values()) {
				//link.getWayToOtherNode(aSwitch).sim = this;
				halfSizeOfEvents++;
				
			}
			
			for (ExitBuffer exitBuffer : aSwitch.physicalLayer.exitBuffers.values()) {
				//exitBuffer.sim = this;
				halfSizeOfEvents++;
			}
			
			for (EntranceBuffer entranceBuffer : aSwitch.physicalLayer.entranceBuffers.values()) {
				//entranceBuffer.sim = this;
				halfSizeOfEvents++;
			}
		}
		
		System.out.println("Number of 1/2 events = " + halfSizeOfEvents);
    }

    	

    public long selectNextCurrentTime(long currentTime)
    {
    	long result = Long.MAX_VALUE;
		
    	return result;
    }
    
    



}
