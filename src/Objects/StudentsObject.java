
package Objects;
import java.util.List;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author emman
 */
public class StudentsObject {   
    String name;
    String level;
    String department;
    String size;
    String hasDisability;
    List courses;
    String createdAt;
    
    public StudentsObject() {
    }

    public StudentsObject(String level, String name, String department, String size, String hasDisability, List courses, String createdAt) {    
        this.name = name;
        this.department = department;
        this.size = size;
        this.hasDisability = hasDisability;
        this.courses = courses;
        this.createdAt = createdAt;
        this.level=level;
    }

    

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
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

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    
    
   
    
    
        public Document toBsonDocs() {
        Document doc = new Document("_id", new ObjectId());
        
        doc.append("level", this.level)
                .append("createdAt", this.createdAt)
                .append("courses", this.courses)
                .append("department", this.department)
                .append("hasDisability", this.hasDisability)
                .append("name", this.name)
                .append("size",this.size);

        return doc;
    }
    
   public StudentsObject fromDocument(Document doc){
       StudentsObject student=new StudentsObject();
        student.createdAt=doc.getString("createdAt");
        student.level=doc.getString("level");
        student.courses=(List<String>) doc.get("courses");
        student.department=doc.getString("department");
        student.hasDisability=doc.getString("hasDisability");
        student.size=doc.getString("size");
        student.name=doc.getString("name");
        return student;
    }
}
