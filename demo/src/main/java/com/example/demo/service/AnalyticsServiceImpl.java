package com.example.demo.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import org.springframework.stereotype.Service;
import com.example.demo.model.Data;
import org.joda.time.DateTime;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

	static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public List<String> listTopNViewedPages(List<Data> datas, int count) {
		PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<Map.Entry<String, Integer>>(
				(a, b) -> b.getValue() - a.getValue());

		Map<String, Integer> map = new HashMap<String, Integer>();
		for (Data p : datas) {
			String page = p.getLocation();
			if (page != null && !page.isEmpty()) {
				page = PageExtract.extractPage(page);
				if (page != null) {
					if (!map.containsKey(page))
						map.put(page, 1);
					else {
						map.put(page, map.get(page) + 1);
					}
				}
			}
		}

		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			pq.offer(entry);
			if (pq.size() > count)
				pq.poll();
		}
		List<String> result = new ArrayList<>();
		while (pq.size() > 0) {
			result.add(pq.poll().getKey());
		}

		return result;
	}

	@Override
	public List<String> listTopNSpentPages(List<Data> datas, int count) {
		PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<Map.Entry<String, Integer>>(
				(a, b) -> b.getValue() - a.getValue());

		Map<String, Integer> map = new HashMap<String, Integer>();
		for (Data p : datas) {
			String page = p.getLocation();
			if (page != null && !page.isEmpty()) {
				page = PageExtract.extractPage(page);
				if (page != null) {
					if (!map.containsKey(page))
						map.put(page, p.getEvent_value());
					else {
						map.put(page, map.get(page) + p.getEvent_value());
					}
				}
			}
		}

		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			pq.offer(entry);
			if (pq.size() > count)
				pq.poll();
		}
		List<String> result = new ArrayList<>();
		while (pq.size() > 0) {
			result.add(pq.poll().getKey());
		}

		return result;
	}

	@Override
	public List<String> listTopNActiveUsers(List<Data> datas, int count) {
		PriorityQueue<Map.Entry<String, Integer>> pq = new PriorityQueue<Map.Entry<String, Integer>>(
				(a, b) -> b.getValue() - a.getValue());

		Map<String, Integer> map = new HashMap<String, Integer>();
		for (Data p : datas) {
			String page = p.getLocation();
			if (page != null && !page.isEmpty()) {
				page = PageExtract.extractPage(page);
				if (page != null) {
					if (!map.containsKey(p.getUuid()))
						map.put(p.getUuid(), p.getEvent_value());
					else {
						map.put(p.getUuid(), map.get(p.getUuid()) + p.getEvent_value());
					}
				}
			}
		}

		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			pq.offer(entry);
			if (pq.size() > count)
				pq.poll();
		}
		List<String> result = new ArrayList<>();
		while (pq.size() > 0) {
			result.add(pq.poll().getKey());
		}

		return result;
	}

	@Override
	public Map<Integer, List<List<String>>> getMonthlyReport(List<Data> datas, String startDate, String endDate,
			int count) {

		Date sd = null, ed = null;
		try {
			sd = formatter.parse(startDate);
			ed = formatter.parse(endDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// get month from date
		LocalDate localDate = sd.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int startMonth = localDate.getMonthValue();
		localDate = ed.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		int endMonth = localDate.getMonthValue();
		Map<Integer, List<List<String>>> map = new HashMap<Integer, List<List<String>>>();
		for (int i = startMonth; i <= endMonth; i++) {
			Date firstDay = DateService.getFirstDay(sd, -1);
			Date endDay = DateService.getLastDay(ed, -1);
			try {
				List<List<String>> list = printMonthlyForLastNMonths(datas, formatter.format(firstDay),
						formatter.format(endDay), count, i);
				map.put(i, list);
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}
		return map;
	}

	public List<List<String>> printMonthlyForLastNMonths(List<Data> datas, String startDate, String endDate, int count,
			int month) throws ParseException {

		Date sd = formatter.parse(startDate);
		Date ed = formatter.parse(endDate);
		List<Data> newData = new ArrayList<Data>();
		for (Data p : datas) {
			String dateString = p.getTstamp().split("\\s")[0];
			Date date = formatter.parse(dateString);
			if (date.compareTo(sd) >= 0 && date.compareTo(ed) <= 0) {

				if (month > 0 && new DateTime(formatter.parse(p.getTstamp())).getMonthOfYear() == month) {
					newData.add(p);
				} else if (month == 0) {
					newData.add(p);
				}
			}
		}
		List<List<String>> list = new ArrayList<List<String>>();

		List<String> topFiveViewedPagesLastQuarter = listTopNViewedPages(newData, count);
		List<String> topFiveSpentPagesLastQuarter = listTopNSpentPages(newData, count);
		List<String> topTenActiveUsersLastQuarter = listTopNActiveUsers(newData, count);
		list.add(topFiveViewedPagesLastQuarter);
		list.add(topFiveSpentPagesLastQuarter);
		list.add(topTenActiveUsersLastQuarter);
		return list;
	}

	@Override
	public Map<Integer, List<List<String>>> getQuarterlyReport(List<Data> datas, String startDate, String endDate,
			int count) {

		Map<Integer, List<List<String>>> map = new HashMap<Integer, List<List<String>>>();
		try {
			Date sd = formatter.parse(startDate);
			Date ed = formatter.parse(endDate);
			List<Data> firstQuarterList = new ArrayList<Data>();
			List<Data> secondQuarterList = new ArrayList<Data>();
			List<Data> thirdQuarterList = new ArrayList<Data>();
			List<Data> forthQuarterList = new ArrayList<Data>();

			for (Data p : datas) {
				String dateString = p.getTstamp().split("\\s")[0];
				Date date = formatter.parse(dateString);
				if (date.compareTo(sd) >= 0 && date.compareTo(ed) <= 0) {
					int m = new DateTime(formatter.parse(p.getTstamp())).getMonthOfYear();
					if (m >= 1 && m <= 3) {
						firstQuarterList.add(p);
					} else if (m >= 4 && m <= 6) {
						secondQuarterList.add(p);
					} else if (m >= 7 && m <= 9) {
						thirdQuarterList.add(p);
					} else if (m >= 10 && m <= 12) {
						forthQuarterList.add(p);
					}
				}
			}
			map.put(1, getQuarterList(firstQuarterList, count));
			map.put(2, getQuarterList(secondQuarterList, count));
			map.put(3, getQuarterList(thirdQuarterList, count));
			map.put(4, getQuarterList(forthQuarterList, count));
		} catch (Exception e) {
			// TODO: handle exception
		}
		return map;
	}

	private List<List<String>> getQuarterList(List<Data> lst, int count) {

		List<List<String>> list = new ArrayList<List<String>>();
		List<String> topFiveViewedPagesLastQuarter = listTopNViewedPages(lst, count);
		List<String> topFiveSpentPagesLastQuarter = listTopNSpentPages(lst, count);
		List<String> topTenActiveUsersLastQuarter = listTopNActiveUsers(lst, count);
		list.add(topFiveViewedPagesLastQuarter);
		list.add(topFiveSpentPagesLastQuarter);
		list.add(topTenActiveUsersLastQuarter);
		return list;
	}

}
