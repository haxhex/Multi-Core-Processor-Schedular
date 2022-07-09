/*
 * This is the Main Functional class of this project
 * Various Functionality has been loosely Coupled
 */

import Scheduler.FCFS;
import Scheduler.Scheduler;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class Processor extends Random
{

    public static ArrayList<Process> processes = new ArrayList<>();
    /*
     * Below serialVersionUID key from JDK 1.1 for interoperability
     * Which is required in order to leverage synchronization between Random Generation
     */
    private static final long serialVersionUID = 1L;
    public static int numberOfThreads;
    int core_id;
    List<Process> waitingProcess;
    // Below Creating object for Random Interval object to allocating cores
    Random normal_distribution_generator = new Random();

     // Below Constructor creates list of process via LinkedList java collection.
    public Processor()
    {
        waitingProcess = new LinkedList<Process>();
    }

    // Scheduling - Part 1
    public void cpuCoreAllocator(int core_id)
    {
        this.core_id = core_id;
        Process process;

        synchronized (waitingProcess)
        {
            // Below logic check the number of process in the Queue.
            while(waitingProcess.size() == 0)
            {
                System.out.println("Core " + core_id + " is free.");
                if (Main.numOfFreeCores < Main.numOfCores)
                    Main.numOfFreeCores ++;
                try
                {
                    waitingProcess.wait();
                }
                catch(InterruptedException e)
                {
                    e.printStackTrace();
                }
            }

            System.out.println("Core "+ core_id + " is ready to use by a process from the queue.");
            process = (Process)((LinkedList<?>) waitingProcess).poll();
        }
        long duration = 0;
        try
        {    
            System.out.println(process.getName() + " is using core " + core_id + " .");
            Main.numOfRunningProcesses ++;
            if (Main.numOfReadyQueueProcesses > 0)
                Main.numOfReadyQueueProcesses --;
            if (Main.numOfCores > 0)
                Main.numOfFreeCores --;
            Main.numOfRunningProcesses ++;
            duration = (int)normal_distribution_generator.nextGaussian() * 5 + 15;    // Generating time for process burst time
            processes.add(process);
            TimeUnit.SECONDS.sleep(duration);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
        System.out.println(process.getName() + " release core " + core_id + " after its burst time " + duration+ " seconds.");
        process.setBurstTime((int) duration);
        process.coreNeeded --;
        Main.numOfRunningProcesses --;
        if(process.coreNeeded != 0) {
            add(process);
            Main.numOfReadyQueueProcesses ++;
        }
        if(waitingProcess.size() == 0)
            System.out.println("Core " + core_id + " waiting for process from the ready queue.");
        else
            //This will take care if any process has been taken by any other core at same moment.
            System.out.println("Core " + core_id + " waiting for process. It can choose process available in ready queue.");

    }

    // Scheduling - Part 2
    public void add(Process process)
    {
        System.out.println(process.getName() + " arrival time is " + process.getArrivalTime() + " .");
 
        synchronized (waitingProcess)
        {
            if(waitingProcess.size() == numberOfThreads)
            {
                System.out.println("No thread is available for process " + process.getName() + " .");
                System.out.println(process.getName() + " added to waiting queue.");
                Main.numOfCores ++;
                return ;
            }
 
            ((LinkedList<Process>) waitingProcess).offer(process);
            System.out.println(process.getName()+ " got a thread.");
             Main.numOfReadyQueueProcesses ++;
            if(waitingProcess.size() == 1)
                waitingProcess.notify();
        }
    }
}