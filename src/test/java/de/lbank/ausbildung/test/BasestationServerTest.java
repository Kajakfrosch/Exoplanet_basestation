package de.lbank.ausbildung.test;

import de.lbank.ausbildung.BasestationServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class BasestationServerTest {

    private  BasestationServer fisch;

    @Before
    public void setUp() throws IOException {
        fisch = new BasestationServer();
        fisch.start();


    }

    @After
    public void tearDown() {

        fisch.interrupt();
    }

    @Test
    public void testServerConnection() throws IOException {
        // Verbindung zum Server aufbauen
        Socket clientSocket = new Socket("localhost", 6000);
        BufferedReader t = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter c = new PrintWriter(clientSocket.getOutputStream(),true);
        c.println("orbit|robotername");

        // Überprüfen, ob Verbindung aufgebaut wurde
        Assert.assertTrue(clientSocket.isConnected());

        // Verbindung schließen
        clientSocket.close();
    }
}
