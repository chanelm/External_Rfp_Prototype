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
 */


/**
 * Validates the max response length for a Choice based on ChoiceType.LONG_TEXT or ChoiceType.SHORT_TEXT .
 */
public class AgendaItemValidator implements ConstraintValidator<ValidAgendaItem, AgendaItem> {

    @Override
    public void initialize(ValidAgendaItem constraintAnnotation) {
    }

    @Override
    public boolean isValid(AgendaItem item, ConstraintValidatorContext context) {

        List<Integer> minuteList = new ArrayList<>(Arrays.asList(0,15,30,45));
        boolean isValid = true;
        String dateMessage = null;
        String nameTypeMessage = null;
        String dayNumberMessage = null;
        
        try{
            Date startTime = DateFormatHelper.parseDate(item.getStartTime());
            Date endTime = DateFormatHelper.parseDate(item.getEndTime());

            if(minuteList.contains(startTime.getMinutes()) && minuteList.contains(endTime.getMinutes()))
            {
                if (startTime.compareTo(endTime) >= 0)
                {
                    isValid = false;
                    dateMessage += "startTime should be before endTime. ";
                }
            }
            else
            {
                isValid = false;
                dateMessage += "Minute of startTime/endTime are not in 0/15/30/45. ";
            }
            
        } catch (ParseException ex) {
            isValid = false;
            dateMessage += "startTime/endTime can not be parsed correctly. ";
        }


        if (StringHelper.isNullOrEmpty(item.getName()) && item.getTypeId() == 0)
        {
            isValid = false;
            nameTypeMessage += "Name and Type cannot both be empty. ";
        }
        
        try {
            List<String> list = Arrays.asList(item.getDayNumber().split(","));
            for (String str : list) Integer.parseInt(str.trim());
        } catch(NumberFormatException ex) {
            isValid = false;
            dayNumberMessage += "dayNumber input is in invalid format. ";
        }

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            if(!StringHelper.isNullOrEmpty(dateMessage)) 
                context.buildConstraintViolationWithTemplate(dateMessage).addConstraintViolation();
            if(!StringHelper.isNullOrEmpty(nameTypeMessage))
                context.buildConstraintViolationWithTemplate(nameTypeMessage).addConstraintViolation();
            if(!StringHelper.isNullOrEmpty(dayNumberMessage))
                context.buildConstraintViolationWithTemplate(dayNumberMessage).addConstraintViolation();
        }

        return isValid;
    }
}