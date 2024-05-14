package com.example.utils;

import com.example.entity.BaseDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.NetworkIF;
import oshi.software.os.OperatingSystem;

import java.io.File;
import java.io.IOException;
import java.net.NetworkInterface;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;

@Component
@Slf4j
public class MonitorUtils {

    private final SystemInfo info = new SystemInfo();
    private final Properties properties = System.getProperties();

    public BaseDetail monitorBaseDetail() {
        OperatingSystem os = info.getOperatingSystem();
        HardwareAbstractionLayer hardware = info.getHardware();
        double memory = hardware.getMemory().getTotal() / 1024.0 / 1024 / 1024;
        double disk = Arrays.stream(File.listRoots()).mapToLong(File::getTotalSpace).sum() / 1024.0 / 1024 / 1024;
        String ip = Objects.requireNonNull(this.findNetworkInterface(hardware)).getIPv4addr()[0];
        return new BaseDetail()
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
