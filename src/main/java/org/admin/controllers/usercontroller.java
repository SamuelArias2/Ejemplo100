package org.admin.controllers;

import org.admin.entities.role;
import org.admin.entities.user;
import org.admin.services.interfaces.iroleservice;
import org.admin.services.interfaces.iuserservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/users")
public class usercontroller {

    @Autowired
    private iuserservice userservice;

    @Autowired
    private iroleservice roleService;


    @GetMapping
    public String index(Model model, @RequestParam("page") Optional<Integer> page, @RequestParam("size") Optional<Integer> size){
        int currentPage = page.orElse(1) - 1; // si no está seteado se asigna 0
        int pageSize = size.orElse(5); // tamaño de la página, se asigna 5
        Pageable pageable = PageRequest.of(currentPage, pageSize);

        Page<user> users = userservice.findAll(pageable);
        model.addAttribute("users", users);

        int totalPages = users.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "user/index";
    }

    @GetMapping("/details/{userid}")
    public String details(@PathVariable("userid") Integer userid, Model model){
        user userd = userservice.findOneById(userid).get();
        model.addAttribute("user", userd);
        return "user/details";
    }

    @GetMapping("/create")
    public String create(user user, Model model){
        model.addAttribute("role", roleService.getAll());
        return "user/create";
    }

    @PostMapping("/save")
    public String save(@RequestParam String firstname,
                       @RequestParam String lastname, @RequestParam String email,
                       @RequestParam String password, @RequestParam Integer roleid,
                       @RequestParam Integer areaid,
                       RedirectAttributes attributes){
        role role = roleService.findOneById(roleid).get();

        if(role != null){
            user userd = new user();
            userd.setFirstname(firstname);
            userd.setLastname(lastname);
            userd.setEmail(email);
            userd.setPassword(password);
            userd.setRole(role);

            userservice.save(userd);
            attributes.addFlashAttribute("msg", "Usuario creado correctamente");
        }
        return "redirect:/users";
    }

    @GetMapping("/edit/{userid}")
    public String edit(@PathVariable("userid") Integer userid, Model model){
        user userd = userservice.findOneById(userid).get();
        model.addAttribute("role", roleService.getAll());
        model.addAttribute("user", userd);
        return "user/edit";
    }

    @PostMapping("/update")
    public String update(@RequestParam Integer userid,@RequestParam String firstname,
                         @RequestParam String lastname, @RequestParam String email,
                         @RequestParam String password, @RequestParam Integer roleid,
                         @RequestParam Integer areaid,
                         RedirectAttributes attributes){
        role role = roleService.findOneById(roleid).get();

        if(role != null){
            user userd = new user();
            userd.setUserid(userid);
            userd.setFirstname(firstname);
            userd.setLastname(lastname);
            userd.setEmail(email);
            userd.setPassword(password);
            userd.setRole(role);

            userservice.createOrEditOne(userd);
            attributes.addFlashAttribute("msg", "Usuario modificado correctamente");
        }
        return "redirect:/users";
    }

    @GetMapping("/remove/{userid}")
    public String remove(@PathVariable("userid") Integer userid, Model model){
        user userd = userservice.findOneById(userid).get();
        model.addAttribute("user", userd);
        return "user/delete";
    }

    @PostMapping("/delete")
    public String delete(user userd, RedirectAttributes attributes){
        userservice.deleteOneById(userd.getUserid());
        attributes.addFlashAttribute("msg", "Usuario eliminado correctamente");
        return "redirect:/users";
    }
}
