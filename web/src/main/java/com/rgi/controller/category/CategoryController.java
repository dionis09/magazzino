package com.rgi.controller.category;

import com.rgi.model.category.Category;
import com.rgi.model.product.Product;
import com.rgi.service.category.CategoryService;
import com.rgi.service.product.ProductService;
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
        model.addAttribute("categories",catService.categories());
        return "categories";
    }


    @PostMapping("/putCategory")
    public String modifyCat(@ModelAttribute Category category,Model model ){
        catService.updateCategory(category.getId(),category);
        model.addAttribute("categories",catService.categories());
        return"categories";
    }
}
