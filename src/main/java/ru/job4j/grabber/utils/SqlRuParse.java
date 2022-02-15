package ru.job4j.grabber.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SqlRuParse implements Parse {

    public final List<Post> lists = new ArrayList<>();

    private final SqlRuDateTimeParser dateTimeParser;

    public SqlRuParse(SqlRuDateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    @Override
    public List<Post> list(String link) {
        try {
            for (int i = 1; i < 6; i++) {
                String page = String.format(link + "%s", i);
                Document doc = Jsoup.connect(page).get();
                Elements row = doc.select(".postslisttopic");
                for (Element element : row) {
                    Post post = detail(element.child(0).attr("href"));
                    if (isRequiredTitle(post.getTitle())) {
                        lists.add(post);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lists;
    }

    @Override
    public Post detail(String link) {
        Post post = null;
        try {
            Document doc = Jsoup.connect(link).get();
            post = new Post(
                    doc.select(".messageHeader").get(0).ownText(),
                    link,
                    doc.select(".msgBody").get(1).text(),
                    dateTimeParser.parse(doc.select(".msgFooter")
                            .get(0).textNodes().get(0).text()
                            .replace(" [", "").substring(1)));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return post;
    }

    private boolean isRequiredTitle(String name) {
        return name.toLowerCase().contains("java") && !name.toLowerCase().contains("javascript");
    }
}