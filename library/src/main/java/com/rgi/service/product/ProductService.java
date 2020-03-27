package com.rgi.service.product;


import com.rgi.dao.product.ProductRepository;
import com.rgi.model.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;


    public Collection<? extends Product> products(){
        return (Collection<? extends Product>)repository.findAll();
    }

    public Collection<? extends Product> products(long subcategoryid){
        return repository.findBySubcategoryId(subcategoryid);
    }

    public Optional<Product> product(long id){
        return repository.findById(id);
    }


    public void addProduct(Product product){
        repository.save(product);
    }

    public void updateProduct(long id ,Product product){
        product.setId(id);
        repository.save(product);
    }

    public void deleteProduct(long id){
        Optional<Product> product= product(id);
        product.ifPresent(value -> repository.delete(value));
    }

}
