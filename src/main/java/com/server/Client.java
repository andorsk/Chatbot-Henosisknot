package com.server;

import com.message.MessageHelpers;
import com.proto.gen.MessageOuterClass;
import com.session.SessionBase;
import javafx.util.Pair;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;


public class Client extends Sender {

    private int mPort;
    private String mHostName;
    private BufferedReader mInput;
    private PrintWriter mOut;
    private BufferedWriter mWriter;
    private BufferedReader mSysIn;
    private ArrayList<SessionBase> mSessionList = new ArrayList<SessionBase>();

    public Client(String hostname, int portnumber) {
        init(hostname, portnumber);
    }

    private void init(String hostname, int mPort) {
        this.mHostName = hostname;
        this.mPort = mPort;
        this.setSenderID(1);
    }

    public Socket getClientSocketCopy() throws IOException {
        return new Socket(this.mHostName, this.mPort);
    }

    public void attachSession(SessionBase session){
        mSessionList.add(session);
    }

    /**
     * When we send a message, we need to get the conversation and session id associated
     * @return
     */
    @Override
    public Pair<String, String> getConversationId(){
        return new Pair<>(this.mSessionList.get(0).getSessionUUID(),this.mSessionList.get(0).getConversationMap().keySet().iterator().next());
    }


    public void sendMessage(String message){

        try {
            try (
                    Socket socket = new Socket(this.mHostName, this.mPort);
                    PrintWriter out =
                            new PrintWriter(socket.getOutputStream(), true);
            ){
                MessageOuterClass.Message msg = MessageHelpers.prepareMessage(message, this);
                out.println(msg.toString());
                out.close();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
