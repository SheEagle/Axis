package com.example.entity.vo.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ClientDetailsVO {
    @NotNull
    String osArch;
    @NotNull
    String osName;
    @NotNull
    String osVersion;
    @NotNull
    Integer osBit;
    @NotNull
    Integer cpuCore;
    @NotNull
    String cpuName;
    @NotNull
    double memory;
    @NotNull
    double disk;
    @NotNull
    String ip;
}
