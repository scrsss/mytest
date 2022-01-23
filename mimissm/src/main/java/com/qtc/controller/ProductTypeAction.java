package com.qtc.controller;

import com.qtc.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class ProductTypeAction {
    @Autowired
    ProductTypeService productTypeService;

}