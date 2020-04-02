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
import java.util.Optional;

@Controller
@RequestMapping("/magazzino")
public class SubcategoryController {

    @Autowired
    private SubcategoryService service;
    @Autowired
    private CategoryService catService;
    @Autowired
    private WarehouseService wareService;

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
        if(subcategory.getCodice().equalsIgnoreCase("ok")){
            model.addAttribute("subcategories",service.subcategories());
            return "subcategories";
        }
        if(subcategory.getCodice().equalsIgnoreCase("eRrrOrE")){
            return"campiVuotiSottocategoria";
        }

        return"erroreSottocat";
    }
    @GetMapping("/putSubcategory/{id}")
    public String modifySubcategory(@PathVariable long id, Model model){
        Subcategory subcategoryDaModificare = service.subcategory(id).orElse(null);
        model.addAttribute("modifySubcategory", subcategoryDaModificare);
        model.addAttribute("categories", catService.categories());
        return "modifySubcategory";
    }
    @GetMapping("/putSubcategoryElimina/{id}")
    public String modifySubcategoryElimina(@PathVariable long id, Model model){
        Subcategory subcategoryDaModificare = service.subcategory(id).orElse(null);
        model.addAttribute("modifySubcategoryElimina", subcategoryDaModificare);
        model.addAttribute("categories", catService.categories());
        return "modifySubcategoryElimina";
    }
    @PostMapping("/putSubcategoryElimina")
    public String modifySubcatElimina(@ModelAttribute Subcategory subcategory,Model model ){
        List<Subcategory> subcategoryList = (List<Subcategory>) service.subcategories();
        long id=0;
        for(Subcategory sub: subcategoryList) {
            if(subcategory.getId() == sub.getId())
                id = sub.getCategory().getId();
        }
        service.updateSubcategory(subcategory.getId(),subcategory);
        if(subcategory.getCodice().equalsIgnoreCase("eRrrOrE")){
            return"campiVuotiModifica";
        }
        if(subcategory.getCodice().equalsIgnoreCase("ok")){
            List<Category> categoriesList= (List<Category>) catService.categories();
            List<Warehouse> warehouseList= (List<Warehouse>) wareService.warehouses();
            List<Warehouse> listaVuota= new ArrayList<>();
            List<Category> listaVuotaCat= new ArrayList<>();
            List<Subcategory> listaVuotaSubcat= new ArrayList<>();
            Optional<Category> cat = catService.category(id);
            for(Warehouse ware : warehouseList) {
                if (ware.getProduct().getSubcategory().getCategory().getName() == cat.get().getName()){
                    listaVuota.add(ware);
                }
            }
            for(Subcategory sub : subcategoryList) {
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
            model.addAttribute("warehouses", wareService.warehouses());
            model.addAttribute("subcategories",service.subcategories());
            return "elimina";
        }
        return "erroreSottocat";
    }
    @PostMapping("/putSubcategory")
    public String modifySubcat(@ModelAttribute Subcategory subcategory,Model model ){
        service.updateSubcategory(subcategory.getId(),subcategory);
        if(subcategory.getCodice().equalsIgnoreCase("eRrrOrE")){
            return"campiVuotiModifica";
        }
        if(subcategory.getCodice().equalsIgnoreCase("ok")){
        model.addAttribute("subcategories",service.subcategories());
        return "subcategories";
        }
        return "erroreSottocat";
    }
    @GetMapping("/elimina/subcat/{id}")
    public String elimina(@PathVariable long id,@ModelAttribute Warehouse warehouse, Model model){
        List<Warehouse> warehouseList= (List<Warehouse>) wareService.warehouses();
        List<Subcategory> subcategoriesList= (List<Subcategory>) service.subcategories();
        List<Warehouse> listaVuota= new ArrayList<>();
        List<Subcategory> listaVuotaSubcat= new ArrayList<>();
        Optional<Subcategory> subcat = service.subcategory(id);
        for(Warehouse ware : warehouseList) {
            if (ware.getProduct().getSubcategory().getName() == subcat.get().getName()){
                listaVuota.add(ware);
            }
        }
        for(Subcategory sub : subcategoriesList) {
            if (sub.getId()== subcat.get().getId()){
                listaVuotaSubcat.add(sub);
            }
        }
        model.addAttribute("listaVuotaSubcat", listaVuotaSubcat);
        model.addAttribute("listaVuota", listaVuota);
        return "eliminaSubcategory";
    }
}