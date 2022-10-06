
package Objects;

import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author emman
 */
public class Venue {
    String name;
    String capacity;
    String isDisabilityAccessible;
    ObjectId _id;

    public Venue(String name, String capacity, String isDisabilityAccessible) {
        this.name = name;
        this.capacity = capacity;
        this.isDisabilityAccessible = isDisabilityAccessible;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getIsDisabilityAccessible() {
        return isDisabilityAccessible;
    }

    public void setIsDisabilityAccessible(String isDisabilityAccessible) {
        this.isDisabilityAccessible = isDisabilityAccessible;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public Venue() {
    }

    public Venue fromDocument(Document doc) {
        Venue venue = new Venue();
        venue.setName(doc.getString("name"));
        venue.setCapacity(doc.getString("capacity"));
        venue.setIsDisabilityAccessible(doc.getString("isDisabilityAccessible"));
        venue.set_id(doc.getObjectId("_id"));
        return venue;
    }

    public Document toDocument() {
        Document doc = new Document();
        doc.append("name", this.getName());
        doc.append("capacity", this.getCapacity());
        doc.append("isDisabilityAccessible", this.getIsDisabilityAccessible());
        doc.append("_id", this.get_id());
        return doc;
    }

}
