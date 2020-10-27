package com.example.demo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.model.Data;
import com.example.demo.service.AnalyticsServiceImpl;
import com.example.demo.service.DateService;
import com.example.demo.service.ReadTSV;

@SpringBootApplication
public class DemoApplication {

	static Scanner sc = new Scanner(System.in);
	static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	public static void main(String[] args) throws ParseException {
		ReadTSV read = new ReadTSV();
		List<Data> datas = read.readBooksFromCSV("/home/kirtisagar/Downloads/events.tsv");
		System.out.println("1. cli");
		System.out.println("2. web api");
		int a = sc.nextInt();
		if (a == 1) {
			System.out.println("==== calling cli ======");
			cliCall(datas);
		} else if (a == 2) {
			System.out.println("==== calling web api ======");
			SpringApplication.run(DemoApplication.class, args);
		}
	}

	private static void cliCall(List<Data> datas) throws ParseException {

		System.out.println("pass the count:");
		int count = sc.nextInt();
		AnalyticsServiceImpl analytics = new AnalyticsServiceImpl();
		List<String> topFiveViewedPages = analytics.listTopNViewedPages(datas, count);
		List<String> topFiveSpentPages = analytics.listTopNSpentPages(datas, count);
		List<String> topTenActiveUsers = analytics.listTopNActiveUsers(datas, count);

		while (true) {
			System.out.println("1. List top n pages that were viewed the most.");
			System.out.println("2. List top n pages where most time was spent.");
			System.out.println("3. List top n active users (by most time spent).");
			System.out.println("4. Print above for last quarter (in the data) overall.");
			System.out.println("5. Print above monthly for last n months.");
			System.out.println("6. Exit.");

			Scanner sc = new Scanner(System.in);
			int option = sc.nextInt();
			switch (option) {
			case 1: {
				System.out.println("top pages that were viewed the most");
				for (String str : topFiveViewedPages) {
					System.out.println(str);
				}
				break;
			}
			case 2: {
				System.out.println("top pages where most time was spent");
				for (String str : topFiveSpentPages) {
					System.out.println(str);
				}
				break;
			}
			case 3: {
				System.out.println("top active users (by most time spent)");
				for (String str : topTenActiveUsers) {
					System.out.println(str);
				}
				break;
			}
			case 4: {
				System.out.println("Print above for last quarter (in the data) overall.");
				Date d1 = DateService.getFirstDay(new Date(), 0);
				Date d2 = DateService.getLastDay(new Date(), 0);
				commonFunction(d1, d2, analytics, datas, count);
				break;
			}
			case 5: {
				System.out.println("Print above monthly for last n months.");
				System.out.println("pass the value of n:");
				int n = sc.nextInt();
				for (int i = n; i > 0; i--) {
					Date d1 = DateService.getFirstDay(new Date(), i);
					Date d2 = DateService.getLastDay(new Date(), i);
					commonFunction(d1, d2, analytics, datas, count);
				}
				break;
			}
			case 6: {
				System.exit(0);
			}
			default: {
				System.out.println("incorrect option");
			}
			}
			System.out.println();
			System.out.println();
		}
	}

	private static void commonFunction(Date d1, Date d2, AnalyticsServiceImpl analytics, List<Data> datas, int count)
			throws ParseException {

		String startDate = formatter.format(d1);
		String endDate = formatter.format(d2);

		List<List<String>> lst = analytics.printMonthlyForLastNMonths(datas, startDate, endDate, count, 0);
		List<String> list1 = lst.get(0);
		List<String> list2 = lst.get(1);
		List<String> list3 = lst.get(2);

		System.out.println("List top n pages that were viewed the most.");
		for (String str : list1) {
			System.out.println(str);
		}
		System.out.println("List top n pages where most time was spent.");
		for (String str : list2) {
			System.out.println(str);
		}
		System.out.println("List top n active users (by most time spent).");
		for (String str : list3) {
			System.out.println(str);
		}
	}
}
