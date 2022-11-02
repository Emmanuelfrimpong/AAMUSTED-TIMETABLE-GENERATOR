/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MongoServices;

import Objects.ClassCoursePair;
import Objects.Configuration;
import Objects.LiberalTimePair;
import Objects.PeriodsObject;
import Objects.TableObject;
import Objects.VenueTimePair;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import org.bson.Document;

/**
 *
 * @author emman
 */
public class TableGeneration {

    DatabaseServices DBServices = new DatabaseServices();
    List<VenueTimePair> ListOfVenues = new ArrayList<>();
    List<ClassCoursePair> ListOfClasses = new ArrayList<>();
    List<ClassCoursePair> ListOfSCClasses = new ArrayList<>();
    List<VenueTimePair> ListSCVenues = new ArrayList<>();
    List<TableObject> AllTables = new ArrayList<>();
    Configuration config;

    public  ObservableList<TableObject> getData() {
        config = DBServices.getConfig();
        MongoDatabase database = DBServices.databaseConnection().getDatabase("AAMUSTED_DB");
        MongoCollection<Document> CCPCollection = database.getCollection("ClassCoursePair");
        MongoCursor<Document> CCPCoursor = CCPCollection.find().iterator();
        while (CCPCoursor.hasNext()) {
            ListOfClasses.add(new ClassCoursePair().fromDocument(CCPCoursor.next()));
        }

        MongoCollection<Document> VTPCollection = database.getCollection("VenueTimePair");
        MongoCursor<Document> VTPCoursor = VTPCollection.find().iterator();
        while (VTPCoursor.hasNext()) {
            ListOfVenues.add(new VenueTimePair().fromDocument(VTPCoursor.next()));
        }

        Collections.shuffle(ListOfVenues);
        Collections.shuffle(ListOfVenues);
        Collections.shuffle(ListOfVenues);
        GetLiberalCourseVenue();
        getEveningList();
        getWeekendList();
        getRegular();
        return DBServices.getTables();
    }

    public void GetLiberalCourseVenue() {
        MongoDatabase database = DBServices.databaseConnection().getDatabase("AAMUSTED_DB");
        List<LiberalTimePair> ListOfLiberalCourses = new ArrayList<>();
        List<VenueTimePair> ListLiberalVenues = new ArrayList<>();
        MongoCollection<Document> LACollection = database.getCollection("LiberalCourseTimePair");
        MongoCursor<Document> LACoursor = LACollection.find().iterator();
        while (LACoursor.hasNext()) {
            ListOfLiberalCourses.add(LiberalTimePair.fromDocument(LACoursor.next()));
        }
        Collections.sort(ListOfVenues, (VenueTimePair o1, VenueTimePair o2) -> Integer.valueOf(o1.getVenueCapacity()).compareTo(Integer.valueOf(o2.getVenueCapacity())));
        Collections.reverse(ListOfVenues);
        if (config != null && config.getLaDay() != null && config.getLaPeriod() != null && !config.getLaDay().isEmpty() && !config.getLaPeriod().isEmpty()) {
            String LCday = config.getLaDay();
            String LCpreriod = config.getLaPeriod();

            List<VenueTimePair> AllLiberalCourseVenues = new ArrayList<>();
            for (int i = 0; i < ListOfVenues.size(); i++) {
                PeriodsObject periodsObject = PeriodsObject.fromDocument(ListOfVenues.get(i).getPeriod());
                String period = periodsObject.getPeriod();
                if (period.equals(LCpreriod) && ListOfVenues.get(i).getDay().equals(LCday)) {
                    AllLiberalCourseVenues.add(ListOfVenues.get(i));
                }
            }

            int numberOfRoom = ListOfLiberalCourses.size() <= AllLiberalCourseVenues.size() ? ListOfLiberalCourses.size() : ListOfVenues.size();
            Collections.sort(AllLiberalCourseVenues, (VenueTimePair o1, VenueTimePair o2) -> Integer.valueOf(o1.getVenueCapacity()).compareTo(Integer.valueOf(o2.getVenueCapacity())));
            Collections.reverse(AllLiberalCourseVenues);
            for (int index = 0; index < numberOfRoom; index++) {
                VenueTimePair VTP = AllLiberalCourseVenues.get(index);
                ListLiberalVenues.add(VTP);
            }
            for (VenueTimePair VTP : ListLiberalVenues) {
                ListOfVenues.remove(VTP);
            }

            GenerateLiberalTable(ListOfLiberalCourses, ListLiberalVenues);
        }

    }

