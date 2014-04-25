/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cvent.rfp.mapper;

import com.cvent.rfp.AgendaItem;
import com.cvent.rfp.Days;
import com.cvent.rfp.Rfp;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

/**
 *
 * @author yxie
 */
public interface RfpMapper {

    String GET_RFP_BY_RFP_STUB = "SELECT " + "r.acct_id, " + "r.rfp_stub, " + "r.rfp_name " + "FROM [dbo].[RFP] r "
            + "WHERE r.rfp_stub = #{rfpStub}";

    String GET_RFP_AGENDA_ITEM_LIST_BY_STUB = "SELECT " + "rai.rfp_acct_id, " + "rai.rfp_stub, "
            + "rai.rfp_agenda_item_stub, " + "rai.agenda_item_name, " + "rai.agenda_item_number, "
            + "rai.agenda_item_type_id, "
            + "ISNULL((SELECT agenda_item_type_name FROM dbo.LU_AGENDA_ITEM_TYPE " 
            + "WHERE agenda_item_type_id = rai.agenda_item_type_id), '') AS agenda_item_type_name, "
            + "rai.agenda_item_setup_id, "
            + "ISNULL((SELECT agenda_item_setup_name FROM dbo.LU_AGENDA_ITEM_SETUP " 
            + "WHERE agenda_item_setup_id = rai.agenda_item_setup_id), '') AS agenda_item_setup_name, "
            + "rai.agenda_addl_info, " + "rai.start_time, " + "rai.end_time, " + "rai.room_size, "
            + "rai.attendee_count, " + "rai.room_info_required_flag, " + "rai.twentyfour_hour_hold_flag, "
            + "rai.located_at_host_venue_flag " + "FROM dbo.RFP_AGENDA_ITEM rai " + "WHERE rai.rfp_stub = #{rfpStub}";

    String GET_RFP_AGENDA_ITEM_BY_STUB = "SELECT " + "rai.rfp_acct_id, " + "rai.rfp_stub, "
            + "rai.rfp_agenda_item_stub, " + "rai.agenda_item_name, " + "rai.agenda_item_number, "
            + "rai.agenda_item_type_id, "
            + "ISNULL((SELECT agenda_item_type_name FROM dbo.LU_AGENDA_ITEM_TYPE "
            + "WHERE agenda_item_type_id = rai.agenda_item_type_id), '') AS agenda_item_type_name, "
            + "rai.agenda_item_setup_id, "
            + "ISNULL((SELECT agenda_item_setup_name FROM dbo.LU_AGENDA_ITEM_SETUP "
            + "WHERE agenda_item_setup_id = rai.agenda_item_setup_id), '') AS agenda_item_setup_name, "
            + "rai.agenda_addl_info, " + "rai.start_time, " + "rai.end_time, " + "rai.room_size, "
            + "rai.attendee_count, " + "rai.room_info_required_flag, " + "rai.twentyfour_hour_hold_flag, "
            + "rai.located_at_host_venue_flag " + "FROM dbo.RFP_AGENDA_ITEM rai "
            + "WHERE rai.rfp_stub = #{rfpStub} AND rai.rfp_agenda_item_stub = #{agendaItemStub}";

    String GET_AGENDA_ITEM_DETAIL_BY_STUB = "SELECT " + "raid.day_number AS day_number, "
            + "DATEADD(dd, day_number, actual_agenda_start_date) AS date " + "FROM dbo.RFP r "
            + "JOIN dbo.RFP_AGENDA_ITEM_DETAIL raid "
            + "ON r.acct_id = raid.rfp_acct_id AND r.rfp_stub = raid.rfp_stub "
            + "WHERE raid.rfp_stub = #{rfpStub} AND raid.rfp_agenda_item_stub = #{agendaItemStub}";

    String DELETE_AGENDA_ITEM_BY_STUB = "DELETE " + "FROM dbo.RFP_AGENDA_ITEM "
            + "WHERE rfp_stub = #{rfpStub} AND rfp_agenda_item_stub = #{agendaItemStub}";

