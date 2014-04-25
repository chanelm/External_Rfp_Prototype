/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cvent.rfp.resources;

import com.cvent.auth.AuthenticatorMethod;
import com.cvent.auth.Authority;
import com.cvent.auth.GrantedAPIKey;
import com.cvent.constants.LookUp;
import com.cvent.filters.SwaggerInternalFilter;
import com.cvent.rfp.AgendaItem;
import com.cvent.rfp.Days;
import com.cvent.rfp.Rfp;
import com.cvent.rfp.dao.LuDAO;
import com.cvent.rfp.dao.RfpDAO;
import com.cvent.rfp.util.StringHelper;
import com.cvent.util.ResponseUtils;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import com.yammer.dropwizard.jersey.ConstraintViolations;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.eclipse.jetty.http.HttpStatus;

/**
 *
 * @author yxie
 */
@Path(value = "CSN/Planner/Rfp")
@Api(value = "CSN/Planner/Rfp", description = "Get Rfp object Info")
@Produces(MediaType.APPLICATION_JSON)
public class RfpResource {

    private RfpDAO dao;
    private LuDAO luDao;

    private static final String OK = "Http operation is successful.";
    private static final String BAD_REQUEST = "Bad Request. Detail: ";
    private static final String NOT_FOUND = "The requested object is not found.";
    private static final String UNAUTHORIZED = "The provided bearer token is not authorized";
    private static final String INTERNAL_SERVER_ERROR = "Internal Server Error. Details: ";
    private static final String UNPROCESSABLE_ENTITY = "Invalid entity parameter(s)";

    public RfpResource() {
    }

    public RfpResource(RfpDAO dao, LuDAO luDao) {
        this.dao = dao;
        this.luDao = luDao;
    }

    public void setRfpDAO(RfpDAO rfpDao) {
        this.dao = rfpDao;
    }

    public void setLuDAO(LuDAO lookupDao) {
        this.luDao = lookupDao;
    }

