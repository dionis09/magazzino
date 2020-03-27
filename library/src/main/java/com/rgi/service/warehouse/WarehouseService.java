package com.rgi.service.warehouse;

import com.rgi.dao.warehouse.WarehouseRepository;
import com.rgi.model.product.Product;
import com.rgi.model.warehouse.Warehouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class WarehouseService {

    @Autowired
    private WarehouseRepository repository;

    public Collection<? extends Warehouse> warehouses(){
        return (Collection<? extends Warehouse>)repository.findAll();
    }

    public Collection<? extends Warehouse> warehouses(long productid){
        return repository.findByProductId(productid);
    }

    public Optional<Warehouse> warehouse(long id){
        return repository.findById(id);
    }


    public void addWarehouse(Warehouse warehouse){
        Boolean ris=false;
        Boolean ris2=false;
        List<Warehouse> warehouseList = (List<Warehouse>) warehouses();
        for(Warehouse ware : warehouseList){
            if(ware.getProduct().getName().equalsIgnoreCase(warehouse.getProduct().getName()) &&
                    ware.getProduct().getShortDescription().equalsIgnoreCase(warehouse.getProduct().getShortDescription()) &&
                    ware.getProduct().getSubcategory().getId() == (warehouse.getProduct().getSubcategory().getId())){
                ware.setQuantity(ware.getQuantity()+warehouse.getQuantity());
                ris=true;
                repository.save(ware);
            }
        }
        if(warehouse.getQuantity() == 0 || warehouse.getPrice()== null || warehouse.getPrice() ==0
                || warehouse.getProduct().getName() == null
                || warehouse.getProduct().getName()==""
                || warehouse.getProduct().getShortDescription()==null || warehouse.getProduct().getShortDescription()==""
                || warehouse.getProduct().getSubcategory().getId()== 0
                || warehouse.getProduct().getSubcategory() == null || warehouse.getProduct() == null || warehouse == null) {
            warehouse.setCodice("eRrrOrE");
            ris2=true;
        }
        if(!ris && !ris2){
            repository.save(warehouse);
        }
    }

    public void updateWarehouse(long id ,Warehouse warehouse){
        Boolean ris2=false;
        Boolean ris=false;
        List<Warehouse> warehouseList = (List<Warehouse>) warehouses();
        for(Warehouse ware : warehouseList){
            if(ware.getProduct().getName().equalsIgnoreCase(warehouse.getProduct().getName()) &&
                    ware.getProduct().getShortDescription().equalsIgnoreCase(warehouse.getProduct().getShortDescription()) &&
                    ware.getProduct().getSubcategory().getId() == (warehouse.getProduct().getSubcategory().getId())){
                ware.setQuantity(ware.getQuantity()+warehouse.getQuantity());
                ris=true;
                repository.save(ware);
            }
        }
        if(warehouse.getQuantity() == 0 || warehouse.getPrice()== null || warehouse.getPrice() ==0
                || warehouse.getProduct().getName() == null
                || warehouse.getProduct().getName()==""
                || warehouse.getProduct().getShortDescription()==null || warehouse.getProduct().getShortDescription()==""
                || warehouse.getProduct().getSubcategory().getId()== 0
                || warehouse.getProduct().getSubcategory() == null || warehouse.getProduct() == null || warehouse == null) {
            warehouse.setCodice("eRrrOrE");
            ris2=true;
        }
        if(ris && !ris2){
            warehouse.setId(id);
            repository.save(warehouse);
        }
    }

    public void deleteWarehouse(long id){
        Optional<Warehouse> warehouse= warehouse(id);
        warehouse.ifPresent(value -> repository.delete(value));
    }
}
