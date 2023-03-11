
import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import de.lbank.ausbildung.BasestationSession;
import de.lbank.ausbildung.Databasecon;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BasestationSessionTest {

    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private BasestationSession session;

    @Before
    public void setUp() throws Exception {

        clientSocket = new Socket("localhost", 1222);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        Databasecon data = new Databasecon();
        session = new BasestationSession(clientSocket, data);
        session.start();
    }

    @After
    public void tearDown() throws Exception {
        session.interrupt();
        clientSocket.close();

    }

    @Test
    public void testInitworld() throws IOException {
        out.println("initworld|world1|10|10|robot1");
        out.flush();
        String response = in.readLine();
        assertEquals("robot1", response);
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
        assertTrue(response.startsWith("1 row inserted"));
    }

    @Test
    public void testIsChunkFree() throws IOException {
        out.println("isChunkFree|1|1|robot1");
        out.flush();
        String response = in.readLine();
        assertEquals("OK", response);
    }

    @Test
    public void testCrashedRoboter() throws IOException {
        out.println("crashedRoboter|robot1");
        out.flush();
        String response = in.readLine();
        assertNull(response);
    }
}
