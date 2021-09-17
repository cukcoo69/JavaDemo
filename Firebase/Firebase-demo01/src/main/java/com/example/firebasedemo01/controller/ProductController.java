package com.example.firebasedemo01.controller;

import com.example.firebasedemo01.entity.Product;
import com.example.firebasedemo01.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/products")
    public String save(@RequestBody Product product) throws ExecutionException, InterruptedException {
        return productService.save(product);
    }

    @GetMapping("/products/{name}")
    public Product getProductDetail(@PathVariable String name) throws ExecutionException, InterruptedException {
        return productService.getDetailByName(name);
    }

    @GetMapping("/products")
    public List<Product> listProduct() throws ExecutionException, InterruptedException {
        return productService.getAllDetail();
    }

    @DeleteMapping("/products/{name}")
    public String delete(@PathVariable String name) throws ExecutionException, InterruptedException {
        return productService.delete(name);
    }

    @PutMapping("/products")
    public String update(@RequestBody Product product) throws ExecutionException, InterruptedException {
        return productService.update(product);
    }
}
