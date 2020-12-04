package events;

import config.Constant;
import infrastructure.element.Element;
import infrastructure.event.Event;
import infrastructure.state.Type;
import network.elements.SourceQueue;
import network.elements.Packet;
import network.states.sourcequeue.Sq1;
import network.states.sourcequeue.Sq2;
import simulator.DiscreteEventSimulator;

public class AGenerationEvent extends Event {
	//Event dai dien cho su kien loai (A): goi tin duoc sinh ra
	

	
	
	public AGenerationEvent(DiscreteEventSimulator sim, long startTime, long endTime, IEventGenerator elem)
	{
		super(sim, endTime);
		//countSubEvent++;
		this.element = elem;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	@Override

	// todo bat dau tu event A
	public void actions() 
	{
		DiscreteEventSimulator sim = DiscreteEventSimulator.getInstance();
		//if(getElement() instanceof SourceQueue)
		{
			SourceQueue sourceQueue = (SourceQueue)getElement();
			
			Packet newPacket = sourceQueue.generatePacket(this.getStartTime());
			if(newPacket == null) { return; }
			newPacket.setId(sim.numSent++);
			this.setPacket(newPacket);
			//newPacket.setState(new StateP1(sourceQueue, newPacket, this));
			newPacket.setType(Type.P1);
			
			// update source queue state
			if(sourceQueue.getState() instanceof Sq1)//it means that elem is an instance of SourceQueue
			{
				sourceQueue.setState(new Sq2(sourceQueue));
			}

			long time = (long)sim.time();
			Event event = new BLeavingSourceQueueEvent(sim, time, time, sourceQueue, newPacket);
			//sourceQueue.insertEvents(event); //chen them su kien moi vao
			
			
			sim.addEvent(event);

			time = (long)sourceQueue.getNextPacketTime();
			
			Event ev = new AGenerationEvent(sim, time, time, sourceQueue);
			
			sim.addEvent(ev);
		}
		
	}

	
}
