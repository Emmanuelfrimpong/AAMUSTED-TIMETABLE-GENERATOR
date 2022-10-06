
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

    public VenueTimePair(ObjectId _id, String venueName, String uniqueId, String venueCapacity, String isDisabilityAccessible, Document period, String day) {
        this._id = _id;
        this.venueName = venueName;
        this.uniqueId = uniqueId;
        this.venueCapacity = venueCapacity;
        this.isDisabilityAccessible = isDisabilityAccessible;
        this.period = period;
        this.day = day;
    }

    public VenueTimePair(){}

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


    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

  

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Document getPeriod() {
        return period;
    }

    public void setPeriod(Document period) {
        this.period = period;
    }

    
    
    public Document toDocument() {

        Document doc = new Document();
        doc.append("venueName", venueName);
        doc.append("venueCapacity", venueCapacity);
        doc.append("isDisabilityAccessible", isDisabilityAccessible);
        doc.append("period", period);
        doc.append("day", day);
        doc.append("_id", _id);
        doc.append("uniqueId", uniqueId);
        return doc;

    }

    public  VenueTimePair fromDocument (Document doc) {
        VenueTimePair VTP=new VenueTimePair();
        VTP.venueName = doc.getString("venueName");
        VTP.venueCapacity = doc.getString("venueCapacity");
        VTP.isDisabilityAccessible = doc.getString("isDisabilityAccessible");
        VTP.period = (Document)doc.get("period");
        VTP.day = doc.getString("day");
        VTP._id =(ObjectId) doc.get("_id");
        VTP.uniqueId=doc.getString("uniqueId");
        return VTP;
    }
}
