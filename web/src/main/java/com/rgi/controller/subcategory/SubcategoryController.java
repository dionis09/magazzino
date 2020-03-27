package com.rgi.controller.subcategory;

import com.rgi.model.category.Category;
import com.rgi.model.product.Product;
import com.rgi.model.subcategory.Subcategory;
import com.rgi.model.warehouse.Warehouse;
import com.rgi.service.category.CategoryService;
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
public class SubcategoryController {

    @Autowired
    private SubcategoryService service;
    @Autowired
    private CategoryService catService;

    @GetMapping("/subcategories")
    public String subcategories(Model model){
        List<Subcategory> subcategoriesList=new ArrayList<Subcategory>();
        subcategoriesList = (List<Subcategory>)service.subcategories();
        model.addAttribute("subcategories",subcategoriesList );
        return "subcategories";
    }

    @GetMapping("/deleteSubcategory/{id}")
    public String deleteSubcategory (@PathVariable long id, Model model){
        service.deleteSubcategory(id);
        model.addAttribute("subcategories", service.subcategories());
        return "subcategories";
    }
    @GetMapping("/newsubcategory")
    public String newsubcategory (Model model){
        Subcategory subcategory = new Subcategory();
        subcategory.setCategory(new Category());
        model.addAttribute("subcategory", subcategory);
        model.addAttribute("categories", catService.categories());
        return "newsubcategory";
    }

    @PostMapping("/newsubcategory")
    public String newsubcategory(@ModelAttribute Subcategory subcategory, Model model){
        Category newCategory = catService.category(subcategory.getCategory().getId()).orElse(null);
        subcategory.setCategory(newCategory);
        service.addSubcategory(subcategory);
        if(!subcategory.getName().equalsIgnoreCase("a")){
            model.addAttribute("subcategories",service.subcategories());
            return "subcategories";
        }
        return"errore";
    }
    @GetMapping("/putSubcategory/{id}")
    public String modifySubcategory(@PathVariable long id, Model model){
        Subcategory subcategoryDaModificare = service.subcategory(id).orElse(null);
        model.addAttribute("modifySubcategory", subcategoryDaModificare);
        model.addAttribute("categories", catService.categories());
        return "modifySubcategory";
    }
    @PostMapping("/putSubcategory")
    public String modifySubcat(@ModelAttribute Subcategory subcategory,Model model ){
        service.updateSubcategory(subcategory.getId(),subcategory);
        model.addAttribute("subcategories",service.subcategories());
        return "subcategories";
    }
}
