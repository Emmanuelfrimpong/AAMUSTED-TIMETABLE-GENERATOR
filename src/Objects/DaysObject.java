
package Objects;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import org.bson.Document;

/**
 *
 * @author emman
 */
public class DaysObject {

    String day;
    boolean reg;
    boolean eve;
    boolean wnd;

    public DaysObject(String day, boolean reg, boolean eve, boolean wnd) {
        this.day = day;
        this.reg = reg;
        this.eve = eve;
        this.wnd = wnd;
    }
public DaysObject() {
       
    }


    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public boolean isReg() {
        return reg;
    }

    public void setReg(boolean reg) {
        this.reg = reg;
    }

    public boolean isEve() {
        return eve;
    }

    public void setEve(boolean eve) {
        this.eve = eve;
    }

    public boolean isWnd() {
        return wnd;
    }

    public void setWnd(boolean wnd) {
        this.wnd = wnd;
    }

    public static DaysObject fromMap(ObservableMap<String,Object> map){
        return new DaysObject((String)map.get("day"),(boolean)map.get("reg"),(boolean)map.get("eve"),(boolean)map.get("wnd"));
    }

    public  ObservableMap<String,Object> toMap(){
        ObservableMap<String,Object> map = FXCollections.observableHashMap();
        map.put("day", this.getDay());
        map.put("reg", this.isReg());
        map.put("eve", this.isEve());
        map.put("wnd", this.isWnd());
        return map;
    }


public static DaysObject fromDocument(Document doc){
        return new DaysObject((String)doc.get("day"),(boolean)doc.get("reg"),(boolean)doc.get("eve"),(boolean)doc.get("wnd"));
    }

    public Document toDocument(){
        Document doc = new Document();
        doc.append("day", day);
        doc.append("reg", reg);
        doc.append("eve", eve);
        doc.append("wnd", wnd);
        return doc;
    }


    



}
