package infrastructure.element;

import infrastructure.event.Event;
import infrastructure.state.State;
import network.elements.Packet;
import simulator.DiscreteEventSimulator;
import infrastructure.state.*;

import java.util.ArrayList;

import events.EMovingInSwitchEvent;
import events.IEventGenerator;

public abstract class Element implements IEventGenerator {
	protected int id;
	protected State state;
	protected long soonestEndTime = Long.MAX_VALUE; /// todo check NHONLV change from 0 to max
	//public ArrayList<Event> allEvents;
	//public DiscreteEventSimulator sim;

	public Element(){
		//allEvents = new ArrayList<Event>();
	}

	public void setId(int id) {
		this.id = id;
	}

	
	
	public void setType(Type type)
	{
		if(this.state == null)
			this.state = new State();
		this.state.type = type;
	}

	public void setSoonestEndTime(long soonestEndTime) {
		this.soonestEndTime = soonestEndTime;
	}

	public int getId() {
		return id;
	}

	public State getState() {
		return state;
	}
	
	public void setState(State state)
	{
		this.state = state;
	}

	public long getSoonestEndTime() { return soonestEndTime; }

	

	public boolean hasEventOfPacket(Packet packet)
	{
		DiscreteEventSimulator sim = DiscreteEventSimulator.getInstance();
		if(sim == null) return false;

		if(packet == null) return false;
		else
		{ 

			return false; 
		}

	}

	


}
