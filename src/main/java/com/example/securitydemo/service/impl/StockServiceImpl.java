package com.example.securitydemo.service.impl;

import com.example.securitydemo.service.StockService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Log4j2
@RequiredArgsConstructor
@Service
public class StockServiceImpl implements StockService {

	private static String HIGHEST_STOCKS_URL;

	private static String CHANGED_VALUE_STOCKS_URL;

	private final RestTemplate restTemplate;

	@Override
	public String getHighestStocks() {
		log.info("IN getHighestStocks : requested highest values stocks");
		return restTemplate.getForObject(HIGHEST_STOCKS_URL, String.class);
	}

	@Override
	public String getChangedStocks() {
		log.info("IN getHighestStocks : requested changed values stocks");
		return restTemplate.getForObject(CHANGED_VALUE_STOCKS_URL, String.class);
	}

	@Value("${highest.value.stocks.url}")
	public void setHIGHEST_STOCKS_URL(String HIGHEST_STOCKS_URL) {
		StockServiceImpl.HIGHEST_STOCKS_URL = HIGHEST_STOCKS_URL;
	}

	@Value("${changed.value.stocks.url}")
	public void setCHANGED_VALUE_STOCKS_URL(String CHANGED_VALUE_STOCKS_URL) {
		StockServiceImpl.CHANGED_VALUE_STOCKS_URL = CHANGED_VALUE_STOCKS_URL;
	}
}
