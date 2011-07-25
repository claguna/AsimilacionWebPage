/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;



import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;


/**
 *
 * @author carlos
 */
@ManagedBean(name = "downloadDataBean")
@ViewScoped
public class DownloadDataBean extends DownloadDataALBean implements Serializable{

   
    private String line;
     
    @PostConstruct
public void postConstruct() {  
                ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
                String deploymentDirectoryPath = ctx.getRealPath("/");                     
}
    
    /** Creates a new instance of DownloadData */
    public DownloadDataBean() {
      zipTxtFileName =  (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ziptxtFilesName");
      zipImgFileName = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("zipimgFilesName");
      
      
    }
}
