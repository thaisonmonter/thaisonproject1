package infrastructure.entity;

//import simulator.DiscreteEventSimulator;

public abstract class Device {
    protected int id;

    //protected static DiscreteEventSimulator sim;

    public Device(int id) {
        this.id = id;
    }

    public void clear() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
//public static void setSim(DiscreteEventSimulator sim) {
    //    NetworkObject.sim = sim;
    //}
}
