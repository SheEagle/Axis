package com.example.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class BaseDetail {
    String osArch;
    String osName;
    String osVersion;
    Integer osBit;
    Integer cpuCore;
    String cpuName;
    double memory;
    double disk;
    String ip;

}
