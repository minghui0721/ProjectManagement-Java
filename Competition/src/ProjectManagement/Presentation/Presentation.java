/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ProjectManagement.Presentation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author ALAN NG
 */
public class Presentation {
    private String module;
    private String presentDate;
    private int presentTime;
    private String extraNote;
    private boolean status;
    
    static ArrayList<Presentation> data = new ArrayList<>();
    
    
    public Presentation(String module, String presentDate, int presentTime, String extraNote, boolean status){
        this.module = module;
        this.presentDate = presentDate;
        this.presentTime = presentTime;
        this.extraNote = extraNote;
        this.status = status;
    }
    
    public static String findPresentDate(String selectedId,String selectedModule) {
         try (BufferedReader br = new BufferedReader(new FileReader("presentationSlot.txt"))) {
         String line;
         while ((line = br.readLine()) != null) {
             String[] data = line.split("\\|");
             if (data[0].equals(selectedId) && data[2].equals(selectedModule)) {
                 String lecturerID = data[1];
                 String date = data[3];
                 String time = data[4];
                 String status = data[6];
                 String reason = data[7];
                 String note =data[5];
                 
                 if (data[5].equals("")) {
                     note = "[No additional note]";
                     return  lecturerID+ "|" +date +"|"+ time + "|" + note + "|" + status + "|" + reason;
                 }else{
                     note = data[5];
                     return  lecturerID+ "|" +date +"|"+ time + "|" + note + "|" + status + "|" + reason;
                 }
                  
             }
         } 
             
         }catch(IOException e) {
            e.printStackTrace();     
         }
         return "Lecture not found";
        }
    
         
        //calculate total request
        public static int totalRequest(String lecturerID) {
            int countTotalRequest = 0; // Initialize count to 0
            try (BufferedReader reader = new BufferedReader(new FileReader("presentationSlot.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] components = line.split("\\|");

                    if (components.length == 8) {
                        String lecturerid = components[1];
                        String statusString = components[6];

                        if (statusString.equalsIgnoreCase("false") && lecturerid.equalsIgnoreCase(lecturerID)) {
                            countTotalRequest++; // Increment count if status is false and lecturer ID matches
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Error Occur.");
            }
            return countTotalRequest; // Return the total count
        }

    
    // setters
    public void setModule(String module){
        this.module = module;
    }
    
    public void setPresentDate(String presentDate){
        this.presentDate = presentDate;
    }
    
    public void setPresentTime(int presentTime){
        this.presentTime = presentTime;
    }
    
    public void setExtraNote(String extraNote){
        this.extraNote = extraNote;
    }
    public void setStatus(boolean status){
        this.status = status;
    }
    
    // getters
    public String getModule(){
        return module;
    }
    
    public String getPresentDate(){
        return presentDate;
    }
    
    public int getPresentTime(){
        return presentTime;
    }
    
    public String getExtraNote(){
        return extraNote;
    }
    
    public boolean getStatus(){
        return status;
    }
    
    
    //Method
    public void schedulePresentation(Date presentDate){

    }
    
    public void updatePresentationModule(String module){
        
    }
    
    public void updateStatus(boolean status){
        
    }
    
    public void checkExistingBooking(){
        
    }
    
    public static void addBookingDetails(String studentID, String lectureID, String module, String presentDate, String presentTime, String extraNote){
        
        Boolean status = false;
        
        try{
        FileWriter fw = new FileWriter("presentationSlot.txt",true);
        PrintWriter b = new PrintWriter(fw);
        String bookingDetail = studentID + "|" + lectureID + "|" + module + "|" + presentDate + "|" + presentTime + "|" + extraNote + "|" + status + "|null";
        b.println(bookingDetail);
        JOptionPane.showMessageDialog(null, "Book presentation time successfully!");
        b.close();
        fw.close();
        
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
    }
    
    public static String convertDate(Date presentDate){
        
        // Convert java.util.Date to java.time.LocalDate
        LocalDate localDate = presentDate.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        // Create a DateTimeFormatter
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        // Format LocalDate to string using formatter
        String formattedDateString = localDate.format(dateFormatter);
        return formattedDateString;
        
    }
    
    public static String convertTime(Date timeValue){
        
        // Convert the java.util.Date object to java.time.LocalTime
        LocalTime time = timeValue.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalTime();
        // Create a DateTimeFormatter for the desired time format
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");
        // Format the LocalTime object into a string using the formatter
        String formattedTime = time.format(timeFormatter);
        
        return formattedTime;
    }
        
    
    //lecturer read data
    public static List<String> readDataFromFile(String filePath) {
    List<String> data = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = br.readLine()) != null) {
            data.add(line);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return data;
}
    
}
