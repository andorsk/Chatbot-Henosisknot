package com.server;

import com.proto.gen.MessageOuterClass;
import com.session.SessionBase;
import play.Logger;
import play.api.mvc.Session;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class Client {

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
    }

    public Socket getClientSocketCopy() throws IOException {
        return new Socket(this.mHostName, this.mPort);
    }

    public void attachSession(SessionBase session){
        mSessionList.add(session);
    }


    public void sendMessage(String message){

        try {
            try (
                    Socket socket = new Socket(this.mHostName, this.mPort);
                    PrintWriter out =
                            new PrintWriter(socket.getOutputStream(), true);
            ){
                out.println(message);
                out.close();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
