/**
 * Multicore CPU scheduler
 * @authors  Helia Ghorbani, Sadaf Abedi
 */

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static int numOfCores;
    public static int numOfFreeCores;
    public static int numOfRunningProcesses = 1;
    public static int numOfReadyQueueProcesses = 0;
    public static final String PURPLE_BOLD = "\033[1;35m"; // PURPLE
    public static final String CYAN_BOLD = "\033[1;36m";   // CYAN
    public static final String RESET = "\033[0m";  // Text Reset

    public static void main(String[] args) throws InterruptedException
    {
        /*
         * To generate processes at interval of at random intervals, 
         * with mean M and standard deviation SD. processGenerator object has been created,
         * and started with Thread object. 
         * processGenerator class has been developed by use of Dynamic Dispatch(Runtime Polymorphism)
         * whereas, Reference of Random class and object of child class, in order to use nextGaussian function.
         * processGenerator class needs object of Processor class,
         * because it has method of align processes in a queue
         */

        // Block for Input Validation for number of cores and number of running threads

        if (args.length == 2){
                numOfCores = Integer.parseInt(args[0]);
                Integer.parseInt(args[1]);
            } else if (args.length == 1) {
                System.out.println("You did not enter number of threads.");
                System.exit(0);
            } else if (args.length == 0) {
                System.out.println("You did not enter number of Cores.");
                System.out.println("You did not enter number of threads.");
                System.exit(0);
            } else {
                System.out.println("You entered number more than needed.");
                System.exit(0);
            }


        //Processor and processGenerator object creation
        Processor processor = new Processor();
        ProcessGenerator processGenerator = new ProcessGenerator(processor);
        //Initializing and accessing available core for Parallel processing using Multi-core
        //Using available core in Executor Services for Multi Core
        final ExecutorService executor = Executors.newFixedThreadPool(Integer.parseInt(args[0]));
        long startTime = System.currentTimeMillis();
        
        //Printing useful information
        System.out.println("*****************************************************************************");
        //Used Input for Number of Process from command line argument, in order to reduce variable in Heap
        System.out.println("\nNumber of Cores in the processor: " + Integer.parseInt(args[0]));
        System.out.println("Number of threads: "+Integer.parseInt(args[1])+"\n");
        System.out.println("*****************************************************************************");

            numOfFreeCores = Integer.parseInt(args[0]) - 1;
            Runnable systemStatus = new Runnable() {
            public void run() {
                long now = System.currentTimeMillis();
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = new Date();
                if (numOfFreeCores < 0)
                    numOfFreeCores ++;
                System.out.println(CYAN_BOLD + "\nSystem Status\n" +
                        PURPLE_BOLD + "--------------------------------------------------------------------------" + "\n" +
                        "Now : " + formatter.format(date) + "\n" +
                        "Number of free cores : " + numOfFreeCores + "\n" +
                        "Number of busy cores : " + (numOfCores - numOfFreeCores) + "\n" +
                        "Running time duration : " +  (now - startTime) + " milliseconds " + "\n" +
                        "Number of running processes : " + numOfRunningProcesses + "\n" +
                        "Number of processes in the ready queue : " + numOfReadyQueueProcesses + "\n" +
                        "--------------------------------------------------------------------------" + "\n" + RESET);
            }
        };

        ScheduledExecutorService ex = Executors.newScheduledThreadPool(1);
        ex.scheduleAtFixedRate(systemStatus, 0, 5, TimeUnit.SECONDS);

        // Creating N Core Object Array
        Core[] core = new Core[Integer.parseInt(args[0])];
        // Creating M Threads , since number of running threads variable is Static variable i.e class variable
        Processor.numberOfThreads = Integer.parseInt(args[1]);
        
        //Starting Executor
        executor.execute(() -> {
            //Core counter starts from 1

            int core_id = 1;
            try {
                    for(int loop = 0; loop<Integer.parseInt(args[0]); loop++ ){
                    core[loop] = new Core(processor,core_id);
                    core_id++;

                    System.out.println("Core "+ (loop + 1) + " id: "+ core[loop].hashCode());
                    System.out.println("Core "+ (loop+1) + " is ready for accepting process.");

                    Thread coreThread = new Thread(core[loop]);
                    coreThread.start();}
                    Thread thpg = new Thread(processGenerator);
                    thpg.start();
                }
            catch (Exception ex1) {
                Logger.getLogger(Executor.class.getName()).log(Level.SEVERE, null, ex1);
             }
         });
 
         executor.shutdown();
         if (executor.awaitTermination(1, TimeUnit.DAYS)) {
             executor.shutdownNow();
         }
         
         long endTime = System.currentTimeMillis();

         System.out.println("\nParallel Execution Time : " + (endTime - startTime)
                 + " milliseconds.\n");
        
    }
}