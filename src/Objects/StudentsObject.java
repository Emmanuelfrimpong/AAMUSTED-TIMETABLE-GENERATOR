/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Objects;

import java.time.Instant;
import java.util.List;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author emman
 */
public class StudentsObject {   
    String Id;
    String name;
    String department;
    String size;
    String hasDisability;
    List courses;
    Instant createdAt;

    public StudentsObject() {
    }

    public StudentsObject(String Id, String name, String department, String size, String hasDisability, List courses, Instant createdAt) {
        this.Id = Id;
        this.name = name;
        this.department = department;
        this.size = size;
        this.hasDisability = hasDisability;
        this.courses = courses;
        this.createdAt = createdAt;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
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

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    
    
   
    
    
        public Document toBsonDocs() {
        Document doc = new Document("_id", new ObjectId());
        
        doc.append("id", this.Id)
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
        student.createdAt=(Instant) doc.get("createdAt");
        student.Id=doc.getString("Id");
        student.courses=(List<String>) doc.get("courses");
        student.department=doc.getString("department");
        student.hasDisability=doc.getString("hasDisability");
        student.size=doc.getString("size");
        return student;
    }
}
