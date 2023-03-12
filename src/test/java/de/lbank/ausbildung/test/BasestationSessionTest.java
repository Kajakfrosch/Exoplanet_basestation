package de.lbank.ausbildung.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.lbank.ausbildung.BasestationServer;
import de.lbank.ausbildung.BasestationSession;
import de.lbank.ausbildung.Databasecon;

public class BasestationSessionTest {

    private static final int PORT = 1222;
    private static final String HOST = "localhost";

    private BasestationServer server;
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private BasestationSession session;

    @Before
    public void setUp() throws Exception {
        server = new BasestationServer();
        new Thread(server::start).start();
        clientSocket = new Socket(HOST, PORT);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        session = new BasestationSession(clientSocket,new Databasecon());
    }

    @After
    public void tearDown() throws Exception {
        in.close();
        out.close();
        clientSocket.close();
        server.stop();
    }

    @Test
    public void testInitworld() throws IOException {
        out.println("initworld|world1|10|10|robot1");
        out.flush();
        String response = in.readLine();
        assertEquals("1001", response);
    }

    @Test
    public void testUpdateKoordinaten() throws IOException {
        out.println("updateKoordinaten|1|1|robot1|N|OK");
        out.flush();
        String response = in.readLine();
        assertEquals("1 row updated", response);
    }

    @Test
    public void testSaveMessdaten() throws IOException {
        out.println("saveMessdaten|1|1|robot1|25.0|grassy");
        out.flush();
        String response = in.readLine();
        assertTrue(response.startsWith("1003"));
    }
    @Test
    public void testInsertRobot() throws IOException {
        out.println("insertRobot|1|1|robot1");
        out.flush();
        String response = in.readLine();
        assertTrue(response.startsWith("1003"));
    }
    @Test
    public void testIsChunkFree() throws IOException {
        out.println("isChunkFree|1|1|robot1");
        out.flush();
        String response = in.readLine();
        assertEquals("1005", response);
    }

    @Test
    public void testCrashedRoboter() throws IOException {
        out.println("crashedRoboter|robot1");
        out.flush();
        String response = in.readLine();
        assertNull(response);
    }
}
