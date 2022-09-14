/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MongoServices;

import Controllers.StudentConfirmPageController;
import GlobalFunctions.GlobalFunctions;
import Objects.InitialStudentData;
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
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class DatabaseServices {

    private static double xOffset = 0;
    private static double yOffset = 0;
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

    public ObservableList<StudentsObject> getAllStudents(String databaseName) {
        MongoDatabase database = this.databaseConnection().getDatabase(databaseName);
        MongoCollection<Document> dataCollection = database.getCollection("Student_Class");
        ArrayList<StudentsObject> listOfStudents = new ArrayList<>();
        MongoCursor<Document> cursor = dataCollection.find().iterator();
        while (cursor.hasNext()) {
            listOfStudents.add(new StudentsObject().fromDocument(cursor.next()));
        }
        return FXCollections.observableArrayList(listOfStudents);

    }

    public void LoadStudentsToDatabase(Stage stage, File file,ObservableList<StudentsObject> list) throws FileNotFoundException, IOException {
        ButtonType submit = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to Upload this Student class?", submit, cancel);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setHeaderText(null);
        alert.getDialogPane().getStylesheets().add("/Styles/dialogStyle.css");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.orElse(cancel) == submit) {
            try ( FileInputStream inputStream = new FileInputStream(file);  Workbook workbook = new XSSFWorkbook(inputStream)) {
                Sheet firstSheet = workbook.getSheetAt(0);
                Row titleRow = firstSheet.getRow(0);
                if (GF.verifyStudentExcel(titleRow)) {
                    Iterator<Row> iterator = firstSheet.iterator();
                    ObservableList<InitialStudentData> data = FXCollections.observableArrayList();
                    while (iterator.hasNext()) {
                        Row nextRow = iterator.next();
                        Iterator<Cell> cellIterator = nextRow.iterator();
                        while (cellIterator.hasNext()) {
                            Cell cell = cellIterator.next();
                            cell.setCellType(CellType.STRING);
                        }
                        if (nextRow != titleRow) {
                            String titleOne = nextRow.getCell(0).getStringCellValue().toLowerCase();
                            String titleTwo = nextRow.getCell(1).getStringCellValue().toLowerCase();
                            String titleThree = nextRow.getCell(2).getStringCellValue().toLowerCase();
                            String titleFour = nextRow.getCell(3).getStringCellValue().toLowerCase();
                            String titleFive = nextRow.getCell(4).getStringCellValue().toLowerCase();
                            List<String> courses = new ArrayList<>();
                            courses.addAll(Arrays.asList(titleFive.split(",")));
                            data.add(new InitialStudentData(titleOne, titleTwo, titleThree, titleFour, courses));
                        }
                    }
                    Stage newStage = new Stage();
                    newStage.setUserData(data);
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/FrontEnds/studentConfirmPage.fxml"));
                    Parent root = fxmlLoader.load();
                    StudentConfirmPageController controller = fxmlLoader.<StudentConfirmPageController>getController();
                    controller.showData(data, stage);
                    controller.loadData(list);
                    Scene scene = new Scene(root);
                    newStage.initStyle(StageStyle.UNDECORATED);
                    newStage.initStyle(StageStyle.TRANSPARENT);
                    scene.setFill(Color.TRANSPARENT);
                    root.setOnMousePressed((MouseEvent event) -> {
                        xOffset = event.getSceneX();
                        yOffset = event.getSceneY();
                    });
                    root.setOnMouseDragged((MouseEvent event) -> {
                        newStage.setX(event.getScreenX() - xOffset);
                        newStage.setY(event.getScreenY() - yOffset);
                    });
                    newStage.setScene(scene);

                    newStage.show();

                } else {
                    GF.inforAlert("Invalid File", "The selected Excel file do not match stated parameters", Alert.AlertType.ERROR);
                }

            }
        }
    }

    public ObservableList<StudentsObject> saveStudentClass(StudentsObject data) {
        MongoDatabase database = this.databaseConnection().getDatabase("AAMUSTED_DB");
        MongoCollection<Document> dataCollection = database.getCollection("Student_Class");
        Document admin = dataCollection.find(new Document("name", data.getName())).first();
        ArrayList<StudentsObject> listOfStudents = new ArrayList<>();
        if (admin == null || admin.isEmpty()) {
            dataCollection.insertOne(data.toBsonDocs());

            MongoCursor<Document> cursor = dataCollection.find().iterator();
            while (cursor.hasNext()) {
                listOfStudents.add(new StudentsObject().fromDocument(cursor.next()));
            }
        }

        return FXCollections.observableArrayList(listOfStudents);
    }
}
