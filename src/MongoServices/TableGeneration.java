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
import java.util.Optional;
import java.util.Random;
import javafx.collections.FXCollections;
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
    Configuration config;

    public void getData() {
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
        List<VenueTimePair> ListEveningVenues = new ArrayList<>();
        for (int i = 0; i < ListOfClasses.size(); i++) {
            if (ListOfClasses.get(i).getType().toLowerCase().equals("evening")) {
                ListOfEveningClasses.add(ListOfClasses.get(i));
            }
        }
        System.out.println("Total Venues========" + ListOfVenues.size());
        System.out.println("\nEvening Course========" + ListOfEveningClasses.size());

        List<VenueTimePair> ListAllEveningVenues = new ArrayList<>();
        for (int j = 0; j < ListOfVenues.size(); j++) {

            VenueTimePair VTP = ListOfVenues.get(j);
            PeriodsObject PO = PeriodsObject.fromDocument(VTP.getPeriod());
            if (VTP.isEve() && PO.isEve()) {
                ListAllEveningVenues.add(VTP);
            }
        }
        System.out.println("Evening Course Venue========" + ListAllEveningVenues.size());

        ObservableMap<String, Object> map = GenerateSpecial(ListOfEveningClasses, ListAllEveningVenues);
        ListOfEveningClasses = (List<ClassCoursePair>) map.get("class");
        ListAllEveningVenues = (List<VenueTimePair>) map.get("venue");
        int numberOfRoom = ListOfEveningClasses.size() <= ListAllEveningVenues.size() ? ListOfEveningClasses.size() : ListAllEveningVenues.size();
        Collections.sort(ListAllEveningVenues, (VenueTimePair o1, VenueTimePair o2) -> Integer.valueOf(o1.getVenueCapacity()).compareTo(Integer.valueOf(o2.getVenueCapacity())));
        Collections.reverse(ListAllEveningVenues);
        for (int i = 0; i < numberOfRoom; i++) {
            VenueTimePair VTP = ListAllEveningVenues.get(i);
            ListEveningVenues.add(VTP);
        }
        //System.out.println("Remain Venues========" + ListOfVenues.size());           
        System.out.println("\nActual Evening Course Venue========" + ListEveningVenues.size());
        GenerateTable(ListOfEveningClasses, ListEveningVenues, "");
    }

    public void getWeekendList() {
        List<ClassCoursePair> ListOfWeekendClasses = new ArrayList<>();
        List<VenueTimePair> ListWeekendVenues = new ArrayList<>();
        for (int i = 0; i < ListOfClasses.size(); i++) {
            if (ListOfClasses.get(i).getType().toLowerCase().equals("weekend")) {
                ListOfWeekendClasses.add(ListOfClasses.get(i));
            }
        }

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
        int numberOfRoom = ListOfWeekendClasses.size() <= AllWeekEndVenues.size() ? ListOfWeekendClasses.size() : AllWeekEndVenues.size();
        Collections.sort(AllWeekEndVenues, (VenueTimePair o1, VenueTimePair o2) -> Integer.valueOf(o1.getVenueCapacity()).compareTo(Integer.valueOf(o2.getVenueCapacity())));
        Collections.reverse(AllWeekEndVenues);
        for (int i = 0; i < numberOfRoom; i++) {
            VenueTimePair VTP = AllWeekEndVenues.get(i);
            ListWeekendVenues.add(VTP);
        }
        GenerateTable(ListOfWeekendClasses, ListWeekendVenues, "");

    }

    public void getRegular() {
        List<ClassCoursePair> ListOfRegularClasses = new ArrayList<>();
        List<VenueTimePair> ListRegularVenues = new ArrayList<>();
        for (int i = 0; i < ListOfClasses.size(); i++) {
            if (ListOfClasses.get(i).getType().toLowerCase().equals("regular")) {
                ListOfRegularClasses.add(ListOfClasses.get(i));
            }
        }
       
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
        int numberOfRoom = ListOfRegularClasses.size() <= AllRegularVenues.size() ? ListOfRegularClasses.size() : AllRegularVenues.size();
        for (int i = 0; i < numberOfRoom; i++) {
            Collections.shuffle(AllRegularVenues);
            VenueTimePair VTP = AllRegularVenues.get(i);
            ListRegularVenues.add(VTP);
        }    
        GenerateTable(ListOfRegularClasses, ListRegularVenues, "");
        
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

        for (ClassCoursePair ccp : ListOfRegularSpecialClasses) {
            CCP.remove(ccp);
        }
        for (VenueTimePair vtp : ListRegularSpecialVenues) {
            VTP.remove(vtp);
        }

        GenerateTable(ListOfRegularSpecialClasses, ListRegularSpecialVenues, "special");
        System.out.println("\n\nSpecial class===============" + ListOfRegularSpecialClasses.size());
        System.out.println("Special venue===============" + ListRegularSpecialVenues.size());
        ObservableMap map = FXCollections.observableHashMap();
        map.put("class", CCP);
        map.put("venue", VTP);
        return map;

    }

    private void GenerateTable(List<ClassCoursePair> CCP, List<VenueTimePair> VTP, String type) {
        Random rand = new Random();
        MongoDatabase database = DBServices.databaseConnection().getDatabase("AAMUSTED_DB");
        MongoCollection<Document> collection = database.getCollection("Tables");
        if (type.equals("special")) {
            for (ClassCoursePair ccp : CCP) {
                TableObject TO = new TableObject();
                VenueTimePair vtp = VTP.get(rand.nextInt(VTP.size()));
                PeriodsObject PO = PeriodsObject.fromDocument(vtp.getPeriod());
                TO.setCourseCode(ccp.getCourseCode());
                TO.setDay(vtp.getDay());
                TO.setLecturer(ccp.getCourseLecturerName());
                TO.setLevel(ccp.getClassLevel());
                TO.setPeriod(PO.getPeriod());
                TO.setStuClass(ccp.getClassName());
                TO.setVenue(vtp.getVenueName());
                TO.setUniqueId(vtp.getUniqueId().replaceAll("\\s+", "") + ccp.getCourseCode().replaceAll("\\s+", "") + ccp.get_id().replaceAll("\\s+", ""));

                collection.findOneAndDelete(new Document("uniqueId", TO.getUniqueId()));
                collection.insertOne(TO.toDocument());
                VTP.remove(vtp);
            }
        } else {
            for (ClassCoursePair ccp : CCP) {
                VenueTimePair vtp = new VenueTimePair();
                Optional<VenueTimePair> option;
                TableObject TO = new TableObject();
                option = VTP.stream().filter(a -> (Integer.parseInt(a.getVenueCapacity()) >= Integer.parseInt(ccp.getClassSize()) - 20 || Integer.parseInt(a.getVenueCapacity()) <= Integer.parseInt(ccp.getClassSize()) + 20) && a.getIsDisabilityAccessible().toLowerCase().equalsIgnoreCase(ccp.getClassHasDisability().toLowerCase())).findAny();
                if (option != null && option.isPresent()) {
                    vtp = option.get();
                    PeriodsObject PO = PeriodsObject.fromDocument(vtp.getPeriod());
                    TO.setCourseCode(ccp.getCourseCode());
                    TO.setDay(vtp.getDay());
                    TO.setLecturer(ccp.getCourseLecturerName());
                    TO.setLevel(ccp.getClassLevel());
                    TO.setPeriod(PO.getPeriod());
                    TO.setStuClass(ccp.getClassName());
                    TO.setVenue(vtp.getVenueName());
                    TO.setUniqueId(vtp.getUniqueId().replaceAll("\\s+", "") + ccp.getCourseCode().replaceAll("\\s+", "") + ccp.get_id().replaceAll("\\s+", ""));
                    collection.findOneAndDelete(new Document("uniqueId", TO.getUniqueId()));
                    collection.insertOne(TO.toDocument());
                    VTP.remove(vtp);
                } else {
                    option = VTP.stream().filter(a -> Integer.parseInt(a.getVenueCapacity()) >= Integer.parseInt(ccp.getClassSize()) - 20 || Integer.parseInt(a.getVenueCapacity()) <= Integer.parseInt(ccp.getClassSize()) + 20).findAny();
                    if (option != null && option.isPresent()) {
                        vtp = option.get();
                        PeriodsObject PO = PeriodsObject.fromDocument(vtp.getPeriod());
                        TO.setCourseCode(ccp.getCourseCode());
                        TO.setDay(vtp.getDay());
                        TO.setLecturer(ccp.getCourseLecturerName());
                        TO.setLevel(ccp.getClassLevel());
                        TO.setPeriod(PO.getPeriod());
                        TO.setStuClass(ccp.getClassName());
                        TO.setVenue(vtp.getVenueName());
                        TO.setUniqueId(vtp.getUniqueId().replaceAll("\\s+", "") + ccp.getCourseCode().replaceAll("\\s+", "") + ccp.get_id().replaceAll("\\s+", ""));
                        collection.findOneAndDelete(new Document("uniqueId", TO.getUniqueId()));
                        collection.insertOne(TO.toDocument());
                        VTP.remove(vtp);
                    } else {
                        option = VTP.stream().filter(a -> a.getIsDisabilityAccessible().toLowerCase().equalsIgnoreCase(ccp.getClassHasDisability().toLowerCase())).findAny();
                        if (option != null && option.isPresent()) {
                            vtp = option.get();
                            PeriodsObject PO = PeriodsObject.fromDocument(vtp.getPeriod());
                            TO.setCourseCode(ccp.getCourseCode());
                            TO.setDay(vtp.getDay());
                            TO.setLecturer(ccp.getCourseLecturerName());
                            TO.setLevel(ccp.getClassLevel());
                            TO.setPeriod(PO.getPeriod());
                            TO.setStuClass(ccp.getClassName());
                            TO.setVenue(vtp.getVenueName());
                            TO.setUniqueId(vtp.getUniqueId().replaceAll("\\s+", "") + ccp.getCourseCode().replaceAll("\\s+", "") + ccp.get_id().replaceAll("\\s+", ""));
                            collection.findOneAndDelete(new Document("uniqueId", TO.getUniqueId()));
                            collection.insertOne(TO.toDocument());
                            VTP.remove(vtp);
                        } else {
                            PeriodsObject PO = PeriodsObject.fromDocument(vtp.getPeriod());
                            TO.setCourseCode(ccp.getCourseCode());
                            TO.setDay(vtp.getDay());
                            TO.setLecturer(ccp.getCourseLecturerName());
                            TO.setLevel(ccp.getClassLevel());
                            TO.setPeriod(PO.getPeriod());
                            TO.setStuClass(ccp.getClassName());
                            TO.setVenue(vtp.getVenueName());
                            TO.setUniqueId(vtp.getUniqueId().replaceAll("\\s+", "") + ccp.getCourseCode().replaceAll("\\s+", "") + ccp.get_id().replaceAll("\\s+", ""));
                            collection.findOneAndDelete(new Document("uniqueId", TO.getUniqueId()));
                            collection.insertOne(TO.toDocument());
                            VTP.remove(vtp);
                        }
                    }
                }
            }
        }

    }

    private void GenerateLiberalTable(List<LiberalTimePair> LCP, List<VenueTimePair> LCV) {
        Random rand = new Random();
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
            TO.setStuClass("");
            TO.setVenue(vtp.getVenueName());
            TO.setUniqueId(vtp.getUniqueId().replaceAll("\\s+", "") + lcp.getCourseCode().replaceAll("\\s+", ""));
            collection.findOneAndDelete(new Document("uniqueId", TO.getUniqueId()));
            collection.insertOne(TO.toDocument());
            LCV.remove(vtp);
        }
    }

}
