package server.persistence;

import server.entity.Article;
import server.entity.Writer;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PersistenceDAO {

    //articles
    public static final String getAllArticleStatementString = "SELECT * FROM a3.`articles`";
    public static final String getArticleByIdStatementString = "SELECT * FROM a3.`articles` WHERE articleid = ?";
    public static final String insertArticleStatementString = "INSERT INTO a3.`articles` (title,abstract,body,writerid) VALUES (?,?,?,?)";
    public static final String deleteArticleStatementString = "DELETE FROM a3.`articles` WHERE articleid = ?";
    public static final String updateArticleStatementString = "UPDATE a3.`articles` SET title=?,abstract=?,body=?,writerid=? WHERE articleid=?";

    //writers
    public static final String loginWriterStatementString = "SELECT * FROM a3.`writers` WHERE username = ? and password =?";
    public static final String getAllWriterStatementString = "SELECT * FROM a3.`writers`";
    public static final String getWriterByIdStatementString = "SELECT * FROM a3.`writers` WHERE writerid = ?";
    public static final String updateWriterStatementString = "UPDATE a3.`writers` SET username=?,password=?,personalinfo=? WHERE writerid=?";

    private static final Logger LOGGER = Logger.getLogger(PersistenceDAO.class.getName());

    private static PersistenceDAO singleInstance = new PersistenceDAO();

    private PersistenceDAO(){
    }

    public static PersistenceDAO getPersistence(){
        return singleInstance;
    }

    //writer persistence

    public synchronized Writer loginWriter(Writer loggingInWriter) throws IllegalArgumentException{

        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement getSpecificStatement = null;
        ResultSet resultSet = null;
        Writer loggedInWriter;
        try{
            getSpecificStatement = dbConnection.prepareStatement(loginWriterStatementString);
            getSpecificStatement.setString(1,loggingInWriter.getUsername());
            getSpecificStatement.setString(2,loggingInWriter.getPassword());
            resultSet = getSpecificStatement.executeQuery();
            if(resultSet.next()){
                loggedInWriter = new Writer();
                loggedInWriter.setWriterid(resultSet.getInt("writerid"));
                loggedInWriter.setUsername(loggingInWriter.getUsername());
                loggedInWriter.setPassword(loggingInWriter.getPassword());
            }
            else
                loggedInWriter = null;
        }catch (SQLException e){
            LOGGER.log(Level.WARNING,"Writer:Login " + e.getMessage());
            throw new IllegalArgumentException("Problem with the Database!\n");
        }finally{
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(getSpecificStatement);
            ConnectionFactory.close(dbConnection);
        }
        return loggedInWriter;
    }

    public synchronized ArrayList<Writer> getAllWriters(){

        ArrayList<Writer> writers = new ArrayList<>();

        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement getAllStatement = null;
        ResultSet resultSet = null;

        try{
            getAllStatement = dbConnection.prepareStatement(getAllWriterStatementString);
            resultSet = getAllStatement.executeQuery();
            while(resultSet.next()){
                Writer writer = new Writer();
                writer.setWriterid(resultSet.getInt("writerid"));
                writer.setName(resultSet.getString("name"));
                writer.setUsername(resultSet.getString("username"));
                writer.setPassword(resultSet.getString("password"));
                writer.setPersonalinfo(resultSet.getString("personalinfo"));
                writer.setAddress(resultSet.getString("address"));

                writers.add(writer);
            }
        }catch(SQLException e){
            LOGGER.log(Level.WARNING,"Writer:getAll" + e.getMessage());
        }finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(getAllStatement);
            ConnectionFactory.close(dbConnection);
        }
        return writers;
    }

    public synchronized void updateWriter(Writer writerToUpdate){

        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement updateStatement = null;
        try{
            updateStatement = dbConnection.prepareStatement(updateWriterStatementString);
            updateStatement.setString(1,writerToUpdate.getUsername());
            updateStatement.setString(2,writerToUpdate.getPassword());
            updateStatement.setString(3,writerToUpdate.getPersonalinfo());
            updateStatement.setInt(4,writerToUpdate.getWriterid());
            updateStatement.executeUpdate();
        }catch(SQLException e){
            LOGGER.log(Level.WARNING,"Writer:update " + e.getMessage());
        }finally {
            ConnectionFactory.close(updateStatement);
            ConnectionFactory.close(dbConnection);
        }
    }

    public synchronized Writer getWriterById(int id){

        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement getSpecificStatement = null;
        ResultSet resultSet = null;
        Writer searchedWriter;
        try {
            getSpecificStatement = dbConnection.prepareStatement(getWriterByIdStatementString);
            getSpecificStatement.setInt(1, id);
            resultSet = getSpecificStatement.executeQuery();

            if (resultSet.next()) {
                searchedWriter = new Writer();
                searchedWriter.setWriterid(id);
                searchedWriter.setUsername(resultSet.getString("username"));
                searchedWriter.setPassword(resultSet.getString("password"));
                searchedWriter.setName(resultSet.getString("name"));
                searchedWriter.setAddress(resultSet.getString("address"));
                searchedWriter.setPersonalinfo(resultSet.getString("personalinfo"));
            }
            else
                searchedWriter = null;
        }catch (SQLException e){
            LOGGER.log(Level.WARNING,"Writer:getById " + e.getMessage());
            searchedWriter = null;
        }finally{
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(getSpecificStatement);
            ConnectionFactory.close(dbConnection);
        }
        return searchedWriter;
    }

    //articles

    public synchronized ArrayList<Article> getAllArticles(){

        ArrayList<Article> articles = new ArrayList<>();

        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement getAllStatement = null;
        ResultSet resultSet = null;

        try{
            getAllStatement = dbConnection.prepareStatement(getAllArticleStatementString);
            resultSet = getAllStatement.executeQuery();
            while(resultSet.next()){
               Article article = new Article();
                article.setArticleid(resultSet.getInt("articleid"));
                article.setTitle(resultSet.getString("title"));
                article.setAuthorid(resultSet.getInt("writerid"));
                article.setAbstractt(resultSet.getString("abstract"));
                article.setBody(resultSet.getString("body"));

                articles.add(article);
            }
        }catch(SQLException e){
            LOGGER.log(Level.WARNING,"Article:getAll" + e.getMessage());
        }finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(getAllStatement);
            ConnectionFactory.close(dbConnection);
        }
        return articles;
    }

    public synchronized Article getArticleById(int id){

        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement getSpecificStatement = null;
        ResultSet resultSet = null;
        Article searchedArticle;
        try {
            getSpecificStatement = dbConnection.prepareStatement(getArticleByIdStatementString);
            getSpecificStatement.setInt(1, id);
            resultSet = getSpecificStatement.executeQuery();

            if (resultSet.next()) {
                searchedArticle = new Article();
                searchedArticle.setArticleid(resultSet.getInt("articleid"));
                searchedArticle.setAuthorid(resultSet.getInt("writerid"));
                searchedArticle.setTitle(resultSet.getString("title"));
                searchedArticle.setAbstractt(resultSet.getString("abstract"));
                searchedArticle.setBody(resultSet.getString("body"));
            }
            else
                searchedArticle = null;
        }catch (SQLException e){
            LOGGER.log(Level.WARNING,"Article:getById " + e.getMessage());
            searchedArticle = null;
        }finally{
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(getSpecificStatement);
            ConnectionFactory.close(dbConnection);
        }
        return searchedArticle;
    }

    public synchronized void updateArticle(Article articleToUpdate){

        Connection dbConnection = ConnectionFactory.getConnection();
        PreparedStatement updateStatement = null;
        try{
            updateStatement = dbConnection.prepareStatement(updateArticleStatementString);
            updateStatement.setString(1,articleToUpdate.getTitle());
            updateStatement.setString(2,articleToUpdate.getAbstractt());
            updateStatement.setString(3,articleToUpdate.getBody());
            updateStatement.setInt(4,articleToUpdate.getAuthorid());
            updateStatement.setInt(5,articleToUpdate.getArticleid());
            updateStatement.executeUpdate();
        }catch(SQLException e){
            LOGGER.log(Level.WARNING,"Article:update " + e.getMessage());
        }finally {
            ConnectionFactory.close(updateStatement);
            ConnectionFactory.close(dbConnection);
        }
    }

    public synchronized int insertArticle(Article articleToInsert){

        Connection dbConnection = ConnectionFactory.getConnection();

        PreparedStatement insertStatement = null;
        int insertedid = -1;
        try{
            insertStatement = dbConnection.prepareStatement(insertArticleStatementString);
            insertStatement.setString(1,articleToInsert.getTitle());
            insertStatement.setString(2,articleToInsert.getAbstractt());
            insertStatement.setString(3,articleToInsert.getBody());
            insertStatement.setInt(4,articleToInsert.getAuthorid());
            insertStatement.executeUpdate();

            ResultSet rs = insertStatement.getGeneratedKeys();
            if(rs.next()){
                insertedid = rs.getInt(1);
            }

        }catch(SQLException e){
            LOGGER.log(Level.WARNING,"Article:insert " + e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        }finally{
            ConnectionFactory.close(insertStatement);
            ConnectionFactory.close(dbConnection);
        }
        return insertedid;
    }
}
