package com.example.redis_demo;

import com.example.redis_demo.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.web.bind.annotation.*;
import com.example.redis_demo.repository.ProductDao;

import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("/product")
public class RedisDemoApplication {
    @Autowired
    private ProductDao dao;

    @Autowired
    private RedisTemplate template;

    @Autowired
    private ChannelTopic topic;

    @PostMapping("/message")
    public String publish(@RequestBody Product product) {
        template.convertAndSend(topic.getTopic(), product.toString());
        return "Event published!!";
    }

    @PostMapping
    public Product save(@RequestBody Product product) {
        return dao.save(product);
    }

    @GetMapping
    public List<Product> gelAllProducts() {
        return dao.findAll();
    }

    @GetMapping("/{id}")
    public Product findProduct(@PathVariable int id) {
        return dao.findProductById(id);
    }

    @DeleteMapping("/{id}")
    public String remove(@PathVariable int id) {
        return dao.deleteProduct(id);
    }

    public static void main(String[] args) {
        SpringApplication.run(RedisDemoApplication.class, args);
    }

}
