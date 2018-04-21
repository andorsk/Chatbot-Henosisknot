package com.server;

import com.api.MessagingProtocol;
import com.proto.gen.MessageOuterClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private int mPort;

    public Server(int portnumber){
        this.mPort = portnumber;
    }

    public void start() {
        try (
                ServerSocket serverSocket = new ServerSocket(this.mPort);
                Socket clientSocket = serverSocket.accept();

                PrintWriter out =
                        new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
        ) {

            String inputLine, outputLine;

            // Initiate conversation with client
            MessagingProtocol mp = new MessagingProtocol();

            while ((inputLine = in.readLine()) != null) {

                MessageOuterClass.Message ms = MessageOuterClass.Message.newBuilder()
                        .setText(inputLine)
                        .setCreationTime(System.currentTimeMillis())
                        .setSenderUsername("User")
                        .setSenderUserid(1)
                        .setServiceType(MessageOuterClass.ServiceType.TEXT)
                        .setReceiversUsername(0, "AI")
                        .build();

                MessageOuterClass.Message response = mp.recieve(ms);
                response.toBuilder()
                        .setCreationTime(System.currentTimeMillis())
                        .build();
                mp.post(response);
            }

        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                    + mPort + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }

}
