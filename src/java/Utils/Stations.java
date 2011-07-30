/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

/**
 *
 * @author carlos
 */
public class Stations {
    private String name;
    private String coordinates;
    private String x;
    private String y;

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }
    
    public Stations(String name, String x, String y){
        this.name = name;
        this.x= x;
        this.y = y;
    }
    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
    
}
