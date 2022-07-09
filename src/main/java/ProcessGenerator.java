/**
 * Author - Satyam Ramawat(19210520)
 * Email - satyam.ramawat2@mail.dcu.ie
 */

import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class ProcessGenerator implements Runnable
{

    Processor processor;
    public int process_counter = 1;
 
    public ProcessGenerator(Processor processor)
    {
        this.processor = processor;
    }

    public void run()
    {
        while(true)
        {
            /*
             * Since nextGaussian() is method of Random Class, so in order to use this method
             * with Process Class Instance, thus created reference variable of Random Class(Parent Class)
             * and Object of process Class(Child Class) i.e Dynamic Dispatch Mechanism(Runtime Polymorphism)
             */
            Random process = new Process(processor);                // Dynamic Dispatch(Runtime Polymorphism)
            ((Process) process).setArrivalTime(new Date());         // Mechanism of Casting Object
            ((Process) process).setCoreNeeded(new Random().ints(1, 1, Main.numOfCores).findFirst().getAsInt());
            Thread thprocess = new Thread((Runnable) process); // Mechanism of Casting Object
            ((Process) process).setName("Process " + process_counter + " that needs " + ((Process) process).coreNeeded + " cores ");
            Main.numOfReadyQueueProcesses ++;
            thprocess.start();
            process_counter++;
            try
            {
                TimeUnit.SECONDS.sleep((long)process.nextGaussian() * 2 + 6);
                TimeUnit.SECONDS.sleep((long)process.nextGaussian() * 2 + 6);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
 
}