    public void getEveningList() {
        List<ClassCoursePair> ListOfEveningClasses = new ArrayList<>();      
        for (int i = 0; i < ListOfClasses.size(); i++) {
            if (ListOfClasses.get(i).getType().toLowerCase().equals("evening")) {
                ListOfEveningClasses.add(ListOfClasses.get(i));
            }
        }
        System.out.println("Evening Clasess==================" + ListOfEveningClasses.size());
        List<VenueTimePair> ListAllEveningVenues = new ArrayList<>();
        for (int j = 0; j < ListOfVenues.size(); j++) {
            VenueTimePair VTP = ListOfVenues.get(j);
            PeriodsObject PO = PeriodsObject.fromDocument(VTP.getPeriod());
            if (VTP.isEve() && PO.isEve()) {
                ListAllEveningVenues.add(VTP);
            }
        }

        ObservableMap<String, Object> map = GenerateSpecial(ListOfEveningClasses, ListAllEveningVenues);
        ListOfEveningClasses = (List<ClassCoursePair>) map.get("class");
        ListAllEveningVenues = (List<VenueTimePair>) map.get("venue");
        Collections.sort(ListAllEveningVenues, (VenueTimePair o1, VenueTimePair o2) -> Integer.valueOf(o1.getVenueCapacity()).compareTo(Integer.valueOf(o2.getVenueCapacity())));
        Collections.reverse(ListAllEveningVenues);
       

        GenerateTable(ListOfEveningClasses, ListAllEveningVenues);
    }

    public void getWeekendList() {
        List<ClassCoursePair> ListOfWeekendClasses = new ArrayList<>();
  
        for (int i = 0; i < ListOfClasses.size(); i++) {
            if (ListOfClasses.get(i).getType().toLowerCase().equals("weekend")) {
                ListOfWeekendClasses.add(ListOfClasses.get(i));
            }
        }
        System.out.println("Weekend Clasess==================" + ListOfWeekendClasses.size());
        List<VenueTimePair> AllWeekEndVenues = new ArrayList<>();
        for (int j = 0; j < ListOfVenues.size(); j++) {
            Collections.shuffle(ListOfVenues);
            VenueTimePair VTP = ListOfVenues.get(j);
            PeriodsObject PO = PeriodsObject.fromDocument(VTP.getPeriod());
            if (VTP.isWnd() && PO.isWnd()) {
                AllWeekEndVenues.add(VTP);
            }
        }

        ObservableMap<String, Object> map = GenerateSpecial(ListOfWeekendClasses, AllWeekEndVenues);
        ListOfWeekendClasses = (List<ClassCoursePair>) map.get("class");
        AllWeekEndVenues = (List<VenueTimePair>) map.get("venue");
        Collections.sort(AllWeekEndVenues, (VenueTimePair o1, VenueTimePair o2) -> Integer.valueOf(o1.getVenueCapacity()).compareTo(Integer.valueOf(o2.getVenueCapacity())));
        Collections.reverse(AllWeekEndVenues);      
        GenerateTable(ListOfWeekendClasses, AllWeekEndVenues);

    }

    public void getRegular() {
        List<ClassCoursePair> ListOfRegularClasses = new ArrayList<>();
      
        for (int i = 0; i < ListOfClasses.size(); i++) {
            if (ListOfClasses.get(i).getType().toLowerCase().equals("regular")) {
                ListOfRegularClasses.add(ListOfClasses.get(i));
            }
        }
        System.out.println("Regular Clasess==================" + ListOfRegularClasses.size());
        List<VenueTimePair> AllRegularVenues = new ArrayList<>();
        for (int j = 0; j < ListOfVenues.size(); j++) {
            VenueTimePair VTP = ListOfVenues.get(j);
            PeriodsObject PO = PeriodsObject.fromDocument(VTP.getPeriod());
            if (VTP.isReg() && PO.isReg()) {
                AllRegularVenues.add(VTP);
            }
        }
        ListOfRegularClasses = (List<ClassCoursePair>) GenerateSpecial(ListOfRegularClasses, AllRegularVenues).get("class");
        AllRegularVenues = (List<VenueTimePair>) GenerateSpecial(ListOfRegularClasses, AllRegularVenues).get("venue");
        Collections.sort(AllRegularVenues, (VenueTimePair o1, VenueTimePair o2) -> Integer.valueOf(o1.getVenueCapacity()).compareTo(Integer.valueOf(o2.getVenueCapacity())));
        Collections.reverse(AllRegularVenues);

       
        GenerateTable(ListOfRegularClasses, AllRegularVenues);

    }

