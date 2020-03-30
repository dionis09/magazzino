package com.rgi.service.subcategory;

import com.rgi.dao.product.ProductRepository;
import com.rgi.dao.subcategory.SubcategoryRepository;
import com.rgi.model.category.Category;
import com.rgi.model.product.Product;
import com.rgi.model.subcategory.Subcategory;
import com.rgi.model.warehouse.Warehouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class SubcategoryService {

    @Autowired
    private SubcategoryRepository repository;

    public Collection<? extends Subcategory> subcategories(){
        return (Collection<? extends Subcategory>)repository.findAll();
    }

    public Collection<? extends Subcategory> subcategories(long categoryid){
        return repository.findByCategoryId(categoryid);
    }

    public Optional<Subcategory> subcategory(long id){
        return repository.findById(id);
    }


    public void addSubcategory(Subcategory subcategory){
        Boolean ris=false;
        boolean ris2=false;
        List<Subcategory> subcategoryList = (List<Subcategory>) subcategories();
        for(Subcategory subcat : subcategoryList){
            if (subcat.getName().equalsIgnoreCase(subcategory.getName())) {
                ris = true;
                break;
            }
        }
        if(subcategory.getName() == null ||subcategory.getName()=="" ||subcategory.getDescription()==""
                || subcategory.getCategory() == null || subcategory.getDescription()==null){
            subcategory.setCodice("eRrrOrE");
            ris2=true;
        }else{
            subcategory.setCodice("ok");
        }
        if(!ris && !ris2){
            repository.save(subcategory);
        }
        if(ris){
            subcategory.setCodice("a");
        }
    }

    public void updateSubcategory(long id ,Subcategory subcategory){
        boolean ris2=false;
        boolean ris= false;
        List<Subcategory> subcategoryList = (List<Subcategory>) subcategories();
        for(Subcategory subcat : subcategoryList){
            if (subcat.getName().equalsIgnoreCase(subcategory.getName())) {
                ris = true;
                break;
            }
        }
        if(subcategory.getId() == 0 || subcategory.getName() == null || subcategory.getCategory() == null
                || subcategory.getDescription()==null || subcategory.getCategory().getName()== null
                || subcategory.getCategory().getId() == 0 || subcategory.getCategory().getTax() == 0){
            subcategory.setCodice("eRrrOrE");
            ris2=true;
        }else{
            subcategory.setCodice("ok");
        }
        if(!ris2 && !ris){
            subcategory.setId(id);
            repository.save(subcategory);
        }
    }

    public void deleteSubcategory(long id){
        Optional<Subcategory> subcategory= subcategory(id);
        subcategory.ifPresent(value -> repository.delete(value));
    }
}
