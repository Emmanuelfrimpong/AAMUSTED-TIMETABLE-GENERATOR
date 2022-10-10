/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Objects;

import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author emman
 */
public class LiberalTimePair {

    ObjectId _id;
    String umiqueId;
    String day;
    String period;
    String courseCode;
    String level;
    String lecturerName;
    String lecturerEmail;
    String lecturerPhone;
    
   

   
    public LiberalTimePair() {
      
    }

    public LiberalTimePair(ObjectId _id, String umiqueId, String day, String period, String courseCode, String level, String lecturerName, String lecturerEmail, String lecturerPhone) {
        this._id = _id;
        this.umiqueId = umiqueId;
        this.day = day;
        this.period = period;
        this.courseCode = courseCode;
        this.level = level;
        this.lecturerName = lecturerName;
        this.lecturerEmail = lecturerEmail;
        this.lecturerPhone = lecturerPhone;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getUmiqueId() {
        return umiqueId;
    }

    public void setUmiqueId(String umiqueId) {
        this.umiqueId = umiqueId;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
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


public Document toDocument() {
        Document doc = new Document();
        doc.append("uniqueId", this.umiqueId);
        doc.append("day", this.day);
        doc.append("period", this.period);
        doc.append("courseCode", this.courseCode);
        doc.append("level", this.level);
        doc.append("lecturerName", this.lecturerName);
        doc.append("lecturerEmail", this.lecturerEmail);
        doc.append("lecturerPhone", this.lecturerPhone);
        return doc;
    }

    public static LiberalTimePair fromDocument(Document doc) {
        LiberalTimePair ltp = new LiberalTimePair();
        ltp.set_id(doc.getObjectId("_id"));
        ltp.setUmiqueId(doc.getString("uniqueId"));
        ltp.setDay(doc.getString("day"));
        ltp.setPeriod(doc.getString("period"));
        ltp.setCourseCode(doc.getString("courseCode"));
        ltp.setLevel(doc.getString("level"));
        ltp.setLecturerName(doc.getString("lecturerName"));
        ltp.setLecturerEmail(doc.getString("lecturerEmail"));
        ltp.setLecturerPhone(doc.getString("lecturerPhone"));
        return ltp;
    }



}