    String DELETE_AGENDA_ITEM_DETAIL_BY_STUB = "DELETE " + "FROM dbo.RFP_AGENDA_ITEM_DETAIL "
            + "WHERE rfp_stub = #{rfpStub} AND rfp_agenda_item_stub = #{agendaItemStub}";

    String INSERT_AGENDA_ITEM_BY_STUB = "INSERT INTO " + "dbo.RFP_AGENDA_ITEM " + "VALUES " + "( " + "#{accountId}, "
            + "#{rfpStub}, " + "#{agendaItemStub}, " + "#{startTime}, " + "#{endTime}, " + "#{agendaItemName}, "
            + "#{agendaItemTypeId}, " + "#{agendaItemSetupId}, " + "#{agendaAddlNote}, " + "#{roomSize}, "
            + "#{attendeeCount}, " + "#{twentyFourHrHoldFlag}, " + "#{infoRequiredFlag}, " + "#{hostVenueFlag}, "
            + "'Cvent - Fred', " + "GETDATE(), " + "'Cvent - Fred', " + "GETDATE(), " + "NULL, " + "NULL, " + "NULL, "
            + "#{agendaItemNumber} " + ") ";

    String INSERT_AGENDA_ITEM_DETAIL_BY_STUB = "INSERT INTO " + "dbo.RFP_AGENDA_ITEM_DETAIL " + "VALUES " + "( "
            + "#{accountId}, " + "#{rfpStub}, " + "#{agendaItemStub}, " + "#{dayNumber}, " + "'Cvent - Fred', "
            + "GETDATE() " + ") ";

    String UPDATE_AGENDA_ITEM_BY_STUB = "UPDATE dbo.RFP_AGENDA_ITEM " + "SET agenda_item_name = #{agendaItemName}, "
            + "agenda_item_type_id = #{agendaItemTypeId}, " + "agenda_item_setup_id = #{agendaItemSetupId}, "
            + "agenda_addl_info = #{agendaAddlNote}, " + "start_time = #{startTime}, " + "end_time = #{endTime}, "
            + "room_size = #{roomSize}, " + "attendee_count = #{attendeeCount}, "
            + "twentyfour_hour_hold_flag = #{twentyFourHrHoldFlag}, "
            + "room_info_required_flag = #{infoRequiredFlag}, " + "located_at_host_venue_flag = #{hostVenueFlag}, "
            + "last_modified_by = 'Cvent - Fred', " + "last_modified_date = GETDATE() "
            + "WHERE rfp_acct_id = #{accountId} AND rfp_stub = #{rfpStub} AND rfp_agenda_item_stub = #{agendaItemStub}";

