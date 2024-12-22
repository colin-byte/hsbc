package com.hsbc.trade.adapter.web;


import com.hsbc.trade.client.api.TransactionFacade;
import com.hsbc.trade.client.dto.TransModifyCmd;
import com.hsbc.trade.client.dto.common.Response;
import com.hsbc.trade.client.dto.data.TransactionVO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("transaction")
public class TransactionController {

    @Resource
    private TransactionFacade transactionService;

    @PostMapping(value = "/process")
    public Response process(@RequestBody TransModifyCmd transModifyCmd) {
        TransactionVO transactionVO = transactionService.modify(transModifyCmd);
        return Response.buildSuccess(transactionVO);
    }
}
