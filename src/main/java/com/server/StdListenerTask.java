package com.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Std Listener allows user to directly type into console and receive feedback. It pushes a
 * request through the socket on the specified port directly rather than through wget.
 */
public class StdListenerTask implements Runnable {
    protected ExecutorService threadPool = Executors.newFixedThreadPool(10);

    private Socket mSocket = null;
    private boolean mClose = false;

    public StdListenerTask(){

    }

    public StdListenerTask(Socket socket) {
        try {
            this.mSocket = new Socket("localhost", 5555);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        PrintWriter out = null;
        BufferedReader reader = null;

        try {

            String userInput;
            reader = new BufferedReader(new InputStreamReader(System.in));

            while ((userInput = reader.readLine()) != null) {
                threadPool.submit(new InputWorker(new Socket("localhost", 5555), userInput));
            }

            reader.close();


        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private class InputWorker implements Runnable {
        private Socket mSocket;
        private String mText;
        public InputWorker(Socket socket, String input){
            this.mSocket = socket;
            this.mText = input;
        }

        @Override
        public void run() {
            try {
                PrintWriter pw  = new PrintWriter(this.mSocket.getOutputStream());
                pw.println(mText);
                pw.close();
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}
