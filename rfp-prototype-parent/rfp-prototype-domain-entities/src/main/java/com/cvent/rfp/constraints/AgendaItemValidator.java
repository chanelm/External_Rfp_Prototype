/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cvent.rfp.constraints;

import com.cvent.rfp.AgendaItem;
import com.cvent.rfp.util.DateFormatHelper;
import com.cvent.rfp.util.StringHelper;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author yxie
 * Validates parameters of AgendaItem object.
 */
public class AgendaItemValidator implements ConstraintValidator<ValidAgendaItem, AgendaItem> {

    public static final int MINUTE_0 = 0;
    public static final int MINUTE_15 = 15;
    public static final int MINUTE_30 = 30;
    public static final int MINUTE_45 = 45;
    
    @Override
    public void initialize(ValidAgendaItem constraintAnnotation) {
    }

    @Override
    public boolean isValid(AgendaItem item, ConstraintValidatorContext context) {

        List<Integer> minuteList = new ArrayList<>(Arrays.asList(MINUTE_0, MINUTE_15, MINUTE_30, MINUTE_45));
        boolean isValid = true;
        List<String> messageList = new ArrayList<>();

        //Validate startTime/endTime
        try {
            Date startTime = DateFormatHelper.parseDate(item.getStartTime());
            Date endTime = DateFormatHelper.parseDate(item.getEndTime());

            if (minuteList.contains(startTime.getMinutes()) && minuteList.contains(endTime.getMinutes())) {
                if (startTime.compareTo(endTime) >= 0) {
                    isValid = false;
                    messageList.add("StartTime must be before EndTime");
                }
            } else {
                isValid = false;
                messageList.add("StartTime/EndTime must have minute in 0/15/30/45");
            }
        } catch (ParseException ex) {
            isValid = false;
            messageList.add("StartTime/EndTime input(s) must be in valid format");
        }

        //Validate AgendaItemName/AgendaItemTypeId
        if (StringHelper.isNullOrEmpty(item.getName()) && item.getTypeId() == 0) {
            isValid = false;
            messageList.add("AgendaItemName and AgendaItemType cannot both be empty");
        }

        //Validate DayNumber
        try {
            List<String> list = Arrays.asList(item.getDayNumber().split(","));
            for (String str : list) {
                Integer.parseInt(str.trim());
            }
        } catch (NumberFormatException ex) {
            isValid = false;
            messageList.add("DayNumber input must be in valid format");
        }

        //Validate AgendaItemTypeId
        try {
            if (!item.isIsTypeIdValid()) {
                isValid = false;
                messageList.add("agendaItemType must be in valid range");
            }
        } catch (Exception ex) {
            isValid = false;
            messageList.add(ex.getMessage());
        }

        //Validate AgendaItemSetupId
        try {
            if (!item.isIsSetupIdValid()) {
                isValid = false;
                messageList.add("AgendaItemSetUp must be in valid range");
            }
        } catch (Exception ex) {
            isValid = false;
            messageList.add(ex.getMessage());
        }

        //generate custom constraintViolation(s)
        if (!isValid) {
            context.disableDefaultConstraintViolation();

            for (String messageStr : messageList) {
                context.buildConstraintViolationWithTemplate(messageStr).addConstraintViolation();
            }
        }

        return isValid;
    }
}
