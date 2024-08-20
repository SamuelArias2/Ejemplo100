package org.admin.controllers;

import org.admin.entities.area;
import org.admin.entities.device;
import org.admin.entities.role;
import org.admin.entities.user;
import org.admin.services.interfaces.iareaservice;
import org.admin.services.interfaces.ideviceservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/devices")
public class devicecontroller {

    @Autowired
    private ideviceservice deviceservice;

    @Autowired
    private iareaservice areaservice;

    @GetMapping
    public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size){
        int currentPage = page.orElse(1) - 1; // si no está seteado se asigna 0
        int pageSize = size.orElse(5); // tamaño de la página, se asigna 5
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        Page<device> devices = deviceservice.findAll(pageable);
        model.addAttribute("devices", devices);

        int totalPages = devices.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "device/index";
    }

    @GetMapping("/details/{deviceid}")
    public String details(@PathVariable("deviceid") Integer id, Model model){
        device deviced = deviceservice.findOneById(id).get();
        model.addAttribute("device", deviced);
        return "device/details";
    }

    @GetMapping("/create")
    public String create(device device, Model model){
        model.addAttribute("area", areaservice.getAll());
        return "device/create";
    }

    @PostMapping("/save")
    public String save(@RequestParam String devicename,
                       @RequestParam String operatingsystem,
                       @RequestParam String brand,
                       @RequestParam java.sql.Date dateofentry,
                       @RequestParam String status,
                       @RequestParam Integer areaid,
                       RedirectAttributes attributes){

        area area = areaservice.findOneById(areaid).get();

        if(area != null){
            device deviced = new device();
            deviced.setDevicename(devicename);
            deviced.setOperatingsystem(operatingsystem);
            deviced.setBrand(brand);
            deviced.setDateofentry(dateofentry);
            deviced.setStatus(status);
            deviced.setArea(area);

            deviceservice.save(deviced);
            attributes.addFlashAttribute("msg", "Equipo creado correctamente");
        }
        return "redirect:/devices";
     }


@GetMapping("/edit/{deviceid}")
public String edit(@PathVariable("deviceid") Integer deviceid, Model model){
    device deviced = deviceservice.findOneById(deviceid).get();
    model.addAttribute("area", areaservice.getAll());
    model.addAttribute("device", deviced);
    return "device/edit";
}

    @PostMapping("/update")
    public String update(@RequestParam Integer deviceid,@RequestParam String devicename,
                       @RequestParam String operatingsystem,
                       @RequestParam String brand,
                       @RequestParam java.sql.Date dateofentry,
                       @RequestParam String status,
                       @RequestParam Integer areaid,
                       RedirectAttributes attributes){

        area area = areaservice.findOneById(areaid).get();

        if(area != null){
            device deviced = new device();
            deviced.setDeviceid(deviceid);
            deviced.setDevicename(devicename);
            deviced.setOperatingsystem(operatingsystem);
            deviced.setBrand(brand);
            deviced.setDateofentry(dateofentry);
            deviced.setStatus(status);
            deviced.setArea(area);

            deviceservice.createOrEditOne(deviced);
            attributes.addFlashAttribute("msg", "Equipo creado correctamente");
        }
        return "redirect:/devices";
    }

    @GetMapping("/remove/{deviceid}")
    public String remove(@PathVariable("deviceid") Integer id, Model model){
        device deviced = deviceservice.findOneById(id).get();
        model.addAttribute("device", deviced);
        return "device/delete";
    }

    @PostMapping("/delete")
    public String delete(device deviced, RedirectAttributes attributes){
        deviceservice.deleteOneById(deviced.getDeviceid());
        attributes.addFlashAttribute("msg", "Equipo eliminado correctamente");
        return "redirect:/devices";
    }
}
