package com.devices.app.controller.admin;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping({"/",""})
    @PreAuthorize("hasRole('ADMIN') or hasRole('MASTER_ADMIN')")
    public String admin() {
        return "redirect:/admin/dashboard"; // Chuyển hướng đến /admin/dashboard
    }


    @GetMapping("/{page}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MASTER_ADMIN')")
    public String loadPage(@PathVariable String page, Model model) {
        model.addAttribute("page", page);
        return "admin/index";
    }

}

