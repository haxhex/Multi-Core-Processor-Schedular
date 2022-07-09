import java.util.Date;
import java.util.Random;

public class Process extends Random implements Runnable
{

    // Parameters
    /*
     * Below serialVersionUID key from JDK 1.1 for interoperability
     * Which is required in order to leverage synchronization
     */

    public static int num;
    private static final long serialVersionUID = 1L;
    String name;
    Date arrivalTime;
    Processor processor;
    int burstTime;
    int coreNeeded;

    public Process(Processor processor)
    {
        this.processor = processor;
        num++;
    }
 
    public String getName() {
        return name;
    }
 
    public Date getArrivalTime() {
        return arrivalTime;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    public void setArrivalTime(Date arrivalTime) {
        this.arrivalTime = arrivalTime;
    }
 
    public void run()
    {
        goForWork();
    }
    private synchronized void goForWork()
    {
        processor.add(this);
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }


    public void setCoreNeeded(int coreNeeded) {
        this.coreNeeded = coreNeeded;
    }
}