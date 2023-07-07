package com.rohi.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="product")
public class Product  {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer product_id;
    private String name;
    private float price;
    private Integer in_stock;

    public Product(String name, float price, Integer in_stock){
        this.name = name;
        this.price = price;
        this.in_stock = in_stock;
    }



}
