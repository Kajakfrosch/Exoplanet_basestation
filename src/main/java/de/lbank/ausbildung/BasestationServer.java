package de.lbank.ausbildung;

import de.lbank.ausbildung.BasestationSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

public class BasestationServer extends Thread {
    private ServerSocket ssock;

    private Databasecon data;
    private ArrayList<BasestationSession> sessions;

    /**
     * Startet den BasestationServer
     * @throws IOException
     */
    public BasestationServer() throws IOException {
        ssock = new ServerSocket(6000);

        data = new Databasecon();
        sessions = new ArrayList<>();

    }

    @Override
    /**
     * run() wartet auf Verbindungen.
     *Wenn eine Verbindung zustande kommt, wird eine neue BasisstationSession eröffnet und in eine Liste gespeichert.
     */
    public void run() {
        System.out.println("Warten auf Verbindungen...");
        while (!Thread.interrupted()) {
            try {
                Socket s = ssock.accept();
                BasestationSession session = new BasestationSession(s, data);
                System.out.println("Verbindung aufgebaut");
                sessions.add(session);
                session.start();
                System.out.println("Warten auf Verbindungen...");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        stopAction();
    }
    /**
     * Stoppt den Basisstation Server und alle BasisstationSession Thread.
     *
     * @return nichts
     */
    public void stopAction() {
        for (BasestationSession session : sessions) {
            session.interrupt();
            session.stopaction();
        }
        sessions.clear();
        try {
            ssock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}