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

    public LiberalTimePair(ObjectId _id, String umiqueId, String day, String period, String courseCode, String level) {
        this._id = _id;
        this.umiqueId = umiqueId;
        this.day = day;
        this.period = period;
        this.courseCode = courseCode;
        this.level = level;
    }

   
    public LiberalTimePair() {
      
    }

    public ObjectId getId() {
        return _id;
    }

    public void setId(ObjectId _id) {
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


    public Document getLiberalToDocument() {
        Document liberalTimePairMap = new Document();
        liberalTimePairMap.put("umiqueId", umiqueId);
        liberalTimePairMap.put("day", day);
        liberalTimePairMap.put("period", period);
         liberalTimePairMap.put("_id", _id);
         liberalTimePairMap.put("courseCode",courseCode) ;
         liberalTimePairMap.put("level", level);
        return liberalTimePairMap;
    }


   public static LiberalTimePair fromDocument(Document doc) {
     LiberalTimePair obj = new LiberalTimePair();
        obj.setUmiqueId(doc.getString("umiqueId"));
        obj.setDay(doc.getString("day"));
        obj.setPeriod(doc.getString("period"));
        obj.setId(doc.getObjectId("_id"));
         obj.setCourseCode(doc.getString("courseCode"));
          obj.setLevel(doc.getString("level"));
        return obj;
   }
    
}
