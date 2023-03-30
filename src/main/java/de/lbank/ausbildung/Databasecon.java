package de.lbank.ausbildung;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Databasecon {
    private String database = "exo";
    private String hostadress = "127.0.0.1";
    private String username = "exo";
    private String password = "egal";
    private Connection con;
    private Statement stmt;

    public Databasecon() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://" + hostadress + "/" + database, username, password);
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("Select * from World");
            while (rs.next()) {
                System.out.println(rs.getString("pname") + "|" + rs.getString(3));
            }
        } catch (ClassNotFoundException | SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public String insertdataMessdaten(int posx, int posy, String robotername, double temp, String bodenbeschaffenheit)
            throws SQLException {
        String roboterID = null;
        ResultSet rs = stmt.executeQuery("Select rid from roboter where beschreibung='"+robotername+"'");
        while (rs.next()) {
            try {
                roboterID = rs.getString(1);
                System.out.println(roboterID);
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "Fehler|in beim Speichern der Messdaten";
            }
        }

        String sql = "Insert INTO Measure (rid,groundtype,ptemp,x,y) VALUES('"+roboterID+"','"+bodenbeschaffenheit+"',"+temp+","+posx+","+posy+")";
        try {
            stmt.execute(sql);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "Fehler|in beim Speichern der Messdaten";
        }
        return "1003";

    }
    public String insertWorld(String worldname,int hoehe,int breite) throws SQLException {
        String worldid;
        ResultSet rs;

        rs = stmt.executeQuery("Select wid from world where pname='"+worldname+"'");
        while (rs.next()) {
            try {
                worldid = rs.getString(1);
                System.out.println(worldid);
                if(worldid != null) {
                    return "1001";}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "Fehler in der Verarbeitung von Daten in der Datenbank  "
                        + "beim der Methode initWorld";
            }
        }
        String sql = "Insert INTO world (pname,pwidth,pheight) VALUES('"+worldname+"',"+breite+","+hoehe+")";
        try {
            stmt.execute(sql);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "Fehler in der Verarbeitung von Daten in der Datenbank  "
                    + "beim der Methode initWorld";
        }
        return "1001";

    }
    public String insertRobot(String Robotname) throws SQLException {
        String Robotid = null;
        ResultSet rs;

        rs = stmt.executeQuery("Select rid from roboter where beschreibung='"+Robotname+"'");
        while (rs.next()) {
            try {
                Robotid = rs.getString(1);
                System.out.println(Robotid);
                if(Robotid != null) {
                    return "1001";}
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "Fehler in der Verarbeitung von Daten in der Datenbank  "
                        + "beim der Methode initWorld";
            }
        }
        String sql = "Insert INTO roboter (beschreibung) VALUES('"+Robotname+"')";
        try {
            stmt.execute(sql);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "Fehler in der Verarbeitung von Daten in der Datenbank  "
                    + "beim der Methode initWorld";
        }
        return "1001";

    }
    public String updateKoordinaten(int posx,int posy, String Robotername,String richtung,String lage) throws SQLException {
        String sql = "update Roboter SET direction='"+richtung+"' , x='"+posx+"' , y='"+posy+"' , lage='"+lage+"' where beschreibung='"+Robotername+"'";
        stmt.execute(sql);
        return "1002";
    }
    public String checkKoordinaten(int posx, int posy,String Robotername) {
        ResultSet rs;
        String fieldclear = null;

        try {
            rs = stmt.executeQuery("Select rid From Roboter WHERE beschreibung!='" + Robotername + "' AND x='" + posx + "'AND y='" + posy + "'");
            while (rs.next()) {

                fieldclear = rs.getString(1);
                System.out.println(fieldclear);
                if (fieldclear == null) {
                    System.out.println("Sie können weiterfahren");
                    return "1005";
                } else {
                    System.out.println("der Platz ist bereits belegt");
                    return "1004";
                }


            }
            if (fieldclear == null) {
                System.out.println("Sie können weiterfahren");
                return "1005";
            }else {
                System.out.println("der Platz ist bereits belegt");
                return "1004";
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return null;
    }
}