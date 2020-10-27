package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Data;
import com.example.demo.service.AnalyticsService;
import com.example.demo.service.ReadTSV;

@RestController
@RequestMapping(value = "/reports")
public class AnalyticsController {

	@Autowired
	AnalyticsService analytics;
	
	ReadTSV read = new ReadTSV();
	List<Data> datas = read.readBooksFromCSV("/home/kirtisagar/Downloads/events.tsv");
	
	@GetMapping(path = "/pages", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> getTopNViewedPages(@RequestParam String orderBy, @RequestParam int count) {
		if(orderBy.equals("views"))
			return analytics.listTopNViewedPages(datas, count);
		else if(orderBy.equals("timeSpent"))
			return analytics.listTopNSpentPages(datas, count);
		return null;
	}
	
	@GetMapping(path = "/users", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<String> getTopNSpentPages(@RequestParam String orderBy, @RequestParam int count) {
		return analytics.listTopNActiveUsers(datas, count);
	}
	
	@GetMapping(path = "/monthly", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<Integer, List<List<String>>> getMonthlyReport(@RequestParam String startDate, @RequestParam String endDate, @RequestParam int count) {
		return analytics.getMonthlyReport(datas, startDate, endDate, count);
	}

	@GetMapping(path = "/quaterly", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<Integer, List<List<String>>> getQuarterlyReport(@RequestParam String startDate, @RequestParam String endDate, @RequestParam int count) {
		return analytics.getQuarterlyReport(datas, startDate, endDate, count);
	}
}
