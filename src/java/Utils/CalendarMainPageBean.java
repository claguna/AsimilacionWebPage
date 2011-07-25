/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;


import java.util.Date;
import java.util.StringTokenizer;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

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
@ManagedBean(name = "calendarMainPageBean")
@ViewScoped
public class CalendarMainPageBean {
 private Date date;
 
 private String getDatesJSONFormat(ArrayList<String> dates){
     String year, month, day, year1, month1, JSON;
     StringTokenizer stok = new StringTokenizer(dates.get(0), "-");
        day = stok.nextToken();
        month = stok.nextToken();
        year = stok.nextToken();
        JSON="{ '"+year+"':{"+"'"+month+"':{";
     for(int i =1; i< dates.size(); i++){
         
        stok = new StringTokenizer(dates.get(i), "-");
        day = stok.nextToken();
        month1 = stok.nextToken();
        year1 = stok.nextToken();
        if(year.compareTo(year1)!=0){
            JSON+="}},  '"+year1+"':{"+"'"+month+"':{";
        }else
            if(month.compareTo(month1)!=0){
                JSON+="}, '"+month1+"':{";
            }
        JSON+="'"+day+"':true,";
        year=year1;
        month=month1;
     }
     JSON+="}}}";
     JSON = JSON.replaceAll("true,}","true}");
     return JSON;
 }
 
 public String getDates() {      
     
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
             return getDatesJSONFormat(dates);
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CalendarMainPageBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(CalendarMainPageBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(CalendarMainPageBean.class.getName()).log(Level.SEVERE, null, ex);
        }  catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
        
    }
       
  
    
    public Date getDate() {
        if(date==null) {
            date = new Date();
        }
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    /** Creates a new instance of CalendarMainPageBean */
    public CalendarMainPageBean() {
    }
}
