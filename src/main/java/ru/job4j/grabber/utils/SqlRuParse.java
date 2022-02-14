package ru.job4j.grabber.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SqlRuParse implements Parse {

    private final List<Post> lists = new ArrayList<>();

    private final SqlRuDateTimeParser dateTimeParser;

    public SqlRuParse(SqlRuDateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    @Override
    public List<Post> list(String link) throws IOException {
        for (int i = 1; i < 6; i++) {
            String page = String.format(link + "%s", i);
            Document doc = Jsoup.connect(page).get();
            Elements row = doc.select(".postslisttopic");
            for (Element element : row) {
                lists.add(detail(element.child(0).attr("href")));
            }
        }
        return lists;
    }

    @Override
    public Post detail(String link) throws IOException {
        Document doc = Jsoup.connect(link).get();
        return new Post(
                doc.select(".messageHeader").get(0).ownText(),
                link,
                doc.select(".msgBody").get(1).text(),
                dateTimeParser.parse(doc.select(".msgFooter").get(0).textNodes().get(0).text()
                        .replace(" [", "").substring(1))
        );
    }
}