    public ObservableMap<String, Object> GenerateSpecial(List<ClassCoursePair> CCP, List<VenueTimePair> VTP) {
        List<ClassCoursePair> ListOfRegularSpecialClasses = new ArrayList<>();
        List<VenueTimePair> ListRegularSpecialVenues = new ArrayList<>();
        for (int i = 0; i < CCP.size(); i++) {
            ClassCoursePair ccp = CCP.get(i);
            if (!ccp.getCourseSpecialVenue().toLowerCase().equals("no")) {
                ListOfRegularSpecialClasses.add(ccp);
            }
        }
        for (int i = 0; i < VTP.size(); i++) {
            VenueTimePair vtp = VTP.get(i);
            if (ListOfRegularSpecialClasses.stream().anyMatch(a -> a.getCourseSpecialVenue().equals(vtp.getVenueName()))) {
                ListRegularSpecialVenues.add(vtp);
            }
        }
        System.out.println("\nSpecial Class======" + ListOfRegularSpecialClasses.size());
        System.out.println("Special Venue======" + ListRegularSpecialVenues.size());

        for (ClassCoursePair ccp : ListOfRegularSpecialClasses) {
            CCP.remove(ccp);
        }
        for (VenueTimePair vtp : ListRegularSpecialVenues) {
            VTP.remove(vtp);
        }
        MongoDatabase database = DBServices.databaseConnection().getDatabase("AAMUSTED_DB");
        MongoCollection<Document> collection = database.getCollection("Tables");
        for (ClassCoursePair ccp : ListOfRegularSpecialClasses) {
            for (VenueTimePair venue : ListRegularSpecialVenues) {
                PeriodsObject P = PeriodsObject.fromDocument(venue.getPeriod());
                String id = venue.getDay().replaceAll("\\s+", "") + ccp.getClassName().replaceAll("\\s+", "") + P.getPeriod().replaceAll("\\s+", "");
                boolean exist = false;
                for (TableObject to : AllTables) {
                    if (to.getUniqueId().equals(id)) {
                        exist = true;
                    }
                }
                if (!exist) {
                    PeriodsObject PO = PeriodsObject.fromDocument(venue.getPeriod());
                    TableObject TO = new TableObject();
                    TO.setCourseCode(ccp.getCourseCode());
                    TO.setDay(venue.getDay());
                    TO.setLecturer(ccp.getCourseLecturerName());
                    TO.setLevel(ccp.getClassLevel());
                    TO.setPeriod(PO.getPeriod());
                    TO.setStuClass(ccp.getClassName());
                    TO.setVenue(venue.getVenueName());
                    TO.setUniqueId(id);
                    AllTables.add(TO);
                    collection.findOneAndDelete(new Document("uniqueId", TO.getUniqueId()));
                    collection.insertOne(TO.toDocument());
                    ListRegularSpecialVenues.remove(venue);
                    break;
                }
            }
        }
        ObservableMap map = FXCollections.observableHashMap();
        map.put("class", CCP);
        map.put("venue", VTP);
        return map;

    }

