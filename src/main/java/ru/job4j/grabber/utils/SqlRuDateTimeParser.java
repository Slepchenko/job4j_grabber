package ru.job4j.grabber.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class SqlRuDateTimeParser implements DateTimeParser {

    private static final Map<String, String> MONTHS = Map.ofEntries(
            Map.entry("янв", "1"),
            Map.entry("фев", "2"),
            Map.entry("мар", "3"),
            Map.entry("апр", "4"),
            Map.entry("май", "5"),
            Map.entry("июн", "6"),
            Map.entry("июл", "7"),
            Map.entry("авг", "8"),
            Map.entry("сен", "9"),
            Map.entry("окт", "10"),
            Map.entry("ноя", "11"),
            Map.entry("дек", "12")
    );

    @Override
    public LocalDateTime parse(String parse) {
        LocalDateTime localDateTime;

        String[] parseDate = parse
                .replaceAll(",", "")
                .replaceAll(":", " ")
                .split(" ");

        if (parseDate.length == 5) {
        localDateTime = LocalDateTime.of(
                Integer.parseInt(parseDate[2]),
                Integer.parseInt(MONTHS.get(parseDate[1])),
                Integer.parseInt(parseDate[0]),
                Integer.parseInt(parseDate[3]),
                Integer.parseInt(parseDate[4]));
        } else {
            if (parseDate[0].equals("сегодня")) {
                localDateTime = LocalDateTime.now();
            } else {
                localDateTime = LocalDateTime.now().minusDays(1);
            }
        }
        return localDateTime;
    }

    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Elements row = doc.select(".postslisttopic");
        SqlRuDateTimeParser a = new SqlRuDateTimeParser();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm");

        for (Element td : row) {
            String parent = td.parent().children().get(5).text();
            System.out.println(parent);
            System.out.println(a.parse(parent).format(formatter));
        }
    }
}