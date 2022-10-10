
package Objects;

import java.util.ArrayList;
import java.util.List;
import javafx.collections.ObservableMap;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author emman
 */
public class Configuration {

    ObjectId _id;
    List<ObservableMap<String, Object>> days;
    List<ObservableMap<String, Object>> periods;
    List<ObservableMap<String, Object>> specialVenue;
    String laDay;
    String laPeriod;

    public Configuration() {
        this.days = new ArrayList<>();
        this.periods = new ArrayList<>();
        this.specialVenue = new ArrayList<>();
    }

    public Configuration(ObjectId _id, List<ObservableMap<String, Object>> days, List<ObservableMap<String, Object>> periods, List<ObservableMap<String, Object>> specialVenue, String laDay, String laPeriod) {
        this._id = _id;
        this.days = days;
        this.periods = periods;
        this.specialVenue = specialVenue;
        this.laDay = laDay;
        this.laPeriod = laPeriod;
    }

    public List<ObservableMap<String, Object>> getDays() {
        return days;
    }

    public void setDays(List<ObservableMap<String, Object>> days) {
        this.days = days;
    }


   

    public List<ObservableMap<String, Object>> getPeriods() {
        return periods;
    }

    public void setPeriods(List<ObservableMap<String, Object>> periods) {
        this.periods = periods;
    }

    public List<ObservableMap<String, Object>> getSpecialVenue() {
        return specialVenue;
    }

    public void setSpecialVenue(List<ObservableMap<String, Object>> specialVenue) {
        this.specialVenue = specialVenue;
    }

    public String getLaDay() {
        return laDay;
    }

    public void setLaDay(String laDay) {
        this.laDay = laDay;
    }

    public String getLaPeriod() {
        return laPeriod;
    }

    public void setLaPeriod(String laPeriod) {
        this.laPeriod = laPeriod;
    }

    public ObjectId getId() {
        return _id;
    }

    public void setId(ObjectId _id) {
        this._id = _id;
    }

    public Document toDocument() {
        Document doc = new Document();
        doc.append("days", days);
        doc.append("periods", periods);
        doc.append("specialVenue", specialVenue);
        doc.append("laDay", laDay);
        doc.append("laPeriod", laPeriod);
        doc.append("_id", _id);
        return doc;
    }

    public Configuration fromDocument(Document doc) {
        Configuration config = new Configuration();
        config.setDays((List<ObservableMap<String, Object>>) doc.get("days"));
        config.setPeriods((List<ObservableMap<String, Object>>) doc.get("periods"));
        config.setLaDay(doc.getString("laDay"));
        config.setLaPeriod(doc.getString("laPeriod"));
        config.setSpecialVenue((List<ObservableMap<String, Object>>) doc.get("specialVenue"));
        config.setId((ObjectId) doc.get("_id"));
        return config;
    }

}
