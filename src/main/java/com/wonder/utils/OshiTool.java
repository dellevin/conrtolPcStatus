package com.wonder.utils;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import lombok.extern.slf4j.Slf4j;
import oshi.SystemInfo;
import oshi.hardware.*;
import oshi.software.os.OperatingSystem;

import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 读取硬件信息工具类（含加密解密）
 * 使用类库:oshi-6.3.0 和 hutool-5.8.6
 * [url=home.php?mod=space&uid=686208]@AuThor[/url] ying
 * [url=home.php?mod=space&uid=1248337]@version[/url] 1.0
 * [url=home.php?mod=space&uid=686237]@date[/url] 2022-10-24
 */

@Slf4j
public class OshiTool {

    public static void main(String[] args) {
        SystemInfo si = new SystemInfo();

        // System.out.println(getSysInfo(si));
        //getCpuInfo(si);
        getDisks(si);
        //getMemInfo(si);
        //getNetWork(si);

        //OshiTool.getDisks(si);
        //OshiTool.getUsbDevices(si);
        // OshiTool.getMemInfo(si);
//        String context=OshiTool.getSysHashCode();
//        System.out.println("原始:"+context);
//        String encyptStr=OshiTool.encryptCode(context);
//        System.out.println("加密:"+encyptStr);
//        System.out.println("解密:"+OshiTool.decryptCode(encyptStr));


    }

    /**
     * 解密
     * [url=home.php?mod=space&uid=952169]@Param[/url] encryptStr 加密字符串
     * [url=home.php?mod=space&uid=155549]@Return[/url] String
     */
    public static String decryptCode(String encryptStr){
        //随机生成密钥
        byte[] key = {12,3,6,8,9,0,7,5,4,3,7,11,88,54,12,35};
        //构建
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);

