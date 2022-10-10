/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Objects;

/**
 *
 * @author emman
 */
public class TableIViewtem {
    String classname;
    String courseCode;
    String venue;
    String lecturer;
    String period;

    public TableIViewtem(String classname, String courseCode, String venue, String lecturer, String period) {
        this.classname = classname;
        this.courseCode = courseCode;
        this.venue = venue;
        this.lecturer = lecturer;
        this.period = period;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
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

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public TableIViewtem() {
    }

   
    public String ToString(){
        return "Class: "+this.classname+"\nCourse: "+this.courseCode+"\nVenue: "+this.venue+"\nLect: "+this.lecturer;
    }
    
}
