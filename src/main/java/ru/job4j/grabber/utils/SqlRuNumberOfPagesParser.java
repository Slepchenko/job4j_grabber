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
        String path = "https://www.sql.ru/forum/job-offers/";
        for (int i = 1; i < 6; i++) {
            String page = String.format(path + "%s", i);
            Document doc = Jsoup.connect(page).get();
            Elements row = doc.select(".postslisttopic");
            SqlRuDateTimeParser dateTimeParser = new SqlRuDateTimeParser();

            for (Element td : row) {
                String parent = td.parent().children().get(5).text();
                System.out.println(page);
                System.out.println(td.text());
                System.out.println(dateTimeParser.parse(parent).format(FORMATTER));
                System.out.println();
            }

        }
    }
}
