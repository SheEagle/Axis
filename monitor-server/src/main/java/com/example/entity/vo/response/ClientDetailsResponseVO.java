package com.example.entity.vo.response;

import lombok.Data;

@Data
public class ClientDetailsResponseVO {
    Integer id;
    String name;
    boolean online;
    String node;
    String location;
    String ip;
    String cpuName;
    String osName;
    String osVersion;
    double memory;
    Integer cpuCore;
    double disk;


}
