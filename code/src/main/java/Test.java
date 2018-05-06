import server.entity.Article;
import server.entity.Writer;
import server.persistence.PersistenceDAO;
import utility.Utility;

import java.util.ArrayList;


public class Test {
    public static void main(String[] args) {

        PersistenceDAO persistenceDAO = PersistenceDAO.getPersistence();

        Article newArticle = new Article();
        newArticle.setTitle("Title1");
        newArticle.setBody("body");
        newArticle.setAbstractt("abstract");
        newArticle.setAuthorid(1);
        persistenceDAO.insertArticle(newArticle);

        Article article = persistenceDAO.getArticleById(2);
        Article article2 = persistenceDAO.getArticleById(1);
        Writer writer = persistenceDAO.getWriterById(1);

        System.out.println(article.toString());
        System.out.println(writer.toString());

        System.out.println("Serialization");

        byte[] articleser = Utility.serializeObject(article);
        byte[] writerser = Utility.serializeObject(writer);

        ArrayList<Article> articlearray = new ArrayList<>();
        articlearray.add(article);
        articlearray.add(article2);

        byte[] array = Utility.serializeObject(articlearray);

        Article deserarticle = (Article)Utility.deserializeObject(articleser);
        Writer deserwriter = (Writer)Utility.deserializeObject(writerser);
        ArrayList<Article> deserarray = (ArrayList<Article>)Utility.deserializeObject(array);


        System.out.println(deserarticle.toString());
        System.out.println(deserwriter.toString());

        System.out.println("array!");
        deserarray.forEach(e-> System.out.println(e));
    }
}
