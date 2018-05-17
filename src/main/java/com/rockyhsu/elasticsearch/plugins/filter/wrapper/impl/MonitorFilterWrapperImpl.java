package com.rockyhsu.elasticsearch.plugins.filter.wrapper.impl;

import com.rockyhsu.elasticsearch.plugins.filter.wrapper.MonitorFilterWrapper;
import com.rockyhsu.elasticsearch.plugins.filter.wrapper.MonitorWrapper;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.support.ActionFilterChain;

import java.util.HashMap;

public class MonitorFilterWrapperImpl implements MonitorFilterWrapper {

    private HashMap<String,MonitorWrapper> filters = new HashMap<>();

    @Override
    public void initFilter() {
        MonitorWrapper dft = new DefaultMonitorWrapper();
        filters.put("default",dft);
    }

    @Override
    public MonitorWrapper getMonitorWrapper(String action, ActionRequest request, ActionListener listener, ActionFilterChain chain) {
        MonitorWrapper monitor = filters.get(action);
        if(null == monitor){
            monitor = filters.get("default");
        }
        return monitor;
    }
}