    //<editor-fold defaultstate="collapsed" desc="getRfpByStub">
    /**
     * Get rfp info by rfp stub from sql server
     *
     * @param rfpStub
     * @return
     * @throws java.lang.Exception
     */
    @Select(GET_RFP_BY_RFP_STUB)
    @Options(statementType = StatementType.CALLABLE)
    @Results(value = {
        @Result(property = "accountId", column = "acct_id"),
        @Result(property = "rfpStub", column = "rfp_stub"),
        @Result(property = "rfpName", column = "rfp_name")
    })
    Rfp getRfpByStub(
            @Param("rfpStub") String rfpStub
    ) throws Exception;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getRfpAgendaItemListByStub">
    /**
     * Get rfp agenda item info by rfp stub from sql server
     *
     * @param rfpStub
     * @return
     * @throws java.lang.Exception
     */
    @Select(GET_RFP_AGENDA_ITEM_LIST_BY_STUB)
    @Options(statementType = StatementType.CALLABLE)
    @Results(value = {
        @Result(property = "id", column = "rfp_agenda_item_stub"),
        @Result(property = "name", column = "agenda_item_name"),
        @Result(property = "number", column = "agenda_item_number"),
        @Result(property = "typeId", column = "agenda_item_type_id"),
        @Result(property = "type", column = "agenda_item_type_name"),
        @Result(property = "setupId", column = "agenda_item_setup_id"),
        @Result(property = "setup", column = "agenda_item_setup_name"),
        @Result(property = "note", column = "agenda_addl_info"),
        @Result(property = "startTime", column = "start_time"),
        @Result(property = "endTime", column = "end_time"),
        @Result(property = "requiredRoomSize", column = "room_size"),
        @Result(property = "expectedNumberOfPeople", column = "attendee_count"),
        @Result(property = "isRoomInfoRequired", column = "room_info_required_flag"),
        @Result(property = "isTwentyFourHourHoldRequired", column = "twentyfour_hour_hold_flag"),
        @Result(property = "isLocatedAtPrimaryEventVenue", column = "located_at_host_venue_flag"),
        @Result(property = "days", column
                = "{accountId = rfp_acct_id, rfpStub = rfp_stub, agendaItemStub = rfp_agenda_item_stub}", javaType
                = List.class, many = @Many(select = "getAgendaItemDaysByStub"))
    })
    List<AgendaItem> getRfpAgendaItemListByStub(
            @Param("rfpStub") String rfpStub
    ) throws Exception;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getRfpAgendaItemByStub">
    /**
     * Get rfp agenda item info by rfp stub from sql server
     *
     * @param rfpStub
     * @param agendaItemStub
     * @return
     * @throws java.lang.Exception
     */
    @Select(GET_RFP_AGENDA_ITEM_BY_STUB)
    @Options(statementType = StatementType.CALLABLE)
    @Results(value = {
        @Result(property = "id", column = "rfp_agenda_item_stub"),
        @Result(property = "name", column = "agenda_item_name"),
        @Result(property = "number", column = "agenda_item_number"),
        @Result(property = "typeId", column = "agenda_item_type_id"),
        @Result(property = "type", column = "agenda_item_type_name"),
        @Result(property = "setupId", column = "agenda_item_setup_id"),
        @Result(property = "setup", column = "agenda_item_setup_name"),
        @Result(property = "note", column = "agenda_addl_info"),
        @Result(property = "startTime", column = "start_time"),
        @Result(property = "endTime", column = "end_time"),
        @Result(property = "requiredRoomSize", column = "room_size"),
        @Result(property = "expectedNumberOfPeople", column = "attendee_count"),
        @Result(property = "isRoomInfoRequired", column = "room_info_required_flag"),
        @Result(property = "isTwentyFourHourHoldRequired", column = "twentyfour_hour_hold_flag"),
        @Result(property = "isLocatedAtPrimaryEventVenue", column = "located_at_host_venue_flag"),
        @Result(property = "days", column
                = "{accountId = rfp_acct_id, rfpStub = rfp_stub, agendaItemStub = rfp_agenda_item_stub}", javaType
                = List.class, many = @Many(select = "getAgendaItemDaysByStub"))
    })
    AgendaItem getRfpAgendaItemByStub(
            @Param("rfpStub") String rfpStub,
            @Param("agendaItemStub") String agendaItemStub
    ) throws Exception;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getAgendaItemDaysByStub">
    /**
     * Get sleep room block info by rfp stub from sql server
     *
     * @param rfpStub
     * @param agendaItemStub
     * @return
     * @throws java.lang.Exception
     */
    @Select(GET_AGENDA_ITEM_DETAIL_BY_STUB)
    @Options(statementType = StatementType.CALLABLE)
    @Results(value = {
        @Result(property = "dayNumber", column = "day_number"),
        @Result(property = "date", column = "date")
    //@Result(property = "date", column = "date", jdbcType = JdbcType.DATE, javaType = Date.class)
    })
    List<Days> getAgendaItemDaysByStub(
            @Param("rfpStub") String rfpStub,
            @Param("agendaItemStub") String agendaItemStub
    ) throws Exception;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="deleteAgendaItemByStub">
    /**
     * Delete agenda item by rfp stub and agenda item stub from sql server
     *
     * @param rfpStub
     * @param agendaItemStub
     * @return
     * @throws java.lang.Exception
     */
    @Delete(DELETE_AGENDA_ITEM_BY_STUB)
    @Options(statementType = StatementType.CALLABLE)
    int deleteAgendaItemByStub(
            @Param("rfpStub") String rfpStub,
            @Param("agendaItemStub") String agendaItemStub
    ) throws Exception;
    //</editor-fold>   

