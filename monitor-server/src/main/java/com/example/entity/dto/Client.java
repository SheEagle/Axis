package com.example.entity.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.example.entity.BaseData;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@TableName("db_client")
@AllArgsConstructor
public class Client implements BaseData {
    Integer id;
    String name;
    String token;
    String location;
    String node;
    Date registerTime;

}
