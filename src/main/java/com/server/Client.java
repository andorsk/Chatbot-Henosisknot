package com.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

    private int mPort;
    private String mHostName;

    public Client(String hostname, int portnumber){
        init(hostname, portnumber);
    }

    private void init( String hostname, int mPort){
        this.mHostName = hostname;
        this.mPort = mPort;
    }

    public void start(){
        try (
                Socket client_socket = new Socket("Client", this.mPort);
                PrintWriter out = new PrintWriter(client_socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(client_socket.getInputStream()));
        ){
            BufferedReader stdIn =
                    new BufferedReader(new InputStreamReader(System.in));
            String fromServer;
            String fromUser;

            while ((fromServer = in.readLine()) != null) {
                System.out.println("Server: " + fromServer);
                if (fromServer.equals("Bye."))
                    break;

                fromUser = stdIn.readLine();
                if (fromUser != null) {
                    System.out.println("Client: " + fromUser);
                    out.println(fromUser);
                }
            }
        }
            catch (UnknownHostException e) {
            System.err.println("Don't know about host " + this.mHostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                   this.mHostName);
            System.exit(1);
        }
    }
}
