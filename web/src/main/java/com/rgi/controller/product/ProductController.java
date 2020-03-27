package com.rgi.controller.product;

import com.rgi.dao.category.CategoryRepository;
import com.rgi.model.category.Category;
import com.rgi.model.product.Product;
import com.rgi.model.subcategory.Subcategory;
import com.rgi.model.warehouse.Warehouse;
import com.rgi.service.category.CategoryService;
import com.rgi.service.product.ProductService;
import com.rgi.service.subcategory.SubcategoryService;
import com.rgi.service.warehouse.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/magazzino")
public class ProductController {

     @Autowired
     private ProductService service;
     @Autowired
     private SubcategoryService catService;
    @Autowired
    private WarehouseService wareService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/products")
     public String products(Model model){
        List<Warehouse> warehousesList=new ArrayList<Warehouse>();
        warehousesList = (List<Warehouse>)wareService.warehouses();
        model.addAttribute("warehouses",warehousesList );
         return "products";
     }


     @GetMapping("/deleteProduct/{id}")
     public String deleteProduct (@PathVariable long id, Model model){
         service.deleteProduct(id);
         model.addAttribute("products", service.products());
         return "products";
     }

    @GetMapping("/newproduct")
    public String newproduct (Model model){
        Product newProduct = new Product();
        Warehouse warehouse = new Warehouse();
        warehouse.setProduct(newProduct);
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("products", service.products());
        model.addAttribute("subcategories", catService.subcategories());
        return "newproduct";
    }

    @PostMapping("/newproduct")
    public String newproduct(@ModelAttribute Warehouse warehouse, Model model){
        service.addProduct(warehouse.getProduct());
        wareService.addWarehouse(warehouse);
//        Product newProduct = service.product(warehouse.getProduct().getId()).orElse(null);
//        warehouse.setProduct(newProduct);
        model.addAttribute("products",service.products());
        model.addAttribute("categories",categoryService.categories());
        model.addAttribute("subcategories",catService.subcategories());
        return "products";
    }
    @GetMapping("/put/{id}")
    public String modifyProduct(@PathVariable long id, Model model){
       Product prodDaModificare = service.product(id).orElse(null);
           model.addAttribute("modifyProduct", prodDaModificare);
           model.addAttribute("subcategories", catService.subcategories());
           return "modifyProduct";
    }
    @PostMapping("/put")
    public String modifyProd(@ModelAttribute Product product,Model model ){
        service.updateProduct(product.getId(),product);
        model.addAttribute("products",service.products());
        return "products";
    }
}
