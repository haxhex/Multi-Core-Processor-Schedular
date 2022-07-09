package Scheduler;

import java.io.*;
import java.util.ArrayList;

public class Scheduler {

  private static ArrayList<Process> processes = new ArrayList<Process>();
  String filepath;

  public Scheduler(String filepath) {
    this.filepath = filepath;
  }

  public void scheduling () throws IOException {
    // user did not provide enough arguments

    File file = null;
    BufferedReader br = null;

    try {
      file = new File(filepath);
      br = new BufferedReader(new FileReader(file));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return;
    }

    String line;
    if (br.ready()) {
      while((line = br.readLine()) != null) {
        String[] processInfo = line.split("\\s+");
        processes.add(new Process(
          Integer.parseInt(processInfo[0].substring(1)), // Process ID
          Integer.parseInt(processInfo[1]), // Process Arrival Time
          Integer.parseInt(processInfo[2]),  // Process Burst Time
          Integer.parseInt(processInfo[3])
        ));
      }

      br.close();
    } else {
      System.err.println("[ERROR] Something is wrong with your file. Please make sure it is formatted correctly.\nEach line represents a process and its details. Each line should be formatted as follows:\n\tP<process number> <arrival time> <burst time>\n");
      br.close();
      return;
    }

    /*
      We can assume the processes will always be sorted by arrival time
      when entered by the user, but this is just a precaution to make
      sure it definitely is
    */
    processes.sort((p1, p2) -> p1.getArrivalTime() - p2.getArrivalTime());

    System.out.println("+" + "-".repeat(34) + "+");
    System.out.println("|     CPU Scheduling Simulator     |");
    System.out.println("+" + "-".repeat(34) + "+");

    FCFS fcfs = new FCFS(processes);
    fcfs.runFCFS();

    System.out.println("\n" + "=".repeat(40));
  }
}
