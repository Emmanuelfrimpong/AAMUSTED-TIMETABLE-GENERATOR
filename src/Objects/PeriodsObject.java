/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import org.bson.Document;

/**
 *
 * @author emman
 */
public class PeriodsObject {

    String period;
    String startTime;
    String endTime;

    public PeriodsObject() {
    }

    public PeriodsObject(String period, String startTime, String endTime) {
        this.period = period;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public ObservableMap<String, Object> getPeriodsObjectMap() {
        ObservableMap<String, Object> periodsObjectMap = FXCollections.observableHashMap();
        periodsObjectMap.put("period", period);
        periodsObjectMap.put("startTime", startTime);
        periodsObjectMap.put("endTime", endTime);
        return periodsObjectMap;
    }

    public static PeriodsObject getPeriodsObjectFromMap(ObservableMap<String, Object> map) {
        PeriodsObject periodsObject = new PeriodsObject((String) map.get("period"), (String) map.get("startTime"), (String) map.get("endTime"));
        return periodsObject;
    }

    public static PeriodsObject getSpecialVenueFromDoc(Document ob) {
        return new PeriodsObject(ob.getString("period"), ob.getString("startTime"), ob.getString("endTime"));
    }

    public Document ToDocument() {
        Document doc = new Document();
        doc.append("period", period);
        doc.append("startTime", startTime);
        doc.append("endTime", endTime);
        return doc;
    }

}
