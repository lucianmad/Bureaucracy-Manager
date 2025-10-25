package org.entities;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Printer  {
//    private final BlockingQueue<PrintJob> printQueue = new LinkedBlockingQueue<>();
//    private volatile boolean running = true;
//    private Thread thread;
//
//    public void start() {
//        thread = new Thread(this, "PrinterThread");
//        thread.start();
//        System.out.println("Printer started.");
//    }
//
//    public void stop() {
//        running = false;
//        if (thread != null) thread.interrupt();
//    }
//
//    // Counters submit print jobs here
//    public void submitJob(PrintJob job) {
//        try {
//            printQueue.put(job);
//            System.out.println("[Printer] Job queued for " + job.getDocument() + " from Counter " + job.getCounter().getId());
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//    }

//    @Override
//    public void run() {
//        while (running) {
//            try {
//                PrintJob job = printQueue.take(); // waits if no jobs
//                print(job);
//            } catch (InterruptedException e) {
//                if (!running) break;
//            }
//        }
//        System.out.println("Printer stopped.");
//    }

//    private void print(PrintJob job) throws InterruptedException {
//        System.out.println("[Printer] Printing " + job.getDocument() +
//                " for " + job.getCustomer().getName() +
//                " (from Counter " + job.getCounter().getId() + ")");
//        Thread.sleep(1000); // simulate print time
//        System.out.println("[Printer] Finished " + job.getDocument());
//        // Signal counter that document is printed
//        job.getCounter().onDocumentPrinted(job.getCustomer(), job.getDocument());
//    }
}
