package network.states.enb;

import config.Constant;
import events.DReachingENBEvent;
import events.GReachingDestinationEvent;
import events.HNotificationEvent;
import infrastructure.entity.Node;
import infrastructure.event.Event;
import infrastructure.state.State;
import network.elements.EntranceBuffer;
import network.elements.Packet;
import network.elements.UnidirectionalWay;

import network.entities.Switch;
//import network.states.packet.SStateP3;

public class N0 extends State {
	//�	State N0: ENB is not full.
    public N0(EntranceBuffer entranceBuffer){
        this.element = entranceBuffer;
        //countStateENB++;
    }
    @Override
    public void act(){
        // add event H
        EntranceBuffer entranceBuffer = (EntranceBuffer)element;
        long time = (long)entranceBuffer.physicalLayer.simulator.time();
        Event event = new HNotificationEvent(
        		entranceBuffer.physicalLayer.simulator,
        		time, time + Constant.CREDIT_DELAY, entranceBuffer);
        event.register(); //chen them su kien moi vao

        UnidirectionalWay unidirectionalWay = entranceBuffer.physicalLayer.links
                .get(entranceBuffer.getConnectNode().getId())
                .getWayToOtherNode(entranceBuffer.getConnectNode());
        Packet packet = unidirectionalWay.getPacket();

        if(packet != null) //todo them event moi phai tao state moi cho packet, vi state nay gan voi event
            if (!unidirectionalWay.hasEventOfPacket(packet)) {
                // add event D
            	time = (long)entranceBuffer.physicalLayer.simulator.time();
                event = new DReachingENBEvent(
                		entranceBuffer.physicalLayer.simulator,
                		time
                        , time + unidirectionalWay.getLink().getTotalLatency(packet.getSize())
                        , unidirectionalWay, packet);
                event.register(); //chen them su kien moi vao

            }

    }
}
