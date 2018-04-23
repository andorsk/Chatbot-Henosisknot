package com.intializersfortest;

import com.server.Server;
import org.testng.annotations.BeforeClass;

public class SetupServer {

    @BeforeClass
    public void SetupServer(){
        Server server = new Server(5555);
        server.startServerThread();

        //wait for threads to start.
        while (!server.isOpen()) {
            System.out.println("Waiting for server to open");
            //wait for threads to start.
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
