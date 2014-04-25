/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cvent.rfp.dao;

import com.cvent.rfp.mapper.LuMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 *
 * @author yxie
 */
public class LuDAO {

    private SqlSessionFactory sessionFactory;

    public LuDAO() {
    }

    public LuDAO(SqlSessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public boolean validateLookUp(String luTableName, String luColumnName, int valueToValidate) throws Exception {
        try (SqlSession session = sessionFactory.openSession(true)) {
            LuMapper mapper = session.getMapper(LuMapper.class);
            int validatedRow = mapper.validateLookUpId(luTableName, luColumnName, valueToValidate);

            return (validatedRow > 0);
        } catch (Exception ex) {
            throw ex;
        }
    }
}
