package com.mabao.util.express;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mabao.util.CustomJsonDateDeserializer;

import java.util.Date;

public class PackDetails {

    private Date time;

    private String context;

    private String location;

    private Date ftime;

    public Date getTime() {
        return time;
    }

    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    public void setTime(Date time) {
        this.time = time;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getFtime() {
        return ftime;
    }

    @JsonDeserialize(using = CustomJsonDateDeserializer.class)
    public void setFtime(Date ftime) {
        this.ftime = ftime;
    }

    @Override
    public String toString() {
        return "PackDetails [time=" + time + ", location=" + location
                + ", context=" + context + ", ftime=" + ftime + "]";
    }

}
