package network.layers;


import config.Constant;
import events.EMovingInSwitchEvent;
import events.IEventGenerator;
import infrastructure.entity.Node;
import infrastructure.event.Event;
import infrastructure.state.State;
import network.elements.EntranceBuffer;
import network.elements.ExitBuffer;
import network.elements.Packet;
import routing.RoutingAlgorithm;


public class NetworkLayer extends Layer implements IEventGenerator{
	
	protected State state;
	public State getState() {
		return state;
	}
	
	public void setState(State state)
	{
		this.state = state;
	}
	
	public NetworkLayer(RoutingAlgorithm ra, Node node) {
		RoutingAlgorithm routingAlgorithm = null;
		try {
			routingAlgorithm = ra.build(node);
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.routingAlgorithm = routingAlgorithm;
	}

	public void controlFlow(ExitBuffer exitBuffer) {
		if (!(exitBuffer.isRequestListEmpty())) {
			int selectedId = Integer.MAX_VALUE;
			EntranceBuffer selectedENB  = null;
			Packet p;
			// lay ra cac enb tu request list cua exb hien tai
			for (EntranceBuffer enb : exitBuffer.getRequestList()) {
				p = enb.getPeekPacket();
				// chon ra Inport co Packet co id nho nhat
				if (p != null && !(enb.hasEventOfPacket(p))) {
					if (p.getId() < selectedId) {
						selectedId = p.getId();
						selectedENB = enb;
					}
				}
			}
			if(selectedENB != null) {
				long time = (long)selectedENB.physicalLayer.simulator.time();
				Event event = new EMovingInSwitchEvent(
						selectedENB.physicalLayer.simulator,
						time, time + Constant.SWITCH_CYCLE,
						selectedENB, selectedENB.getPeekPacket());
				event.register(); //chen them su kien moi vao
			}
		}
	}

	public void route(EntranceBuffer entranceBuffer) {
		if (entranceBuffer.getNextNodeId() == -1) {
			Packet packet = entranceBuffer.getPeekPacket();
			if((packet == null)){
				System.out.println("ERROR: 2");
			}
			
			int nextNodeID = //fatTreeRoutingAlgorithm.next(packet.getSource(), entranceBuffer.getNode().getId(), packet.getDestination());
					routingAlgorithm.next(packet, entranceBuffer.getNode());

			entranceBuffer.setNextNode(nextNodeID);

			ExitBuffer exitBuffer = entranceBuffer.physicalLayer.exitBuffers.get(nextNodeID);
			exitBuffer.addToRequestList(entranceBuffer);
			controlFlow(exitBuffer);
		} else {
			ExitBuffer exitBuffer = entranceBuffer.physicalLayer.exitBuffers
					.get(entranceBuffer.getNextNodeId());
			controlFlow(exitBuffer);
		}
	}
	
	public long getDurrationTime()
	{
		return 1000*1000*1000;
	}
}
