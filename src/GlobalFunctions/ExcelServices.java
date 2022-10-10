/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GlobalFunctions;

import Controllers.DepartmentFilesController;
import Objects.CoursesObject;
import Objects.ExcellHeaders;
import Objects.StudentsObject;
import java.awt.Desktop;
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
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
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

    public void ExportDepartment(String results) {
        LoadingDailog loading = new LoadingDailog("Downloading Data........");
        Service<File> service = new Service< File>() {
            @Override
            protected Task< File> createTask() {
                return new Task<File>() {
                    @Override
                    protected File call() throws Exception {
                        if (results.equals("For Department")) {
                            return createForDepartment();
                        } else {
                            return createForAfrican();
                        }

                    }
                };
            }
        };
        service.setOnRunning((WorkerStateEvent event) -> loading.show());
        service.setOnSucceeded((WorkerStateEvent event) -> {
            loading.close();
            try {
                String file = new JFileChooser().getFileSystemView().getDefaultDirectory().toString() + "/TIME TABLE GEN";
                if (new File(file).exists()) {
                    Desktop.getDesktop().open(new File(file));
                }
            } catch (IOException ex) {
                Logger.getLogger(DepartmentFilesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        service.start();

    }

    private File createForDepartment() {
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
            headerCell1.setCellValue(ExcellHeaders.getLevel());
            headerCell1.setCellStyle(headerStyle);

            Cell headerCell6 = rowhead.createCell(1);
            headerCell6.setCellValue(ExcellHeaders.getType());
            headerCell6.setCellStyle(headerStyle);

            Cell headerCell2 = rowhead.createCell(2);
            headerCell2.setCellValue(ExcellHeaders.getClassName());
            headerCell2.setCellStyle(headerStyle);

            Cell headerCell3 = rowhead.createCell(3);
            headerCell3.setCellValue(ExcellHeaders.getClassSize());
            headerCell3.setCellStyle(headerStyle);

            Cell headerCell4 = rowhead.createCell(4);
            headerCell4.setCellValue(ExcellHeaders.getHasDisability());
            headerCell4.setCellStyle(headerStyle);

            Cell headerCell5 = rowhead.createCell(5);
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
        File file = new File(folder);
        return file;

    }

    private File createForAfrican() {
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
            return new File("");
        } finally {
            try {
                fileOut.close();
                workbook.close();

            } catch (IOException x) {

            }
            return new File(folder);
        }

    }

    public void ExportVenue() {
        LoadingDailog loading = new LoadingDailog("Downloading Data........");
        Service<File> service = new Service< File>() {
            @Override
            protected Task< File> createTask() {
                return new Task<File>() {
                    @Override
                    protected File call() throws Exception {
                        return createVenueFile();
                    }
                };
            }
        };
        service.setOnRunning((WorkerStateEvent event) -> loading.show());
        service.setOnSucceeded((WorkerStateEvent event) -> {
            loading.close();
            try {
                String file = new JFileChooser().getFileSystemView().getDefaultDirectory().toString() + "/TIME TABLE GEN";
                if (new File(file).exists()) {
                    Desktop.getDesktop().open(new File(file));
                }
            } catch (IOException ex) {
                Logger.getLogger(DepartmentFilesController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        service.start();
    }

    private File createVenueFile() {
        final String folder = new JFileChooser().getFileSystemView().getDefaultDirectory().toString() + "/TIME TABLE GEN";
        Path path = Paths.get(folder);
        try {
            Files.createDirectories(path);
        } catch (IOException ex) {
            Logger.getLogger(ExcelServices.class.getName()).log(Level.SEVERE, null, ex);
        }
        final String filename = folder + "/venues.xlsx";
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
            XSSFSheet sheet2 = workbook.createSheet("Venues");
            //sheet2.protectSheet("");
            XSSFRow rowhead2 = sheet2.createRow((short) 0);
            sheet2.setDefaultColumnWidth(width);
            
            Cell sheet2Header1 = rowhead2.createCell(0);
            sheet2Header1.setCellValue(ExcellHeaders.getRoomName());
            sheet2Header1.setCellStyle(headerStyle);

            Cell sheet2Header2 = rowhead2.createCell(1);
            sheet2Header2.setCellValue(ExcellHeaders.getCapacity());
            sheet2Header2.setCellStyle(headerStyle);

            Cell sheet2Header3 = rowhead2.createCell(2);
            sheet2Header3.setCellValue(ExcellHeaders.getDisbility());
            sheet2Header3.setCellStyle(headerStyle);        
            fileOut = new FileOutputStream(filename);
            workbook.write(fileOut);

        } catch (IOException ex) {
            GF.inforAlert("Error", ex.getMessage(), Alert.AlertType.ERROR);
            return new File("");
        } finally {
            try {
                fileOut.close();
                workbook.close();

            } catch (IOException x) {

            }
            return new File(folder);
        }

    }
}
