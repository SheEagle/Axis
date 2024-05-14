package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("db_client_details")
public class ClientDetails {
    @TableId
    Integer id;
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
