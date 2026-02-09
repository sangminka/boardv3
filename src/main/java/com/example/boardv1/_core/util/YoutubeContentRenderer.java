package com.example.boardv1._core.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Safelist;
import org.jsoup.select.Elements;

public class YoutubeContentRenderer {

    public static String render(String html) {
        if (html == null)
            return null;

        Document doc = Jsoup.parseBodyFragment(html);

        // <a href="..."> 중 유튜브만 iframe로 교체
        Elements links = doc.select("a[href]");
        for (Element a : links) {
            String href = a.attr("href").trim();
            String id = extractYoutubeId(href);
            if (id == null)
                continue;

            a.after(buildIframe(id));
            a.remove();
        }

        Safelist wl = Safelist.basic()
                .addTags("iframe", "div", "br", "p", "table", "thead", "tbody", "tr", "td", "th")
                .addAttributes("div", "class") // ✅ 추가 (yt-wrap 유지)
                .addAttributes("table", "class")
                .addAttributes("td", "colspan", "rowspan")
                .addAttributes("th", "colspan", "rowspan")
                .addAttributes("iframe", "src", "title", "class", "frameborder", "allow", "allowfullscreen")
                .addProtocols("iframe", "src", "https");

        String cleaned = Jsoup.clean(doc.body().html(), wl);

        // 유튜브 embed만 남기기
        Document safe = Jsoup.parseBodyFragment(cleaned);
        for (Element iframe : safe.select("iframe[src]")) {
            String src = iframe.attr("src");
            boolean ok = src.startsWith("https://www.youtube-nocookie.com/embed/")
                    || src.startsWith("https://www.youtube.com/embed/");
            if (!ok)
                iframe.remove();
        }

        return safe.body().html();
    }

    private static String extractYoutubeId(String url) {
        if (url == null)
            return null;

        String watch = "https://www.youtube.com/watch?v=";
        if (url.startsWith(watch)) {
            String id = url.substring(watch.length()).split("&", 2)[0];
            return id.matches("[\\w-]{11}") ? id : null;
        }

        String shortUrl = "https://youtu.be/";
        if (url.startsWith(shortUrl)) {
            String id = url.substring(shortUrl.length()).split("[?&]", 2)[0];
            return id.matches("[\\w-]{11}") ? id : null;
        }

        return null;
    }

    private static String buildIframe(String id) {
        return "<div class=\"yt-wrap\">" +
                "<iframe class=\"yt-iframe\" " +
                "src=\"https://www.youtube-nocookie.com/embed/" + id + "\" " +
                "title=\"YouTube video\" " +
                "frameborder=\"0\" " +
                "allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" " +
                "allowfullscreen></iframe></div>";
    }
}
