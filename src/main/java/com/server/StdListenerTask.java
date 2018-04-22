package com.server;

import com.message.MessageHelpers;
import com.proto.gen.MessageOuterClass;
import com.session.SessionBase;
import javafx.util.Pair;

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
public class StdListenerTask extends Sender implements Runnable {
    protected ExecutorService threadPool = Executors.newFixedThreadPool(10);

    private Socket mSocket = null;
    private boolean mClose = false;
    private SessionBase mAttachedSession = null;
    private String mAttachedConversationID = null;

    public StdListenerTask(){
        this.setSenderID(1);
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
                threadPool.submit(new InputWorker(new Socket("localhost", 5555), userInput, this));
            }

            reader.close();


        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void attachCurrentSession(SessionBase session){
        this.mAttachedSession = session;
    }

    /**
     * When we send a message, we need to get the conversation and session id associated
     * @return
     */
    @Override
    public Pair<String, String> getConversationId(){
        return new Pair<>(this.mAttachedSession.getSessionUUID(),this.mAttachedSession.getConversationMap().keySet().iterator().next());
    }

    /**
     * Attaches the latest conversation id to the std listener task.
     * @param session
     * @throws NullPointerException
     */
    public void attachCurrentConversationId(SessionBase session) throws NullPointerException {

        long earliestConversation = Long.MAX_VALUE;
        MessageOuterClass.Converation conv = null;
        for(String id : session.getConversationMap().keySet()){
            if(session.getConversationMap().get(id).getStartTime() < earliestConversation){
                conv = session.getConversationMap().get(id);
            };
        }
        if(conv != null){
            this.mAttachedConversationID = conv.getId();
            System.out.println("Std in is attached to " + conv.getId() + " with participate ids " + conv.getParticipantidsList());
        } else {
            throw new NullPointerException("No conversation detected. Error attaching conversation id.");
        }

    }

    private class InputWorker implements Runnable {
        private Socket mSocket;
        private String mText;
        private Sender mSender;
        public InputWorker(Socket socket, String input, Sender sender){
            this.mSocket = socket;
            this.mText = input;
            this.mSender = sender;
        }

        @Override
        public void run() {
            try {
                PrintWriter pw  = new PrintWriter(this.mSocket.getOutputStream());
                MessageOuterClass.Message msg = MessageHelpers.prepareMessage(mText, mSender);
                pw.println(msg.toString());
                pw.close();
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}
