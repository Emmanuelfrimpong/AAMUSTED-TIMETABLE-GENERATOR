/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MongoServices;

import GlobalFunctions.GlobalFunctions;
import GlobalFunctions.RingProgressIndicator;
import Objects.ClassCoursePair;
import Objects.CoursesObject;
import Objects.StudentsObject;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

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
    Stage loadingStage = new Stage();

    public ObservableMap<String, Object> LoadStudentsToDatabase(File file) throws FileNotFoundException, IOException {
        try {
            List<String> departments = new ArrayList();
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
                Screen screen = Screen.getPrimary();
                Rectangle2D bounds = screen.getVisualBounds();
                VBox cont = new VBox();
                cont.setAlignment(Pos.CENTER);
                Label lb = new Label("Saving Data..........");
                cont.getChildren().addAll(indicator, lb);
                Scene scene = new Scene(cont);
                loadingStage.setX(bounds.getMinX());
                loadingStage.setY(bounds.getMinY());
                loadingStage.setWidth(bounds.getWidth());
                loadingStage.setHeight(bounds.getHeight());
                loadingStage.setOpacity(0.95);
                loadingStage.setResizable(false);
                loadingStage.initStyle(StageStyle.UNDECORATED);
                loadingStage.initStyle(StageStyle.TRANSPARENT);
                scene.setFill(Color.TRANSPARENT);
                loadingStage.setScene(scene);
                loadingStage.show();
                try ( FileInputStream inputStream = new FileInputStream(file);  Workbook workbook = new XSSFWorkbook(inputStream)) {
                    Sheet classSheet = workbook.getSheetAt(0);
                    Sheet courseSheet = workbook.getSheetAt(1);
                    Row classTitleRow = classSheet.getRow(0);
                    Row courseTitleRow = courseSheet.getRow(0);
                    if (GF.verifyExcelFile(classTitleRow, courseTitleRow)) {
                        saved = ReadData(results, classSheet, courseSheet);
                    } else {
                        loadingStage.close();
                        saved = false;
                        GF.inforAlert("Invalid File", "The selected Excel file do not match stated parameters",
                                Alert.AlertType.ERROR);
                    }

                } catch (FileNotFoundException ex) {
                    loadingStage.close();
                    saved = false;
                    GF.inforAlert("File Error", ex.getMessage(), Alert.AlertType.ERROR);

                } catch (IOException ex) {
                    loadingStage.close();
                    saved = false;
                    GF.inforAlert("File Error", ex.getMessage(), Alert.AlertType.ERROR);
                }
            });
        } catch (Exception error) {
            loadingStage.close();
            GF.inforAlert("Excel Error", error.toString(), Alert.AlertType.ERROR);
        }
        ObservableMap<String, Object> data = FXCollections.observableHashMap();
        data.put("isSaved", saved);
        data.put("stage", loadingStage);
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
            if (nextRow != classTitleRow) {
                String titleOne = nextRow.getCell(0) != null || nextRow.getCell(0).getCellType() != CellType.BLANK
                        ? nextRow.getCell(0).getStringCellValue()
                        : "Blank";
                String titleTwo = nextRow.getCell(1) != null || nextRow.getCell(1).getCellType() != CellType.BLANK
                        ? nextRow.getCell(1).getStringCellValue()
                        : "Blank";
                String titleThree = nextRow.getCell(2) != null || nextRow.getCell(2).getCellType() != CellType.BLANK
                        ? nextRow.getCell(2).getStringCellValue()
                        : "Blank";
                String titleFour = nextRow.getCell(3) != null || nextRow.getCell(3).getCellType() != CellType.BLANK
                        ? nextRow.getCell(3).getStringCellValue()
                        : "Blank";
                String titleFive = nextRow.getCell(4) != null || nextRow.getCell(4).getCellType() != CellType.BLANK
                        ? nextRow.getCell(4).getStringCellValue()
                        : "Blank";
                List<String> courses = new ArrayList<>();
                courses.addAll(Arrays.asList(titleFive.split(",")));
                studentData.add(
                        new StudentsObject(titleOne, titleTwo, titleThree, titleFour, courses, department, timeStamp,
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
                String titleOne = nextRow.getCell(0) != null || nextRow.getCell(0).getCellType() != CellType.BLANK
                        ? nextRow.getCell(0).getStringCellValue()
                        : "Blank";
                String titleTwo = nextRow.getCell(1) != null || nextRow.getCell(1).getCellType() != CellType.BLANK
                        ? nextRow.getCell(1).getStringCellValue()
                        : "Blank";
                String titleThree = nextRow.getCell(2) != null || nextRow.getCell(2).getCellType() != CellType.BLANK
                        ? nextRow.getCell(2).getStringCellValue()
                        : "Blank";
                String titleFour = nextRow.getCell(3) != null || nextRow.getCell(3).getCellType() != CellType.BLANK
                        ? nextRow.getCell(3).getStringCellValue()
                        : "Blank";
                String titleFive = nextRow.getCell(4) != null || nextRow.getCell(4).getCellType() != CellType.BLANK
                        ? nextRow.getCell(4).getStringCellValue()
                        : "Blank";
                String titleSix = nextRow.getCell(5) != null || nextRow.getCell(5).getCellType() != CellType.BLANK
                        ? nextRow.getCell(5).getStringCellValue()
                        : "Blank";
                String titleSeven = nextRow.getCell(6) != null || nextRow.getCell(6).getCellType() != CellType.BLANK
                        ? nextRow.getCell(6).getStringCellValue()
                        : "Blank";
                courseData.add(
                        new CoursesObject(titleOne, titleTwo, titleThree, titleFour, titleFive, titleSix, titleSeven,
                                new ObjectId()));
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

    public void CreateClassCoursePair() {
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
                listOfCourses.add(new CoursesObject().fromDocument(cursor2.next()));
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
}
