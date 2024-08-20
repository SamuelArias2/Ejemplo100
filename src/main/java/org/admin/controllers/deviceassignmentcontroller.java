package org.admin.controllers;

import org.admin.entities.*;
import org.admin.services.interfaces.ideviceassignmentservice;
import org.admin.services.interfaces.ideviceservice;
import org.admin.services.interfaces.iuserservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Controller
@RequestMapping("/deviceassignments")
public class deviceassignmentcontroller {

    @Autowired
    private iuserservice userservice;

    @Autowired
    private ideviceservice deviceservice;
    //comentario
    @Autowired
    private ideviceassignmentservice deviceassignmentservice;
    @GetMapping
    public String index(Model model, @RequestParam("page")Optional<Integer> page, @RequestParam("size") Optional<Integer> size){

        int currentPage = page.orElse(1) -1; //si no esta seteado se asigna 0
        int pageSize = size.orElse(5); //tamaño de la pagina, se asigna 5
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        Page<deviceassignment> deviceassignments = deviceassignmentservice.findAll(pageable);
        model.addAttribute("deviceassignments", deviceassignments);

        int totalPages = deviceassignments.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers",pageNumbers);
        }
         return  "deviceassignment/index";
    }
    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model model){
        deviceassignment deviceassignment = deviceassignmentservice.findOneById(id).get();
        model.addAttribute("deviceassignment", deviceassignment);
        return "deviceassignment/details";
    }
    @GetMapping("/create")
    public String create(deviceassignment deviceassignment, Model model){
        model.addAttribute("user", userservice.getAll());
        model.addAttribute("device", deviceservice.getAll());
        return "deviceassignment/create";
    }
    @PostMapping("/save")
    public String save(@RequestParam String status,
                       @RequestParam Integer userid,
                       @RequestParam Integer deviceid,
                       RedirectAttributes attributes){
        Date assignmentdate = new Date();
        user user= userservice.findOneById(userid).get();
        device device = deviceservice.findOneById(deviceid).get();

        if(user != null && device != null){
            deviceassignment assignmentd = new deviceassignment();
            assignmentd.setAssignmentdate(assignmentdate);
            assignmentd.setStatus(status);
            assignmentd.setUser(user);
            assignmentd.setDevice(device);


            deviceassignmentservice.save(assignmentd);
            attributes.addFlashAttribute("msg", "Equipo ingresado correctamente");
        }
        return "redirect:/deviceassignments";
     }

    @GetMapping("/edit/{assignmentid}")
    public String edit(@PathVariable("assignmentid") Integer assignmentid, Model model){
        deviceassignment deviceassignment = deviceassignmentservice.findOneById(assignmentid).get();
        model.addAttribute("user", userservice.getAll());
        model.addAttribute("device", deviceservice.getAll());
        model.addAttribute("deviceassignment", deviceassignment);
        return "deviceassignment/edit";
    }
    @PostMapping("/update")
    public String update(@RequestParam Date assignmendate,
                         @RequestParam String status,
                         @RequestParam Integer userid,
                         @RequestParam Integer deviceid,
                         RedirectAttributes attributes) {
        user user = userservice.findOneById(userid).get();
        device device = deviceservice.findOneById(deviceid).get();

        if (user != null && device != null) {
            deviceassignment assignmentid = new deviceassignment();
            assignmentid.setAssignmentdate(assignmendate);
            assignmentid.setStatus(status);
            assignmentid.setUser(user);
            assignmentid.setDevice(device);

            deviceassignmentservice.save(assignmentid);
            attributes.addFlashAttribute("msg", "Equipo modificado correctamente");
        }
        return "redirect:/deviceassignments";
    }


    @GetMapping("/remove/{assignmentid}")
    public String remove(@PathVariable("assignmentid") Integer assignmentid, Model model){
        deviceassignment deviceassignment = deviceassignmentservice.findOneById(assignmentid).get();
        model.addAttribute("deviceassignment", deviceassignment);
        return "deviceassignment/delete";
    }

    @PostMapping("/delete")
    public String delete(deviceassignment deviceassignment, RedirectAttributes attributes){
        deviceassignmentservice.deleteOneById(deviceassignment.getAssignmentid());
        attributes.addFlashAttribute("msg", "Asignación de equipo eliminado correctamente");
        return "redirect:/deviceassignments";
    }
    //comentario
    //comentario
}
