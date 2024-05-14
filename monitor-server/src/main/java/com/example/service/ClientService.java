package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Client;
import com.example.entity.vo.request.ClientDetailsVO;

public interface ClientService extends IService<Client> {
    boolean verifyAndRegister(String token);

    String registerToken();

    Client findClientById(int id);

    Client findClientByToken(String token);

    void updateClientDetail(ClientDetailsVO vo, Client client);
}
