package Objects;

import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author emman
 */
public class VenueTimePair {

    ObjectId _id;
    String venueName;
    String uniqueId;
    String venueCapacity;
    String isDisabilityAccessible;
    Document period;
    String day;
    boolean reg;
    boolean eve;
    boolean wnd;

   

    public VenueTimePair() {
    }

    public VenueTimePair(ObjectId _id, String venueName, String uniqueId, String venueCapacity, String isDisabilityAccessible, Document period, String day, boolean reg, boolean eve, boolean wnd) {
        this._id = _id;
        this.venueName = venueName;
        this.uniqueId = uniqueId;
        this.venueCapacity = venueCapacity;
        this.isDisabilityAccessible = isDisabilityAccessible;
        this.period = period;
        this.day = day;
        this.reg = reg;
        this.eve = eve;
        this.wnd = wnd;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getVenueCapacity() {
        return venueCapacity;
    }

    public void setVenueCapacity(String venueCapacity) {
        this.venueCapacity = venueCapacity;
    }

    public String getIsDisabilityAccessible() {
        return isDisabilityAccessible;
    }

    public void setIsDisabilityAccessible(String isDisabilityAccessible) {
        this.isDisabilityAccessible = isDisabilityAccessible;
    }

    public Document getPeriod() {
        return period;
    }

    public void setPeriod(Document period) {
        this.period = period;
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

    public Document toDocument() {
        Document doc = new Document();
        doc.append("venueName", venueName);
        doc.append("uniqueId", uniqueId);
        doc.append("venueCapacity", venueCapacity);
        doc.append("isDisabilityAccessible", isDisabilityAccessible);
        doc.append("period", period);
        doc.append("day", day);
        doc.append("reg", reg);
        doc.append("eve", eve);
        doc.append("wnd", wnd);
        return doc;
    }

    public static VenueTimePair fromDocument(Document doc) {
        VenueTimePair vtp = new VenueTimePair();
        vtp.setVenueName(doc.getString("venueName"));
        vtp.setUniqueId(doc.getString("uniqueId"));
        vtp.setVenueCapacity(doc.getString("venueCapacity"));
        vtp.setIsDisabilityAccessible(doc.getString("isDisabilityAccessible"));
        vtp.setPeriod(doc.get("period", Document.class));
        vtp.setDay(doc.getString("day"));
        vtp.setReg(doc.getBoolean("reg"));
        vtp.setEve(doc.getBoolean("eve"));
        vtp.setWnd(doc.getBoolean("wnd"));
        return vtp;
    }

    
}
