package com.example.demo.service;

import java.util.List;
import java.util.Map;

import com.example.demo.model.Data;

public interface AnalyticsService {

	List<String> listTopNViewedPages(List<Data> players, int count);
	List<String> listTopNSpentPages(List<Data> players, int count);
	List<String> listTopNActiveUsers(List<Data> players, int count);
	Map<Integer, List<List<String>>> getMonthlyReport(List<Data> datas, String startDate, String endDate, int count);
	Map<Integer, List<List<String>>> getQuarterlyReport(List<Data> datas, String startDate, String endDate, int count);
}
