package ru.job4j.grabber.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class SqlRuNumberOfPagesParser {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm");

    public static void main(String[] args) throws IOException {
        String path = "https://www.sql.ru/forum/job-offers";
        Document doc = Jsoup.connect(path).get();

        Elements pages = doc.select(".sort_options");
        Elements els = pages.get(1).child(0).child(0).child(0).children();

        int index = 1;
        for (int i = 1; i < 6; i++) {
            doc = Jsoup.connect(path).get();
            Elements row = doc.select(".postslisttopic");
            SqlRuDateTimeParser dateTimeParser = new SqlRuDateTimeParser();
            System.out.println("Page " + i);

            for (Element td : row) {
                String parent = td.parent().children().get(5).text();
                System.out.println(index++ + " - " + dateTimeParser.parse(parent).format(FORMATTER));
            }

            Element href = els.get(i);
            path = href.attr("href");

            System.out.println();
            System.out.println("=======================");
            System.out.println();
        }
    }
}
