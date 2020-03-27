package com.rgi.service.category;

import com.rgi.dao.category.CategoryRepository;
import com.rgi.model.category.Category;

import com.rgi.model.product.Product;
import com.rgi.model.warehouse.Warehouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Service
public class CategoryService {

    @Autowired
    private CategoryRepository repository;

    public Collection<? extends Category> categories() {
        return (Collection<? extends Category>) repository.findAll();
    }

    public  Optional<Category> category(long id){
        return repository.findById(id);
    }


    public void addCategory(Category category) {
        Boolean ris=false;
        List<Category> categoryList = (List<Category>) categories();
        for(Category cat : categoryList){
            if(cat.getName().equalsIgnoreCase(category.getName())){
               ris=true;
            }
        }
        if(!ris){
            repository.save(category);
        }
        if(ris){
            category.setName("a");
        }
    }
    public void updateCategory(long id , Category category){
        category.setId(id);
        repository.save(category);
    }
    public void deleteCategory(long id){
        Optional<Category> category= repository.findById(id);
        category.ifPresent(value -> repository.delete(value));
    }
}
