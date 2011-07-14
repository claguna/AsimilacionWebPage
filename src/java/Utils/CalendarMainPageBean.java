/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import DataBase.MainPageDB;
import java.util.ArrayList;
import java.util.Date;
import java.util.StringTokenizer;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;


/**
 *
 * @author carlos
 */
@ManagedBean
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
        return getDatesJSONFormat(MainPageDB.getAvailableDates());
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
