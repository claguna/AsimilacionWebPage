/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.util.ArrayList;
import java.io.*;
 import java.util.zip.*;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

/**
 *
 * @author carlos
 */
public abstract class AsimilacionUtils {
    
    private static Boolean compressFiles(ArrayList<String> files, String outputFilename){         
        ServletContext ctx = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String deploymentDirectoryPath = ctx.getRealPath("/");      
        
        if (files.isEmpty()){
            return false;
        }
        
        //We will verify whether all files exist
        File verifier;
        for(int i =0 ; i<files.size();i++){
            verifier = new File(deploymentDirectoryPath+files.get(i));
            if (!verifier.exists()){ //We will remove unexisting files
                files.remove(i);
            }
        }
        
            
        
// Create a buffer for reading the files
        byte[] buf = new byte[1024];
        try {
            // Create the ZIP file            
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(deploymentDirectoryPath+outputFilename));

            // Compress the files
            for (int i = 0; i < files.size(); i++) {
                FileInputStream in = new FileInputStream(deploymentDirectoryPath+files.get(i));

                // Add ZIP entry to output stream.
                out.putNextEntry(new ZipEntry(files.get(i)));

                // Transfer bytes from the file to the ZIP file
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

                // Complete the entry
                out.closeEntry();
                in.close();
            }

            // Complete the ZIP file
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    
    public static String compressAsimilacionTxt(ArrayList<String> textFiles,  String di, String df){
        String result = "Downloadable/"+di+"_"+df+"Data.zip";
        if( compressFiles(textFiles,result ))            
            return result;
        else
            return "";
    }
    
    /**
     * This method will compress all image files and will store them in the server
     * @param imgFiles
     * @return Zip file
     */
    public static String compressAsimilacionImages(ArrayList<String> imgFiles, String di, String df){
        String result = "Downloadable/"+di+"_"+df+"Img.zip";
        if(compressFiles(imgFiles,result )==true)
            return result;
        else
            return "";
    }
    
}
