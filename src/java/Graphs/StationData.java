/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Graphs;

import java.io.Serializable;

/**
 *
 * @author carlos
 */
public class StationData implements Serializable{
    
private float  realvalue, asimilacionValue,rainrateValue;
private int hour;
private String stationName;

public StationData(float realValue, float asimilacionValue, float rainrateValue, int hour) {
    this.realvalue= realValue;
    this.asimilacionValue = asimilacionValue;
    this.rainrateValue=rainrateValue;
    this.hour= hour;
}

    public float getAsimilacionValue() {
        return asimilacionValue;
    }

    public void setAsimilacionValue(float asimilacionValue) {
        this.asimilacionValue = asimilacionValue;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public float getRainrateValue() {
        return rainrateValue;
    }

    public void setRainrateValue(float rainrateValue) {
        this.rainrateValue = rainrateValue;
    }

    public float getRealvalue() {
        return realvalue;
    }

    public void setRealvalue(float realvalue) {
        this.realvalue = realvalue;
    }


}
