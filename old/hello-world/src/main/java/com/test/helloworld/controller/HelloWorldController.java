package com.test.helloworld.controller;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class HelloWorldController {
	
	@RequestMapping(path = "/")
	public String home(Model model) {
		return "index";
	}
}
