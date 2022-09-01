/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MongoServices;

import GlobalFunctions.GlobalFunctions;
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
import java.util.Iterator;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

/**
 *
 * @author emman
 */
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

    public ObservableList<StudentsObject> getAllStudents(String databaseName) {
        MongoDatabase database = this.databaseConnection().getDatabase(databaseName);
        MongoCollection<Document> dataCollection = database.getCollection("Students");
        ArrayList<StudentsObject> listOfStudents = new ArrayList<>();
        MongoCursor<Document> cursor = dataCollection.find().iterator();
        while (cursor.hasNext()) {
            listOfStudents.add(new StudentsObject().fromDocument(cursor.next()));
        }
        return FXCollections.observableArrayList(listOfStudents);
        // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void LoadStudentsToDatabase(Stage stage, File file) throws FileNotFoundException, IOException {
        ButtonType submit = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to Upload this Student class?", submit, cancel);
        alert.initStyle(StageStyle.UNDECORATED);
        alert.setHeaderText(null);
        alert.getDialogPane().getStylesheets().add("/styles/dialog.css");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.orElse(cancel) == submit) {
            FileInputStream inputStream = new FileInputStream(file);
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet firstSheet = workbook.getSheetAt(0);
            Row titleRow = firstSheet.getRow(0);

            if (GF.verifyStudentExcel(titleRow)) {
                Iterator<Row> iterator = firstSheet.iterator();
                while (iterator.hasNext()) {
                    Row nextRow = iterator.next();
                    Iterator<Cell> cellIterator = nextRow.cellIterator();
                    System.out.println();
                }
            } else {
                GF.inforAlert("Invalid File", "The selected Excel file do not match stated parameters", Alert.AlertType.ERROR);
            }

            workbook.close();
            inputStream.close();
        }
    }
}
