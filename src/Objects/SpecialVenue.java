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
public class SpecialVenue {

    
    String course;
    String venue;
    public SpecialVenue(String course, String venue) {
        this.course = course;
        this.venue = venue;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }
    

public ObservableMap<String, Object> getSpecialVenueMap() {
        ObservableMap<String, Object> specialVenueMap = FXCollections.observableHashMap();
        specialVenueMap.put("course", course);
        specialVenueMap.put("venue", venue);
        return specialVenueMap;
    }
public static SpecialVenue getSpecialVenueFromDoc(Document ob) {
       return new SpecialVenue(ob.getString("course"), (String) ob.getString("venue"));
    }

    public static SpecialVenue getSpecialVenueFromMap(ObservableMap<String, Object> map) {
        SpecialVenue specialVenue = new SpecialVenue((String) map.get("course"), (String) map.get("venue"));
        return specialVenue;
    }

    
    
    
}
