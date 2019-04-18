package com.lenovo.doc;

public class time_model {
    private String time;
    private String traffic;

    public time_model(String time, String traffic) {
        this.time = time;
        this.traffic = traffic;
    }

    public String getTime() {
        return time;
    }

    public String getTraffic() {
        return traffic;
    }
}
