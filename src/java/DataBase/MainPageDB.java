/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DataBase;

import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author carlos
 */
public abstract class MainPageDB {
    public static ArrayList <String> getAvailableDates(){        
        
        ArrayList <String> dates = new ArrayList<String>();
        Connection con = null;
        String url = "jdbc:mysql://132.248.17.180:3306/Asimilacion1";
        String user = "root";
        String password = "laila.mysql";

        try {            
             Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection(url, user, password);
            Statement st = con.createStatement();
            ResultSet result = st.executeQuery("SELECT DISTINCT( DATE_FORMAT(fecha, '%e-%c-%Y')) FROM Interpoltxt order by fecha");
            
            while (result.next()) {
                dates.add(result.getString(1));
            }
            
            con.close();
            return  dates;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainPageDB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(MainPageDB.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MainPageDB.class.getName()).log(Level.SEVERE, null, ex);
        }  catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
        
    }
}
