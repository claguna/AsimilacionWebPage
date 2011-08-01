/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Graphs.StationData;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author carlos
 */
@ManagedBean(name = "displayImagesBean")
@ViewScoped
public class DisplayImagesBean implements Serializable {
    private ArrayList<SelectItem> availableStations;
    private String selectedStation;
    private List<StationData> series;
    public String NotificationMessages="";
     public StreamedContent file;  
    public String zipTxtFileName, zipImgFileName;
    private ArrayList<String> asimImages;

    public String getNotificationMessages() {
        return NotificationMessages;
    }

    public void setNotificationMessages(String NotificationMessages) {
        this.NotificationMessages = NotificationMessages;
    }
        
    public StreamedContent getFile() {  
        return file;  
    }

    public String getZipImgFileName() {
        return zipImgFileName;
    }

    public void setZipImgFileName(String zipImgFileName) {
        this.zipImgFileName = zipImgFileName;
    }

    public String getZipTxtFileName() {
        return zipTxtFileName;
    }

    public void setZipTxtFileName(String zipTxtFileName) {
        this.zipTxtFileName = zipTxtFileName;
    }
  
    public void setFile(StreamedContent file) {  
        this.file = file;  
    }  


    
    private void prepareGraphs(ArrayList<String> errorFileNames) {
        String line="";
        String Name,lon,lat,vReal, vRainrate,vAsim;
        
        for(int i =0; i< errorFileNames.size(); i++){
            try {
                //We will check whether there is information for this hour
                if(errorFileNames.get(i).compareTo("Nodata")!=0){
                    BufferedReader input =  new BufferedReader(new FileReader(new File(errorFileNames.get(i))));
                    //Read Date and Hour
                    line = input.readLine();
                    //Read Headers
                    line = input.readLine();

                    //Read station per station
                     while (( line = input.readLine()) != null){
                         StringTokenizer st = new StringTokenizer(line, ","); 
                         //Nombre
                         Name = st.nextToken();
                         //Longitud
                         lon = st.nextToken();
                         //latitud
                         lat = st.nextToken();
                         //Valor Real
                         vReal = st.nextToken();
                         //Valor Rainrate
                         vRainrate = st.nextToken();
                         //Valor Asimilado
                         vAsim = st.nextToken();
                      }
                }                
            } catch (IOException ex) {
                Logger.getLogger(DisplayImagesBean.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
    
    
    /** Creates a new instance of DisplayImagesBean */
    public DisplayImagesBean() {
       zipTxtFileName =  (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ziptxtFilesName");
       zipImgFileName = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("zipimgFilesName");
       NotificationMessages = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("errorMessages");
       asimImages =(ArrayList<String>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ImgFiles");      
        //FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,null, errorMessages));         
        ArrayList<String>errorFiles= (ArrayList<String>) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("errorFiles");
       // if(errorFiles!=null)
         //   prepareGraphs(errorFiles);
        availableStations = new ArrayList<SelectItem>();
        availableStations.add(new SelectItem("1", "Acala"));
        availableStations.add(new SelectItem("1", "Boqueron"));
        series = new ArrayList<StationData>();
        series.add(new StationData(1.0f, 1.1f, 2.0f, 1));
        series.add(new StationData(2.0f, 2.1f, 3.0f, 2));
        series.add(new StationData(0.0f, 5.1f, 1.0f, 3));       
        
    }

    
    public void updateStationLineChart(){
 
        for (int i =0; i<series.size(); i++){
            series.get(i).setRealvalue( series.get(i).getRealvalue()+1);
        }
    }

    
    
    
    public List<StationData> getSeries() {
        return series;
    }

    public void setSeries(List<StationData> series) {
        this.series = series;
    }

    public ArrayList<SelectItem> getAvailableStations() {
        return availableStations;
    }

    public void setAvailableStations(ArrayList<SelectItem> availableStations) {
        this.availableStations = availableStations;
    }

    public String getSelectedStation() {
        return selectedStation;
    }

    public void setSelectedStation(String selectedStation) {
        this.selectedStation = selectedStation;
    }

    /**
     * @return the asimImages
     */
    public ArrayList<String> getAsimImages() {
        return asimImages;
    }

    /**
     * @param asimImages the asimImages to set
     */
    public void setAsimImages(ArrayList<String> asimImages) {
        this.asimImages = asimImages;
    }
    
}
