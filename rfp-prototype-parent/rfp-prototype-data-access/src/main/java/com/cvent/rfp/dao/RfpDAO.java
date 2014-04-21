/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cvent.rfp.dao;

import com.cvent.rfp.AgendaItem;
import com.cvent.rfp.Rfp;
import com.cvent.rfp.mapper.RfpMapper;
import com.cvent.rfp.util.DateFormatHelper;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 *
 * @author yxie
 */
public class RfpDAO {
    private SqlSessionFactory sessionFactory;
            
    public RfpDAO() { }
    
    public RfpDAO(SqlSessionFactory sessionFactory)
    {
        this.sessionFactory = sessionFactory;
    }
    
    /**
     * Gets rfp info by rfp stub
     *
     * @param rfpStub
     * @return
     * @throws Exception
     */
    public Rfp getRfp(String rfpStub) throws Exception
    {
        try (SqlSession session = sessionFactory.openSession(true)) {
            RfpMapper mapper = session.getMapper(RfpMapper.class);
            Rfp rfp = mapper.getRfpByStub(rfpStub);
            if (rfp == null) {
                return null;
            }
            return rfp;
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * Gets rfp agenda item info by rfp stub
     *
     * @param rfpStub
     * @return
     * @throws Exception
     */
    public List<AgendaItem> getRfpAgendaItemListByRfpStub(String rfpStub) throws Exception
    {
        try (SqlSession session = sessionFactory.openSession(true)) {
            
            RfpMapper mapper = session.getMapper(RfpMapper.class);
            List<AgendaItem> agendaList = mapper.getRfpAgendaItemListByStub(rfpStub);
            
            if (agendaList == null || agendaList.isEmpty()) {
                return null;
            }
            return agendaList;
        } catch (Exception ex) {
            throw ex;
        }
    }
    
    /**
     * Gets rfp agenda item info by rfp stub
     *
     * @param agendaItemStub
     * @return
     * @throws Exception
     */
    public AgendaItem getRfpAgendaByAgendaItemStub(String agendaItemStub) throws Exception
    {
        try (SqlSession session = sessionFactory.openSession(true)) {
            
            RfpMapper mapper = session.getMapper(RfpMapper.class);
            AgendaItem agendaItem = mapper.getRfpAgendaItemByStub(agendaItemStub);
            
            if (agendaItem == null) {
                return null;
            }
            return agendaItem;
        } catch (Exception ex) {
            throw ex;
        }
    }
    
    /**
     * Deletes rfp agenda item info by rfp stub and agenda item stub
     *
     * @param rfpStub
     * @param agendaItemStub
     * @return 
     * @throws Exception
     */
    public int deleteAgendaByStub(String rfpStub, String agendaItemStub) throws Exception
    {
        try (SqlSession session = sessionFactory.openSession(true)) {
            
            RfpMapper mapper = session.getMapper(RfpMapper.class);
            int deletedRow = mapper.deleteAgendaItemByStub(rfpStub, agendaItemStub);
            
            if(deletedRow > 0)
            {
                mapper.deleteAgendaItemDetailByStub(rfpStub, agendaItemStub);
            }
            
            return deletedRow;
        } catch (Exception ex) {
            throw ex;
        }
    }    
    
    
    /**
     * Create rfp agenda item info
     *
     * @param accountId
     * @param rfpStub
     * @param agendaItemName
     * @param agendaItemTypeId
     * @param agendaItemSetupId
     * @param agendaAddlNote
     * @param hostVenueFlag
     * @param endTime
     * @param roomSize
     * @param attendeeCount
     * @param infoRequiredFlag
     * @param twentyFourHrHoldFlag
     * @param startTime
     * @param dayNumber
     * @param agendaItemCount
     * @return
     * @throws Exception
     */
    public int createAgendaItem(long accountId, String rfpStub, String agendaItemName, int agendaItemTypeId, int agendaItemSetupId, String agendaAddlNote, String startTime, String endTime, int roomSize, int attendeeCount, boolean infoRequiredFlag, boolean twentyFourHrHoldFlag, boolean hostVenueFlag, String dayNumber, int agendaItemCount) throws Exception
    {
        try (SqlSession session = sessionFactory.openSession(true)) {

            String agendaItemStub = UUID.randomUUID().toString();
            RfpMapper mapper = session.getMapper(RfpMapper.class);
            int createdRow = mapper.createAgendaItem(
                    accountId,
                    rfpStub,
                    agendaItemStub,
                    agendaItemName,
                    agendaItemTypeId,
                    agendaItemSetupId,
                    agendaAddlNote,
                    DateFormatHelper.parseDate(startTime),
                    DateFormatHelper.parseDate(endTime),
                    roomSize,
                    attendeeCount,
                    infoRequiredFlag ? 1 : 0,
                    twentyFourHrHoldFlag? 1 : 0,
                    hostVenueFlag? 1 : 0,
                    (agendaItemCount + 1));
            
            if(createdRow > 0)
            {
                List<String> dayList = Arrays.asList(dayNumber.split(","));
            
                for (String str : dayList)
                {
                    mapper.createAgendaItemDetail(
                        accountId,
                        rfpStub,
                        agendaItemStub,
                        Integer.parseInt(str.trim())
                        );
                }
            }
            return createdRow;
        } catch (Exception ex) {
            throw ex;
        }
    }
    
    /**
     * Updates rfp agenda item info by rfp stub and agenda item stub
     *
     * @param accountId
     * @param rfpStub
     * @param agendaItemStub
     * @param agendaItemName
     * @param agendaItemTypeId
     * @param agendaItemSetupId
     * @param agendaAddlNote
     * @param roomSize
     * @param startTimeString
     * @param endTimeString
     * @param attendeeCount
     * @param infoRequiredFlag
     * @param twentyFourHrHoldFlag
     * @param dayNumber
     * @param hostVenueFlag
     * @return 
     * @throws Exception
     */
    public int updateAgendaItemByStub(long accountId, String rfpStub, String agendaItemStub, String agendaItemName, int agendaItemTypeId, int agendaItemSetupId, String agendaAddlNote, String startTimeString, String endTimeString, int roomSize, int attendeeCount, boolean infoRequiredFlag, boolean twentyFourHrHoldFlag, boolean hostVenueFlag, String dayNumber) throws Exception
    {
        try (SqlSession session = sessionFactory.openSession(true)) {
            
            RfpMapper mapper = session.getMapper(RfpMapper.class);
            int updatedRow = mapper.updateAgendaItemByStub(
                    accountId, 
                    rfpStub, 
                    agendaItemStub, 
                    agendaItemName, 
                    agendaItemTypeId, 
                    agendaItemSetupId, 
                    agendaAddlNote, 
                    DateFormatHelper.parseDate(startTimeString), 
                    DateFormatHelper.parseDate(endTimeString), 
                    roomSize, 
                    attendeeCount, 
                    infoRequiredFlag? 1 :0, 
                    twentyFourHrHoldFlag? 1 :0, 
                    hostVenueFlag? 1 :0
            );
            
            if(updatedRow > 0)
            {
                mapper.deleteAgendaItemDetailByStub(rfpStub, agendaItemStub);
                
                List<String> dayList = Arrays.asList(dayNumber.split(","));
            
                for (String str : dayList)
                {
                    mapper.createAgendaItemDetail(
                        accountId,
                        rfpStub,
                        agendaItemStub,
                        Integer.parseInt(str.trim())
                    );
                }
            }
            
            return updatedRow;
        } catch (Exception ex) {
            throw ex;
        }
    }       
}
