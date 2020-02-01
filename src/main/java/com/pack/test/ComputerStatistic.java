package com.pack.test;

import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;
import oshi.util.FormatUtil;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


public class ComputerStatistic {

    private String cpuLoad;
    private String ramLoad;
    private String hddLoad;
    private String cpuTemp;
    private LinkedList<MyProcess> processList;
    private String computerName;
    private String upTime;
    private String date;

    public ComputerStatistic() {

        SystemInfo si = new SystemInfo();
        OperatingSystem os = si.getOperatingSystem();
        HardwareAbstractionLayer hal = si.getHardware();

        processList = new LinkedList<>();

        List<OSProcess> procs = Arrays.asList(os.getProcesses(5, OperatingSystem.ProcessSort.CPU));

        upTime = FormatUtil.formatElapsedSecs(hal.getProcessor().getSystemUptime());
        computerName = os.getNetworkParams().getHostName();
        cpuLoad = String.format("%.2f",(hal.getProcessor().getSystemCpuLoad() * 100));
        ramLoad = FormatUtil.formatBytes(hal.getMemory().getAvailable());
        hddLoad = FormatUtil.formatBytes(getHDDLoad(os));
        cpuTemp = String.format("%.1f°C",hal.getSensors().getCpuTemperature());
        date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        for (int i = 0; i < procs.size() && i < 5; i++) {

            OSProcess p = procs.get(i);

            String name = p.getName();
            String PID = String.valueOf(p.getProcessID());
            String cpuload = String.format("%.2f", (1f * (p.getKernelTime() + p.getUserTime()) / p.getUpTime()) * 100);
            String vsz = FormatUtil.formatBytes(p.getVirtualSize());
            String rss = FormatUtil.formatBytes(p.getResidentSetSize());

            MyProcess process = new MyProcess(name,PID,cpuload,vsz,rss);

            processList.add(process);

        }


    }

    private long getHDDLoad(OperatingSystem os) {

        Long returned = null;

        FileSystem fileSystem = os.getFileSystem();
        OSFileStore[] fsArray = fileSystem.getFileStores();
        for (OSFileStore fs : fsArray) {
           returned  = fs.getUsableSpace();
           break;
        }
        return returned;
    }
}
