/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cvent.rfp.util;

/**
 *
 * @author yxie
 */
public final class StringHelper {

    private StringHelper() {
    }
    
    /**
     *
     * @param str
     * @return
     */
    public static boolean isNullOrEmpty(String str) {
        return (str == null || str.trim().isEmpty());
    }
}
