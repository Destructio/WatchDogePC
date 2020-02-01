package com.pack.test;

public class MyProcess {
    private String name;
    private String PID;
    private String cpuload;
    private String VSZ;
    private String RSS;

    public String getName() {
        return name;
    }

    public String getPID() {
        return PID;
    }

    public String getCpuload() {
        return cpuload;
    }


    public String getVSZ() {
        return VSZ;
    }

    public String getRSS() {
        return RSS;
    }

    public MyProcess(String name, String PID, String cpuload,String VSZ, String RSS) {
        this.name = name;
        this.PID = PID;
        this.cpuload = cpuload;
        this.VSZ = VSZ;
        this.RSS = RSS;
    }
}
