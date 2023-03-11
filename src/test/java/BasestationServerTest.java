

import de.lbank.ausbildung.BasestationServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import java.io.IOException;
import java.net.Socket;

public class BasestationServerTest {

    private BasestationServer server;
    private Thread serverThread;

    @Before
    public void setUp() {
        server = new BasestationServer();
        serverThread = new Thread(server::start);
        serverThread.start();
    }

    @After
    public void tearDown() {
        server.stopAction();
        serverThread.interrupt();
    }

    @Test
    public void testServerConnection() throws IOException {
        // Verbindung zum Server aufbauen
        Socket clientSocket = new Socket("localhost", 1222);
        // Überprüfen, ob Verbindung aufgebaut wurde
        Assert.assertTrue(clientSocket.isConnected());
        // Verbindung schließen
        clientSocket.close();
    }
}
