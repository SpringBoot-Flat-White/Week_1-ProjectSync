package com.example.ProjectSync.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * RootController - Handles root path routing
 * Ensures that accessing / loads the index.html page
 */
@Controller
public class RootController {

    /**
     * Serve the index.html page at the root path
     * @return view name to render
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }
}
