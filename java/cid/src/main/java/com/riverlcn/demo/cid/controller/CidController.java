package com.riverlcn.demo.cid.controller;

import com.riverlcn.demo.cid.service.CustomerIdService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author river
 */
@RestController
@RequestMapping("/api/cid")
public class CidController {

    @Resource
    private CustomerIdService customerIdService;



    @RequestMapping(value = "/next", method = RequestMethod.GET)
    public String getNextCid() {
        return customerIdService.getNextCustomerId();
    }

}
