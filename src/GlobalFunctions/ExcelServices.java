/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GlobalFunctions;

import Objects.ExcellHeaders;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;
import javax.swing.JFileChooser;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author emman
 */
public class ExcelServices {

    FileOutputStream fileOut;
    GlobalFunctions GF = new GlobalFunctions();

    public void ExportDepartment() {

        List<String> departments = new ArrayList();
        departments.add("For Department");
        departments.add("For Libral/African Studies");
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
            if (results.equals("For Department")) {
                createForDepartment();
            } else {
                createForAfrican();
            }
        });

    }

    private void createForDepartment() {
        final String folder = new JFileChooser().getFileSystemView().getDefaultDirectory().toString() + "/TIME TABLE GEN";
        Path path = Paths.get(folder);
        try {
            Files.createDirectories(path);
        } catch (IOException ex) {
            Logger.getLogger(ExcelServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        final String filename = folder + "/department.xlsx";
        XSSFWorkbook workbook = new XSSFWorkbook();
        try {
            File file = new File(filename);
            if (file.exists()) {
                file.delete();
            }
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setLocked(true);
            XSSFFont font = workbook.createFont();
            font.setFontName(HSSFFont.FONT_ARIAL);
            font.setFontHeightInPoints((short) 10);
            font.setBold(true);
            headerStyle.setFont(font);

            CellStyle unlockedCellStyle = workbook.createCellStyle();
            unlockedCellStyle.setLocked(false);
            CellStyle lockedCellStyle = workbook.createCellStyle();
            lockedCellStyle.setLocked(true);

            //create sheet one=======================================================================
            XSSFSheet sheet = workbook.createSheet("Student Classes");
            XSSFRow rowhead = sheet.createRow((short) 0);
            int width = 25;
            sheet.setDefaultColumnWidth(width);

            Cell headerCell1 = rowhead.createCell(0);
            headerCell1.setCellValue( ExcellHeaders.getLevel());
            headerCell1.setCellStyle(headerStyle);

            Cell headerCell2 = rowhead.createCell(1);
            headerCell2.setCellValue(ExcellHeaders.getClassName());
            headerCell2.setCellStyle(headerStyle);

            Cell headerCell3 = rowhead.createCell(2);
            headerCell3.setCellValue(ExcellHeaders.getClassSize());
            headerCell3.setCellStyle(headerStyle);

            Cell headerCell4 = rowhead.createCell(3);
            headerCell4.setCellValue(ExcellHeaders.getHasDisability());
            headerCell4.setCellStyle(headerStyle);

            Cell headerCell5 = rowhead.createCell(4);
            headerCell5.setCellValue(ExcellHeaders.getCourses());
            headerCell5.setCellStyle(headerStyle);
            sheet.createFreezePane(0, 1);
            //end of sheet one======================================================================
            //new sheet==============================================================================
            XSSFSheet sheet2 = workbook.createSheet("Courses");
            //sheet2.protectSheet("");
            XSSFRow rowhead2 = sheet2.createRow((short) 0);
            sheet2.setDefaultColumnWidth(width);
            Cell sheet2Header1 = rowhead2.createCell(0);
            sheet2Header1.setCellValue(ExcellHeaders.getCourseCode());
            sheet2Header1.setCellStyle(headerStyle);

            Cell sheet2Header2 = rowhead2.createCell(1);
            sheet2Header2.setCellValue(ExcellHeaders.getCouseTitle());
            sheet2Header2.setCellStyle(headerStyle);

            Cell sheet2Header3 = rowhead2.createCell(2);
            sheet2Header3.setCellValue(ExcellHeaders.getCreaditHourse());
            sheet2Header3.setCellStyle(headerStyle);

            Cell sheet2Header4 = rowhead2.createCell(3);
            sheet2Header4.setCellValue(ExcellHeaders.getSpecialVenue());
            sheet2Header4.setCellStyle(headerStyle);

            Cell sheet2Header5 = rowhead2.createCell(4);
            sheet2Header5.setCellValue(ExcellHeaders.getLecturerName());
            sheet2Header5.setCellStyle(headerStyle);

            Cell sheet2Header6 = rowhead2.createCell(5);
            sheet2Header6.setCellValue(ExcellHeaders.getLecturerEmail());
            sheet2Header6.setCellStyle(headerStyle);

            Cell sheet2Header7 = rowhead2.createCell(6);
            sheet2Header7.setCellValue(ExcellHeaders.getLecturerPhone());
            sheet2Header7.setCellStyle(headerStyle);
            fileOut = new FileOutputStream(filename);
            workbook.write(fileOut);
        } catch (IOException ex) {
            GF.inforAlert("Error", ex.getMessage(), Alert.AlertType.ERROR);
        } finally {
            try {
                fileOut.close();
                workbook.close();
            } catch (IOException x) {

            }
        }
    }

    private void createForAfrican() {
        final String folder = new JFileChooser().getFileSystemView().getDefaultDirectory().toString() + "/TIME TABLE GEN";
        Path path = Paths.get(folder);
        try {
            Files.createDirectories(path);
        } catch (IOException ex) {
            Logger.getLogger(ExcelServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        final String filename = folder + "/AfricanStudies.xlsx";
        XSSFWorkbook workbook = new XSSFWorkbook();
        try {
            File file = new File(filename);
            if (file.exists()) {
                file.delete();
            }
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setLocked(true);
            XSSFFont font = workbook.createFont();
            font.setFontName(HSSFFont.FONT_ARIAL);
            font.setFontHeightInPoints((short) 10);
            font.setBold(true);
            headerStyle.setFont(font);

            CellStyle unlockedCellStyle = workbook.createCellStyle();
            unlockedCellStyle.setLocked(false);
            CellStyle lockedCellStyle = workbook.createCellStyle();
            lockedCellStyle.setLocked(true);
            int width = 25;

            //new sheet==============================================================================
            XSSFSheet sheet2 = workbook.createSheet("Courses");
            //sheet2.protectSheet("");
            XSSFRow rowhead2 = sheet2.createRow((short) 0);
            sheet2.setDefaultColumnWidth(width);
            Cell sheet2Header1 = rowhead2.createCell(0);
            sheet2Header1.setCellValue(ExcellHeaders.getCourseCode());
            sheet2Header1.setCellStyle(headerStyle);

            Cell sheet2Header2 = rowhead2.createCell(1);
            sheet2Header2.setCellValue(ExcellHeaders.getCouseTitle());
            sheet2Header2.setCellStyle(headerStyle);

            Cell sheet2Header3 = rowhead2.createCell(2);
            sheet2Header3.setCellValue(ExcellHeaders.getCreaditHourse());
            sheet2Header3.setCellStyle(headerStyle);

            Cell sheet2Header4 = rowhead2.createCell(3);
            sheet2Header4.setCellValue(ExcellHeaders.getSpecialVenue());
            sheet2Header4.setCellStyle(headerStyle);

            Cell sheet2Header5 = rowhead2.createCell(4);
            sheet2Header5.setCellValue(ExcellHeaders.getLecturerName());
            sheet2Header5.setCellStyle(headerStyle);

            Cell sheet2Header6 = rowhead2.createCell(5);
            sheet2Header6.setCellValue(ExcellHeaders.getLecturerEmail());
            sheet2Header6.setCellStyle(headerStyle);

            Cell sheet2Header7 = rowhead2.createCell(6);
            sheet2Header7.setCellValue(ExcellHeaders.getLecturerPhone());
            sheet2Header7.setCellStyle(headerStyle);
            fileOut = new FileOutputStream(filename);
            workbook.write(fileOut);
        } catch (IOException ex) {
            GF.inforAlert("Error", ex.getMessage(), Alert.AlertType.ERROR);
        } finally {
            try {
                fileOut.close();
                workbook.close();
            } catch (IOException x) {

            }
        }
    }
}
