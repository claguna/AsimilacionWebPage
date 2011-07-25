/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import Utils.AsimilacionUtils;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

/**
 *
 * @author carlos
 */
public class MainPageALBean {

    public Date startDate, endDate;
    public String startHour = "", endHour = "";
    public MapModel emptyModel;
    public double lat, lon;
    public ArrayList<SelectItem> availableHours;
    public ArrayList<Point2D.Double> polygon;
    public ArrayList<String> cuencas;
    public ArrayList<String> asimilacionfiles;
    public ArrayList<DefaultStreamedContent> images;
    public ArrayList<String> asimImages;
    private static int BUFFER_SIZE = 512;
    public Integer progress = 0;
    private String errorMessages = "Mensajes de notificación <br>\n";

    public void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    private ArrayList<String> getHoursInInterval(Calendar sdate, Calendar edate) {
        ArrayList<String> hours = new ArrayList<String>();
        String month, day;

        while (sdate.before(edate)) {
            if (sdate.get(Calendar.MONTH) < 10) {
                month = "0" + String.valueOf(sdate.get(Calendar.MONTH));
            } else {
                month = String.valueOf(sdate.get(Calendar.MONTH));
            }
            if (sdate.get(Calendar.DAY_OF_MONTH) < 10) {
                day = "0" + String.valueOf(sdate.get(Calendar.DAY_OF_MONTH));
            } else {
                day = String.valueOf(sdate.get(Calendar.DAY_OF_MONTH));
            }
            String fecha = String.valueOf(sdate.get(Calendar.YEAR)) + "-" + month + "-" + day + " "
                    + String.valueOf(sdate.get(Calendar.HOUR_OF_DAY));
            hours.add(fecha);
            sdate.add(Calendar.HOUR_OF_DAY, 1);
        }

        return hours;
    }