    private List<VenueTimePair> GenerateTable(List<ClassCoursePair> CCP, List<VenueTimePair> VTP) {
        MongoDatabase database = DBServices.databaseConnection().getDatabase("AAMUSTED_DB");
        MongoCollection<Document> collection = database.getCollection("Tables");
        for (ClassCoursePair ccp : CCP) {
            for (VenueTimePair venue : VTP) {
                PeriodsObject P = PeriodsObject.fromDocument(venue.getPeriod());
                String id = venue.getDay().replaceAll("\\s+", "") + ccp.getClassName().replaceAll("\\s+", "") + P.getPeriod().replaceAll("\\s+", "");
                boolean exist = false;
                for (TableObject to : AllTables) {
                    if (to.getUniqueId().equals(id)) {
                        exist = true;
                    }
                }
                if (!exist) {
                    if ((Integer.parseInt(venue.getVenueCapacity()) >= Integer.parseInt(ccp.getClassSize()) - 20 || Integer.parseInt(venue.getVenueCapacity()) <= Integer.parseInt(ccp.getClassSize()) + 20) && venue.getIsDisabilityAccessible().toLowerCase().equalsIgnoreCase(ccp.getClassHasDisability().toLowerCase())) {
                        PeriodsObject PO = PeriodsObject.fromDocument(venue.getPeriod());
                        TableObject TO = new TableObject();
                        TO.setCourseCode(ccp.getCourseCode());
                        TO.setDay(venue.getDay());
                        TO.setLecturer(ccp.getCourseLecturerName());
                        TO.setLevel(ccp.getClassLevel());
                        TO.setPeriod(PO.getPeriod());
                        TO.setStuClass(ccp.getClassName());
                        TO.setVenue(venue.getVenueName());
                        TO.setUniqueId(id);
                        TO.setType(ccp.getType());                       
                        AllTables.add(TO);
                        collection.findOneAndDelete(new Document("uniqueId", TO.getUniqueId()));
                        collection.insertOne(TO.toDocument());
                        VTP.remove(venue);
                        break;
                    } else {
                        if (venue.getIsDisabilityAccessible().toLowerCase().equalsIgnoreCase(ccp.getClassHasDisability().toLowerCase())) {
                            PeriodsObject PO = PeriodsObject.fromDocument(venue.getPeriod());
                            TableObject TO = new TableObject();
                            TO.setCourseCode(ccp.getCourseCode());
                            TO.setDay(venue.getDay());
                            TO.setLecturer(ccp.getCourseLecturerName());
                            TO.setLevel(ccp.getClassLevel());
                            TO.setPeriod(PO.getPeriod());
                            TO.setStuClass(ccp.getClassName());
                            TO.setVenue(venue.getVenueName());
                            TO.setUniqueId(id);
                            TO.setType(ccp.getType());  
                            AllTables.add(TO);
                            collection.findOneAndDelete(new Document("uniqueId", TO.getUniqueId()));
                            collection.insertOne(TO.toDocument());
                            VTP.remove(venue);
                            break;
                        } else {
                            PeriodsObject PO = PeriodsObject.fromDocument(venue.getPeriod());
                            TableObject TO = new TableObject();
                            TO.setCourseCode(ccp.getCourseCode());
                            TO.setDay(venue.getDay());
                            TO.setLecturer(ccp.getCourseLecturerName());
                            TO.setLevel(ccp.getClassLevel());
                            TO.setPeriod(PO.getPeriod());
                            TO.setStuClass(ccp.getClassName());
                            TO.setVenue(venue.getVenueName());
                            TO.setUniqueId(id);
                            TO.setType(ccp.getType());  
                            AllTables.add(TO);
                            collection.findOneAndDelete(new Document("uniqueId", TO.getUniqueId()));
                            collection.insertOne(TO.toDocument());
                            VTP.remove(venue);
                            break;
                        }
                    }
                }

            }

        }
        return VTP;
    }

    private void GenerateLiberalTable(List<LiberalTimePair> LCP, List<VenueTimePair> LCV) {
        Random rand = new Random();
        System.out.println("Liberal Courses======================================" + LCP.size());
        MongoDatabase database = DBServices.databaseConnection().getDatabase("AAMUSTED_DB");
        MongoCollection<Document> collection = database.getCollection("Tables");
        collection.drop();
        for (LiberalTimePair lcp : LCP) {
            TableObject TO = new TableObject();
            VenueTimePair vtp = LCV.get(rand.nextInt(LCV.size()));
            PeriodsObject PO = PeriodsObject.fromDocument(vtp.getPeriod());
            TO.setCourseCode(lcp.getCourseCode());
            TO.setDay(vtp.getDay());
            TO.setLecturer(lcp.getLecturerName());
            TO.setLevel(lcp.getLevel());
            TO.setPeriod(PO.getPeriod());
            TO.setStuClass("Liberal Course");
            TO.setVenue(vtp.getVenueName());      
            TO.setUniqueId(vtp.getVenueName().replaceAll("\\s+", "") + lcp.getCourseCode().replaceAll("\\s+", ""));
            collection.findOneAndDelete(new Document("uniqueId", TO.getUniqueId()));
            collection.insertOne(TO.toDocument());
            LCV.remove(vtp);
        }
    }

}
