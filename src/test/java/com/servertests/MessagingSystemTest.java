package com.servertests;

import com.server.Client;
import com.server.Server;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MessagingSystemTest {

    Server SERVER;
    @BeforeTest
    public void setUp(){
        SERVER = new Server(2222);
    }


    @Test
    public void TestSendMessage(){
        final String message1 = "This is message 1";

        Client cl = new Client("Client 1", 2222);
        responseOK(cl.sendMessage(message1));

    }

    private boolean responseOK(String response){

        return false;
    }

    @Test
    public void TestConcurrentMessage(){

    }
}
