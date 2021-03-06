package com.server;

import com.api.MessageController;
import com.message.MessageHelpers;
import com.nlp.StatementProcessingPipeline;
import com.proto.gen.MessageOuterClass;
import com.session.SessionBase;
import javafx.util.Pair;
import play.libs.Json;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server extends Sender {
    private int mPort;
    private boolean mOpen = false;
    protected ExecutorService threadPool = Executors.newFixedThreadPool(10);
    protected Thread runningThread= null;
    protected ServerSocket mServerSocket;
    protected boolean mClose = false;
    protected StatementProcessingPipeline mSPP;
    private ArrayList<SessionBase> mSessionList = new ArrayList<SessionBase>();
    private boolean mIsReady = false;

    /**
     * Start a threaded server
     * @param portnumber
     */
    public Server(int portnumber) {
        this.mPort = portnumber;
        this.setSenderID(0);
        init();
    }

    public ArrayList<? extends SessionBase> getSessionList(){
        return mSessionList;
    }

    public void init(){
       System.out.println("Starting ChatBot Engine...");
       StatementProcessingPipeline spp = new StatementProcessingPipeline();
       spp.start();
       this.mSPP = spp;

       while(!spp.getParsingEngine().isReady()){
           try {
               System.out.print("#");
               Thread.sleep(1000);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }
       mIsReady = true;
       System.out.println("Server is ready");

    }

    public boolean isReady(){
        return this.mIsReady;
    }

    public StatementProcessingPipeline getStatementProcessingPipeline(){
        return this.mSPP;
    }

    public ServerSocket getSocket(){
        return this.mServerSocket;
    }

    /**
     * Needs to be updated. Gives the sesion and conversatoin id
     * @return
     */
    public Pair<String, String> getConversationId(){
        return new Pair<>(this.mSessionList.get(0).getSessionUUID(),this.mSessionList.get(0).getConversationMap().keySet().iterator().next());
    }


    /**
     * Start the server and open a connection between client socket.
     */
    public void start() {

        try {
            openServerSocket();

            while (true) {

                Socket clientSocket = null;
                String request = null;

                try{
                    clientSocket = this.mServerSocket.accept();
                } catch (IOException e) {
                    if(isClosed()){
                        System.out.println("Server Stopped.") ;
                        break;
                    }
                }

                threadPool.submit(new WorkerRunnable(clientSocket, this));
            }
        } catch(RuntimeException e){
            throw new RuntimeException("Cannot read socket for client on port " + this.mPort, e);
        }
    }

    /**
     * Performes synchronized stop on thread
     */
    public synchronized void stop(){
        this.mClose = true;
        try {
            this.mServerSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    private void openServerSocket() {
        try {
            this.mServerSocket = new ServerSocket(this.mPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + this.mPort, e);
        }
        this.mOpen = true;
        System.out.println("Server running on port  " + this.mPort + " ...");
    }

    //is the socket connection open?/
    public boolean isOpen(){
        return mOpen;
    }

    private boolean isClosed(){
        return this.mClose;
    }

    public void attachSession(SessionBase session){
        mSessionList.add(session);
    }

    /**
     *
     */
    public void startServerThread() {

        System.out.println("Starting threads for server listener");

        Runnable serverTask = new Runnable() {
            @Override
            public void run() {
                start();
            }
        };

        Thread serverThread = new Thread(serverTask);
        serverThread.start();
    }

    private class WorkerRunnable implements Runnable{
        protected Socket clientSocket;
        protected String json = "";
        protected Server mServer = null;

        private WorkerRunnable(Socket clientSocket, Server server) {
            this.clientSocket = clientSocket;
            this.mServer = server;
        }

        @Override
        public void run() {

            try {

                InputStream input  = clientSocket.getInputStream();
                OutputStream out = clientSocket.getOutputStream();

                BufferedReader br = new BufferedReader(new InputStreamReader(input));
                String line;

                while((line = br.readLine()) != null){
                    json = json + line + "\n";
                }

                System.out.println("Received message: \n" + json);

                MessageController.processInput(json, mServer);

                out.close();
                input.close();

            } catch (IOException e) {
                //report exception somewhere.
                e.printStackTrace();
            }
        }
    }


}
