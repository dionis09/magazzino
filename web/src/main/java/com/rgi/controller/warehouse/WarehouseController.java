package com.rgi.controller.warehouse;

import com.rgi.controller.subcategory.SubcategoryController;
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
        if(warehouse.getCodice().equalsIgnoreCase("eRrrOrE")){
            return"campiVuoti";
        }else{
            model.addAttribute("products",prodService.products());
            model.addAttribute("categories",categoryService.categories());
            model.addAttribute("subcategories",catService.subcategories());
            model.addAttribute("warehouses",service.warehouses());
            return "warehouses";
        }
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
    @GetMapping("/putWarehouseElimina/{id}")
    public String modifyWarehouseElimina(@PathVariable long id, Model model){
        Warehouse warehouseDaModificare = service.warehouse(id).orElse(null);
        model.addAttribute("modifyWarehouseElimina", warehouseDaModificare);
        model.addAttribute("categories",categoryService.categories());
        model.addAttribute("subcategories",catService.subcategories());
        model.addAttribute("products", prodService.products());
        return "modifyWarehouseElimina";
    }
    @PostMapping("/putWarehouseElimina")
    public String modifyWareElimina(@ModelAttribute Warehouse warehouse, Model model ){
        List<Warehouse> warehouseList = (List<Warehouse>) service.warehouses();
        long id=0;
        for(Warehouse ware: warehouseList) {
            if(warehouse.getId() == ware.getId())
                id = ware.getProduct().getSubcategory().getCategory().getId();
        }
        prodService.updateProduct(warehouse.getProduct().getId(),warehouse.getProduct());
        service.updateWarehouse(warehouse.getId(),warehouse);
        if(warehouse.getCodice().equalsIgnoreCase("eRrrOrE")){
            return"campiVuotiModifica";
        }else{
            List<Category> categoriesList= (List<Category>) categoryService.categories();
            List<Subcategory> subcategoriesList= (List<Subcategory>) catService.subcategories();
            List<Warehouse> listaVuota= new ArrayList<>();
            List<Category> listaVuotaCat= new ArrayList<>();
            List<Subcategory> listaVuotaSubcat= new ArrayList<>();
            Optional<Category> cat = categoryService.category(id);
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
            model.addAttribute("warehouses", service.warehouses());
            model.addAttribute("subcategories", catService.subcategories());
            return "elimina";
        }
    }
    @GetMapping("/putWarehouseEliminaSubcategory/{id}")
    public String modifyWarehouseEliminaSubcategory(@PathVariable long id, Model model){
        Warehouse warehouseDaModificare = service.warehouse(id).orElse(null);
        model.addAttribute("modifyWarehouseEliminaSubcategory", warehouseDaModificare);
        model.addAttribute("categories",categoryService.categories());
        model.addAttribute("subcategories",catService.subcategories());
        model.addAttribute("products", prodService.products());
        return "modifyWarehouseEliminaSubcategory";
    }
    @PostMapping("/putWarehouseEliminaSubcategory")
    public String modifyWareEliminaSubcategory(@ModelAttribute Warehouse warehouse, Model model ){
        List<Warehouse> warehouseList = (List<Warehouse>) service.warehouses();
        long id=0;
        for(Warehouse ware: warehouseList) {
            if(warehouse.getId() == ware.getId())
             id = ware.getProduct().getSubcategory().getId();
        }
        prodService.updateProduct(warehouse.getProduct().getId(),warehouse.getProduct());
        service.updateWarehouse(warehouse.getId(),warehouse);
        if(warehouse.getCodice().equalsIgnoreCase("eRrrOrE")){
            return"campiVuotiModifica";
        }else{
            List<Subcategory> subcategoriesList= (List<Subcategory>) catService.subcategories();
            List<Warehouse> listaVuota= new ArrayList<>();
            List<Subcategory> listaVuotaSubcat= new ArrayList<>();
            Optional<Subcategory> subcat = catService.subcategory(id);
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
            model.addAttribute("warehouses", service.warehouses());model.addAttribute("subcategories", catService.subcategories());
            return "eliminaSubcategory";
        }
    }
    @PostMapping("/putWarehouse")
    public String modifyWare(@ModelAttribute Warehouse warehouse, Model model ){
        prodService.updateProduct(warehouse.getProduct().getId(),warehouse.getProduct());
        service.updateWarehouse(warehouse.getId(),warehouse);
        if(warehouse.getCodice().equalsIgnoreCase("eRrrOrE")){
            return"campiVuotiModifica";
        }else{
            model.addAttribute("warehouses", service.warehouses());
            return "warehouses";
        }
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
    @GetMapping("/campiVuoti")
    public String campiVuoti(Model model){
        return "campiVuoti";
    }
    @GetMapping("/regole")
    public String regole(Model model){
        return "regole";
    }
}
