/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Admin;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author carlos
 */
@ManagedBean
@ViewScoped
public class administratorBean {

    /** Creates a new instance of administratorBean */
    public administratorBean() {
    }
    public void handleFilesUpload(FileUploadEvent event) {  
        FacesMessage msg = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");  
        FacesContext.getCurrentInstance().addMessage(null, msg);  
    }  
}
