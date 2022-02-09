package ru.job4j.grabber.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class SqlRuNumberOfPagesParser {

    public static void main(String[] args) throws IOException {
        String path = "https://www.sql.ru/forum/job-offers/";
        for (int i = 1; i < 6; i++) {
            String page = String.format(path + "%s", i);
            Document doc = Jsoup.connect(page).get();
            Elements row = doc.select(".postslisttopic");

            for (Element td : row) {
                System.out.println(td.child(0).attr("href"));
                System.out.println(td.text());
                System.out.println();
            }

        }
    }
}
