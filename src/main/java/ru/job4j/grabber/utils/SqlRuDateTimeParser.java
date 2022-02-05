package ru.job4j.grabber.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private static final String TODAY = "сегодня";
    private static final String YESTERDAY = "вчера";

    @Override
    public LocalDateTime parse(String parse) {
        LocalDateTime result;
        String[] dateTime = parse.split(", ");
        String[] timeParse = dateTime[1].split(":");
        LocalTime time = LocalTime.of(
                Integer.parseInt(timeParse[0]),
                Integer.parseInt(timeParse[1]));
        if (dateTime[0].equals(TODAY)) {
            LocalDate date = LocalDate.now();
            result = LocalDateTime.of(date, time);
        } else if (dateTime[0].equals(YESTERDAY)) {
            LocalDate date = LocalDate.now().minusDays(1);
            result = LocalDateTime.of(date, time);
        } else {
            String[] dateParse = dateTime[0].split(" ");
            LocalDate date = LocalDate.of(
                    Integer.parseInt(dateParse[2]),
                    Integer.parseInt(MONTHS.get(dateParse[1])),
                    Integer.parseInt(dateParse[0])
            );
            result = LocalDateTime.of(date, time);
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Elements row = doc.select(".postslisttopic");
        SqlRuDateTimeParser a = new SqlRuDateTimeParser();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (Element td : row) {
            String parent = td.parent().children().get(5).text();
            System.out.println(a.parse(parent).format(formatter));
        }
    }
}