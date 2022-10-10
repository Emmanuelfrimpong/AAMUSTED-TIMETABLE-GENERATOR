package Objects;

import java.util.List;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author emman
 */
public class StudentsObject {

    ObjectId _id;
    String level;
    String type;
    String name;
    String size;
    String hasDisability;
    List courses;
    String department;
    String createdAt;

    public StudentsObject() {
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getHasDisability() {
        return hasDisability;
    }

    public void setHasDisability(String hasDisability) {
        this.hasDisability = hasDisability;
    }

    public List getCourses() {
        return courses;
    }

    public void setCourses(List courses) {
        this.courses = courses;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public StudentsObject(ObjectId _id, String level, String type, String name, String size, String hasDisability, List courses, String department, String createdAt) {
        this._id = _id;
        this.level = level;
        this.type = type;
        this.name = name;
        this.size = size;
        this.hasDisability = hasDisability;
        this.courses = courses;
        this.department = department;
        this.createdAt = createdAt;
    }

   

    public Document toDocument() {
        Document doc = new Document();
        doc.put("level", level);
        doc.put("name", name);
        doc.put("size", size);
        doc.put("hasDisability", hasDisability);
        doc.put("courses", courses);
        doc.put("department", department);
        doc.put("createdAt", createdAt);
        doc.put("_id", _id);
        doc.put("type", type);
        return doc;
    }

    public static StudentsObject fromDocument(Document doc) {
        StudentsObject obj = new StudentsObject();
        obj.setLevel(doc.getString("level"));
        obj.setName(doc.getString("name"));
        obj.setSize(doc.getString("size"));
        obj.setHasDisability(doc.getString("hasDisability"));
        obj.setCourses(doc.getList("courses", String.class));
        obj.setDepartment(doc.getString("department"));
        obj.setCreatedAt(doc.getString("createdAt"));
        obj.set_id(doc.getObjectId("_id"));
        obj.setType(doc.getString("type"));

        return obj;
    }

}
