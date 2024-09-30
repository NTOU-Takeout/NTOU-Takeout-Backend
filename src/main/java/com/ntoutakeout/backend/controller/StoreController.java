package com.ntoutakeout.backend.controller;

import com.ntoutakeout.backend.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController("/StoreAPI")
public class StoreController {
    @Autowired
    private StoreService storeService;
}
