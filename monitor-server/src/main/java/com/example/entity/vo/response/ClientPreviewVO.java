package com.example.entity.vo.response;

import lombok.Data;

@Data
public class ClientPreviewVO {
    Integer id;
    boolean online;
    String name;
    String location;
    String osName;
    String osVersion;
    String ip;
    String cpuName;
    Integer cpuCore;
    double memory;
    double cpuUsage;
    double memoryUsage;
    double networkUpload;
    double networkDownload;
}