    //<editor-fold defaultstate="collapsed" desc="deleteAgendaItemDetailByStub">
    /**
     * Delete agenda item by rfp stub and agenda item stub from sql server
     *
     * @param rfpStub
     * @param agendaItemStub
     * @return
     * @throws java.lang.Exception
     */
    @Delete(DELETE_AGENDA_ITEM_DETAIL_BY_STUB)
    @Options(statementType = StatementType.CALLABLE)
    int deleteAgendaItemDetailByStub(
            @Param("rfpStub") String rfpStub,
            @Param("agendaItemStub") String agendaItemStub
    ) throws Exception;
    //</editor-fold>       

    //<editor-fold defaultstate="collapsed" desc="createAgendaItem">
    /**
     * Create agenda item in database
     *
     * @param accountId
     * @param rfpStub
     * @param agendaItemStub
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
     * @param agendaItemNumber
     * @return
     * @throws java.lang.Exception
     */
    @Insert(INSERT_AGENDA_ITEM_BY_STUB)
    @Options(statementType = StatementType.CALLABLE)
    int createAgendaItem(
            @Param("accountId") long accountId,
            @Param("rfpStub") String rfpStub,
            @Param("agendaItemStub") String agendaItemStub,
            @Param("agendaItemName") String agendaItemName,
            @Param("agendaItemTypeId") int agendaItemTypeId,
            @Param("agendaItemSetupId") int agendaItemSetupId,
            @Param("agendaAddlNote") String agendaAddlNote,
            @Param("startTime") Date startTime,
            @Param("endTime") Date endTime,
            @Param("roomSize") int roomSize,
            @Param("attendeeCount") int attendeeCount,
            @Param("infoRequiredFlag") int infoRequiredFlag,
            @Param("twentyFourHrHoldFlag") int twentyFourHrHoldFlag,
            @Param("hostVenueFlag") int hostVenueFlag,
            @Param("agendaItemNumber") int agendaItemNumber
    ) throws Exception;
    //</editor-fold>   

    //<editor-fold defaultstate="collapsed" desc="createAgendaItemDetail">
    /**
     * Create agenda item detail in database
     *
     * @param accountId
     * @param rfpStub
     * @param agendaItemStub
     * @param dayNumber
     * @return
     * @throws java.lang.Exception
     */
    @Insert(INSERT_AGENDA_ITEM_DETAIL_BY_STUB)
    @Options(statementType = StatementType.CALLABLE)
    int createAgendaItemDetail(
            @Param("accountId") long accountId,
            @Param("rfpStub") String rfpStub,
            @Param("agendaItemStub") String agendaItemStub,
            @Param("dayNumber") int dayNumber
    ) throws Exception;
    //</editor-fold>   

    //<editor-fold defaultstate="collapsed" desc="updateAgendaItemByStub">
    /**
     * Update agenda item by rfp stub and agenda item stub from sql server
     *
     * @param accountId
     * @param rfpStub
     * @param agendaItemStub
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
     * @return
     * @throws java.lang.Exception
     */
    @Delete(UPDATE_AGENDA_ITEM_BY_STUB)
    @Options(statementType = StatementType.CALLABLE)
    int updateAgendaItemByStub(
            @Param("accountId") long accountId,
            @Param("rfpStub") String rfpStub,
            @Param("agendaItemStub") String agendaItemStub,
            @Param("agendaItemName") String agendaItemName,
            @Param("agendaItemTypeId") int agendaItemTypeId,
            @Param("agendaItemSetupId") int agendaItemSetupId,
            @Param("agendaAddlNote") String agendaAddlNote,
            @Param("startTime") Date startTime,
            @Param("endTime") Date endTime,
            @Param("roomSize") int roomSize,
            @Param("attendeeCount") int attendeeCount,
            @Param("infoRequiredFlag") int infoRequiredFlag,
            @Param("twentyFourHrHoldFlag") int twentyFourHrHoldFlag,
            @Param("hostVenueFlag") int hostVenueFlag
    ) throws Exception;
    //</editor-fold>

}
