package com.cegeka.tetherj;

import com.cegeka.tetherj.pojo.FilterLogObject;

/**
 * Created by andreicg on 6/16/16.
 */
public class EthEvent {

    private Object[] data;
    private FilterLogObject filterLogObject;

    public Object[] getData() {
        return data;
    }

    public void setData(Object[] data) {
        this.data = data;
    }

    public FilterLogObject getFilterLogObject() {
        return filterLogObject;
    }

    public void setFilterLogObject(FilterLogObject filterLogObject) {
        this.filterLogObject = filterLogObject;
    }
}
