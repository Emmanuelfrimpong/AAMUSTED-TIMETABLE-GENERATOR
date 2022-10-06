/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Objects;

import org.bson.Document;

/**
 *
 * @author emman
 */
public class TableObject {
    String uniqueId;
    String day;
    String period;
    String venue;
    String lecturer;
    String courseCode;
    String level;
    String stuClass;

    public TableObject(String uniqueId, String day, String period, String venue, String lecturer, String courseCode, String level, String stuClass) {
        this.uniqueId = uniqueId;
        this.day = day;
        this.period = period;
        this.venue = venue;
        this.lecturer = lecturer;
        this.courseCode = courseCode;
        this.level = level;
        this.stuClass = stuClass;
    }
     public TableObject() {
        
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
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

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
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

    public String getStuClass() {
        return stuClass;
    }

    public void setStuClass(String stuClass) {
        this.stuClass = stuClass;
    }


    public Document toDocument(){
        Document doc = new Document();
        doc.append("uniqueId", uniqueId);
        doc.append("day", day);
        doc.append("period", period);
        doc.append("venue", venue);
        doc.append("lecturer", lecturer);
        doc.append("courseCode", courseCode);
        doc.append("level", level);
        doc.append("stuClass", stuClass);
        return doc;
    }

    public static TableObject fromDocument(Document doc){
        TableObject tableObject = new TableObject();
        tableObject.setUniqueId(doc.getString("uniqueId"));
        tableObject.setDay(doc.getString("day"));
        tableObject.setPeriod(doc.getString("period"));
        tableObject.setVenue(doc.getString("venue"));
        tableObject.setLecturer(doc.getString("lecturer"));
        tableObject.setCourseCode(doc.getString("courseCode"));
        tableObject.setLevel(doc.getString("level"));
        tableObject.setStuClass(doc.getString("stuClass"));
        return tableObject;
    }
    
    
    
}
