
package Objects;

import org.bson.Document;
import org.bson.types.ObjectId;


/**
 *
 * @author emman
 */
public class ClassCoursePair {

    String _id;
    ObjectId classId;
    ObjectId courseId;
    String className;
    String courseCode;
    String courseTitle;
    String courseCreditHours;
    String courseSpecialVenue;
    String courseLecturerName;
    String classLevel;
    String classSize;
    String classHasDisability;
    String type;

    public ClassCoursePair() {
    }

    public ClassCoursePair(String _id, ObjectId classId, ObjectId courseId, String className, String courseCode, String courseTitle, String courseCreditHours, String courseSpecialVenue, String courseLecturerName, String classLevel, String classSize, String classHasDisability, String type) {
        this._id = _id;
        this.classId = classId;
        this.courseId = courseId;
        this.className = className;
        this.courseCode = courseCode;
        this.courseTitle = courseTitle;
        this.courseCreditHours = courseCreditHours;
        this.courseSpecialVenue = courseSpecialVenue;
        this.courseLecturerName = courseLecturerName;
        this.classLevel = classLevel;
        this.classSize = classSize;
        this.classHasDisability = classHasDisability;
        this.type = type;
    }

   

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public ObjectId getClassId() {
        return classId;
    }

    public void setClassId(ObjectId classId) {
        this.classId = classId;
    }

    public ObjectId getCourseId() {
        return courseId;
    }

    public void setCourseId(ObjectId courseId) {
        this.courseId = courseId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseCreditHours() {
        return courseCreditHours;
    }

    public void setCourseCreditHours(String courseCreditHours) {
        this.courseCreditHours = courseCreditHours;
    }

    public String getCourseSpecialVenue() {
        return courseSpecialVenue;
    }

    public void setCourseSpecialVenue(String courseSpecialVenue) {
        this.courseSpecialVenue = courseSpecialVenue;
    }

    public String getCourseLecturerName() {
        return courseLecturerName;
    }

    public void setCourseLecturerName(String courseLecturerName) {
        this.courseLecturerName = courseLecturerName;
    }

    public String getClassLevel() {
        return classLevel;
    }

    public void setClassLevel(String classLevel) {
        this.classLevel = classLevel;
    }

    public String getClassSize() {
        return classSize;
    }

    public void setClassSize(String classSize) {
        this.classSize = classSize;
    }

    public String getClassHasDisability() {
        return classHasDisability;
    }

    public void setClassHasDisability(String classHasDisability) {
        this.classHasDisability = classHasDisability;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    
    
    public Document toDocument() {
        Document doc = new Document();
        doc.append("_id", _id);
        doc.append("classId", classId);
        doc.append("courseId", courseId);
        doc.append("className", className);
        doc.append("courseCode", courseCode);
        doc.append("courseTitle", courseTitle);
        doc.append("courseCreditHours", courseCreditHours);
        doc.append("courseSpecialVenue", courseSpecialVenue);
        doc.append("courseLecturerName", courseLecturerName);
        doc.append("classLevel", classLevel);
        doc.append("classSize", classSize);
        doc.append("classHasDisability", classHasDisability);
         doc.append("type", type);
        return doc;
    }

    public ClassCoursePair fromDocument(Document doc) {
        ClassCoursePair ccp = new ClassCoursePair();
        ccp.set_id(doc.getString("_id"));
        ccp.setClassId(doc.getObjectId("classId"));
        ccp.setCourseId(doc.getObjectId("courseId"));
        ccp.setClassName(doc.getString("className"));
        ccp.setCourseCode(doc.getString("courseCode"));
        ccp.setCourseTitle(doc.getString("courseTitle"));
        ccp.setCourseCreditHours(doc.getString("courseCreditHours"));
        ccp.setCourseSpecialVenue(doc.getString("courseSpecialVenue"));
        ccp.setCourseLecturerName(doc.getString("courseLecturerName"));
        ccp.setClassLevel(doc.getString("classLevel"));
        ccp.setClassSize(doc.getString("classSize"));
        ccp.setClassHasDisability(doc.getString("classHasDisability"));
        ccp.setType(doc.getString("type"));
        return ccp;
    }

}
