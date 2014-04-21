/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cvent.rfp.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 *
 * @author yxie
 */
public class ValidationHelper {
    
    public ValidationHelper() {}
    
    public static boolean isLookUpIdValid(String tableName, int luId)
    {
        return true;
    }
    
    /**
     *
     * @param agendaItemName
     * @param agendaItemTypeId
     * @param agendaItemSetupId
     * @param agendaAddlNote
     * @param startTime
     * @param endTime
     * @param roomSize
     * @param attendeeCount
     * @param infoRequiredFlag
     * @param twentyFourHrHoldFlag
     * @param hostVenueFlag
     * @param dayNumber
     * @return
     * @throws java.lang.Exception
     */
    public static List<String> validateAgendaItem(String agendaItemName, int agendaItemTypeId, int agendaItemSetupId, String agendaAddlNote, String startTime, String endTime, int roomSize, int attendeeCount, boolean infoRequiredFlag, boolean twentyFourHrHoldFlag, boolean hostVenueFlag, String dayNumber) throws Exception
    {
        List<String> errorList = new ArrayList<>();
        List<Integer> minuteList = new ArrayList<>(Arrays.asList(0,15,30,45));
        
        //validate item name
        //done in object annotation
        if (StringHelper.isNullOrEmpty(agendaItemName) && agendaItemTypeId == 0)
        {
            errorList.add("Either 'Name' or 'Type' is required to create an agenda item.");
        }
        
        //validate item type& item setup (lookup values)
        
        //validate note
        //done in object annotation
        
        //validate start/end time
        try {
            Date startDate = DateFormatHelper.parseDate(startTime);
            Date endDate = DateFormatHelper.parseDate(endTime);
            
            if(minuteList.contains(startDate.getMinutes()) && minuteList.contains(endDate.getMinutes())) 
            {
                if(startDate.compareTo(endDate) >= 0)
                {
                    errorList.add("startTime must be before endTime.");
                }
            }
            else
            {
                errorList.add("Minute of startTime/endTime is not in list of (0,15,30,45)");
            }
        } catch (ParseException ex) {
            errorList.add(ex.getMessage());
        }
        
        //validate room size
        //done in object annotation
        
        //validate attendee count
        //done in object annotation
        
        //validate day number
        try {
            List<String> list = Arrays.asList(dayNumber.split(","));
            for (String str : list) Integer.parseInt(str.trim());
        } catch(NumberFormatException ex) {
            errorList.add(ex.getMessage());
        }
        
        return errorList;
    }
}
