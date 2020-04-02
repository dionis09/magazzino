package com.rgi.controller.category;

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
import java.util.Optional;

@Controller
@RequestMapping("/magazzino")
public class CategoryController {

    @Autowired
    private ProductService service;
    @Autowired
    private CategoryService catService;
    @Autowired
    private WarehouseService wareService;
    @Autowired
    private SubcategoryService subService;

    @GetMapping
    public String index(Model model){
        return "index";
    }

    @GetMapping("/categories")
    public String categories(Model model){
        List<Category> categoryList=new ArrayList<Category>();
        categoryList = (List<Category>)catService.categories();
        model.addAttribute("categories",categoryList);
        return "categories";
    }

    @GetMapping("/newCategory")
    public String newCategory (Model model){
        Category category = new Category();
        model.addAttribute("category", category);
        return "newCategory";
    }
    @GetMapping("/putCategory/{id}")
    public String modifyCategory(@PathVariable long id,@ModelAttribute Category editCategory, Model model){
        Category catDaModificare = catService.category(id).orElse(null);
        model.addAttribute("editCategory",catDaModificare);
        return "modifyCategory";
    }
    @GetMapping("/delete/category/{id}")
    public String deleteCategory (@PathVariable long id, Model model){
        catService.deleteCategory(id);
        model.addAttribute("categories", catService.categories());
        return "categories";
    }

    @PostMapping("/newCategory")
    public String newCategory(@ModelAttribute Category category, Model model){
        catService.addCategory(category);
        if(category.getName().equalsIgnoreCase("a")){
            return"errore";
        }
        if(category.getCodice().equalsIgnoreCase("ok")){
        model.addAttribute("categories",catService.categories());
        return "categories";
        }
        return "campiVuotiCat";

    }


    @PostMapping("/putCategory")
    public String modifyCat(@ModelAttribute Category category,Model model ){
        catService.updateCategory(category.getId(),category);
        if(category.getCodice().equalsIgnoreCase("a")){
            return"errore";
        }
        if(category.getCodice().equalsIgnoreCase("ok")){
            model.addAttribute("categories",catService.categories());
            return"categories";
        }
        return "campiVuotiModifica";
    }

    @GetMapping("/elimina/{id}")
    public String elimina(@PathVariable long id,@ModelAttribute Warehouse warehouse, Model model){
        List<Warehouse> warehouseList= (List<Warehouse>) wareService.warehouses();
        List<Category> categoriesList= (List<Category>) catService.categories();
        List<Subcategory> subcategoriesList= (List<Subcategory>) subService.subcategories();
        List<Warehouse> listaVuota= new ArrayList<>();
        List<Category> listaVuotaCat= new ArrayList<>();
        List<Subcategory> listaVuotaSubcat= new ArrayList<>();
        Optional<Category> cat = catService.category(id);
        for(Warehouse ware : warehouseList) {
            if (ware.getProduct().getSubcategory().getCategory().getName() == cat.get().getName()){
                listaVuota.add(ware);
            }
        }
        for(Subcategory sub : subcategoriesList) {
            if (sub.getCategory().getName() == cat.get().getName()){
                listaVuotaSubcat.add(sub);
            }
        }
        for(Category ware : categoriesList) {
            if (ware.getId() == cat.get().getId()){
                listaVuotaCat.add(ware);
            }
        }
        model.addAttribute("listaVuotaSubcat", listaVuotaSubcat);
        model.addAttribute("listaVuotaCat", listaVuotaCat);
        model.addAttribute("listaVuota", listaVuota);
        return "elimina";
    }
}
