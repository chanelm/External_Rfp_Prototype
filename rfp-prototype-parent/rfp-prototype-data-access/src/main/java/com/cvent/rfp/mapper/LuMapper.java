/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cvent.rfp.mapper;

import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.mapping.StatementType;

/**
 *
 * @author yxie
 */
public interface LuMapper {

    String VALIDATE_LOOKUP_ID = "SELECT " + "COUNT (*) " + "FROM " + "${tableName} " + "WHERE "
            + "${idColumnName} = #{luId}";

    //<editor-fold defaultstate="collapsed" desc="validateLookUpTable">
    /**
     * Validate lookup id
     *
     * @param luTableName
     * @param idColumnName
     * @param luId
     * @return
     * @throws java.lang.Exception
     */
    @Select(VALIDATE_LOOKUP_ID)
    @Options(statementType = StatementType.CALLABLE)
    int validateLookUpId(
            @Param("tableName") String luTableName,
            @Param("idColumnName") String idColumnName,
            @Param("luId") int luId
    ) throws Exception;
    //</editor-fold>   
}