    /**
     *
     * @param uriInfo
     * @param grantedAPIKey
     * @param rfpId
     * @return
     * @throws Exception
     */
    @GET
    @Path("/{RfpId}")
    @ApiOperation(value = "Get Rpf Object", notes = "This method gets Rfp Object from database.", response = Rfp.class)
    @ApiResponses(value = {
        @ApiResponse(code = HttpStatus.NOT_FOUND_404, message = NOT_FOUND),
        @ApiResponse(code = HttpStatus.INTERNAL_SERVER_ERROR_500, message = INTERNAL_SERVER_ERROR)
    })
    public Response getRfp(
            @Context UriInfo uriInfo,
            @Authority(methods = { AuthenticatorMethod.BEARER, AuthenticatorMethod.API_KEY })
            @ApiParam(access = SwaggerInternalFilter.INTERNAL) GrantedAPIKey grantedAPIKey,
            @ApiParam(value = "rfp stub", required = true)
            @PathParam("RfpId") UUID rfpId
    ) throws Exception {
        try {
            Rfp rfp = dao.getRfp(rfpId.toString());
            if (rfp == null) {
                return ResponseUtils.getErrorResponse(HttpStatus.NOT_FOUND_404, NOT_FOUND);
            }
            return Response.ok(rfp).build();
        } catch (Exception ex) {
            return ResponseUtils.getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR_500, INTERNAL_SERVER_ERROR + ex.
                    getMessage());
        }
    }

    /**
     *
     * @param uriInfo
     * @param grantedAPIKey
     * @param rfpId
     * @return
     * @throws Exception
     */
    @GET
    @Path("/{RfpId}/AgendaItems")
    @ApiOperation(value = "Get Rpf's list of AgendaItem Object", notes
            = "This method gets Rfp's list of AgendaItem Object from database.", response = AgendaItem.class)
    @ApiResponses(value = {
        @ApiResponse(code = HttpStatus.UNAUTHORIZED_401, message = UNAUTHORIZED),
        @ApiResponse(code = HttpStatus.NOT_FOUND_404, message = NOT_FOUND),
        @ApiResponse(code = HttpStatus.INTERNAL_SERVER_ERROR_500, message = INTERNAL_SERVER_ERROR)
    })
    public Response getRfpAgendaItemList(
            @Context UriInfo uriInfo,
            @Authority(methods = { AuthenticatorMethod.BEARER, AuthenticatorMethod.API_KEY })
            @ApiParam(access = SwaggerInternalFilter.INTERNAL) GrantedAPIKey grantedAPIKey,
            @ApiParam(value = "rfp stub", required = true)
            @PathParam("RfpId") UUID rfpId
    ) throws Exception {
        try {
            List<AgendaItem> ai = dao.getRfpAgendaItemListByRfpStub(rfpId.toString());
            if (ai == null) {
                return ResponseUtils.getErrorResponse(HttpStatus.NOT_FOUND_404, NOT_FOUND);
            }
            return Response.ok(ai).build();
        } catch (Exception ex) {
            return ResponseUtils.getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR_500, INTERNAL_SERVER_ERROR + ex.
                    getMessage());
        }
    }

    /**
     *
     * @param uriInfo
     * @param grantedAPIKey
     * @param rfpId
     * @param id
     * @return
     * @throws Exception
     */
    @GET
    @Path("/{RfpId}/AgendaItems/{Id}")
    @ApiOperation(value = "Get specific AgendaItem Object by stub", notes
            = "This method gets specific Rfp AgendaItem Object from database by stub.", response = AgendaItem.class)
    @ApiResponses(value = {
        @ApiResponse(code = HttpStatus.UNAUTHORIZED_401, message = UNAUTHORIZED),
        @ApiResponse(code = HttpStatus.NOT_FOUND_404, message = NOT_FOUND),
        @ApiResponse(code = HttpStatus.INTERNAL_SERVER_ERROR_500, message = INTERNAL_SERVER_ERROR)
    })
    public Response getRfpAgendaItem(
            @Context UriInfo uriInfo,
            @Authority(methods = { AuthenticatorMethod.BEARER, AuthenticatorMethod.API_KEY })
            @ApiParam(access = SwaggerInternalFilter.INTERNAL) GrantedAPIKey grantedAPIKey,
            @ApiParam(value = "rfp stub", required = true)
            @PathParam("RfpId") UUID rfpId,
            @ApiParam(value = "rfp agenda item stub", required = true)
            @PathParam("Id") UUID id
    ) throws Exception {
        try {
            AgendaItem item = dao.getRfpAgendaByAgendaItemStub(rfpId.toString(), id.toString());
            if (item == null) {
                return ResponseUtils.getErrorResponse(HttpStatus.NOT_FOUND_404, NOT_FOUND);
            }
            return Response.ok(item).build();
        } catch (Exception ex) {
            return ResponseUtils.getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR_500, INTERNAL_SERVER_ERROR + ex.
                    getMessage());
        }
    }

    /**
     *
     * @param uriInfo
     * @param grantedAPIKey
     * @param rfpId
     * @param id
     * @return
     * @throws Exception
     */
    @DELETE
    @Path("/{RfpId}/AgendaItems/{Id}")
    @ApiOperation(value = "Delete rfp agenda item by stub", notes
            = "This method delete rfp agenda item by agenda_item_stub", response = String.class)
    @ApiResponses(value = {
        @ApiResponse(code = HttpStatus.UNAUTHORIZED_401, message = UNAUTHORIZED),
        @ApiResponse(code = HttpStatus.NOT_FOUND_404, message = NOT_FOUND),
        @ApiResponse(code = HttpStatus.INTERNAL_SERVER_ERROR_500, message = INTERNAL_SERVER_ERROR)
    })
    public Response deleteAgendaItemByStub(
            @Context UriInfo uriInfo,
            @Authority(methods = { AuthenticatorMethod.BEARER, AuthenticatorMethod.API_KEY })
            @ApiParam(access = SwaggerInternalFilter.INTERNAL) GrantedAPIKey grantedAPIKey,
            @ApiParam(value = "rfp stub", required = true)
            @PathParam("RfpId") UUID rfpId,
            @ApiParam(value = "rfp agenda item stub", required = true)
            @PathParam("Id") UUID id
    ) throws Exception {
        try {
            int deletedRowNum = dao.deleteAgendaByStub(rfpId.toString(), id.toString());
            if (deletedRowNum > 0) {
                return ResponseUtils.getErrorResponse(HttpStatus.OK_200, OK);
            }
            return ResponseUtils.getErrorResponse(HttpStatus.NOT_FOUND_404, NOT_FOUND);
        } catch (Exception ex) {
            return ResponseUtils.getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR_500, INTERNAL_SERVER_ERROR + ex.
                    getMessage());
        }
    }

    /**
     *
     * @param uriInfo
     * @param grantedAPIKey
     * @param rfpId
     * @param item
     * @return
     * @throws Exception
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/{RfpId}/AgendaItems")
    @ApiOperation(value = "Create new agenda item for this rfp (Json input)", notes
            = "This method create rfp agenda item for this rfp", response = String.class)
    @ApiResponses(value = {
        @ApiResponse(code = HttpStatus.BAD_REQUEST_400, message = BAD_REQUEST),
        @ApiResponse(code = HttpStatus.UNAUTHORIZED_401, message = UNAUTHORIZED),
        @ApiResponse(code = HttpStatus.NOT_FOUND_404, message = NOT_FOUND),
        @ApiResponse(code = HttpStatus.UNPROCESSABLE_ENTITY_422, message = UNPROCESSABLE_ENTITY),
        @ApiResponse(code = HttpStatus.INTERNAL_SERVER_ERROR_500, message = INTERNAL_SERVER_ERROR)
    })
    public Response createAgendaItemJson(
            @Context UriInfo uriInfo,
            @Authority(methods = { AuthenticatorMethod.BEARER, AuthenticatorMethod.API_KEY })
            @ApiParam(access = SwaggerInternalFilter.INTERNAL) GrantedAPIKey grantedAPIKey,
            @ApiParam(value = "rfp stub", required = true)
            @PathParam("RfpId") UUID rfpId,
            AgendaItem item
    ) throws Exception {
        try {
            Rfp rfp = dao.getRfp(rfpId.toString());
            List<AgendaItem> itemList = dao.getRfpAgendaItemListByRfpStub(rfpId.toString());

            if (rfp == null) {
                return ResponseUtils.getErrorResponse(HttpStatus.BAD_REQUEST_400, BAD_REQUEST);
            }

            item.setIsTypeIdValid(luDao.validateLookUp(LookUp.LU_AGENDA_ITEM_TYPE_TABLE_NAME,
                    LookUp.LU_AGENDA_ITEM_TYPE_ID_COLUMN_NAME, item.getTypeId()));
            item.setIsSetupIdValid(luDao.validateLookUp(LookUp.LU_AGENDA_ITEM_SETUP_TABLE_NAME,
                    LookUp.LU_AGENDA_ITEM_SETUP_ID_COLUMN_NAME, item.getSetupId()));

            Set<ConstraintViolation<AgendaItem>> violations = Validation.buildDefaultValidatorFactory().getValidator().
                    validate(item);

            if (violations.size() > 0) {
                throw new ConstraintViolationException(ConstraintViolations.copyOf(violations));
            }

            int createdRowNum = dao.createAgendaItem(
                    rfp.getAccountId(),
                    rfpId.toString(),
                    item.getName(),
                    item.getTypeId(),
                    item.getSetupId(),
                    item.getNote(),
                    item.getStartTime(),
                    item.getEndTime(),
                    item.getRequiredRoomSize(),
                    item.getExpectedNumberOfPeople(),
                    item.isIsRoomInfoRequired(),
                    item.isIsTwentyFourHourHoldRequired(),
                    item.isIsLocatedAtPrimaryEventVenue(),
                    item.getDayNumber(),
                    itemList.size());

            if (createdRowNum > 0) {
                return ResponseUtils.getErrorResponse(HttpStatus.OK_200, OK);
            }

            return ResponseUtils.getErrorResponse(HttpStatus.BAD_REQUEST_400, BAD_REQUEST);

        } catch (ConstraintViolationException ex) {
            throw ex;
        } catch (Exception ex) {
            return ResponseUtils.getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR_500, INTERNAL_SERVER_ERROR + ex.
                    getMessage());
        }
    }

    /**
     *
     * @param uriInfo
     * @param grantedAPIKey
     * @param rfpId
     * @param id
     * @param item
     * @return
     * @throws Exception
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/RFP/{RfpId}/AgendaItem/{Id}")
    @ApiOperation(value = "Update agenda item (Json input)", notes
            = "This method update rfp agenda item by agenda_item_stub", response = String.class)
    @ApiResponses(value = {
        @ApiResponse(code = HttpStatus.BAD_REQUEST_400, message = BAD_REQUEST),
        @ApiResponse(code = HttpStatus.UNAUTHORIZED_401, message = UNAUTHORIZED),
        @ApiResponse(code = HttpStatus.NOT_FOUND_404, message = NOT_FOUND),
        @ApiResponse(code = HttpStatus.UNPROCESSABLE_ENTITY_422, message = UNPROCESSABLE_ENTITY),
        @ApiResponse(code = HttpStatus.INTERNAL_SERVER_ERROR_500, message = INTERNAL_SERVER_ERROR)
    })
    public Response updateAgendaItemByStubJson(
            @Context UriInfo uriInfo,
            @Authority(methods = { AuthenticatorMethod.BEARER, AuthenticatorMethod.API_KEY })
            @ApiParam(access = SwaggerInternalFilter.INTERNAL) GrantedAPIKey grantedAPIKey,
            @ApiParam(value = "rfp stub", required = true)
            @PathParam("RfpId") UUID rfpId,
            @ApiParam(value = "rfp agenda item stub", required = true)
            @PathParam("Id") UUID id,
            AgendaItem item
    ) throws Exception {
        try {
            Rfp rfp = dao.getRfp(rfpId.toString());

            if (rfp == null) {
                return ResponseUtils.getErrorResponse(HttpStatus.BAD_REQUEST_400, BAD_REQUEST);
            }

            AgendaItem agendaItem = dao.getRfpAgendaByAgendaItemStub(rfp.getRfpStub(), id.toString());

            if (agendaItem == null) {
                return ResponseUtils.getErrorResponse(HttpStatus.BAD_REQUEST_400, BAD_REQUEST);
            }

            String dayNumberStr = "";
            for (Days day : agendaItem.getDays()) {
                dayNumberStr = String.format("%s,%s", day.getDayNumber(), dayNumberStr);
            }

            item.setName(StringHelper.isNullOrEmpty(item.getName()) ? agendaItem.getName() : item.getName());
            item.setNote(StringHelper.isNullOrEmpty(item.getNote()) ? agendaItem.getNote() : item.getNote());
            item.setTypeId((item.getTypeId() == null) ? agendaItem.getTypeId() : item.getTypeId());
            item.setSetupId((item.getTypeId() == null) ? agendaItem.getSetupId() : item.getTypeId());
            item.setStartTime(StringHelper.isNullOrEmpty(item.getStartTime()) ? agendaItem.getStartTime() : item.
                    getStartTime());
            item.setEndTime(StringHelper.isNullOrEmpty(item.getEndTime()) 
                    ? agendaItem.getEndTime() : item.getEndTime());
            item.setRequiredRoomSize((item.getRequiredRoomSize() == null) ? agendaItem.getRequiredRoomSize() : item.
                    getRequiredRoomSize());
            item.setExpectedNumberOfPeople((item.getExpectedNumberOfPeople() == null) ? agendaItem.
                    getExpectedNumberOfPeople() : item.getExpectedNumberOfPeople());
            item.setIsRoomInfoRequired((item.isIsRoomInfoRequired() == null) ? agendaItem.isIsRoomInfoRequired() : item.
                    isIsRoomInfoRequired());
            item.setIsTwentyFourHourHoldRequired((item.isIsTwentyFourHourHoldRequired() == null) ? agendaItem.
                    isIsTwentyFourHourHoldRequired() : item.isIsTwentyFourHourHoldRequired());
            item.setIsLocatedAtPrimaryEventVenue((item.isIsLocatedAtPrimaryEventVenue() == null) ? agendaItem.
                    isIsLocatedAtPrimaryEventVenue() : item.isIsLocatedAtPrimaryEventVenue());
            item.setDayNumber(StringHelper.isNullOrEmpty(item.getDayNumber()) ? dayNumberStr : item.getDayNumber());
            item.setIsTypeIdValid(luDao.validateLookUp(LookUp.LU_AGENDA_ITEM_TYPE_TABLE_NAME,
                    LookUp.LU_AGENDA_ITEM_TYPE_ID_COLUMN_NAME, item.getTypeId()));
            item.setIsSetupIdValid(luDao.validateLookUp(LookUp.LU_AGENDA_ITEM_SETUP_TABLE_NAME,
                    LookUp.LU_AGENDA_ITEM_SETUP_ID_COLUMN_NAME, item.getSetupId()));

            Set<ConstraintViolation<AgendaItem>> violations = Validation.buildDefaultValidatorFactory().getValidator().
                    validate(item);

            if (violations.size() > 0) {
                throw new ConstraintViolationException(ConstraintViolations.copyOf(violations));
            }

            int updatedRowNum = dao.updateAgendaItemByStub(
                    rfp.getAccountId(),
                    rfpId.toString(),
                    id.toString(),
                    item.getName(),
                    item.getTypeId(),
                    item.getSetupId(),
                    item.getNote(),
                    item.getStartTime(),
                    item.getEndTime(),
                    item.getRequiredRoomSize(),
                    item.getExpectedNumberOfPeople(),
                    item.isIsRoomInfoRequired(),
                    item.isIsTwentyFourHourHoldRequired(),
                    item.isIsLocatedAtPrimaryEventVenue(),
                    item.getDayNumber());
            if (updatedRowNum > 0) {
                return ResponseUtils.getErrorResponse(HttpStatus.OK_200, OK);
            }
            return ResponseUtils.getErrorResponse(HttpStatus.NOT_FOUND_404, NOT_FOUND);
        } catch (ConstraintViolationException cve) {
            throw cve;
        } catch (Exception ex) {
            return ResponseUtils.getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR_500, INTERNAL_SERVER_ERROR + ex.
                    getMessage());
        }
    }

    
//    //<editor-fold defaultstate="collapsed" desc="updateAgendaItemByStub">
//    /**
//     *
//     * @param uriInfo
//     * @param grantedAPIKey
//     * @param rfpId
//     * @param id
//     * @param agendaItemName
//     * @param agendaItemTypeId
//     * @param agendaItemSetupId
//     * @param agendaAddlNote
//     * @param roomSize
//     * @param startTime
//     * @param endTime
//     * @param attendeeCount
//     * @param infoRequiredBooleanFlag
//     * @param twentyFourHrHoldBooleanFlag
//     * @param hostVenueBooleanFlag
//     * @param dayNumber
//     * @return
//     * @throws Exception
//     */
//    @POST
//    @Path("/RFP/{RfpId}/AgendaItem/{Id}")
//    @ApiOperation(value = "Update agenda item name by agenda item stub", notes
//            = "This method update rfp agenda item by agenda_item_stub", response = String.class)
//    @ApiResponses(value = {
//        @ApiResponse(code = HttpStatus.BAD_REQUEST_400, message = BAD_REQUEST),
//        @ApiResponse(code = HttpStatus.UNAUTHORIZED_401, message = UNAUTHORIZED),
//        @ApiResponse(code = HttpStatus.NOT_FOUND_404, message = NOT_FOUND),
//        @ApiResponse(code = HttpStatus.UNPROCESSABLE_ENTITY_422, message = UNPROCESSABLE_ENTITY),
//        @ApiResponse(code = HttpStatus.INTERNAL_SERVER_ERROR_500, message = INTERNAL_SERVER_ERROR)
//    })
//    public Response updateAgendaItemByStub(
//            @Context UriInfo uriInfo,
//            @Authority(methods = { AuthenticatorMethod.BEARER, AuthenticatorMethod.API_KEY })
//            @ApiParam(access = SwaggerInternalFilter.INTERNAL) GrantedAPIKey grantedAPIKey,
//            @ApiParam(value = "rfp stub", required = true)
//            @PathParam("RfpId") UUID rfpId,
//            @ApiParam(value = "rfp agenda item stub", required = true)
//            @PathParam("Id") UUID id,
//            @ApiParam(value = "agenda item name", required = false)
//            @QueryParam("Name") String agendaItemName,
//            @ApiParam(value = "agenda item type id", required = false)
//            @QueryParam("Type") int agendaItemTypeId,
//            @ApiParam(value = "agenda item setup id", required = false)
//            @QueryParam("Setup") int agendaItemSetupId,
//            @ApiParam(value = "agenda note", required = false)
//            @QueryParam("Note") String agendaAddlNote,
//            @ApiParam(value = "start time", required = false)
//            @QueryParam("StartTime") String startTime,
//            @ApiParam(value = "end time", required = false)
//            @QueryParam("EndTime") String endTime,
//            @ApiParam(value = "room size", required = false)
//            @QueryParam("RequiredRoomSize") int roomSize,
//            @ApiParam(value = "attendee count", required = false)
//            @QueryParam("ExpectedNumberOfPeople") int attendeeCount,
//            @ApiParam(value = "info required flag", required = false)
//            @QueryParam("IsRoomInfoRequired") Boolean infoRequiredBooleanFlag,
//            @ApiParam(value = "24 hr hold flag", required = false)
//            @QueryParam("IsTwentyFourHourHoldRequired") Boolean twentyFourHrHoldBooleanFlag,
//            @ApiParam(value = "locate at host venue flag", required = false)
//            @QueryParam("IsLocatedAtPrimaryEventVenue") Boolean hostVenueBooleanFlag,
//            @ApiParam(value = "list of day number", required = false)
//            @QueryParam("DayNumber") String dayNumber
//    ) throws Exception {
//        try {
//            Rfp rfp = dao.getRfp(rfpId.toString());
//
//            if (rfp == null) {
//                return ResponseUtils.getErrorResponse(HttpStatus.BAD_REQUEST_400, BAD_REQUEST);
//            }
//
//            AgendaItem agendaItem = dao.getRfpAgendaByAgendaItemStub(rfp.getRfpStub(), id.toString());
//
//            if (agendaItem == null) {
//                return ResponseUtils.getErrorResponse(HttpStatus.BAD_REQUEST_400, BAD_REQUEST);
//            }
//
//            String dayNumberStr = "";
//            for (Days day : agendaItem.getDays()) {
//                dayNumberStr = String.format("%s,%s", day.getDayNumber(), dayNumberStr);
//            }
//
//            agendaItemName = StringHelper.isNullOrEmpty(agendaItemName) ? agendaItem.getName() : agendaItemName;
//            agendaItemTypeId = (agendaItemTypeId == 0) ? agendaItem.getTypeId() : agendaItemTypeId;
//            agendaItemSetupId = (agendaItemSetupId == 0) ? agendaItem.getSetupId() : agendaItemSetupId;
//            agendaAddlNote = StringHelper.isNullOrEmpty(agendaAddlNote) ? agendaItem.getNote() : agendaAddlNote;
//            startTime = StringHelper.isNullOrEmpty(startTime) ? agendaItem.getStartTime() : startTime;
//            endTime = StringHelper.isNullOrEmpty(endTime) ? agendaItem.getEndTime() : endTime;
//            roomSize = (roomSize == 0) ? agendaItem.getRequiredRoomSize() : roomSize;
//            attendeeCount = (attendeeCount == 0) ? agendaItem.getExpectedNumberOfPeople() : attendeeCount;
//            boolean infoRequiredFlag = (infoRequiredBooleanFlag == null) ? agendaItem.isIsRoomInfoRequired()
//                    : infoRequiredBooleanFlag;
//            boolean twentyFourHrHoldFlag = (twentyFourHrHoldBooleanFlag == null) ? agendaItem.
//                    isIsTwentyFourHourHoldRequired() : twentyFourHrHoldBooleanFlag;
//            boolean hostVenueFlag = (hostVenueBooleanFlag == null) ? agendaItem.isIsLocatedAtPrimaryEventVenue()
//                    : hostVenueBooleanFlag;
//            dayNumber = (dayNumber == null) ? dayNumberStr : dayNumber;
//
//            AgendaItem item = new AgendaItem();
//            item.setName(agendaItemName);
//            item.setTypeId(agendaItemTypeId);
//            item.setSetupId(agendaItemSetupId);
//            item.setIsTypeIdValid(luDao.validateLookUp(LookUp.LU_AGENDA_ITEM_TYPE_TABLE_NAME,
//                    LookUp.LU_AGENDA_ITEM_TYPE_ID_COLUMN_NAME, agendaItemTypeId));
//            item.setIsSetupIdValid(luDao.validateLookUp(LookUp.LU_AGENDA_ITEM_SETUP_TABLE_NAME,
//                    LookUp.LU_AGENDA_ITEM_SETUP_ID_COLUMN_NAME, agendaItemSetupId));
//            item.setNote(agendaAddlNote);
//            item.setStartTime(startTime);
//            item.setEndTime(endTime);
//            item.setRequiredRoomSize(roomSize);
//            item.setExpectedNumberOfPeople(attendeeCount);
//            item.setDayNumber(dayNumber);
//
//            Set<ConstraintViolation<AgendaItem>> violations 
//                    = Validation.buildDefaultValidatorFactory().getValidator().
//                    validate(item);
//
//            if (violations.size() > 0) {
//                throw new ConstraintViolationException(ConstraintViolations.copyOf(violations));
//            }
//
//            int updatedRowNum = dao.updateAgendaItemByStub(
//                    rfp.getAccountId(),
//                    rfpId.toString(),
//                    id.toString(),
//                    agendaItemName,
//                    agendaItemTypeId,
//                    agendaItemSetupId,
//                    agendaAddlNote,
//                    startTime,
//                    endTime,
//                    roomSize,
//                    attendeeCount,
//                    infoRequiredFlag,
//                    twentyFourHrHoldFlag,
//                    hostVenueFlag,
//                    dayNumber);
//            if (updatedRowNum > 0) {
//                return ResponseUtils.getErrorResponse(HttpStatus.OK_200, OK);
//            }
//            return ResponseUtils.getErrorResponse(HttpStatus.NOT_FOUND_404, NOT_FOUND);
//        } catch (ConstraintViolationException cve) {
//            throw cve;
//        } catch (Exception ex) {
//            return ResponseUtils.getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR_500, INTERNAL_SERVER_ERROR + ex.
//                    getMessage());
//        }
//    }
//    //</editor-fold>
    
