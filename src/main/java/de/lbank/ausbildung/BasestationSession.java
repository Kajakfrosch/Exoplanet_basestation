package de.lbank.ausbildung;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;

public class BasestationSession extends Thread{
    private BufferedReader in;
    private PrintWriter out;
    private Socket s;
    private BasestationSession t;
    private Databasecon data;

    public BasestationSession( Socket s,  Databasecon data) throws IOException {

        this.s = s;
        this.data = data;
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        out = new PrintWriter(s.getOutputStream());
       t.start();
    }

    public boolean initworld(String worldname, int hoehe, int breite, String robotnername) {
        try {

            data.insertWorld(worldname, hoehe, breite);
            String roboter = data.insertRobot(robotnername);
            sendtext(roboter);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean updateKoordinaten(int posx, int posy, String robotername, String richtung, String status) {
        try {
            String update = data.updateKoordinaten(posx, posy, robotername, richtung, status);
            sendtext(update);
            return true;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
    }

    public boolean saveMessdaten(int posx, int posy, String robotername, double temp, String bodenbeschaffenheit) {
        try {
            String saveMessdaten = data.insertdataMessdaten(posx, posy, robotername, temp, bodenbeschaffenheit);
            return true;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

    }

    public boolean isChunkFree(int posx, int posy, String robotername) {
        String isChunk = data.checkKoordinaten(posx, posy, robotername);
        sendtext(isChunk);
        return true;
    }

    public boolean crashedRoboter(String robotername) {
        try {
            data.updateKoordinaten(-1, -1, robotername, "F", "UNKOWN");
            return true;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public void run() {
        while(!Thread.interrupted()) {
            try {
                String read = in.readLine();

                String[] token = read.trim().split("\\|");
                switch(token[0]) {
                    case "initworld":
                        initworld(token[1],Integer.parseInt(token[2]), Integer.parseInt(token[3]), token[4]);
                        break;
                    case "updateKoordinaten":
                        updateKoordinaten(Integer.parseInt(token[1]), Integer.parseInt(token[2]), token[3], token[4], token[5]);
                        break;
                    case "saveMessdaten":
                        saveMessdaten(Integer.parseInt(token[1]), Integer.parseInt(token[2]), token[3], Double.parseDouble(token[4]), token[5]);
                        break;
                    case "isChunkFree":
                        isChunkFree(Integer.parseInt(token[1]), Integer.parseInt(token[2]), token[3]);
                        break;
                    case "crashedRoboter":
                        crashedRoboter(token[1]);
                        System.out.println("INFO:"+token[1]+" ist nicht mehr auffindbar");
                        break;
                };




            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }}

    }
    public void sendtext(String text) {
        out.println(text);
        out.flush();
    }
}
