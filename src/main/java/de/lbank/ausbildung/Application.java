package de.lbank.ausbildung;

import java.io.IOException;
import java.sql.SQLException;

public class Application {
    public static void main(String[] args) throws SQLException, IOException {
        // TODO Auto-generated method stub
        BasestationServer fisch = new BasestationServer();
        fisch.start();


    }

}
