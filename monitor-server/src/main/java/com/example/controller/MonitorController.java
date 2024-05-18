package com.example.controller;

import com.example.entity.RestBean;
import com.example.entity.vo.request.RenameClientVO;
import com.example.entity.vo.request.RenameNodeVO;
import com.example.entity.vo.request.RuntimeDetailsVO;
import com.example.entity.vo.response.ClientDetailsResponseVO;
import com.example.entity.vo.response.ClientPreviewVO;
import com.example.entity.vo.response.RuntimeHistoryVO;
import com.example.service.ClientService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/monitor")
public class MonitorController {
    @Resource
    ClientService service;

    @GetMapping("/list")
    public RestBean<List<ClientPreviewVO>> listAllClient() {
        return RestBean.success(service.listClients());
    }

    @PostMapping("/rename")
    public RestBean<Void> renameClient(@RequestBody @Valid RenameClientVO vo) {
        service.renameClient(vo);
        return RestBean.success();
    }

    @GetMapping("/details")
    public RestBean<ClientDetailsResponseVO> details(Integer clientId) {
        return RestBean.success(service.clientDetails(clientId));
    }

    @PostMapping("/node")
    public RestBean<Void> renameNode(@RequestBody @Valid RenameNodeVO vo) {
        service.renameNode(vo);
        return RestBean.success();
    }

    @GetMapping("/runtime-history")
    public RestBean<RuntimeHistoryVO> runtimeHistory(int clientId) {
        return RestBean.success(service.clientRuntimeDetailsHistory(clientId));
    }

    @GetMapping("/runtime-now")
    public RestBean<RuntimeDetailsVO> runtimeNow(int clientId) {
        return RestBean.success(service.clientRuntimeDetailsNow(clientId));
    }


}
