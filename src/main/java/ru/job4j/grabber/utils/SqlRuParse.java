package ru.job4j.grabber.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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
        lists.add(detail(link));
        return lists;
    }

    @Override
    public Post detail(String link) throws IOException {

        Document doc = Jsoup.connect(link).get();
        SqlRuDateTimeParser date = new SqlRuDateTimeParser();
        return new Post(
                doc.select(".messageHeader").get(0).ownText(),
                link,
                doc.select(".msgBody").get(1).text(),
                date.parse(doc.select(".msgFooter").get(0).textNodes().get(0).text()
                        .replace(" [", "").substring(1))
        );
    }
}