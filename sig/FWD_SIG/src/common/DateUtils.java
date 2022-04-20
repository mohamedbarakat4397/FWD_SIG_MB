package common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

public class DateUtils {
    
        public static Date formatDate(String date){
        Date formattedDate=null;  
        try {
            formattedDate = new SimpleDateFormat("dd-MM-yyyy").parse(date);
        } catch (ParseException ex) {
            Logger.getLogger(DateUtils.class.getName()).log(Level.SEVERE, null, ex);
        }
        return formattedDate;
    }
        
                public static String readDate(Date date){
        String formattedDate="";  
formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(date);
        return formattedDate;
    }
                
                public static void validateDate(String date) throws ParseException{
                    new SimpleDateFormat("dd-MM-yyyy").parse(date);
                }
 
}
