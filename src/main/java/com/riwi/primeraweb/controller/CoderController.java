package com.riwi.primeraweb.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.riwi.primeraweb.entity.Coder;
import com.riwi.primeraweb.services.CoderService;

@Controller
@RequestMapping("/") /* "/" para no tener rutas por el momento */
public class CoderController {
    @Autowired
    private CoderService objCoderService;

    /**
     * Método para mostrar la vista y enviarle la lista coders
     */
    @GetMapping
    public String showViewGetAll(Model objModel, @RequestParam(defaultValue = "1") int page, 
            @RequestParam(defaultValue = "3") int size){
        // Llamo del servicio y guardo la lista de coders
        Page<Coder> list = this.objCoderService.findPaginated(page -1, size);
        objModel.addAttribute("coderList", list); //Llave - valor
        objModel.addAttribute("currentPage", page); //Llave - valor
        objModel.addAttribute("totalPages", list.getTotalPages()); //Llave - valor

        //Se debe retornar el nombre exacto de la vista html, sin la extensión
        return "viewCoder";
    }

    @GetMapping("/form") //Mostrar el formulario
    public String showViewFormCoder(Model objModel) {

        objModel.addAttribute("coder", new Coder()); //El valor es una nueva instancia de un coder vacío
        objModel.addAttribute("action", "/coder/create"); //Acción a ejecutar
        return "viewForm";
    }

    /*
     * Método para mostrar el formulario de actualizar un coder
     */
    @GetMapping("/update/{id}") //{} porque el valor será dinámico. //PathVariable retorna el valor de la ruta, se debe llamar exactamente igual
    public String showFormUpdate(@PathVariable Long id, Model objModel){
        Coder objCoderFind = this.objCoderService.findById(id);
        objModel.addAttribute("coder", objCoderFind); //El valor es una nueva instancia de un coder lleno, con ese id
        objModel.addAttribute("action", "/edit/" + id); //Acción a ejecutar
        return "viewForm";
    }

    /*
     * Método para actualizar
     */
    @PostMapping("/edit/{id}")
    public String updateCoder(@PathVariable Long id, @ModelAttribute Coder objCoder){ //Obtener coder actualizado que se lleno en el formulario
        this.objCoderService.update(id, objCoder);

        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteCoder(@PathVariable Long id){
        this.objCoderService.delete(id);
        return "redirect:/";
    }

    /**
     * Método para insertar un coder mediante el verbo POST
     * ModelAttribute se encarga de obtener la información enviada desde la vista
     */
    @PostMapping("/coder/create") //Cuando se le da al botón enviar
    public String createCoder(@ModelAttribute Coder objCoder){ 
        //Llamamos al servicio enviándole al coder a insertar
        this.objCoderService.insert(objCoder);
        return "redirect:/";
    }
}
