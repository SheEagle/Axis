package com.example.entity.vo.response;

import lombok.Data;

@Data
public class SshSettingsVO {
    String ip;
    Integer port = 22;
    String username;
    String password;
}
