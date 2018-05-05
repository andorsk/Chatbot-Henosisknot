package com.server;

import com.message.MessageHelpers;
import com.proto.gen.MessageOuterClass;
import com.session.SessionBase;
import javafx.util.Pair;

import javax.ws.rs.ClientErrorException;
import java.io.*;
import java.net.Socket;
import java.rmi.ServerError;
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


    public String sendMessage(String message) throws ClientErrorException {
        StringBuffer stb;
        try {
            try (
                    Socket socket = new Socket(this.mHostName, this.mPort);
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader isr = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            ){
                MessageOuterClass.Message msg = MessageHelpers.prepareMessage(message, this);
                out.println(msg.toString());

                stb = new StringBuffer();
                String line = "";
                while((line = isr.readLine()) != null){
                    stb.append(line);
                }
                out.close();
                return stb.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new ClientErrorException(406);
        }
    }
}
