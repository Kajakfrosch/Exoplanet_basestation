

import de.lbank.ausbildung.BasestationServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import java.io.IOException;
import java.net.Socket;

public class BasestationServerTest {

    private  BasestationServer fisch;

    @Before
    public void setUp()  {
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
        Socket clientSocket = new Socket("localhost", 1222);
        // Überprüfen, ob Verbindung aufgebaut wurde
        Assert.assertTrue(clientSocket.isConnected());
        // Verbindung schließen
        clientSocket.close();
    }
}
