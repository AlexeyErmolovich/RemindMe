package com.myproject.remindme.eventbus;

import com.squareup.otto.Bus;

/**
 * Created by Алексей on 16/01/2016.
 */
public class BusProvider {

    private static final Bus BUS = new Bus();

    private BusProvider(){}

    public static Bus getInstance(){
        return BUS;
    }
}
