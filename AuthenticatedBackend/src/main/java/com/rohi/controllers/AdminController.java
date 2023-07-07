package com.rohi.controllers;

import com.rohi.models.Product;
import com.rohi.services.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class AdminController {

    @Autowired
    private ProductService service;
    
    @GetMapping("/")
    public String helloAdminController(){
        return "Admin level access";
    }

    @PostMapping("/save")
    @Transactional
    public Object addProduct(@RequestBody Product product){
        return service.addProduct(product);
    }


}
