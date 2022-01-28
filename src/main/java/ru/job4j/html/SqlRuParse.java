package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Elements row = doc.select(".icon_cell");
        Elements row1 = doc.select(".postslisttopic");

        for (int i = 0; i < row1.size(); i++) {

            Element href = row1.get(i).child(0);

            System.out.println(href.attr("href"));
            System.out.println(href.text());

            Element parent = row.get(i).child(5);

            System.out.println(parent.text());
        }
    }
}