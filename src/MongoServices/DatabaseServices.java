/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MongoServices;

import GlobalFunctions.GlobalFunctions;
import GlobalFunctions.LoadingDailog;
import Objects.ClassCoursePair;
import Objects.Configuration;
import Objects.CoursesObject;
import Objects.DaysObject;
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
        LoadingDailog loading = new LoadingDailog("Importing Data........");
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
                loading.show();
                try ( FileInputStream inputStream = new FileInputStream(file);  Workbook workbook = new XSSFWorkbook(inputStream)) {
                    if (results.equals("Liberal/African Studies")) {
                        Sheet courseSheet = workbook.getSheetAt(0);
                        Row courseTitleRow = courseSheet.getRow(0);
                        if (GF.verifyAfricanSheet(courseTitleRow)) {
                            saved = ReadAfricanData(results, courseSheet);
                        } else {
                            loading.close();
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
                            loading.close();
                            saved = false;
                            GF.inforAlert("Invalid File", "The selected Excel file do not match stated parameters",
                                    Alert.AlertType.ERROR);
                        }
                    }
                    loading.close();
                } catch (FileNotFoundException ex) {
                    loading.close();
                    saved = false;
                    GF.inforAlert("File Error", ex.getMessage(), Alert.AlertType.ERROR);

                } catch (IOException ex) {
                    loading.close();
                    saved = false;
                    GF.inforAlert("File Error", ex.getMessage(), Alert.AlertType.ERROR);
                }
            });
        } catch (Exception error) {
            loading.close();
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
            String one = "Blank", two = "Blank", three = "Blank", four = "Blank", five = "Blank", six = "Blank";
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
                if (nextRow.getCell(5) != null) {
                    six = nextRow.getCell(5) != null || nextRow.getCell(5).getCellType() != CellType.BLANK
                            ? nextRow.getCell(5).getStringCellValue()
                            : "Blank";
                }
                List<String> courses = new ArrayList<>();
                courses.addAll(Arrays.asList(six.split(",")));
                studentData.add(new StudentsObject(new ObjectId(), one, two, three, four, five, courses, department, timeStamp));
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

    public List<MongoCursor<Document>> saveConfig(Configuration config) {

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
        MongoCollection<Document> tableCollection = database.getCollection("Tables");
        tableCollection.drop();
        
        return CreatePairs(config);

    }

    private void UpdateCourse(Bson eq, Bson updates) {
        MongoDatabase database = this.databaseConnection().getDatabase("AAMUSTED_DB");
        MongoCollection<Document> dataCollection = database.getCollection("Courses");
        FindOneAndUpdateOptions optionAfter = new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER);
        dataCollection.findOneAndUpdate(eq, updates, optionAfter);
    }

    private List<MongoCursor<Document>> CreatePairs(Configuration config) {
        List<MongoCursor<Document>> data = new ArrayList<>();
        MongoCursor<Document> VTP = CreateVTP(config);
        MongoCursor<Document> CCP = CreateClassCourse();
        MongoCursor<Document> LTP = CreateLTP(config);
        data.add(VTP);
        data.add(CCP);
        data.add(LTP);
        return data;

    }

    public MongoCursor<Document> CreateClassCourse() {
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
        MongoCollection<Document> dataCollection = database.getCollection("ClassCoursePair");
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
                        ccp.setType(studentClass.getType());

                        Document admin = dataCollection.find(new Document("_id", ccp.get_id())).first();
                        if (admin == null || admin.isEmpty()) {
                            dataCollection.insertOne(ccp.toDocument());
                        }
                    }
                }
            }
        }

        return dataCollection.find().iterator();
    }

    private MongoCursor<Document> CreateVTP(Configuration config) {
        MongoDatabase database = this.databaseConnection().getDatabase("AAMUSTED_DB");
        MongoCollection<Document> dataCollection = database.getCollection("VenueTimePair");
        ObservableList<Venue> venueData = FXCollections.observableArrayList();
        VenueTimePair VTP = new VenueTimePair();
        venueData = getVenues();
        for (ObservableMap<String, Object> day : config.getDays()) {
            DaysObject d=DaysObject.fromMap(day);
            for (Iterator<ObservableMap<String, Object>> it = config.getPeriods().iterator(); it.hasNext();) {
                PeriodsObject PO = PeriodsObject.fromMap(it.next());
                for (Venue venue : venueData) {                
                    VTP.setUniqueId(d.getDay().replaceAll("\\s+", "") + PO.getPeriod().replaceAll("\\s+", "") + venue.getName().replaceAll("\\s+", ""));
                    VTP.set_id(new ObjectId());
                    VTP.setDay(d.getDay());
                    VTP.setIsDisabilityAccessible(venue.getIsDisabilityAccessible());
                    VTP.setPeriod(PO.toDocument());
                    VTP.setVenueCapacity(venue.getCapacity());
                    VTP.setVenueName(venue.getName());
                    VTP.setEve(d.isEve());
                    VTP.setReg(d.isReg());
                    VTP.setWnd(d.isWnd());                  
                    Document data = dataCollection.find(new Document("uniqueId", VTP.getUniqueId())).first();
                    if (data != null && !data.isEmpty()) {
                        dataCollection.deleteOne(new Document("uniqueId", VTP.getUniqueId()));
                    }
                    dataCollection.insertOne(VTP.toDocument());
                }

            }
        }
        return dataCollection.find().iterator();

    }

    private MongoCursor<Document> CreateLTP(Configuration config) {
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
            LTP.setLecturerName(course.getLecturerName());
            LTP.setLecturerEmail(course.getLecturerEmail());
            LTP.setLecturerPhone(course.getLecturerPhone());
            LTP.setCourseCode(course.getCode());
            LTP.setLevel("");
            LTP.set_id(new ObjectId());
            collection.findOneAndDelete(new Document("umiqueId", LTP.getUmiqueId()));
            collection.insertOne(LTP.toDocument());

        }
        return collection.find().iterator();
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

    public Document DeleteConfig(Configuration config) {
        MongoDatabase database = this.databaseConnection().getDatabase("AAMUSTED_DB");
        MongoCollection<Document> configCollection = database.getCollection("Config");
        MongoCollection<Document> LCTPCollection = database.getCollection("LiberalCourseTimePair");
        MongoCollection<Document> VTPCollection = database.getCollection("VenueTimePair");
        MongoCollection<Document> CCPCollection = database.getCollection("ClassCoursePair");
        MongoCollection<Document> tableCollection = database.getCollection("Tables");
        LCTPCollection.drop();
        VTPCollection.drop();
        CCPCollection.drop();      
        tableCollection.drop();
        return configCollection.findOneAndDelete(new Document("_id", config.getId()));
    }

}