    private String Asimilacion(String date, String hour, String imgFileName, String polygonFileName) {
        try {
            ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            String deploymentDirectoryPath = ctx.getRealPath("/");
            String dirExe = deploymentDirectoryPath + "Asimilacion/asimilacion2";
            int returncode = 0;

            String arg = "--a1 --fpolygon " + deploymentDirectoryPath + polygonFileName + "  --date " + date + " --hour " + hour + " --outputdir " + deploymentDirectoryPath;
            String args[] = {"bash", "-c", dirExe + " " + arg};
            Process process = null;

            process = new ProcessBuilder(args).start();
            InputStream isnormal = process.getInputStream();
            InputStreamReader isrnormal = new InputStreamReader(isnormal);
            BufferedReader brnormal = new BufferedReader(isrnormal);

            InputStream is = process.getErrorStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            process.waitFor();
            returncode = process.exitValue();
            //Guardamos en el log los errores               
            String line = "";

            if (returncode == 0) {
                imgFileName = "Data/" + date.replace("-", "_") + "_" + hour.trim() + "/" + date.replace("-", "_") + "_" + hour.trim() + ".eps.jpeg";
            } else {
                imgFileName = "";
            }

            while ((line = brnormal.readLine()) != null) {
                Logger.getLogger(MainPageBean.class.getCanonicalName()).info(line);
            }

            while ((line = br.readLine()) != null) {
                Logger.getLogger(MainPageBean.class.getCanonicalName()).info(line);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(MainPageALBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainPageALBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        return imgFileName;
    }

    public String gmailPolygon() {
        FileWriter outFile = null;
        try {
            String startDate_s, startHour_s, endDate_s, endHour_s;
            Calendar sdate = Calendar.getInstance();
            Calendar edate = Calendar.getInstance();
            ArrayList<String> hours = new ArrayList<String>();
            ArrayList<String> errorAnalysisFiles = new ArrayList<String>();
            Boolean thereAreErrors = false;
            String msg = "";
            ArrayList<String> images = new ArrayList<String>();
            if (startDate != null && endDate != null) {
                msg = "Entrando al action " + startDate.toString() + " " + startHour + " " + endDate.toString() + " " + endHour;
            }
            for (int i = 0; i < polygon.size(); i++) {
                msg += polygon.get(i).toString() + " ";
            }
            Logger.getLogger(MainPageBean.class.getCanonicalName()).info(msg);

            //Write Polygon
            outFile = new FileWriter("/tmp/polygon.txt");
            PrintWriter fpol = new PrintWriter(outFile);
            for (Point2D.Double p : polygon) {
                fpol.println(p.getY() + " " + p.getX());
            }
            fpol.close();

            //Get array of hours
            startDate.setHours(Integer.valueOf(startHour));
            endDate.setHours(Integer.valueOf(endHour));
            sdate.setTime(startDate);
            edate.setTime(endDate);
            //For some reason we need to add 1 month to both dates
            sdate.add(Calendar.MONTH, 1);
            edate.add(Calendar.MONTH, 1);
            hours = getHoursInInterval(sdate, edate);
            // For each hour Run Asimilacion
            String imgAsimilacion = "";
            String fileAsimilacion = "";
            String fileEst = "";
            String errorFile = "";
            String tmp_Date, tmp_Hour;
            progress = 5;
            errorMessages = "";
            for (String h : hours) {
                tmp_Date = h.substring(0, 10);
                tmp_Hour = h.substring(10, h.length());
                imgAsimilacion = Asimilacion(tmp_Date, tmp_Hour, h + ".jpeg", "/tmp/polygon.txt");
                progress += 100 / hours.size();
                errorFile = "Nodata";
                if (imgAsimilacion.compareTo("") != 0) {
                    tmp_Hour = tmp_Hour.trim();
                    fileAsimilacion = "Data/" + tmp_Date.replace("-", "_") + "_" + tmp_Hour + "/" + tmp_Date + "_" + tmp_Hour + ".a1.debug";
                    fileEst = "Data/" + tmp_Date.replace("-", "_") + "_" + tmp_Hour + "/" + tmp_Date + tmp_Hour + "_0.hr.rrtmp.est";
                    errorFile = "Data/" + tmp_Date.replace("-", "_") + "_" + tmp_Hour + "/" + tmp_Date + "_" + tmp_Hour + ".errorAnalysis.csv";
                    asimImages.add(imgAsimilacion);
                    asimilacionfiles.add(fileAsimilacion);
                    asimilacionfiles.add(fileEst);
                    asimilacionfiles.add(errorFile);

                } else {
                    errorMessages += "No hay datos para la hora " + h + "\n </br>";
                    thereAreErrors = true;
                }
                errorAnalysisFiles.add(errorFile);
            }

            //Save error messages
            if (thereAreErrors == false) {
                errorMessages += "No hubo ningún error <br>\n";
            }
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, errorMessages, null);
            FacesContext.getCurrentInstance().addMessage(null, message);
            //Clean Previous info
            if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("ziptxtFilesName")) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("ziptxtFilesName");
            }

            if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("zipimgFilesName")) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("zipimgFilesName");
            }
            if (asimilacionfiles.size() > 0) {
                //Save files into Session
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("txtFiles", asimilacionfiles);
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("ImgFiles", asimImages);
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("errorFiles", errorAnalysisFiles);
                //Compress files
                startDate_s = hours.get(0).substring(0, 10);
                startHour_s = hours.get(0).substring(10, hours.get(0).length()).trim();
                endDate_s = hours.get(hours.size() - 1).substring(0, 10);
                endHour_s = hours.get(hours.size() - 1).substring(10, hours.get(hours.size() - 1).length()).trim();
                String zipimg = AsimilacionUtils.compressAsimilacionImages(asimImages, startDate_s.replace("-", "_") + "_" + startHour_s, endDate_s.replace("-", "_") + "_" + endHour_s);
                String ziptxt = AsimilacionUtils.compressAsimilacionTxt(asimilacionfiles, startDate_s.replace("-", "_") + "_" + startHour_s, endDate_s.replace("-", "_") + "_" + endHour_s);

                //Save Compressed files into session             


                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("ziptxtFilesName", ziptxt);
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("zipimgFilesName", zipimg);
            }

