
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
    boolean reg;
    boolean eve;
    boolean wnd;

    public PeriodsObject() {
    }

    public PeriodsObject(String period, String startTime, String endTime, boolean reg, boolean eve, boolean wnd) {
        this.period = period;
        this.startTime = startTime;
        this.endTime = endTime;
        this.reg = reg;
        this.eve = eve;
        this.wnd = wnd;
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


public static PeriodsObject fromMap(ObservableMap<String,Object>map){
        PeriodsObject p = new PeriodsObject();
        p.setPeriod((String)map.get("period"));
        p.setStartTime((String)map.get("startTime"));
        p.setEndTime((String)map.get("endTime"));
        p.setReg((boolean)map.get("reg"));
        p.setEve((boolean)map.get("eve"));
        p.setWnd((boolean)map.get("wnd"));
        return p;
    }
    
    public static PeriodsObject fromDocument(Document doc){
        PeriodsObject p = new PeriodsObject();
        p.setPeriod((String)doc.get("period"));
        p.setStartTime((String)doc.get("startTime"));
        p.setEndTime((String)doc.get("endTime"));
        p.setReg((boolean)doc.get("reg"));
        p.setEve((boolean)doc.get("eve"));
        p.setWnd((boolean)doc.get("wnd"));
        return p;
    }
    
    public Document toDocument(){
        Document doc = new Document();
        doc.append("period", period);
        doc.append("startTime", startTime);
        doc.append("endTime", endTime);
        doc.append("reg", reg);
        doc.append("eve", eve);
        doc.append("wnd", wnd);
        return doc;
    }
    
    public ObservableMap<String,Object> toMap(){
        ObservableMap<String,Object> map = FXCollections.observableHashMap();
        map.put("period", period);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        map.put("reg", reg);
        map.put("eve", eve);
        map.put("wnd", wnd);
        return map;
    }
}
