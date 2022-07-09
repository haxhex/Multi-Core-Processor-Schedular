package Scheduler;

import java.io.IOException;

public class SchedulerTest {
    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.err.println("[ERROR] Incorrect usage!\nMake sure you are providing a txt file");
        } else {
            Scheduler scheduler = new Scheduler(args[0]);
            scheduler.scheduling();
        }
    }
}
