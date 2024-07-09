package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.dto.Client;
import com.example.entity.vo.request.*;
import com.example.entity.vo.response.*;

import java.util.List;

public interface ClientService extends IService<Client> {
    boolean verifyAndRegister(String token);

    String registerToken();

    Client findClientById(int id);

    Client findClientByToken(String token);

    void updateClientDetail(ClientDetailsVO vo, Client client);

    List<ClientPreviewVO> listClients();

    void updateRuntimeDetails(RuntimeDetailsVO vo, Client client);

    void renameClient(RenameClientVO vo);

    void renameNode(RenameNodeVO vo);

    ClientDetailsResponseVO clientDetails(Integer clientId);

    RuntimeHistoryVO clientRuntimeDetailsHistory(int clientId);

    RuntimeDetailsVO clientRuntimeDetailsNow(int clientId);

    void deleteClient(int clientId);

    List<ClientSimpleVO> listSimpleList();

    void saveClientSshConnection(SshConnectionVO vo);

    SshSettingsVO sshSettings(int clientId);

}
