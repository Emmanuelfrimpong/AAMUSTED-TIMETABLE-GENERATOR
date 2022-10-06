/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MongoServices;

import GlobalFunctions.GlobalFunctions;
import GlobalFunctions.RingProgressIndicator;
import Objects.ClassCoursePair;
import Objects.Configuration;
import Objects.CoursesObject;
import Objects.ExcellHeaders;
import Objects.LiberalTimePair;
import Objects.PeriodsObject;
import Objects.SpecialVenue;
import Objects.StudentsObject;
import Objects.TableObject;
import Objects.Venue;
import Objects.VenueTimePair;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import static com.mongodb.client.model.Updates.set;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.stage.StageStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import java.util.Optional;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.VBox;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class DatabaseServices {
    
    GlobalFunctions GF = new GlobalFunctions();
    
    public MongoClient databaseConnection() {
        String connetionPath = "mongodb://localhost:27017/?readPreference=primary&appname=MongoDB%20Compass&directConnection=true&ssl=false";
        return MongoClients.create(connetionPath);
        
    }
    
    public void createConfigurations() {
        MongoDatabase sampleTrainingDB = this.databaseConnection().getDatabase("AAMUSTED_DB");
        MongoCollection<Document> adminCollection = sampleTrainingDB.getCollection("Admin");
        Document admin = adminCollection.find(new Document("id", "admin".hashCode())).first();
        if (admin == null || admin.isEmpty()) {
            Document newAdmin = new Document("_id", new ObjectId());
            newAdmin.append("userName", "admin")
                    .append("password", "admin")
                    .append("id", "admin".hashCode())
                    .append("createAt", Instant.now());
            adminCollection.insertOne(newAdmin);
        }
    }
    
    public Document updateDocument(String collection, Bson filter, Bson updates) {
        MongoDatabase sampleTrainingDB = this.databaseConnection().getDatabase("AAMUSTED_DB");
        MongoCollection<Document> dataCollection = sampleTrainingDB.getCollection(collection);
        FindOneAndUpdateOptions optionAfter = new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER);
        return dataCollection.findOneAndUpdate(filter, updates, optionAfter);
    }
    
    public boolean loginAdmin(String userName, String password) {
        MongoDatabase sampleTrainingDB = this.databaseConnection().getDatabase("AAMUSTED_DB");
        MongoCollection<Document> adminCollection = sampleTrainingDB.getCollection("Admin");
        BasicDBObject criteria = new BasicDBObject();
        criteria.append("userName", userName);
        criteria.append("password", password);
        Document admin = adminCollection.find(criteria).first();
        return admin != null && !admin.isEmpty();
    }
    
    public ObservableMap<String, Object> getDepartmentData(String databaseName) {
        ObservableMap<String, Object> data = FXCollections.observableHashMap();
        ObservableList<CoursesObject> listOfCourses = FXCollections.observableArrayList();
        ObservableList<StudentsObject> listOfStudents = FXCollections.observableArrayList();
        try {
            MongoDatabase database = this.databaseConnection().getDatabase(databaseName);
            MongoCollection<Document> classCollection = database.getCollection("Student_Class");
            MongoCollection<Document> CourseCollection = database.getCollection("Courses");
            
            MongoCursor<Document> cursor = classCollection.find().iterator();
            while (cursor.hasNext()) {
                listOfStudents.add(new StudentsObject().fromDocument(cursor.next()));
            }
            
            MongoCursor<Document> cursor2 = CourseCollection.find().iterator();
            while (cursor2.hasNext()) {
                listOfCourses.add(new CoursesObject().fromDocument(cursor2.next()));
            }
            
            data.put("classes", listOfStudents);
            data.put("courses", listOfCourses);
            
        } catch (Exception error) {
            Logger.getLogger(DatabaseServices.class.getName()).log(Level.SEVERE, null, error);
        }
        return data;
        
    }
    
    boolean saved = false;
    
    public ObservableMap<String, Object> LoadStudentsToDatabase(File file) throws FileNotFoundException, IOException {
        try {
            List<String> departments = new ArrayList();
            departments.add("Liberal/African Studies");
            departments.add("Catering & Hospitality Education");
            departments.add("Fashion & Textiles Design Education");
            departments.add("Construction & Wood Technology Education");
            departments.add("Automotive & Mechnical Technology Education");
            departments.add("Electrical & Electronics Technology Education");
            departments.add("Languages Education");
            departments.add("Interdisciplinary Studies");
            departments.add("Accounting Studies");
            departments.add("Management Studies");
            departments.add("Information Technology Education");
            departments.add("Mathematics Education");
            departments.add("Others");
            ButtonType submit = new ButtonType("Submit Data", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            Dialog<String> dialog = new Dialog<>();
            dialog.setHeaderText("Select Department and proceed to save data");
            DialogPane dialogPane = dialog.getDialogPane();
            dialog.initStyle(StageStyle.UNDECORATED);
            dialogPane.getStylesheets().add("/Styles/dialogStyle.css");
            dialogPane.getButtonTypes().addAll(submit, cancel);
            ComboBox<String> comboBox = new ComboBox<>();
            comboBox.getItems().addAll(departments);
            comboBox.getSelectionModel().selectFirst();
            dialogPane.setContent(new VBox(8, comboBox));
            dialog.setResultConverter((ButtonType button) -> {
                if (button == submit) {
                    return comboBox.getValue();
                }
                return null;
            });
            Optional<String> optionalResult = dialog.showAndWait();
            optionalResult.ifPresent((String results) -> {
                RingProgressIndicator indicator = new RingProgressIndicator();
                indicator.makeIndeterminate();
                try ( FileInputStream inputStream = new FileInputStream(file);  Workbook workbook = new XSSFWorkbook(inputStream)) {
                    
                    if (results.equals("Liberal/African Studies")) {
                        
                        Sheet courseSheet = workbook.getSheetAt(0);
                        
                        Row courseTitleRow = courseSheet.getRow(0);
                        if (GF.verifyAfricanSheet(courseTitleRow)) {
                            saved = ReadAfricanData(results, courseSheet);
                        } else {
                            saved = false;
                            GF.inforAlert("Invalid File", "The selected Excel file do not match stated parameters",
                                    Alert.AlertType.ERROR);
                        }
                    } else {
                        Sheet classSheet = workbook.getSheetAt(0);
                        Sheet courseSheet = workbook.getSheetAt(1);
                        Row classTitleRow = classSheet.getRow(0);
                        Row courseTitleRow = courseSheet.getRow(0);
                        if (GF.verifyExcelFile(classTitleRow, courseTitleRow)) {
                            saved = ReadData(results, classSheet, courseSheet);
                        } else {
                            saved = false;
                            GF.inforAlert("Invalid File", "The selected Excel file do not match stated parameters",
                                    Alert.AlertType.ERROR);
                        }
                    }
                    
                } catch (FileNotFoundException ex) {
                    
                    saved = false;
                    GF.inforAlert("File Error", ex.getMessage(), Alert.AlertType.ERROR);
                    
                } catch (IOException ex) {
                    saved = false;
                    GF.inforAlert("File Error", ex.getMessage(), Alert.AlertType.ERROR);
                }
            });
        } catch (Exception error) {
            
            GF.inforAlert("Excel Error", error.toString(), Alert.AlertType.ERROR);
        }
        ObservableMap<String, Object> data = FXCollections.observableHashMap();
        data.put("isSaved", saved);
        return data;
    }
    
    private boolean ReadData(String department, Sheet classSheet, Sheet courseSheet) {
        String timeStamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm a").format(Calendar.getInstance().getTime());
        Row classTitleRow = classSheet.getRow(0);
        Row courseTitleRow = courseSheet.getRow(0);
        Iterator<Row> iterator1 = classSheet.iterator();
        ObservableList<StudentsObject> studentData = FXCollections.observableArrayList();
        while (iterator1.hasNext()) {
            Row nextRow = iterator1.next();
            Iterator<Cell> cellIterator = nextRow.iterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                cell.setCellType(CellType.STRING);
            }
            String one = "Blank", two = "Blank", three = "Blank", four = "Blank", five = "Blank";
            if (nextRow != classTitleRow) {
                if (nextRow.getCell(0) != null) {
                    one = nextRow.getCell(0) != null || nextRow.getCell(0).getCellType() != CellType.BLANK
                            ? nextRow.getCell(0).getStringCellValue()
                            : "Blank";
                }
                if (nextRow.getCell(1) != null) {
                    two = nextRow.getCell(1) != null || nextRow.getCell(1).getCellType() != CellType.BLANK
                            ? nextRow.getCell(1).getStringCellValue()
                            : "Blank";
                }
                if (nextRow.getCell(2) != null) {
                    three = nextRow.getCell(2) != null || nextRow.getCell(2).getCellType() != CellType.BLANK
                            ? nextRow.getCell(2).getStringCellValue()
                            : "Blank";
                }
                if (nextRow.getCell(3) != null) {
                    four = nextRow.getCell(3) != null || nextRow.getCell(3).getCellType() != CellType.BLANK
                            ? nextRow.getCell(3).getStringCellValue()
                            : "Blank";
                }
                if (nextRow.getCell(4) != null) {
                    five = nextRow.getCell(4) != null || nextRow.getCell(4).getCellType() != CellType.BLANK
                            ? nextRow.getCell(4).getStringCellValue()
                            : "Blank";
                }
                List<String> courses = new ArrayList<>();
                courses.addAll(Arrays.asList(five.split(",")));
                studentData.add(
                        new StudentsObject(one, two, three, four, courses, department, timeStamp,
                                new ObjectId()));
            }
            
        }
        
        Iterator<Row> iterator2 = courseSheet.iterator();
        ObservableList<CoursesObject> courseData = FXCollections.observableArrayList();
        while (iterator2.hasNext()) {
            Row nextRow = iterator2.next();
            Iterator<Cell> cellIterator = nextRow.iterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                cell.setCellType(CellType.STRING);
                
            }
            if (nextRow != courseTitleRow) {
                String one = "Blank", two = "Blank", three = "Blank", four = "Blank", five = "Blank", six = "Blank", seven = "Blank";
                if (nextRow.getCell(0) != null) {
                    one = nextRow.getCell(0) != null || nextRow.getCell(0).getCellType() != CellType.BLANK
                            ? nextRow.getCell(0).getStringCellValue()
                            : "Blank";
                }
                if (nextRow.getCell(1) != null) {
                    two = nextRow.getCell(1) != null || nextRow.getCell(1).getCellType() != CellType.BLANK
                            ? nextRow.getCell(1).getStringCellValue()
                            : "Blank";
                }
                if (nextRow.getCell(2) != null) {
                    three = nextRow.getCell(2) != null || nextRow.getCell(2).getCellType() != CellType.BLANK
                            ? nextRow.getCell(2).getStringCellValue()
                            : "Blank";
                }
                if (nextRow.getCell(3) != null) {
                    four = nextRow.getCell(3) != null || nextRow.getCell(3).getCellType() != CellType.BLANK
                            ? nextRow.getCell(3).getStringCellValue()
                            : "Blank";
                }
                if (nextRow.getCell(4) != null) {
                    five = nextRow.getCell(4) != null || nextRow.getCell(4).getCellType() != CellType.BLANK
                            ? nextRow.getCell(4).getStringCellValue()
                            : "Blank";
                }
                if (nextRow.getCell(5) != null) {
                    six = nextRow.getCell(5) != null || nextRow.getCell(5).getCellType() != CellType.BLANK
                            ? nextRow.getCell(5).getStringCellValue()
                            : "Blank";
                }
                if (nextRow.getCell(6) != null) {
                    seven = nextRow.getCell(6) != null || nextRow.getCell(6).getCellType() != CellType.BLANK
                            ? nextRow.getCell(6).getStringCellValue()
                            : "Blank";
                }
                
                courseData.add(new CoursesObject(one, two, three, four, five, six, seven, new ObjectId(), department));
            }
            
        }
        
        return saveData(studentData, courseData);
        
    }
    
    public void saveStudentClass(StudentsObject data) {
        MongoDatabase database = this.databaseConnection().getDatabase("AAMUSTED_DB");
        MongoCollection<Document> dataCollection = database.getCollection("Student_Class");
        Document admin = dataCollection.find(new Document("name", data.getName())).first();
        if (admin == null || admin.isEmpty()) {
            dataCollection.insertOne(data.toDocument());
        }
        
    }
    
    private boolean saveData(ObservableList<StudentsObject> studentData, ObservableList<CoursesObject> courseData) {
        try {
            for (StudentsObject student : studentData) {
                saveStudentClass(student);
            }
            for (CoursesObject course : courseData) {
                saveCourse(course);
            }
            return true;
        } catch (Exception error) {
            return false;
        }
        
    }
    
    private void saveCourse(CoursesObject course) {
        MongoDatabase database = this.databaseConnection().getDatabase("AAMUSTED_DB");
        MongoCollection<Document> dataCollection = database.getCollection("Courses");
        Document admin = dataCollection.find(new Document("code", course.getCode())).first();
        if (admin == null || admin.isEmpty()) {
            dataCollection.insertOne(course.toDocument());
        }
        
    }
    
    public Document deleteData(String collection, ObjectId id) {
        MongoDatabase database = this.databaseConnection().getDatabase("AAMUSTED_DB");
        MongoCollection<Document> dataCollection = database.getCollection(collection);
        return dataCollection.findOneAndDelete(new Document("_id", id));
    }
    
    private void saveVenue(Venue venue) {
        MongoDatabase database = this.databaseConnection().getDatabase("AAMUSTED_DB");
        MongoCollection<Document> dataCollection = database.getCollection("Venue");
        Document admin = dataCollection.find(new Document("name", venue.getName())).first();
        if (admin == null || admin.isEmpty()) {
            dataCollection.insertOne(venue.toDocument());
        }
        
    }
    
    public ObservableList<Venue> LoadVenue(File selectedFile) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to save the list of venues?",
                ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Load Data");
        alert.getDialogPane().getStylesheets().add("/Styles/dialogStyle.css");
        alert.getDialogPane().setMinSize(400, 200);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            
            try {
                FileInputStream file = new FileInputStream(selectedFile);
                XSSFWorkbook workbook = new XSSFWorkbook(file);
                XSSFSheet sheet = workbook.getSheetAt(0);
                Row titleRow = sheet.getRow(0);
                if (titleRow.getCell(0).getStringCellValue().equals(ExcellHeaders.getRoomName())
                        && titleRow.getCell(1).getStringCellValue().equals(ExcellHeaders.getCapacity())
                        && titleRow.getCell(2).getStringCellValue().equals(ExcellHeaders.getDisbility())) {
                    Iterator<Row> rowIterator = sheet.iterator();
                    while (rowIterator.hasNext()) {
                        Row nextRow = rowIterator.next();
                        Iterator<Cell> cellIterator = nextRow.iterator();
                        while (cellIterator.hasNext()) {
                            Cell cell = cellIterator.next();
                            cell.setCellType(CellType.STRING);
                            
                        }
                        if (nextRow.getRowNum() == 0) {
                            continue;
                        }
                        String one = "Blank", two = "Blank", three = "Blank";
                        
                        if (nextRow.getCell(0) != null) {
                            one = nextRow.getCell(0) != null || nextRow.getCell(0).getCellType() != CellType.BLANK
                                    ? nextRow.getCell(0).getStringCellValue()
                                    : "Blank";
                        }
                        if (nextRow.getCell(1) != null) {
                            two = nextRow.getCell(1) != null || nextRow.getCell(1).getCellType() != CellType.BLANK
                                    ? nextRow.getCell(1).getStringCellValue()
                                    : "Blank";
                        }
                        if (nextRow.getCell(2) != null) {
                            three = nextRow.getCell(2) != null || nextRow.getCell(2).getCellType() != CellType.BLANK
                                    ? nextRow.getCell(2).getStringCellValue()
                                    : "Blank";
                        }
                        Venue venue = new Venue(one, two, three);
                        venue.set_id(new ObjectId());
                        
                        saveVenue(venue);
                    }
                    
                } else {
                    GF.inforAlert("Invalid File", "The file you selected is not a valid venue file", Alert.AlertType.ERROR);
                }
            } catch (IOException error) {
                GF.inforAlert("Error", error.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            alert.close();
        }
        return getVenues();
    }
    
    public ObservableList<Venue> getVenues() {
        ObservableList<Venue> venueData = FXCollections.observableArrayList();
        try {
            MongoDatabase database = this.databaseConnection().getDatabase("AAMUSTED_DB");
            MongoCollection<Document> dataCollection = database.getCollection("Venue");
            MongoCursor<Document> cursor = dataCollection.find().iterator();
            while (cursor.hasNext()) {
                venueData.add(new Venue().fromDocument(cursor.next()));
            }
        } catch (Exception error) {
            GF.inforAlert("Error", error.getMessage(), Alert.AlertType.ERROR);
        }
        return venueData;
    }
    

    
    public Configuration getConfig() {
        MongoDatabase database = this.databaseConnection().getDatabase("AAMUSTED_DB");
        MongoCollection<Document> dataCollection = database.getCollection("Config");
        Document setting = dataCollection.find().first();
        if (setting != null && !setting.isEmpty()) {
            return new Configuration().fromDocument(setting);
        } else {
            return new Configuration();
        }
    }
    
    private boolean ReadAfricanData(String results, Sheet courseSheet) {
        Iterator<Row> iterator2 = courseSheet.iterator();
        ObservableList<CoursesObject> courseData = FXCollections.observableArrayList();
        Row courseTitleRow = courseSheet.getRow(0);
        while (iterator2.hasNext()) {
            Row nextRow = iterator2.next();
            Iterator<Cell> cellIterator = nextRow.iterator();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                cell.setCellType(CellType.STRING);
            }
            if (nextRow != courseTitleRow) {
                String one = "Blank", two = "Blank", three = "Blank", four = "Blank", five = "Blank", six = "Blank", seven = "Blank";
                if (nextRow.getCell(0) != null) {
                    one = nextRow.getCell(0) != null || nextRow.getCell(0).getCellType() != CellType.BLANK
                            ? nextRow.getCell(0).getStringCellValue()
                            : "Blank";
                }
                if (nextRow.getCell(1) != null) {
                    two = nextRow.getCell(1) != null || nextRow.getCell(1).getCellType() != CellType.BLANK
                            ? nextRow.getCell(1).getStringCellValue()
                            : "Blank";
                }
                if (nextRow.getCell(2) != null) {
                    three = nextRow.getCell(2) != null || nextRow.getCell(2).getCellType() != CellType.BLANK
                            ? nextRow.getCell(2).getStringCellValue()
                            : "Blank";
                }
                if (nextRow.getCell(3) != null) {
                    four = nextRow.getCell(3) != null || nextRow.getCell(3).getCellType() != CellType.BLANK
                            ? nextRow.getCell(3).getStringCellValue()
                            : "Blank";
                }
                if (nextRow.getCell(4) != null) {
                    five = nextRow.getCell(4) != null || nextRow.getCell(4).getCellType() != CellType.BLANK
                            ? nextRow.getCell(4).getStringCellValue()
                            : "Blank";
                }
                if (nextRow.getCell(5) != null) {
                    six = nextRow.getCell(5) != null || nextRow.getCell(5).getCellType() != CellType.BLANK
                            ? nextRow.getCell(5).getStringCellValue()
                            : "Blank";
                }
                if (nextRow.getCell(6) != null) {
                    seven = nextRow.getCell(6) != null || nextRow.getCell(6).getCellType() != CellType.BLANK
                            ? nextRow.getCell(6).getStringCellValue()
                            : "Blank";
                }
                courseData.add(new CoursesObject(one, two, three, four, five, six, seven, new ObjectId(), results));
            }
            
        }
        for (CoursesObject course : courseData) {
            saveCourse(course);
        }
        return true;
        
    }
    
    public void saveConfig(Configuration config) {
        MongoDatabase database = this.databaseConnection().getDatabase("AAMUSTED_DB");
        for (ObservableMap<String, Object> map : config.getSpecialVenue()) {
            SpecialVenue venue = SpecialVenue.getSpecialVenueFromMap(map);
            Bson updates = set("specialVenue", venue.getVenue());
            UpdateCourse(eq("code", venue.getCourse()), updates);
        }
        
        MongoCollection<Document> dataCollection = database.getCollection("Config");
        Document setting = dataCollection.find(new Document("_id", config.getId())).first();
        if (setting != null && !setting.isEmpty()) {
            dataCollection.deleteOne(new Document("_id", config.getId()));
            
        }
        config.setId(new ObjectId());
        dataCollection.insertOne(config.toDocument());
        
        CreatePairs(config);
        
    }
    
    private void UpdateCourse(Bson eq, Bson updates) {
        MongoDatabase database = this.databaseConnection().getDatabase("AAMUSTED_DB");
        MongoCollection<Document> dataCollection = database.getCollection("Courses");
        FindOneAndUpdateOptions optionAfter = new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER);
        dataCollection.findOneAndUpdate(eq, updates, optionAfter);
    }
    
    private void CreatePairs(Configuration config) {
        CreateVTP(config);
        CreateClassCourse();
        CreateLTP(config);
        
    }
    
    public void CreateClassCourse() {
        new Thread(() -> {
            MongoDatabase database = this.databaseConnection().getDatabase("AAMUSTED_DB");
            MongoCollection<Document> classCollection = database.getCollection("Student_Class");
            MongoCollection<Document> CourseCollection = database.getCollection("Courses");
            ObservableList<CoursesObject> listOfCourses = FXCollections.observableArrayList();
            ObservableList<StudentsObject> listOfStudents = FXCollections.observableArrayList();
            MongoCursor<Document> cursor = classCollection.find().iterator();
            while (cursor.hasNext()) {
                listOfStudents.add(new StudentsObject().fromDocument(cursor.next()));
            }
            
            MongoCursor<Document> cursor2 = CourseCollection.find().iterator();
            while (cursor2.hasNext()) {
                CoursesObject data = new CoursesObject().fromDocument(cursor2.next());
                if (!data.getDepartment().equals("Liberal/African Studies")) {
                    listOfCourses.add(data);
                }
            }
            
            for (StudentsObject studentClass : listOfStudents) {
                List courses = studentClass.getCourses();
                for (Object courseCode : courses) {
                    for (CoursesObject course : listOfCourses) {
                        if (course.getCode().toLowerCase() == null ? courseCode.toString().toLowerCase() == null
                                : course.getCode().toLowerCase().equals(courseCode.toString().toLowerCase())) {
                            ClassCoursePair ccp = new ClassCoursePair();
                            String id = studentClass.get_id().toString() + course.getId().toString();
                            int ccpId = id.hashCode();
                            ccp.set_id(String.valueOf(ccpId));
                            ccp.setClassHasDisability(studentClass.getHasDisability());
                            ccp.setClassId(studentClass.get_id());
                            ccp.setClassName(studentClass.getName());
                            ccp.setCourseCode(course.getCode());
                            ccp.setClassLevel(studentClass.getLevel());
                            ccp.setCourseTitle(course.getTitle());
                            ccp.setCourseCreditHours(course.getCreditHours());
                            ccp.setCourseLecturerName(course.getLecturerName());
                            ccp.setCourseSpecialVenue(course.getSpecialVenue());
                            ccp.setCourseId(course.getId());
                            ccp.setClassSize(studentClass.getSize());
                            MongoCollection<Document> dataCollection = database.getCollection("ClassCoursePair");
                            Document admin = dataCollection.find(new Document("_id", ccp.get_id())).first();
                            if (admin == null || admin.isEmpty()) {
                                dataCollection.insertOne(ccp.toDocument());
                            }
                        }
                    }
                }
            }
            
        }).start();
    }
    
    private void CreateVTP(Configuration config) {
        MongoDatabase database = this.databaseConnection().getDatabase("AAMUSTED_DB");
        MongoCollection<Document> dataCollection = database.getCollection("VenueTimePair");
        ObservableList<Venue> venueData = FXCollections.observableArrayList();
        VenueTimePair VTP = new VenueTimePair();
        venueData = getVenues();
        for (String day : config.getDays()) {
            for (Iterator<ObservableMap<String, Object>> it = config.getPeriods().iterator(); it.hasNext();) {
                PeriodsObject PO = PeriodsObject.getPeriodsObjectFromMap(it.next());
                for (Venue venue : venueData) {
                    VTP.setUniqueId(day.replaceAll("\\s+", "") + PO.getPeriod().replaceAll("\\s+", "") + venue.getName().replaceAll("\\s+", ""));
                    VTP.set_id(new ObjectId());
                    VTP.setDay(day);
                    VTP.setIsDisabilityAccessible(venue.getIsDisabilityAccessible());
                    VTP.setPeriod(PO.ToDocument());
                    VTP.setVenueCapacity(venue.getCapacity());
                    VTP.setVenueName(venue.getName());
                    Document data = dataCollection.find(new Document("uniqueId", VTP.getUniqueId())).first();
                    if (data != null && !data.isEmpty()) {
                        dataCollection.deleteOne(new Document("uniqueId", VTP.getUniqueId()));
                    }
                    dataCollection.insertOne(VTP.toDocument());
                    
                }
                
            }
        }
        
    }
    
    private void CreateLTP(Configuration config) {
        MongoDatabase database = this.databaseConnection().getDatabase("AAMUSTED_DB");
        MongoCollection<Document> CourseCollection = database.getCollection("Courses");
        ObservableList<CoursesObject> listOfCourses = FXCollections.observableArrayList();
        MongoCursor<Document> cursor2 = CourseCollection.find().iterator();
        while (cursor2.hasNext()) {
            CoursesObject data = new CoursesObject().fromDocument(cursor2.next());
            if (data.getDepartment().equals("Liberal/African Studies")) {
                listOfCourses.add(data);
            }
        }
        
        MongoCollection<Document> collection = database.getCollection("LiberalCourseTimePair");
        for (CoursesObject course : listOfCourses) {
            LiberalTimePair LTP = new LiberalTimePair();
            LTP.setUmiqueId(course.getCode() + config.getLaDay().replaceAll("\\s+", "") + config.getLaPeriod().replaceAll("\\s+", ""));
            LTP.setDay(config.getLaDay());
            LTP.setPeriod(config.getLaPeriod());
            LTP.setCourseCode(course.getCode());
            LTP.setLevel("");            
            LTP.setId(new ObjectId());
            collection.findOneAndDelete(new Document("umiqueId", LTP.getUmiqueId()));
            collection.insertOne(LTP.getLiberalToDocument());
            
        }
    }
    
    public void GenerateTable(String results) {
        Configuration config = getConfig();
        MongoDatabase database = this.databaseConnection().getDatabase("AAMUSTED_DB");
        MongoCollection<Document> CCPCollection = database.getCollection("ClassCoursePair");
        ObservableList<ClassCoursePair> ListCCP = FXCollections.observableArrayList();
        MongoCursor<Document> CCPCoursor = CCPCollection.find().iterator();
        while (CCPCoursor.hasNext()) {
            ListCCP.add(new ClassCoursePair().fromDocument(CCPCoursor.next()));
        }
        
        ObservableList<VenueTimePair> ListVTP = FXCollections.observableArrayList();
        MongoCollection<Document> VTPCollection = database.getCollection("VenueTimePair");
        MongoCursor<Document> VTPCoursor = VTPCollection.find().iterator();
        while (VTPCoursor.hasNext()) {
            ListVTP.add(new VenueTimePair().fromDocument(VTPCoursor.next()));
        }
        
        ObservableList<LiberalTimePair> ListLCP = FXCollections.observableArrayList();
        MongoCollection<Document> LCPCollection = database.getCollection("LiberalCourseTimePair");
        MongoCursor<Document> LCPCoursor = LCPCollection.find().iterator();
        while (LCPCoursor.hasNext()) {
            ListLCP.add(LiberalTimePair.fromDocument(LCPCoursor.next()));
        }
        
        int count = 1;
        Random rand = new Random();
        Collections.shuffle(ListVTP);
        //Asign Special Classes==============================================
        for (int i = 0; i < ListCCP.size(); i++) {
            ClassCoursePair CCP = ListCCP.get(i);
            for (int j = 0; j < ListVTP.size(); j++) {
                VenueTimePair VTP = ListVTP.get(j);
                if (CCP.getCourseSpecialVenue().equals(VTP.getVenueName())) {
                    PeriodsObject p = PeriodsObject.getSpecialVenueFromDoc(VTP.getPeriod());
                    TableObject table = new TableObject();
                    table.setCourseCode(CCP.getCourseCode());
                    table.setLevel(CCP.getClassLevel());
                    table.setDay(VTP.getDay());
                    table.setStuClass(CCP.getClassName());
                    table.setVenue(VTP.getVenueName());
                    table.setPeriod(p.getPeriod());
                    table.setUniqueId(CCP.getClassName().replaceAll("\\s+", "") + VTP.getDay().replaceAll("\\s+", "") + p.getPeriod().replaceAll("\\s+", "") + VTP.getVenueName().replaceAll("\\s+", ""));
                    saveTable(table);
                    ListCCP.remove(CCP);
                    ListVTP.remove(VTP);
                }
            }
        }
        //end of special course========================================================================
        //Liberal Course=================================================================
        Collections.sort(ListVTP, (VenueTimePair o1, VenueTimePair o2) -> Integer.valueOf(o1.getVenueCapacity()).compareTo(Integer.valueOf(o2.getVenueCapacity())));
        FXCollections.reverse(ListVTP);
        ObservableList<VenueTimePair> LAVenues = FXCollections.observableArrayList();
        int end = ListLCP.size();
        for (int j = 0; j < ListVTP.size(); j++) {
            if (isLiberal(config, ListVTP.get(j))) {
                end--;
                LAVenues.add(ListVTP.get(j));
                ListVTP.remove(j);
            }
            if (end == 0) {
                break;
            }
        }
        createLaberalCourseTable(LAVenues, ListLCP);
        Collections.shuffle(ListVTP);
        for (VenueTimePair VTP : ListVTP) {
            if (ListCCP.isEmpty()) {
                break;
            } else {
                while (true) {
                    Collections.shuffle(ListCCP);
                    ClassCoursePair CCP = ListCCP.get(rand.nextInt(ListCCP.size()));
                    if (matchCapacity(VTP, CCP)) {
                        PeriodsObject p = PeriodsObject.getSpecialVenueFromDoc(VTP.getPeriod());
                        TableObject table = new TableObject();
                        table.setCourseCode(CCP.getCourseCode());
                        table.setLevel(CCP.getClassLevel());
                        table.setDay(VTP.getDay());
                        table.setStuClass(CCP.getClassName());
                        table.setVenue(VTP.getVenueName());
                        table.setPeriod(p.getPeriod());
                        table.setUniqueId(CCP.getClassName().replaceAll("\\s+", "") + VTP.getDay().replaceAll("\\s+", "") + p.getPeriod().replaceAll("\\s+", "") + VTP.getVenueName().replaceAll("\\s+", ""));
                        saveTable(table);
                        ListCCP.remove(CCP);
                        break;
                    }
                }
                
            }
        }
        
    }
    
    private boolean matchCapacity(VenueTimePair VTP, ClassCoursePair CCP) {
        int cap = Integer.parseInt(CCP.getClassSize());
        int size = Integer.parseInt(VTP.getVenueCapacity());
        return cap < size - 15 || cap + 20 > size;
    }
    
    private boolean matchDisability(VenueTimePair VTP, ClassCoursePair CCP) {
        return VTP.getIsDisabilityAccessible().replaceAll("\\s+", "").toLowerCase().equals(CCP.getClassHasDisability().replaceAll("\\s+", "").toLowerCase());
    }
    
    private boolean isLiberal(Configuration config, VenueTimePair get) {
        PeriodsObject p = PeriodsObject.getSpecialVenueFromDoc(get.getPeriod());
        return config.getLaDay().equals(get.getDay()) && config.getLaPeriod().equals(p.getPeriod());
    }
    
    private void createLaberalCourseTable(ObservableList<VenueTimePair> LAVenues, ObservableList<LiberalTimePair> ListLCP) {
        Random rand = new Random();
        for (VenueTimePair VTP : LAVenues) {
            if (ListLCP.isEmpty()) {
                break;
            } else {
                LiberalTimePair LCP = ListLCP.get(rand.nextInt(ListLCP.size()));
                PeriodsObject p = PeriodsObject.getSpecialVenueFromDoc(VTP.getPeriod());
                TableObject table = new TableObject();
                table.setCourseCode(LCP.getCourseCode());
                table.setLevel(LCP.getLevel());
                table.setDay(VTP.getDay());
                table.setStuClass("");
                table.setVenue(VTP.getVenueName());
                table.setPeriod(p.getPeriod());
                table.setUniqueId(VTP.getDay().replaceAll("\\s+", "") + p.getPeriod().replaceAll("\\s+", "") + VTP.getVenueName().replaceAll("\\s+", ""));
                saveTable(table);
                ListLCP.remove(LCP);
            }
        }
    }
    
    private void saveTable(TableObject table) {
        MongoDatabase database = this.databaseConnection().getDatabase("AAMUSTED_DB");
        MongoCollection<Document> dataCollection = database.getCollection("Tables");
        dataCollection.findOneAndDelete(new Document("uniqueId", table.getUniqueId()));
        dataCollection.insertOne(table.toDocument());
    }
    
    public ObservableList<TableObject> getTables() {
        MongoDatabase database = this.databaseConnection().getDatabase("AAMUSTED_DB");
        MongoCollection<Document> dataCollection = database.getCollection("Tables");
        ObservableList<TableObject> table = FXCollections.observableArrayList();
        MongoCursor<Document> LCPCoursor = dataCollection.find().iterator();
        while (LCPCoursor.hasNext()) {
            table.add(TableObject.fromDocument(LCPCoursor.next()));
        }
        return table;
    }
    
}