//    //<editor-fold defaultstate="collapsed" desc="createAgendaItem">
//    /**
//     *
//     * @param uriInfo
//     * @param grantedAPIKey
//     * @param rfpId
//     * @param agendaItemName
//     * @param agendaItemTypeId
//     * @param agendaItemSetupId
//     * @param agendaAddlNote
//     * @param startTime
//     * @param endTime
//     * @param roomSize
//     * @param attendeeCount
//     * @param infoRequiredFlag
//     * @param twentyFourHrHoldFlag
//     * @param hostVenueFlag
//     * @param dayNumber
//     * @return
//     * @throws Exception
//     */
//    @POST
//    @Path("/{RfpId}/AgendaItems")
//    @ApiOperation(value = "Create new agenda item for this rfp", notes
//            = "This method create rfp agenda item for this rfp", response = String.class)
//    @ApiResponses(value = {
//        @ApiResponse(code = HttpStatus.BAD_REQUEST_400, message = BAD_REQUEST),
//        @ApiResponse(code = HttpStatus.UNAUTHORIZED_401, message = UNAUTHORIZED),
//        @ApiResponse(code = HttpStatus.NOT_FOUND_404, message = NOT_FOUND),
//        @ApiResponse(code = HttpStatus.UNPROCESSABLE_ENTITY_422, message = UNPROCESSABLE_ENTITY),
//        @ApiResponse(code = HttpStatus.INTERNAL_SERVER_ERROR_500, message = INTERNAL_SERVER_ERROR)
//    })
//    public Response createAgendaItem(
//            @Context UriInfo uriInfo,
//            @Authority(methods = { AuthenticatorMethod.BEARER, AuthenticatorMethod.API_KEY })
//            @ApiParam(access = SwaggerInternalFilter.INTERNAL) GrantedAPIKey grantedAPIKey,
//            @ApiParam(value = "rfp stub", required = true)
//            @PathParam("RfpId") UUID rfpId,
//            @ApiParam(value = "agenda item name", required = false)
//            @QueryParam("Name") String agendaItemName,
//            @ApiParam(value = "agenda item type id", required = false)
//            @QueryParam("Type") @DefaultValue("0") int agendaItemTypeId,
//            @ApiParam(value = "agenda item setup id", required = false)
//            @QueryParam("Setup") @DefaultValue("0") int agendaItemSetupId,
//            @ApiParam(value = "agenda note", required = false)
//            @QueryParam("Note") @DefaultValue("") String agendaAddlNote,
//            @ApiParam(value = "start time", required = true)
//            @QueryParam("StartTime") String startTime,
//            @ApiParam(value = "end time", required = true)
//            @QueryParam("EndTime") String endTime,
//            @ApiParam(value = "room size", required = true)
//            @QueryParam("RequiredRoomSize") int roomSize,
//            @ApiParam(value = "attendee count", required = true)
//            @QueryParam("ExpectedNumberOfPeople") int attendeeCount,
//            @ApiParam(value = "info required flag", required = true)
//            @QueryParam("IsRoomInfoRequired") @DefaultValue("false") boolean infoRequiredFlag,
//            @ApiParam(value = "24 hr hold flag", required = true)
//            @QueryParam("IsTwentyFourHourHoldRequired") @DefaultValue("false") boolean twentyFourHrHoldFlag,
//            @ApiParam(value = "locate at host venue flag", required = true)
//            @QueryParam("IsLocatedAtPrimaryEventVenue") @DefaultValue("false") boolean hostVenueFlag,
//            @ApiParam(value = "list of day number", required = true)
//            @QueryParam("DayNumber") String dayNumber
//    ) throws Exception {
//        try {
//            Rfp rfp = dao.getRfp(rfpId.toString());
//            List<AgendaItem> ai = dao.getRfpAgendaItemListByRfpStub(rfpId.toString());
//
//            if (rfp == null) {
//                return ResponseUtils.getErrorResponse(HttpStatus.BAD_REQUEST_400, BAD_REQUEST);
//            }
//
//            AgendaItem item = new AgendaItem();
//            item.setName(agendaItemName);
//            item.setSetupId(agendaItemTypeId);
//            item.setSetupId(agendaItemSetupId);
//            item.setIsTypeIdValid(luDao.validateLookUp(LookUp.LU_AGENDA_ITEM_TYPE_TABLE_NAME,
//                    LookUp.LU_AGENDA_ITEM_TYPE_ID_COLUMN_NAME, agendaItemTypeId));
//            item.setIsSetupIdValid(luDao.validateLookUp(LookUp.LU_AGENDA_ITEM_SETUP_TABLE_NAME,
//                    LookUp.LU_AGENDA_ITEM_SETUP_ID_COLUMN_NAME, agendaItemSetupId));
//            item.setNote(agendaAddlNote);
//            item.setStartTime(startTime);
//            item.setEndTime(endTime);
//            item.setRequiredRoomSize(roomSize);
//            item.setExpectedNumberOfPeople(attendeeCount);
//            item.setDayNumber(dayNumber);
//
//            Set<ConstraintViolation<AgendaItem>> violations 
//                    = Validation.buildDefaultValidatorFactory().getValidator().validate(item);
//
//            if (violations.size() > 0) {
//                throw new ConstraintViolationException(ConstraintViolations.copyOf(violations));
//            }
//
//            int createdRowNum = dao.createAgendaItem(
//                    rfp.getAccountId(),
//                    rfpId.toString(),
//                    agendaItemName,
//                    agendaItemTypeId,
//                    agendaItemSetupId,
//                    agendaAddlNote,
//                    startTime,
//                    endTime,
//                    roomSize,
//                    attendeeCount,
//                    infoRequiredFlag,
//                    twentyFourHrHoldFlag,
//                    hostVenueFlag,
//                    dayNumber,
//                    ai.size());
//
//            if (createdRowNum > 0) {
//                return ResponseUtils.getErrorResponse(HttpStatus.OK_200, OK);
//            }
//
//            return ResponseUtils.getErrorResponse(HttpStatus.BAD_REQUEST_400, BAD_REQUEST);
//
//        } catch (ConstraintViolationException ex) {
//            throw ex;
//        } catch (Exception ex) {
//            return ResponseUtils.getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR_500, INTERNAL_SERVER_ERROR + ex.
//                    getMessage());
//        }
//    }
//    //</editor-fold>
}
