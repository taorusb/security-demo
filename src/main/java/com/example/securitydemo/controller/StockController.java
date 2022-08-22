package com.example.securitydemo.controller;

import com.example.securitydemo.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@RequiredArgsConstructor
@Controller
public class StockController {

	private final StockService stockService;

	@GetMapping("/highest_stocks")
	@ResponseBody
	public String getHighestStocks() {
		return stockService.getHighestStocks();
	}

	@GetMapping("/changed_stocks")
	@ResponseBody
	public String getChangedStocks() {
		return stockService.getChangedStocks();
	}

	@GetMapping
	public String index() {
		return "index";
	}

}
