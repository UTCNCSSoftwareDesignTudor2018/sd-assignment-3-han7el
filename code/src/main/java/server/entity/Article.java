package server.entity;

import java.io.Serializable;

public class Article implements Serializable {

    private int articleid;
    private String title;
    private String abstractt;
    private String body;
    private int authorid;

    public Article()
    {

    }

    public int getArticleid() {
        return articleid;
    }

    public void setArticleid(int articleid) {
        this.articleid = articleid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstractt() {
        return abstractt;
    }

    public void setAbstractt(String abstractt) {
        this.abstractt = abstractt;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getAuthorid() {
        return authorid;
    }

    public void setAuthorid(int authorid) {
        this.authorid = authorid;
    }

    @Override
    public String toString() {
        return "Article{" +
                "articleid=" + articleid +
                ", title='" + title + '\'' +
                ", abstractt='" + abstractt + '\'' +
                ", body='" + body + '\'' +
                ", authorid=" + authorid +
                '}';
    }
}
