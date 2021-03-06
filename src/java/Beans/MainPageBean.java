/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Utils.MainPageUtils;
import Utils.Stations;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import org.primefaces.event.DateSelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Polygon;

/**
 *
 * @author carlos
 */
@ManagedBean(name = "mainPageBean")
@ViewScoped
public class MainPageBean extends MainPageALBean implements Serializable {

    public boolean getCanWeGoToNextPage() {
        return canWeGoToNextPage;
    }

    public void setCanWeGoToNextPage(boolean canWeGoToNextPage) {
        this.canWeGoToNextPage = canWeGoToNextPage;
    }

    public Integer getProgress() {
          if(progress == 0)  
            progress = 0;          
          if(progress>100) //we must  set progress to 100 otherwise, the event "oncomplete" of the progress bar will not be invoked
              return 100;
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }
    
    
    
    public ArrayList<String> getAsimilacionfiles() {
        return asimilacionfiles;
    }

    public void setAsimilacionfiles(ArrayList<String> asimilacionfiles) {
        this.asimilacionfiles = asimilacionfiles;
    }

    public ArrayList<String> getAsimImages() {
        return asimImages;
    }

    public void setAsimImages(ArrayList<String> asimImages) {
        this.asimImages = asimImages;
    }
    

    public void handleAgendaDateChange(DateSelectEvent event){
         Logger.getLogger(MainPageBean.class.getCanonicalName()).info(event.getDate().toString());
         System.err.println("handleAgendaDateChange = " + event.getDate());
    }
    
    
    public ArrayList<SelectItem> getAvailableHours() {
        return availableHours;
    }

    public void setAvailableHours(ArrayList<SelectItem> availableHours) {
        this.availableHours = availableHours;
    }
   

    public MapModel getEmptyModel() {        
        return emptyModel;
    }

    public void setEmptyModel(MapModel emptyModel) {
        this.emptyModel = emptyModel;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }    

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getEndHour() {
        return endHour;
    }

    public void setEndHour(String endHour) {
        this.endHour = endHour;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getStartHour() {
        return startHour;
    }

    public ArrayList<String> getCuencas() {
        return cuencas;
    }

    public void setCuencas(ArrayList<String> cuencas) {
        this.cuencas = cuencas;
    }

    public void setStartHour(String startHour) {
        this.startHour = startHour;
    }

    public ArrayList<DefaultStreamedContent> getImages() {
        return images;
    }

    public void setImages(ArrayList<DefaultStreamedContent> images) {
        this.images = images;
    }

    public boolean isEnabledComputeButton() {
        return enabledComputeButton;
    }

    public void setEnabledComputeButton(boolean enabledComputeButton) {
        this.enabledComputeButton = enabledComputeButton;
    }
    
    
    
    /** Creates a new instance of MainPageBean */
    public MainPageBean() {
        polygon = new ArrayList<java.awt.geom.Point2D.Double>();
        cuencas = new ArrayList<String>();
        images = new ArrayList<DefaultStreamedContent>();
        asimImages = new ArrayList<String> ();
        asimilacionfiles = new ArrayList<String> ();
       //images.add("algo.jpg");
//        images.add("tree-frog.jpg");
        
        emptyModel = new DefaultMapModel();      
        cuencas.add("Chicoasen");
        progress=0;
        availableHours = new ArrayList<SelectItem>();
        availableHours.add(new SelectItem("1", "1:00"));
        availableHours.add(new SelectItem("2", "2:00"));
        availableHours.add(new SelectItem("3", "3:00"));
        availableHours.add(new SelectItem("4", "4:00"));
        availableHours.add(new SelectItem("5", "5:00"));
        availableHours.add(new SelectItem("6", "6:00"));
        availableHours.add(new SelectItem("7", "7:00"));
        availableHours.add(new SelectItem("8", "8:00"));
        availableHours.add(new SelectItem("9", "9:00"));
        availableHours.add(new SelectItem("10", "10:00"));
        availableHours.add(new SelectItem("11", "11:00"));
        availableHours.add(new SelectItem("12", "12:00"));
        
        availableHours.add(new SelectItem("13", "13:00"));
        availableHours.add(new SelectItem("14", "14:00"));
        availableHours.add(new SelectItem("15", "15:00"));
        availableHours.add(new SelectItem("16", "16:00"));
        availableHours.add(new SelectItem("17", "17:00"));
        availableHours.add(new SelectItem("18", "18:00"));
        availableHours.add(new SelectItem("19", "19:00"));
        availableHours.add(new SelectItem("20", "20:00"));
        availableHours.add(new SelectItem("21", "21:00"));
        availableHours.add(new SelectItem("22", "22:00"));
        availableHours.add(new SelectItem("23", "23:00"));
        
         //Cities  
        List<String> cuencasSource = new ArrayList<String>();  
        List<String> cuencasTarget = new ArrayList<String>();  
        List<Stations> stations = MainPageUtils.getStations();
         cuencasSource = MainPageUtils.getCuencas();            
        cuencaslist = new DualListModel<String>(cuencasSource, cuencasTarget); 
         //LatLng coord= new LatLng(16.11542, -95.122893);
         //emptyModel.addOverlay(new Marker(coord, "Konyaalti"));
        for (int i = 0 ; i< stations.size(); i++){
            LatLng coord = new LatLng(Double.valueOf(stations.get(i).getY()), Double.valueOf(stations.get(i).getX()));  
            emptyModel.addOverlay(new Marker(coord , stations.get(i).getName(),null, "img/station.png"));  
        }
//        theWholeWindowGmap = new DefaultMapModel();    
//          //Shared coordinates  
//        LatLng coord1 = new LatLng(13, -98.971832);  
//        LatLng coord2 = new LatLng(13, -91.30986 );  
//        LatLng coord4 = new LatLng(18.92,-98.971832);
//        LatLng coord3 = new LatLng(18.92,-91.30986 );
//  
//        //Polygon  
//        Polygon polygon = new Polygon();  
//        polygon.getPaths().add(coord1);  
//        polygon.getPaths().add(coord2);  
//        polygon.getPaths().add(coord3);  
//        polygon.getPaths().add(coord4);  
//  
//        polygon.setStrokeColor("#FF9900");  
//        polygon.setFillColor("#FF9900");  
//        polygon.setStrokeOpacity(0.7);  
//        polygon.setFillOpacity(0.7);  
//          
        //theWholeWindowGmap.addOverlay(polygon); 
         //emptyModel.addOverlay(polygon); 
    }

    public MapModel getTheWholeWindowGmap() {
        return theWholeWindowGmap;
    }

    public void setTheWholeWindowGmap(MapModel theWholeWindowGmap) {
        this.theWholeWindowGmap = theWholeWindowGmap;
    }
    
       private String nombre;
    private String mensaje;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getMensaje() {
        return mensaje;
    }

    public String saluda(ActionEvent event) {
        Logger.getLogger(MainPageBean.class.getCanonicalName()).info("Entrando al action");
        if(nombre == null || nombre.isEmpty()) {
            mensaje = "Debes ingresar tu nombre";
            return "tmp";
        } else {
            mensaje = "Hola " + nombre;
            return "index";
        }
    }
        public String saluda() {
        Logger.getLogger(MainPageBean.class.getCanonicalName()).info("Entrando al action "+startDate.toString()+" "+endDate.toString());
        if(nombre == null || nombre.isEmpty()) {
            mensaje = "Debes ingresar tu nombre";          
            return "tmp";
        } else {
            mensaje = "Hola " + nombre;
            return "index";
        }
    }
 
}