        //解密为字符串
        String decryptStr = aes.decryptStr(encryptStr, CharsetUtil.CHARSET_UTF_8);
        return decryptStr;
    }

    /**
     * 加密
     * @param content 加密内容
     * @return String
     */
    public static String encryptCode(String content) {
        //固定密钥key
        byte[] key = {12,3,6,8,9,0,7,5,4,3,7,11,88,54,12,35};
        //构建
        SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, key);
        //加密为16进制表示
        String encryptHex = aes.encryptHex(content);


        return encryptHex;
    }

    /**
     * 获取电脑 品牌+序列号+硬盘UUID
     * 用于注册码
     *
     * @return String
     */
    public static String getSysHashCode() {
        //系统信息
        SystemInfo si = new SystemInfo();
        //硬件信息
        HardwareAbstractionLayer hardware = si.getHardware();
        //计算机系统
        ComputerSystem cs = hardware.getComputerSystem();
        //生成硬件特征码
        String vendor = cs.getManufacturer();
        String processorSerialNumber = cs.getSerialNumber();
        String uuid = cs.getHardwareUUID();

        String hardcode = String.format("%08x", vendor.hashCode()) + String.format("%08x", processorSerialNumber.hashCode()) + String.format("%08x", uuid.hashCode());


        return hardcode;
    }

    /**
     * 获取外接设备信息
     *
     * @param si oshi.SystemInfo
     */
    public static void getUsbDevices(SystemInfo si) {
        try {
            HardwareAbstractionLayer hardware = si.getHardware();
            List<UsbDevice> devs = hardware.getUsbDevices(true);
            for (UsbDevice ud : devs) {
                System.out.println("Usb名称:" + ud.getName());
                System.out.println("Usb产品ID:" + ud.getProductId());
                System.out.println("Usb序列号:" + ud.getSerialNumber());
                System.out.println("Usb供应商:" + ud.getVendor());
                System.out.println("Usb唯一设备号:" + ud.getUniqueDeviceId());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 获取硬盘信息
     *
     * @param si oshi.SystemInfo
     */
    public static void getDisks(SystemInfo si) {
        List<HWDiskStore> list = si.getHardware().getDiskStores();
        for (HWDiskStore disk : list) {
            System.out.println("品牌型号：" + disk.getName() + " " + disk.getModel());

            System.out.println("硬盘序号: " + disk.getSerial());

            File win = null;

            List<HWPartition> partitions = disk.getPartitions();
            long sumFreeSize = 0;
            for (HWPartition part : partitions) {
                win = new File(part.getMountPoint());
                sumFreeSize += win.getFreeSpace();
                System.out.print("盘符：" + part.getMountPoint() + "\t");
                System.out.print("名称：" + part.getName() + "\t");
                System.out.print("大小：" + convertFileSize(part.getSize()) + "\t");
                System.out.print("使用：" + convertFileSize(part.getSize() - win.getFreeSpace()) + "\t");
                System.out.print("空闲: " + convertFileSize(win.getFreeSpace()) + "\t");
                System.out.println("使用率： " + new DecimalFormat("#.##%").format((part.getSize() - win.getFreeSpace()) * 1.0 / part.getSize()));
            }
            System.out.print("硬盘总大小: " + convertFileSize(disk.getSize()) + "\t");
            System.out.print("硬盘总空闲: " + convertFileSize(sumFreeSize) + "\t");
            System.out.print("硬盘总使用: " + convertFileSize(disk.getSize() - sumFreeSize) + "\t");
            System.out.println("硬盘总使用率: " + new DecimalFormat("#.##%").format((disk.getSize() - sumFreeSize) * 1.0 / disk.getSize()));
        }
    }

    /**
     * 获取网络信息
     *
     * @param si oshi.SystemInfo
     */
    public static void getNetWork(SystemInfo si) {

        HardwareAbstractionLayer hardware = si.getHardware();
        List<NetworkIF> netWorkifs = hardware.getNetworkIFs();
        for (NetworkIF networkIF : netWorkifs) {
            System.out.println(String.format("IPV4:%s\t网络接收:%s\t网络发送:%s\t显示名称:%s\tMAC地址:%s\t", Arrays.toString(networkIF.getIPv4addr()), networkIF.getBytesRecv(), networkIF.getBytesSent(), networkIF.getDisplayName(), networkIF.getMacaddr()));
        }
    }

    /**
     * 获取JVM信息
     *
     * @param si oshi.SystemInfo
     */
    public static void getJvmInfo(SystemInfo si) {
        Properties props = System.getProperties();
        // 当前可用的内存总量MB
        long totalMemory = Runtime.getRuntime().totalMemory();
        System.out.println("JVM当前可用的内存总量:" + convertFileSize(totalMemory));
        // 当前内存总量的近似值
        long freeMemory = Runtime.getRuntime().freeMemory();
        System.out.println("JVM当前内存总量的近似值:" + convertFileSize(freeMemory));
        // 虚拟机的最大内存容量
        long maxMemory = Runtime.getRuntime().maxMemory();
        System.out.println("JVM最大内存容量" + convertFileSize(maxMemory));
        System.out.println("JAVA版本:" + props.getProperty("java.version"));
        System.out.println("JAVA_HOME:" + props.getProperty("java.home"));
        System.out.println("用户时区：" + props.getProperty("user.timezone"));
    }

    /**
     * 获取CPU信息
     *
     * @param si oshi.SystemInfo
     * @return
     */
    public static Map<String, Object> getCpuInfo(SystemInfo si) {
        HardwareAbstractionLayer hardware = si.getHardware();
        Sensors sensors = hardware.getSensors();
        CentralProcessor processor = hardware.getProcessor();
//        System.out.println("CPU型号: " + processor.getProcessorIdentifier().getName());
//        System.out.println("CPU序列号: " + processor.getProcessorIdentifier().getProcessorID());
//        System.out.println("CPU核心: " + processor.getLogicalProcessorCount());
        //System.out.println("CPU温度: " + sensors.getCpuTemperature());
        ComputerSystem cs = hardware.getComputerSystem();
//        System.out.println("主板品牌: " + cs.getManufacturer());
//        System.out.println("主板型号: " + cs.getModel());
//        System.out.println("主板UUID: " + cs.getHardwareUUID());
//        System.out.println("主板序列号: " + cs.getSerialNumber());

        String CPUXingHao =processor.getProcessorIdentifier().getName();
        String CPUXuLieHao=processor.getProcessorIdentifier().getProcessorID();
        String CPUHeXin = String.valueOf(processor.getLogicalProcessorCount());
        String CPUWenDu = String.valueOf(sensors.getCpuTemperature());
        String ZhuBanPinPai=cs.getManufacturer();
        String ZhuBanXingHao=cs.getModel();
        String ZhuBanUUID=cs.getHardwareUUID();
        String ZhuBanXuLieHao=cs.getSerialNumber();
        Map<String,Object> cpuInfoMap = new HashMap<String,Object>();
        cpuInfoMap.put("CPUXingHao",CPUXingHao);
        cpuInfoMap.put("CPUXuLieHao",CPUXuLieHao);
        cpuInfoMap.put("CPUHeXin",CPUHeXin);
        //cpuInfoMap.put("CPUWenDu",CPUWenDu);
        cpuInfoMap.put("ZhuBanPinPai",ZhuBanPinPai);
        cpuInfoMap.put("ZhuBanXingHao",ZhuBanXingHao);
        cpuInfoMap.put("ZhuBanUUID",ZhuBanUUID);
        cpuInfoMap.put("ZhuBanXuLieHao",ZhuBanXuLieHao);
        return cpuInfoMap;
    }

    /**
     * 获取内存信息
     *
     * @param si oshi.SystemInfo
     */
    public static void getMemInfo(SystemInfo si) {
        HardwareAbstractionLayer hardware = si.getHardware();
        GlobalMemory m = hardware.getMemory();

        System.out.println("total总内存:" + convertFileSize(m.getTotal()));
        System.out.println("used已用内存:" + convertFileSize(m.getTotal() - m.getAvailable()));
        System.out.println("free剩余内存:" + convertFileSize(m.getAvailable()));
        System.out.println("usageRate内存使用率:" + new DecimalFormat("#.##%").format((m.getTotal() - m.getAvailable()) * 1.0 / m.getTotal()));

        List<PhysicalMemory> memoryList = m.getPhysicalMemory();
        int i = 0;
        for (PhysicalMemory pm : memoryList) {
            i++;
            System.out.println("第[" + i + "]根内存");
            System.out.print("内存型号:" + pm.getManufacturer() + "\t");
            System.out.print("内存规格:" + pm.getMemoryType() + "\t");
            System.out.print("内存主频:" + convertFileSize(pm.getClockSpeed()) + "\t");
            System.out.println("内存大小:" + convertFileSize(pm.getCapacity()));
        }

    }

    /**
     * 字节转换
     *
     * @param size 字节大小
     * @return 转换后值
     */
    public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else {
            return String.format("%d B", size);
        }
    }

    /**
     * 获得系统基础信息
     *
     * @param si oshi.SystemInfo
     */
    public static Map<String, Object> getSysInfo(SystemInfo si) {
        try {
            OperatingSystem os = si.getOperatingSystem();
            InetAddress ip = Inet4Address.getLocalHost();
            Properties properties = System.getProperties();

            String hostname = ip.getHostName();
            String osVersion = os.getManufacturer() + " " + os.getFamily() + os.getVersionInfo();
            String netInfo = os.getNetworkParams().toString() ;
            String gateWay =os.getNetworkParams().getIpv4DefaultGateway();
            String fileOS =  os.getFileSystem().toString();
            String bitNess = String.valueOf(os.getBitness());
            String processCount = String.valueOf(os.getProcessCount());
            String threadCount =  String.valueOf(os.getThreadCount());
            String propertyArch = properties.getProperty("os.arch");
            String propertyVersion = properties.getProperty("os.version");
            //System.out.println("主机名: " + ip.getHostName());
            //System.out.println("系统版本：" + os.getManufacturer() + " " + os.getFamily() + os.getVersionInfo());
            //System.out.println("网络信息：" + os.getNetworkParams().toString() + " 网关：" + os.getNetworkParams().getIpv4DefaultGateway());
            //System.out.println("文件系统: " + os.getFileSystem().toString());
            //System.out.println("系统支持位数: " + os.getBitness());
            //System.out.println("进程运行数量: " + os.getProcessCount());
            //System.out.println("线程运行数量: " + os.getThreadCount());
            //System.out.println("系统位数: " + properties.getProperty("os.arch"));
            //System.out.println("系统版本: " + properties.getProperty("os.version"));
            Map<String,Object> sysInfoMap = new HashMap<String,Object>();
            sysInfoMap.put("hostname",hostname);
            sysInfoMap.put("osVersion",osVersion);
            //sysInfoMap.put("netInfo",netInfo);
            sysInfoMap.put("gateWay",gateWay);
            sysInfoMap.put("fileOS",fileOS);
            sysInfoMap.put("bitNess",bitNess);
            sysInfoMap.put("processCount",processCount);
            sysInfoMap.put("threadCount",threadCount);
            sysInfoMap.put("propertyArch",propertyArch);
            sysInfoMap.put("propertyVersion",propertyVersion);
            return sysInfoMap;
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }


}