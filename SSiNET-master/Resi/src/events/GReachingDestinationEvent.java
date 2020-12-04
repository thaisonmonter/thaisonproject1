package events;

import config.Constant;
import infrastructure.element.Element;
import infrastructure.entity.Node;
import infrastructure.event.Event;
import infrastructure.state.Type;
import network.elements.ExitBuffer;
import network.elements.Packet;
import network.elements.UnidirectionalWay;

import network.entities.Host;
import network.entities.Switch;
import network.states.enb.N0;


import network.states.unidirectionalway.W0;
import network.states.unidirectionalway.W1;
import network.states.unidirectionalway.W2;
import simulator.DiscreteEventSimulator;

public class GReachingDestinationEvent extends Event {
	//Event dai dien cho su kien loai (G): goi tin den duoc nut dich

    public GReachingDestinationEvent(
    		DiscreteEventSimulator sim,
    		long startTime, long endTime, Element elem, Packet p)
    {
    	//countSubEvent++;
    	super(sim, endTime);
        this.startTime = startTime;
        this.endTime = endTime;
        this.element = elem;
        this.packet = p;
    }
    @Override
    public void actions()
    {
        //if(getElement() instanceof UnidirectionalWay) 
        {
            UnidirectionalWay unidirectionalWay = (UnidirectionalWay)element;
            
            Node nextNode = unidirectionalWay.getToNode();

            if(//packet.getState() instanceof SStateP3
            	packet.getState().type == Type.P3
            		&& unidirectionalWay.getState() instanceof W1
                    && nextNode.isDestinationNode()
                    && unidirectionalWay.getPacket() == packet)
            {
                unidirectionalWay.removePacket();
                Host destinationNode = (Host)nextNode;
                destinationNode.receivePacket(packet);

                //change state packet
                //packet.setState(new StateP6(packet, this));
                packet.setType(Type.P6);
                //packet.getState().act();
                //change state of uniWay
                unidirectionalWay.setState(new W0(unidirectionalWay));
                unidirectionalWay.getState().act();

                //change state of EXB
                ExitBuffer sendExitBuffer = unidirectionalWay.getFromNode().physicalLayer
                        .exitBuffers.get(unidirectionalWay.getToNode().getId());
                if (sendExitBuffer.getState().type == Type.X00) {
                    //sendExitBuffer.setState(new X01(sendExitBuffer));
                	sendExitBuffer.setType(Type.X01);
                    sendExitBuffer.getState().act();
                }
                if (sendExitBuffer.getState().type == Type.X10) {
                    //sendExitBuffer.setState(new X11(sendExitBuffer));
                	sendExitBuffer.setType(Type.X11);
                    sendExitBuffer.getState().act();
                }

//                System.out.println("p: " + packet.getId() +  ", G to destination");

            }
        }
        //else 
        {
            //System.out.println("ERROR: Event " + this.toString() + "khong the chua element: " + getElement().toString());
        }
    }
}
