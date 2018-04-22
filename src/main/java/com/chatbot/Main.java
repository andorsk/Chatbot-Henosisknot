package com.chatbot;

import com.log.ChatLogger;
import com.server.Client;
import com.server.Server;
import com.server.StdListenerTask;
import com.session.SessionBase;
import com.session.TemporarySession;

/**
 * This is the main class as denoted by the pom.xml file. Instantiate and run high level objects from
 * here.
 */
public class Main {

    public static void main(String args[]) {
        init();
    }

    private static void init() {

        System.out.println("Starting logger...");
        ChatLogger.getInstance();

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

        Client client = new Client("localhost", 5555);

        //attach session to client.
        TemporarySession ts = new TemporarySession();
        ts.startSession();

        //attach session to both client and server.
        client.attachSession(ts);
        server.attachSession(ts);


        //Listen to console for input as well.
        StdListenerTask stdlistnertask = new StdListenerTask();
        stdlistnertask.attachCurrentConversationId(ts);
        Thread serverThread = new Thread(stdlistnertask);
        serverThread.start();

        System.out.println("Waiting for server to be ready..");
        while(!server.isReady()){
            //wait for threads to start.
            try {
                System.out.println("waiting...");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        client.sendMessage("Test Message sent from client");

    }
}