            return "displayImages";
        } catch (IOException ex) {
            Logger.getLogger(MainPageALBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {

            try {
                outFile.close();
            } catch (IOException ex) {
                Logger.getLogger(MainPageALBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return "tmp";
    }

    public void addMarker(ActionEvent actionEvent) {
        Marker marker = new Marker(new LatLng(lat, lon), "");
        emptyModel.addOverlay(marker);
        Logger.getLogger(MainPageBean.class.getCanonicalName()).info(" punto " + lat + " " + lon);
        addMessage(new FacesMessage(FacesMessage.SEVERITY_INFO, "Punto añadido en ", "Lat:" + lat + ", Lng:" + lon));
        polygon.add(new Point2D.Double(lat, lon));
    }

    public void handleFileUpload(FileUploadEvent event) {
        //     logger.info("Uploaded: {}", event.getFile().getFileName());  

        FacesMessage msg = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
        FacesContext.getCurrentInstance().addMessage(null, msg);


        File result = new File("/tmp/" + event.getFile().getFileName());

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(result);

            byte[] buffer = new byte[BUFFER_SIZE];

            int bulk;
            InputStream inputStream = event.getFile().getInputstream();
            while (true) {
                bulk = inputStream.read(buffer);
                if (bulk < 0) {
                    break;
                }
                fileOutputStream.write(buffer, 0, bulk);
                fileOutputStream.flush();
            }

            fileOutputStream.close();
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
            FacesMessage error = new FacesMessage("The files were not uploaded!");
            FacesContext.getCurrentInstance().addMessage(null, error);
        }
    }

    public String allAvailableWindow() {
        String startDate_s, startHour_s, endDate_s, endHour_s;
        Calendar sdate = Calendar.getInstance();
        Calendar edate = Calendar.getInstance();
        ArrayList<String> hours = new ArrayList<String>();
        ArrayList<String> errorAnalysisFiles = new ArrayList<String>();
        Boolean thereAreErrors = false;

        //Get array of hours
        startDate.setHours(Integer.valueOf(startHour));
        endDate.setHours(Integer.valueOf(endHour));
        sdate.setTime(startDate);
        edate.setTime(endDate);

        //For some reason we need to add 1 month to both dates
        sdate.add(Calendar.MONTH, 1);
        edate.add(Calendar.MONTH, 1);
        hours = getHoursInInterval(sdate, edate);

        // For each hour Run Asimilacion
        String imgAsimilacion = "";
        String fileAsimilacion = "";
        String fileEst = "";
        String errorFile = "";

        asimImages.clear();
        asimilacionfiles.clear();
        String tmp_Date, tmp_Hour;
        progress = 5;
        errorMessages = "";
        for (String h : hours) {
            tmp_Date = h.substring(0, 10);
            tmp_Hour = h.substring(10, h.length());
            imgAsimilacion = Asimilacion(tmp_Date, tmp_Hour, h + ".jpeg", "Asimilacion/AsimilacionConf/allWindowPolygon");
            progress += 100 / hours.size();
            errorFile = "Nodata";
            if (imgAsimilacion.compareTo("") != 0) {
                tmp_Hour = tmp_Hour.trim();
                fileAsimilacion = "Data/" + tmp_Date.replace("-", "_") + "_" + tmp_Hour + "/" + tmp_Date + "_" + tmp_Hour + ".a1.debug";
                fileEst = "Data/" + tmp_Date.replace("-", "_") + "_" + tmp_Hour + "/" + tmp_Date + tmp_Hour + "_0.hr.rrtmp.est";
                errorFile = "Data/" + tmp_Date.replace("-", "_") + "_" + tmp_Hour + "/" + tmp_Date + "_" + tmp_Hour + ".errorAnalysis.csv";
                asimImages.add(imgAsimilacion);
                asimilacionfiles.add(fileAsimilacion);
                asimilacionfiles.add(fileEst);
                asimilacionfiles.add(errorFile);

            } else {
                errorMessages += "No hay datos para la hora " + h + "\n </br>";
                thereAreErrors = true;
            }
            errorAnalysisFiles.add(errorFile);

        }
        //Save error messages
        if (thereAreErrors == false) {
            errorMessages += "No hubo ningún error <br>\n";
        }
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, errorMessages, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
        //Clean Previous info
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("ziptxtFilesName")) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("ziptxtFilesName");
        }

        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("zipimgFilesName")) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("zipimgFilesName");
        }
        if (asimilacionfiles.size() > 0) {
            //Save files into Session
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("txtFiles", asimilacionfiles);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("ImgFiles", asimImages);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("errorFiles", errorAnalysisFiles);
            //Compress files
            startDate_s = hours.get(0).substring(0, 10);
            startHour_s = hours.get(0).substring(10, hours.get(0).length()).trim();
            endDate_s = hours.get(hours.size() - 1).substring(0, 10);
            endHour_s = hours.get(hours.size() - 1).substring(10, hours.get(hours.size() - 1).length()).trim();
            String zipimg = AsimilacionUtils.compressAsimilacionImages(asimImages, startDate_s.replace("-", "_") + "_" + startHour_s, endDate_s.replace("-", "_") + "_" + endHour_s);
            String ziptxt = AsimilacionUtils.compressAsimilacionTxt(asimilacionfiles, startDate_s.replace("-", "_") + "_" + startHour_s, endDate_s.replace("-", "_") + "_" + endHour_s);

            //Save Compressed files into session             


            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("ziptxtFilesName", ziptxt);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("zipimgFilesName", zipimg);
        }

        return "displayImages";
    }

    public String nextWindow() {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, errorMessages, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
        return "displayImages";
    }
}
