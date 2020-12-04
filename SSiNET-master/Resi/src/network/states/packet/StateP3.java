package network.states.packet;

import events.AGenerationEvent;
import events.CLeavingEXBEvent;
import infrastructure.event.Event;
import infrastructure.state.State;
import network.elements.Packet;
import network.elements.SourceQueue;
import network.elements.UnidirectionalWay;

public class StateP3 extends State {
	//�	State P3: the packet is moved in a unidirectional way.

    public Packet packet;


    public StateP3(UnidirectionalWay unidirectionalWay, Packet p, Event ev)
    {
        this.element = unidirectionalWay;
        this.packet = p;
        //this.ancestorEvent = ev;
    }

    @Override
    public void act(){

    }
}
