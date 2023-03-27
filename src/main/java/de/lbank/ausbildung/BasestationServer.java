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

    public BasestationServer() throws IOException {
        ssock = new ServerSocket(1222);

        data = new Databasecon();
        sessions = new ArrayList<>();

    }

    @Override
    public void run() {
        System.out.println("Waiting for connections...");
        while (!Thread.interrupted()) {
            try {
                Socket s = ssock.accept();
                BasestationSession session = new BasestationSession(s, data);
                System.out.println("find Connection");
                sessions.add(session);
                session.start();
                System.out.println("Waiting for connections...");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        stopAction();
    }

    public void stopAction() {
        for (BasestationSession session : sessions) {
            session.interrupt();
        }
        sessions.clear();
        try {
            ssock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}