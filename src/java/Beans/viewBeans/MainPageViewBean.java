/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans.viewBeans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author carlos
 */
@ManagedBean(name = "mainPageViewBean")
@ViewScoped
public class MainPageViewBean {
    String allWindowButton;
    String gmailPolygonButton;
    String userDefinedButton;
    

    /** Creates a new instance of MainPageViewBean */
    public MainPageViewBean() {
        allWindowButton = "false";
        gmailPolygonButton = "true";
        userDefinedButton = "true";
    }

    public String getAllWindowButton() {
        return allWindowButton;
    }

    public void setAllWindowButton(String allWindowButton) {
        this.allWindowButton = allWindowButton;
    }

    public String getGmailPolygonButton() {
        return gmailPolygonButton;
    }

    public void setGmailPolygonButton(String gmailPolygonButton) {
        this.gmailPolygonButton = gmailPolygonButton;
    }

    public String getUserDefinedButton() {
        return userDefinedButton;
    }

    public void setUserDefinedButton(String userDefinedButton) {
        this.userDefinedButton = userDefinedButton;
    }
    
    
}
