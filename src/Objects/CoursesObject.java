
package Objects;

import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author emman
 */
public class CoursesObject {
    String code;
    String title;
    String creditHours;
    String specialVenue;
    String lecturerName;
    String lecturerEmail;
    String lecturerPhone;
    ObjectId _id;

    public CoursesObject() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreditHours() {
        return creditHours;
    }

    public void setCreditHours(String creditHours) {
        this.creditHours = creditHours;
    }

    public String getSpecialVenue() {
        return specialVenue;
    }

    public void setSpecialVenue(String specialVenue) {
        this.specialVenue = specialVenue;
    }

    public String getLecturerName() {
        return lecturerName;
    }

    public void setLecturerName(String lecturerName) {
        this.lecturerName = lecturerName;
    }

    public String getLecturerEmail() {
        return lecturerEmail;
    }

    public void setLecturerEmail(String lecturerEmail) {
        this.lecturerEmail = lecturerEmail;
    }

    public String getLecturerPhone() {
        return lecturerPhone;
    }

    public void setLecturerPhone(String lecturerPhone) {
        this.lecturerPhone = lecturerPhone;
    }

    public ObjectId getId() {
        return _id;
    }

    public void setId(ObjectId _id) {
        this._id = _id;
    }

    public CoursesObject(String code, String title, String creditHours, String specialVenue, String lecturerName,
            String lecturerEmail, String lecturerPhone, ObjectId _id) {
        this.code = code;
        this.title = title;
        this.creditHours = creditHours;
        this.specialVenue = specialVenue;
        this.lecturerName = lecturerName;
        this.lecturerEmail = lecturerEmail;
        this.lecturerPhone = lecturerPhone;
        this._id = _id;
    }

    public Document toDocument() {
        Document doc = new Document();
        doc.put("code", code);
        doc.put("title", title);
        doc.put("creditHours", creditHours);
        doc.put("specialVenue", specialVenue);
        doc.put("lecturerName", lecturerName);
        doc.put("lecturerEmail", lecturerEmail);
        doc.put("lecturerPhone", lecturerPhone);
        doc.put("_id", _id);
        return doc;
    }

    public static CoursesObject fromDocument(Document doc) {
        CoursesObject obj = new CoursesObject();
        obj.setCode(doc.getString("code"));
        obj.setTitle(doc.getString("title"));
        obj.setCreditHours(doc.getString("creditHours"));
        obj.setSpecialVenue(doc.getString("specialVenue"));
        obj.setLecturerName(doc.getString("lecturerName"));
        obj.setLecturerEmail(doc.getString("lecturerEmail"));
        obj.setLecturerPhone(doc.getString("lecturerPhone"));
        obj.setId(doc.getObjectId("_id"));
        return obj;
    }

}
