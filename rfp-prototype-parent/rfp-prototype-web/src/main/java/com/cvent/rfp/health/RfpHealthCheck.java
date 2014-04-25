/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cvent.rfp.health;

import com.yammer.metrics.core.HealthCheck;

/**
 *
 * @author yxie
 */
public class RfpHealthCheck extends HealthCheck {

    public RfpHealthCheck() {
        super("rfp-prototype");
    }

    @Override
    protected Result check() throws Exception {
        return Result.healthy();
    }

}
