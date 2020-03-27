package com.rgi.controller.warehouse;

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
import java.util.List;

@Controller
@RequestMapping("/magazzino")
public class WarehouseController {
    @Autowired
    private WarehouseService service;
    @Autowired
    private ProductService prodService;
    @Autowired
    private SubcategoryService catService;
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/warehouses")
    public String warehouses(Model model){
        List<Warehouse> warehousesList=new ArrayList<Warehouse>();
        warehousesList = (List<Warehouse>)service.warehouses();
        model.addAttribute("warehouses",warehousesList );
        return "warehouses";
    }

    @GetMapping("/deleteWarehouse/{id}")
    public String deleteWarehouse (@PathVariable long id, Model model){
        service.deleteWarehouse(id);
        model.addAttribute("warehouses", service.warehouses());
        return "warehouses";
    }

    @GetMapping("/newwarehouse")
    public String newwarehouse (Model model){
        Product newProduct = new Product();
        Warehouse warehouse = new Warehouse();
        warehouse.setProduct(newProduct);
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("subcategories", catService.subcategories());
        return "newwarehouse";
    }

    @PostMapping("/newwarehouse")
    public String newwarehouse(@ModelAttribute Warehouse warehouse, Model model){
        prodService.addProduct(warehouse.getProduct());
        service.addWarehouse(warehouse);
        model.addAttribute("products",prodService.products());
        model.addAttribute("categories",categoryService.categories());
        model.addAttribute("subcategories",catService.subcategories());
        model.addAttribute("warehouses",service.warehouses());
        return "warehouses";
    }

    @GetMapping("/putWarehouse/{id}")
    public String modifyWarehouse(@PathVariable long id, Model model){
        Warehouse warehouseDaModificare = service.warehouse(id).orElse(null);
        model.addAttribute("modifyWarehouse", warehouseDaModificare);
        model.addAttribute("categories",categoryService.categories());
        model.addAttribute("subcategories",catService.subcategories());
        model.addAttribute("products", prodService.products());
        return "modifyWarehouse";
    }
    @PostMapping("/putWarehouse")
    public String modifyWare(@ModelAttribute Warehouse warehouse, Model model ){
        prodService.updateProduct(warehouse.getProduct().getId(),warehouse.getProduct());
        service.updateWarehouse(warehouse.getId(),warehouse);
        model.addAttribute("warehouses",service.warehouses());
        return "warehouses";
    }
    @GetMapping("/help")
    public String help(Model model){
        return "help";
    }
    @GetMapping("/creareProdotto")
    public String creareprodotto(Model model){
        return "creareProdotto";
    }
    @GetMapping("/bottoni")
    public String fnBottoni(Model model){
        return "bottoni";
    }
}
