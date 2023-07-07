package com.rohi.services;

import com.rohi.models.Product;
import com.rohi.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;


    @PostConstruct
    public void initialize_database(){
        List<Product> products = new ArrayList<>();
        products.add(new Product("Video game console",750,12));
        products.add(new Product("Refrigerator",5000,14));
        products.add(new Product("Laptop",25000,85));
        repository.saveAll(products);
    }


    public List<Product> displayAll(){
        return repository.findAll();
    }

    public Object addProduct(Product product){
        return repository.save(product);
    }
}
