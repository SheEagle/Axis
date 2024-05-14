package com.example.utils;

import com.example.entity.BaseDetails;
import com.example.entity.RuntimeDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.software.os.OperatingSystem;

import java.io.File;
import java.io.IOException;
import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.Properties;

@Component
@Slf4j
public class MonitorUtils {

    private final SystemInfo info = new SystemInfo();
    private final Properties properties = System.getProperties();

    public BaseDetails monitorBaseDetail() {
        OperatingSystem os = info.getOperatingSystem();
        HardwareAbstractionLayer hardware = info.getHardware();
        double memory = hardware.getMemory().getTotal() / 1024.0 / 1024 / 1024;
        double disk = Arrays.stream(File.listRoots()).mapToLong(File::getTotalSpace).sum() / 1024.0 / 1024 / 1024;
        String ip = Objects.requireNonNull(this.findNetworkInterface(hardware)).getIPv4addr()[0];
        return new BaseDetails()
                .setOsArch(properties.getProperty("os.arch"))
                .setOsName(os.getFamily())
                .setOsVersion(os.getVersionInfo().getVersion())
                .setOsBit(os.getBitness())
                .setCpuName(hardware.getProcessor().getProcessorIdentifier().getName())
                .setCpuCore(hardware.getProcessor().getLogicalProcessorCount())
                .setMemory(memory)
                .setDisk(disk)
                .setIp(ip);
    }

    public RuntimeDetails monitorRuntimeDetail() {
        // 统计时间为0.5秒
        double statisticTime = 0.5;
        try {
            // 获取硬件抽象层
            HardwareAbstractionLayer hardware = info.getHardware();
            // 获取网络接口，确保不为空
            NetworkIF networkInterface = Objects.requireNonNull(this.findNetworkInterface(hardware));
            // 获取处理器信息
            CentralProcessor processor = hardware.getProcessor();
            // 获取初始上传和下载字节数
            double upload = networkInterface.getBytesSent(), download = networkInterface.getBytesRecv();
            // 获取初始磁盘读写字节数
            double read = hardware.getDiskStores().stream().mapToLong(HWDiskStore::getReadBytes).sum();
            double write = hardware.getDiskStores().stream().mapToLong(HWDiskStore::getWriteBytes).sum();
            // 获取初始CPU负载滴答数
            long[] ticks = processor.getSystemCpuLoadTicks();
            // 线程休眠统计时间（0.5秒）
            Thread.sleep((long) (statisticTime * 1000));
            // 再次获取网络接口，确保不为空
            networkInterface = Objects.requireNonNull(this.findNetworkInterface(hardware));
            // 计算上传速度（字节/秒）
            upload = (networkInterface.getBytesSent() - upload) / statisticTime;
            // 计算下载速度（字节/秒）
            download = (networkInterface.getBytesRecv() - download) / statisticTime;
            // 计算磁盘读取速度（字节/秒）
            read = (hardware.getDiskStores().stream().mapToLong(HWDiskStore::getReadBytes).sum() - read) / statisticTime;
            // 计算磁盘写入速度（字节/秒）
            write = (hardware.getDiskStores().stream().mapToLong(HWDiskStore::getWriteBytes).sum() - write) / statisticTime;
            // 计算内存使用量（GB）
            double memory = (hardware.getMemory().getTotal() - hardware.getMemory().getAvailable()) / 1024.0 / 1024 / 1024;
            // 计算磁盘使用量（GB）
            double disk = Arrays.stream(File.listRoots())
                    .mapToLong(file -> file.getTotalSpace() - file.getFreeSpace()).sum() / 1024.0 / 1024 / 1024;
            // 返回包含运行时详细信息的对象
            return new RuntimeDetails()
                    .setCpuUsage(this.calculateCpuUsage(processor, ticks)) // 设置CPU使用率
                    .setMemoryUsage(memory)
                    .setDiskUsage(disk) // 设置磁盘使用量
                    .setNetworkUpload(upload / 1024) // 设置网络上传速度（KB/秒）
                    .setNetworkDownload(download / 1024) // 设置网络下载速度（KB/秒）
                    .setDiskRead(read / 1024 / 1024) // 设置磁盘读取速度（MB/秒）
                    .setDiskWrite(write / 1024 / 1024) // 设置磁盘写入速度（MB/秒）
                    .setTimestamp(new Date().getTime()); // 设置时间戳
        } catch (Exception e) {
            // 捕捉异常并记录错误信息
            log.error("读取运行时数据出现问题", e);
        }
        return null; // 发生异常时返回null
    }

    private double calculateCpuUsage(CentralProcessor processor, long[] prevTicks) {
        // 获取当前CPU负载滴答数
        long[] ticks = processor.getSystemCpuLoadTicks();
        // 计算NICE模式下的CPU时间差
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        // 计算IRQ模式下的CPU时间差
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        // 计算SOFTIRQ模式下的CPU时间差
        long softIrq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        // 计算STEAL模式下的CPU时间差
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        // 计算SYSTEM模式下的CPU时间差
        long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        // 计算USER模式下的CPU时间差
        long cUser = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        // 计算IOWAIT模式下的CPU时间差
        long ioWait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        // 计算IDLE模式下的CPU时间差
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        // 计算CPU的总时间差
        long totalCpu = cUser + nice + cSys + idle + ioWait + irq + softIrq + steal;
        // 返回CPU使用率（系统时间差+用户时间差）/ 总时间差
        return (cSys + cUser) * 1.0 / totalCpu;
    }


    // 定义一个名为 findNetworkInterface 的私有方法，接收一个 HardwareAbstractionLayer 类型的参数 hardware
    private NetworkIF findNetworkInterface(HardwareAbstractionLayer hardware) {
        try {
            // 遍历 hardware 对象中的所有网络接口
            for (NetworkIF network : hardware.getNetworkIFs()) {
                // 获取当前网络接口的 IPv4 地址数组
                String[] ipv4Addr = network.getIPv4addr();
                // 查询当前网络接口的详细信息
                NetworkInterface ni = network.queryNetworkInterface();
                // 检查网络接口是否符合以下条件：
                // 1. 不是回环接口
                // 2. 不是点对点接口
                // 3. 处于启用状态
                // 4. 不是虚拟接口
                // 5. 接口名称以 "eth" 或 "en" 开头
                // 6. IPv4 地址数组长度大于 0
                if (!ni.isLoopback() && !ni.isPointToPoint() && ni.isUp() && !ni.isVirtual()
                        && (ni.getName().startsWith("eth") || ni.getName().startsWith("en"))
                        && ipv4Addr.length > 0) {
                    // 如果以上条件都满足，则返回该网络接口
                    return network;
                }
            }
        } catch (IOException e) {
            // 捕获并处理 IO 异常，记录错误信息
            log.info("读取网络接口信息时出错！");
        }
        // 如果没有找到符合条件的网络接口，则返回 null
        return null;
    }

}
