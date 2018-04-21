package com.server;

import com.proto.gen.MessageOuterClass;
import play.Logger;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client {

    private int mPort;
    private String mHostName;
    private BufferedReader mInput;
    private PrintWriter mOut;
    private BufferedWriter mWriter;
    private BufferedReader mSysIn;

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
