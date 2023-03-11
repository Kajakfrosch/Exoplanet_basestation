package de.lbank.ausbildung;

import de.lbank.ausbildung.BasestationSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

public class BasestationServer extends Thread {
    private BufferedReader in;
    private PrintWriter out;
    private Socket s;
    private RequestListener t;
    private Databasecon data;
    private ArrayList<BasestationSession> a;
    public BasestationServer() {
        startAction();

    };

    public void startAction() {
        t = new RequestListener(1222);
        a = new ArrayList<BasestationSession>();
        t.start();

    }

    public void stopAction() {
        t.interrupt();
        try {
            for (int x=0;x == a.size(); x++) {
                BasestationSession c = a.get(x);
                c.interrupt();

            }
            in.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        in = null;
        out = null;
    };





    class RequestListener extends Thread {

        private ServerSocket ssock;

        private int port;
        private BasestationServer b;

        public RequestListener(int port) {
            this.port = port;
            try {
                ssock = new ServerSocket(port);
                s = ssock.accept();
                in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                out = new PrintWriter(s.getOutputStream());
                data = new Databasecon();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            while (!Thread.interrupted()) {
                try {
                    System.out.println("Warte auf Verbindungen");
                    s = ssock.accept();
                    System.out.println("Verbunden mit:" + s.getLocalAddress());
                    BasestationSession r = new BasestationSession(in,  out,  s,  data);
                    a.add(r);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
}