/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cvent.rfp.resources;


import com.cvent.auth.AuthenticatorMethod;
import com.cvent.auth.Authority;
import com.cvent.auth.GrantedAPIKey;
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
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.eclipse.jetty.http.HttpStatus;

/**
 *
 * @author yxie
 */

@Path(value = "CSN/Planner/RFPs")
@Api(value = "CSN/Planner/RFPs", description = "Get Rfp object Info")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RfpResource {
    
    private RfpDAO dao;
    private final LuDAO luDAO = new LuDAO();

    private static final String OK = "Http operation is successful.";
    private static final String BAD_REQUEST = "Bad Request. Detail: ";
    private static final String NOT_FOUND  = "The requested object is not found.";
    private static final String UNAUTHORIZED = "The provided bearer token is not authorized";
    private static final String INTERNAL_SERVER_ERROR = "Internal Server Error. Details: ";
    private static final String UNPROCESSABLE_ENTITY = "Invalid entity parameter(s)";

    public RfpResource(){}
    
    public RfpResource(RfpDAO dao)
    {
        this.dao = dao;
    }
    
    public void setRfpDAO(RfpDAO dao)
    {
        this.dao = dao;
    }
    
    /**
     *
     * @param uriInfo
     * @param grantedAPIKey
     * @param rfpStub
     * @return
     * @throws Exception
     */
    @GET
    @Path("/{rfpStub}")
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
            @PathParam("rfpStub") UUID  rfpStub
            ) throws Exception 
    {
         try {
            Rfp rfp = dao.getRfp(rfpStub.toString());
            if (rfp == null) {
                return ResponseUtils.getErrorResponse(HttpStatus.NOT_FOUND_404, NOT_FOUND);
            }
            return Response.ok(rfp).build();
        } catch (Exception ex) {
            return ResponseUtils.getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR_500, INTERNAL_SERVER_ERROR + ex.getMessage());
        }
    }
    
    /**
     *
     * @param uriInfo
     * @param grantedAPIKey
     * @param rfpStub
     * @return
     * @throws Exception
     */
    @GET
    @Path("/{rfpStub}/AgendaItems")
    @ApiOperation(value = "Get Rpf AgendaItem Object", notes = "This method gets Rfp AgendaItem Object from database.", response = AgendaItem.class)
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
            @PathParam("rfpStub") UUID  rfpStub
            ) throws Exception 
    {
         try {
            List<AgendaItem> ai = dao.getRfpAgendaItemListByRfpStub(rfpStub.toString());
            if (ai == null) {
                return ResponseUtils.getErrorResponse(HttpStatus.NOT_FOUND_404, NOT_FOUND);
            }
            return Response.ok(ai).build();
        } catch (Exception ex) {
            return ResponseUtils.getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR_500, INTERNAL_SERVER_ERROR + ex.getMessage());
        }
    }  
    
    /**
     *
     * @param uriInfo
     * @param grantedAPIKey
     * @param rfpStub
     * @param agendaItemStub
     * @return
     * @throws Exception
     */
    @DELETE
    @Path("/{rfpStub}/AgendaItems/{agendaItemStub}")
    @ApiOperation(value = "Delete rfp agenda item by stub", notes = "This method delete rfp agenda item by agenda_item_stub", response = String.class)
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
            @PathParam("rfpStub") UUID rfpStub,
            @ApiParam(value = "rfp agenda item stub", required = true)
            @PathParam("agendaItemStub") UUID agendaItemStub
            ) throws Exception
    {
        try
        {
            int deletedRowNum = dao.deleteAgendaByStub(rfpStub.toString(), agendaItemStub.toString());
            if(deletedRowNum > 0)
            {
                return ResponseUtils.getErrorResponse(HttpStatus.OK_200, OK);
            }
             return ResponseUtils.getErrorResponse(HttpStatus.NOT_FOUND_404, NOT_FOUND);
        }
        catch(Exception ex)
        {
            return ResponseUtils.getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR_500, INTERNAL_SERVER_ERROR + ex.getMessage());
        }
    }
    
    /**
     *
     * @param uriInfo
     * @param grantedAPIKey
     * @param rfpStub
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
     * @throws Exception
     */
    @POST
    @Path("/{rfpStub}/AgendaItems")
    @ApiOperation(value = "Create new agenda item for this rfp", notes = "This method create rfp agenda item for this rfp", response = String.class)
    @ApiResponses(value = {
        @ApiResponse(code = HttpStatus.BAD_REQUEST_400, message = BAD_REQUEST),
        @ApiResponse(code = HttpStatus.UNAUTHORIZED_401, message = UNAUTHORIZED),        
        @ApiResponse(code = HttpStatus.NOT_FOUND_404, message = NOT_FOUND),        
        @ApiResponse(code = HttpStatus.UNPROCESSABLE_ENTITY_422, message = UNPROCESSABLE_ENTITY),
        @ApiResponse(code = HttpStatus.INTERNAL_SERVER_ERROR_500, message = INTERNAL_SERVER_ERROR)
    })
    public Response createAgendaItem(
            @Context UriInfo uriInfo,
            @Authority(methods = { AuthenticatorMethod.BEARER, AuthenticatorMethod.API_KEY })
            @ApiParam(access = SwaggerInternalFilter.INTERNAL) GrantedAPIKey grantedAPIKey,
            @ApiParam(value = "rfp stub", required = true)
            @PathParam("rfpStub") UUID rfpStub,
            @ApiParam(value = "agenda item name", required = false)
            @QueryParam("agendaItemName") String agendaItemName,
            @ApiParam(value = "agenda item type id", required = false)
            @QueryParam("agendaItemType") @DefaultValue("0") int agendaItemTypeId,   //need validation from LU table
            @ApiParam(value = "agenda item setup id", required = false)
            @QueryParam("AgendaItemSetUp") @DefaultValue("0") int agendaItemSetupId, //need validation from LU table
            @ApiParam(value = "agenda note", required = false)
            @QueryParam("agendaAddlNote") @DefaultValue("") String agendaAddlNote,
            @ApiParam(value = "start time", required = true)
            @QueryParam("startTime") String startTime,
            @ApiParam(value = "end time", required = true)
            @QueryParam("endTime") String endTime,
            @ApiParam(value = "room size", required = true)
            @QueryParam("roomSize") int roomSize,
            @ApiParam(value = "attendee count", required = true)
            @QueryParam("attendeeCount") int attendeeCount,
            @ApiParam(value = "info required flag", required = true)
            @QueryParam("infoRequiredFlag") @DefaultValue("false") boolean infoRequiredFlag,
            @ApiParam(value = "24 hr hold flag", required = true)
            @QueryParam("24HrHoldFlag") @DefaultValue("false") boolean twentyFourHrHoldFlag,
            @ApiParam(value = "locate at host venue flag", required = true)
            @QueryParam("hostVenueFlag") @DefaultValue("false") boolean hostVenueFlag,
            @ApiParam(value = "list of day number", required = true)
            @QueryParam("DayNumber") String dayNumber
    ) throws Exception
    {
        try
        {   
            Rfp rfp = dao.getRfp(rfpStub.toString());
            List<AgendaItem> ai = dao.getRfpAgendaItemListByRfpStub(rfpStub.toString());
            
            if(rfp == null)
            {
                return ResponseUtils.getErrorResponse(HttpStatus.BAD_REQUEST_400, BAD_REQUEST);
            }
            
            AgendaItem item = new AgendaItem();
            item.setName(agendaItemName);
            item.setSetupId(agendaItemTypeId);
            item.setSetupId(agendaItemSetupId);
            item.setIsTypeIdValid(luDAO.validateLookUpValue("dbo.LU_AGENDA_ITEM_TYPE", "agenda_item_type_id", agendaItemTypeId));
            item.setIsSetupIdValid(luDAO.validateLookUpValue("dbo.LU_AGENDA_ITEM_SETUP", "agenda_item_setup_id", agendaItemSetupId));
            item.setNote(agendaAddlNote);
            item.setStartTime(startTime);
            item.setEndTime(endTime);
            item.setRequiredRoomSize(roomSize);
            item.setExpectedNumberOfPeople(attendeeCount);
            item.setDayNumber(dayNumber);
            
            Set<ConstraintViolation<AgendaItem>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(item);

            if (violations.size() > 0) {
                throw new ConstraintViolationException(ConstraintViolations.copyOf(violations));
            }

            int createdRowNum = dao.createAgendaItem(
                    rfp.getAccountId(),
                    rfpStub.toString(),
                    agendaItemName,
                    agendaItemTypeId,
                    agendaItemSetupId,
                    agendaAddlNote,
                    startTime,
                    endTime,
                    roomSize,
                    attendeeCount,
                    infoRequiredFlag,
                    twentyFourHrHoldFlag,
                    hostVenueFlag,
                    dayNumber,
                    ai.size());
                
                if(createdRowNum > 0)
                {
                    return ResponseUtils.getErrorResponse(HttpStatus.OK_200, OK);
                }
                
                return ResponseUtils.getErrorResponse(HttpStatus.BAD_REQUEST_400, BAD_REQUEST);
        
        } catch(ConstraintViolationException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            return ResponseUtils.getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR_500, INTERNAL_SERVER_ERROR + ex.getMessage());
        }
    }
    
    /**
     *
     * @param uriInfo
     * @param grantedAPIKey
     * @param rfpStub
     * @param agendaItemStub
     * @param agendaItemName
     * @param agendaItemTypeId
     * @param agendaItemSetupId
     * @param agendaAddlNote
     * @param roomSize
     * @param startTime
     * @param endTime
     * @param attendeeCount
     * @param infoRequiredBooleanFlag
     * @param twentyFourHrHoldBooleanFlag
     * @param hostVenueBooleanFlag
     * @param dayNumber
     * @return
     * @throws Exception
     */
    @POST
    @Path("/RFP/{rfpStub}/AgendaItem/{agendaItemStub}")
    @ApiOperation(value = "Update agenda item name by agenda item stub", notes = "This method update rfp agenda item by agenda_item_stub", response = String.class)
    @ApiResponses(value = {
        @ApiResponse(code = HttpStatus.BAD_REQUEST_400, message = BAD_REQUEST),
        @ApiResponse(code = HttpStatus.UNAUTHORIZED_401, message = UNAUTHORIZED),
        @ApiResponse(code = HttpStatus.NOT_FOUND_404, message = NOT_FOUND),
        @ApiResponse(code = HttpStatus.UNPROCESSABLE_ENTITY_422, message = UNPROCESSABLE_ENTITY),
        @ApiResponse(code = HttpStatus.INTERNAL_SERVER_ERROR_500, message = INTERNAL_SERVER_ERROR)
    })
    public Response updateAgendaItemByStub(
            @Context UriInfo uriInfo,
            @Authority(methods = { AuthenticatorMethod.BEARER, AuthenticatorMethod.API_KEY })
            @ApiParam(access = SwaggerInternalFilter.INTERNAL) GrantedAPIKey grantedAPIKey,
            @ApiParam(value = "rfp stub", required = true)
            @PathParam("rfpStub") UUID rfpStub,
            @ApiParam(value = "rfp agenda item stub", required = true)
            @PathParam("agendaItemStub") UUID agendaItemStub,
            @ApiParam(value = "agenda item name", required = false)
            @QueryParam("agendaItemName")  String agendaItemName,
            @ApiParam(value = "agenda item type id", required = false)
            @QueryParam("agendaItemType") int agendaItemTypeId,   //need validation from LU table
            @ApiParam(value = "agenda item setup id", required = false)
            @QueryParam("AgendaItemSetUp") int agendaItemSetupId, //need validation from LU table
            @ApiParam(value = "agenda note", required = false)
            @QueryParam("agendaAddlNote")  String agendaAddlNote,
            @ApiParam(value = "start time", required = false)
            @QueryParam("startTime") String startTime,
            @ApiParam(value = "end time", required = false)
            @QueryParam("endTime") String endTime,
            @ApiParam(value = "room size", required = false)
            @QueryParam("roomSize") int roomSize,
            @ApiParam(value = "attendee count", required = false)
            @QueryParam("attendeeCount") int attendeeCount,
            @ApiParam(value = "info required flag", required = false)
            @QueryParam("infoRequiredFlag") Boolean infoRequiredBooleanFlag,
            @ApiParam(value = "24 hr hold flag", required = false)
            @QueryParam("24HrHoldFlag") Boolean twentyFourHrHoldBooleanFlag,
            @ApiParam(value = "locate at host venue flag", required = false)
            @QueryParam("hostVenueFlag") Boolean hostVenueBooleanFlag,
            @ApiParam(value = "list of day number", required = false)
            @QueryParam("DayNumber") String dayNumber
            ) throws Exception
    {
        try
        {   
            Rfp rfp = dao.getRfp(rfpStub.toString());
            AgendaItem agendaItem = dao.getRfpAgendaByAgendaItemStub(agendaItemStub.toString());
            
            if(agendaItem == null)
            {
                return ResponseUtils.getErrorResponse(HttpStatus.BAD_REQUEST_400, BAD_REQUEST);
            }
            
            String dayNumberStr = "";
            for(Days day : agendaItem.getDays())
            {
                dayNumberStr = String.format("%s,%s", day.getDayNumber(), dayNumberStr);
            }
            
            agendaItemName = StringHelper.isNullOrEmpty(agendaItemName)? agendaItem.getName() : agendaItemName;
            agendaItemTypeId = (agendaItemTypeId == 0)? agendaItem.getTypeId() : agendaItemTypeId;
            agendaItemSetupId = (agendaItemSetupId == 0)? agendaItem.getSetupId() : agendaItemSetupId;
            agendaAddlNote = StringHelper.isNullOrEmpty(agendaAddlNote)? agendaItem.getNote() : agendaAddlNote;
            startTime = StringHelper.isNullOrEmpty(startTime)? agendaItem.getStartTime() : startTime;
            endTime = StringHelper.isNullOrEmpty(endTime)? agendaItem.getEndTime() : endTime;
            roomSize = (roomSize == 0)? agendaItem.getRequiredRoomSize() : roomSize;
            attendeeCount = (attendeeCount == 0)? agendaItem.getExpectedNumberOfPeople() : attendeeCount;
            boolean infoRequiredFlag = (infoRequiredBooleanFlag == null)? agendaItem.isIsRoomInfoRequired() : infoRequiredBooleanFlag;
            boolean twentyFourHrHoldFlag = (twentyFourHrHoldBooleanFlag == null)? agendaItem.isIsTwentyFourHourHoldRequired() : twentyFourHrHoldBooleanFlag;
            boolean hostVenueFlag = (hostVenueBooleanFlag == null)? agendaItem.isIsLocatedAtPrimaryEventVenue() : hostVenueBooleanFlag;
            dayNumber = (dayNumber == null)? dayNumberStr : dayNumber;
                        
            AgendaItem item = new AgendaItem();
            item.setName(agendaItemName);
            item.setTypeId(agendaItemTypeId);
            item.setSetupId(agendaItemSetupId);
            item.setIsTypeIdValid(luDAO.validateLookUpValue("dbo.LU_AGENDA_ITEM_TYPE", "agenda_item_type_id", agendaItemTypeId));
            item.setIsSetupIdValid(luDAO.validateLookUpValue("dbo.LU_AGENDA_ITEM_SETUP", "agenda_item_setup_id", agendaItemSetupId));
            item.setNote(agendaAddlNote);
            item.setStartTime(startTime);
            item.setEndTime(endTime);
            item.setRequiredRoomSize(roomSize);
            item.setExpectedNumberOfPeople(attendeeCount);
            item.setDayNumber(dayNumber);
            
            Set<ConstraintViolation<AgendaItem>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(item);

            if (violations.size() > 0) {
                throw new ConstraintViolationException(ConstraintViolations.copyOf(violations));
            }

            int updatedRowNum = dao.updateAgendaItemByStub(
                    rfp.getAccountId(),
                    rfpStub.toString(),
                    agendaItem.getId(),
                    agendaItemName,
                    agendaItemTypeId,
                    agendaItemSetupId,
                    agendaAddlNote,
                    startTime,
                    endTime,
                    roomSize,
                    attendeeCount,
                    infoRequiredFlag,
                    twentyFourHrHoldFlag,
                    hostVenueFlag,
                    dayNumber);
            if(updatedRowNum > 0)
            {
                return ResponseUtils.getErrorResponse(HttpStatus.OK_200, OK);
            }
             return ResponseUtils.getErrorResponse(HttpStatus.NOT_FOUND_404, NOT_FOUND);
        }
        catch (ConstraintViolationException cve)
        {
            throw cve;
        }
        catch (Exception ex)
        {
            return ResponseUtils.getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR_500, INTERNAL_SERVER_ERROR + ex.getMessage());
        }
    }
}
