/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin;


import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.context.RequestContext;

/**
 *
 * @author carlos
 */
@ManagedBean(name = "loginBean")
@RequestScoped
public class loginBean {
     private String username;        
    private String password;  
    boolean loggedIn = false;  
    /** Creates a new instance of loginBean */
    public loginBean() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String redirectLogin(){
        if(loggedIn){
             FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("admLogged","true");
            return "adminMainPage?faces-redirect=true";
        }
        else 
            return "";
    }
     public void login(ActionEvent actionEvent) { 
         RequestContext context = RequestContext.getCurrentInstance();  
        FacesMessage msg = null;  
        
          
        if(username != null  && username.equals("admin") && password != null  && password.equals("admin")) {  
            loggedIn = true;  
            msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Bienvenido", username);              
        } else {  
            loggedIn = false;  
            msg = new FacesMessage(FacesMessage.SEVERITY_WARN, "Acceso no permitido", "Sus credenciales no son válidas");  
        }  
          
        FacesContext.getCurrentInstance().addMessage(null, msg);  
        
      //  context.addCallbackParam("loggedIn", loggedIn);  
         
     }
}
