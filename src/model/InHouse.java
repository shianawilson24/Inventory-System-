package model;

/**
 * @author Shiana Wilson  This class supplies the InHouse object.
 */
public class InHouse extends Part {
    private int machineId;

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId){
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * @return the machine ID
     */
    public int getMachineId() {
        return machineId;
    }

    /**
     * @param machineId the machine ID to set
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }
}