package com.rohi.controllers;

import com.rohi.models.Product;
import com.rohi.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {

    @Autowired
    private ProductService service;
    @GetMapping("/view")
    public List<Product> displayProducts(){
        return service.displayAll() ;
    }
    
}
