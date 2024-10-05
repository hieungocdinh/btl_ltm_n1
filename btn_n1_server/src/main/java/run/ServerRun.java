package run;

import java.io.IOException;
import service.ClientManager;
import service.Client;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerRun {

    public static volatile ClientManager clientManager;
    public static boolean isShutDown = false;
    public static ServerSocket ss;

    public ServerRun() {
        try {
            int port = 2000;

            ss = new ServerSocket(port);
            System.out.println("Created Server at port " + port + ".");

            // init managers
            clientManager = new ClientManager();
            
            // create threadpool
            ThreadPoolExecutor executor = new ThreadPoolExecutor(
                    10, // corePoolSize
                    100, // maximumPoolSize
                    10, // thread timeout
                    TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(8) // queueCapacity
            );

            // server main loop - listen to client's connection
            while (!isShutDown) {
                try {
                    // socket object to receive incoming client requests
                    Socket s = ss.accept();
                    System.out.println("+ New Client connected: " + s);

                    // create new client runnable object
                    Client c = new Client(s);
                    clientManager.add(c);
                    System.out.println("Count of client online: " + clientManager.getSize());
                    // execute client runnable
                    executor.execute(c);

                } catch (IOException ex) {
                    // Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                    isShutDown = true;
                }
            }

            System.out.println("shutingdown executor...");
            executor.shutdownNow();

        } catch (IOException ex) {
            Logger.getLogger(ServerRun.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
        new ServerRun();
    }  
}
