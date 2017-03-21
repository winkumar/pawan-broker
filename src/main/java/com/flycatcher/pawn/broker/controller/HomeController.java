package com.flycatcher.pawn.broker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author kumar
 *
 */

@Controller
public class HomeController {
	
//	@RequestMapping("/")
//    public String home() {
//		System.out.println("----- Inside parent method -----");
//        return "redirect:swagger-ui.html";
//    }
	
	
    @RequestMapping("/api-docs")
    public String apiDocs() {
    	System.out.println("----- Inside api-docs method -----");
        return "redirect:swagger-ui.html";
    }
}
