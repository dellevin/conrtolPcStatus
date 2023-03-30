package com.wonder.utils;
import java.util.*;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Tlhelp32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.win32.W32APIOptions;

public class ProcList{

    //data members
    private String dwSize;
    private String cntUsage;
    private String th32ProcessID;
    private String th32DefaultHeapID;
    private String th32ModuleID;
    private String cntThreads;
    private String th32ParentProcessID;
    private String pcPriClassBase;
    private String dwFlags;
    private String szExeFile;
    private String procPath;
    private List<String[]> procData = new ArrayList<String[]>();;

    //getting proc details (without path)
    public List<String[]> getProcList(){

        Kernel32 kernel32 = (Kernel32) Native.loadLibrary(Kernel32.class, W32APIOptions.DEFAULT_OPTIONS);
        Tlhelp32.PROCESSENTRY32.ByReference processEntry = new Tlhelp32.PROCESSENTRY32.ByReference();
        WinNT.HANDLE processSnapshot = kernel32.CreateToolhelp32Snapshot(Tlhelp32.TH32CS_SNAPPROCESS, new WinDef.DWORD(0));

        try {

            //looping ProcList
            while (kernel32.Process32Next(processSnapshot, processEntry)){

                //assigning details
                dwSize = processEntry.dwSize.toString();
                cntUsage = processEntry.cntUsage.toString();
                th32ProcessID = processEntry.th32ProcessID.toString();
                th32DefaultHeapID = processEntry.th32DefaultHeapID.toString();
                th32ModuleID = processEntry.th32ModuleID.toString();
                cntThreads = processEntry.cntThreads.toString();
                th32ParentProcessID = processEntry.th32ParentProcessID.toString();
                pcPriClassBase = processEntry.pcPriClassBase.toString();
                dwFlags = processEntry.dwFlags.toString();
                szExeFile = Native.toString(processEntry.szExeFile);

                //final 2d result array
                procData.add(new String[] {dwSize, cntUsage, th32ProcessID, th32DefaultHeapID, th32ModuleID, cntThreads, th32ParentProcessID, pcPriClassBase, pcPriClassBase, dwFlags, szExeFile});

            }
        }
        finally {
            kernel32.CloseHandle(processSnapshot);
        }

        //returning data
        return procData;

    }

    //getting proc details (with path)
    public List<String[]> getProcListExt(){

        Kernel32 kernel32 = (Kernel32) Native.loadLibrary(Kernel32.class, W32APIOptions.DEFAULT_OPTIONS);
        Tlhelp32.PROCESSENTRY32.ByReference processEntry = new Tlhelp32.PROCESSENTRY32.ByReference();
        WinNT.HANDLE processSnapshot = kernel32.CreateToolhelp32Snapshot(Tlhelp32.TH32CS_SNAPPROCESS, new WinDef.DWORD(0));

        try {

            //looping ProcList
            while (kernel32.Process32Next(processSnapshot, processEntry)){

                //assigning details
                dwSize = processEntry.dwSize.toString();
                cntUsage = processEntry.cntUsage.toString();
                th32ProcessID = processEntry.th32ProcessID.toString();
                th32DefaultHeapID = processEntry.th32DefaultHeapID.toString();
                th32ModuleID = processEntry.th32ModuleID.toString();
                cntThreads = processEntry.cntThreads.toString();
                th32ParentProcessID = processEntry.th32ParentProcessID.toString();
                pcPriClassBase = processEntry.pcPriClassBase.toString();
                dwFlags = processEntry.dwFlags.toString();
                szExeFile = Native.toString(processEntry.szExeFile);

                //getting path
                WinNT.HANDLE moduleSnapshot = kernel32.CreateToolhelp32Snapshot(Tlhelp32.TH32CS_SNAPMODULE, processEntry.th32ProcessID);
                ProcessPathKernel32.MODULEENTRY32.ByReference me = new ProcessPathKernel32.MODULEENTRY32.ByReference();
                ProcessPathKernel32.INSTANCE.Module32First(moduleSnapshot, me);
                procPath = me.szExePath();


                //final 2d result array
                procData.add(new String[] {dwSize, cntUsage, th32ProcessID, th32DefaultHeapID, th32ModuleID, cntThreads, th32ParentProcessID, pcPriClassBase, pcPriClassBase, dwFlags, szExeFile, procPath});


            }
        }
        finally {
            kernel32.CloseHandle(processSnapshot);
        }

        //returning data
        return procData;

    }

    public String PidAndName() {
        List<String[]> test = new ArrayList<String[]>();
        Map<String, String> coursesMap =new HashMap<String, String>();
        ProcList pl = new ProcList();
        test = pl.getProcListExt();
        String sjson="";
        for (int i = 0; i < test.size(); i++) {
            //System.out.println(Arrays.toString(test.get(i)));
            //System.out.println(test.get(i)[2]+"---"+test.get(i)[10]);
//            coursesMap.put("PID",test.get(i)[2]);
//            coursesMap.put("processName",test.get(i)[10]);

//            System.out.println("PID" + ":" + test.get(i)[2]);
//            System.out.println("processName" + ":" + test.get(i)[10]);
            sjson=sjson+"{"+"\"PID\"" + ":" +"\""+ test.get(i)[2] +"\""+","+"\"processName\"" + ":" +"\""+ test.get(i)[10]+"\""+"},";
        }
        sjson ="["+sjson.substring(0,sjson.length() - 1)+"]";
        return sjson;
//        for (int i = 0; i < test.size(); i++) {
//            //System.out.println(Arrays.toString(test.get(i)));
//            //System.out.println(test.get(i)[2]+"---"+test.get(i)[10]);
//            coursesMap.put(test.get(i)[2], test.get(i)[10]);
//        }
//        return coursesMap;
    }


    public static void main(String[] args) {
        List<String[]> test = new ArrayList<String[]>();
        ProcList pl = new ProcList();
        test = pl.getProcListExt();
//        for (String[] row : test) {
//            System.out.println(row[0]+"\t"+row[9]+"\t\t"+row[10]);
//        }
//        for (String[] strings : test) {
//            System.out.println("pid: "+strings[2]+"---"+"name: "+strings[10]);
//        }
        Map<String, String> coursesMap =new HashMap<String, String>();
//        for (String[] strings : test) {
//            System.out.println("pid: "+strings[2]+"---"+"name: "+strings[10]);
//        }
        System.out.println(test.size());
        String sjson="";
        for (int i = 0; i < test.size(); i++) {
            //System.out.println(Arrays.toString(test.get(i)));
            //System.out.println(test.get(i)[2]+"---"+test.get(i)[10]);
//            coursesMap.put("PID",test.get(i)[2]);
//            coursesMap.put("processName",test.get(i)[10]);

//            System.out.println("PID" + ":" + test.get(i)[2]);
//            System.out.println("processName" + ":" + test.get(i)[10]);
            sjson=sjson+"{"+"\"PID\"" + ":" + "\"test.get(i)[2]\""+","+"\"processName\"" + ":" + "\"test.get(i)[10]\""+"},";

        }
        sjson =sjson.substring(0,sjson.length() - 1);
        System.out.println(sjson);
//        coursesMap.entrySet().stream().forEach((entry) -> {
//                System.out.println(entry.getKey()+"---"+entry.getValue());
//        });
    }
}