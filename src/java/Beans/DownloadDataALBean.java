/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Beans;

import org.primefaces.model.StreamedContent;  

/**
 *
 * @author carlos
 */
public class DownloadDataALBean {
    public StreamedContent file;  
    public String zipTxtFileName, zipImgFileName;

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
}
