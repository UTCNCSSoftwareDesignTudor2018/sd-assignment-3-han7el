package client.commonmark;

import org.commonmark.node.*;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import javax.swing.text.html.HTMLDocument;

public class CMParser {

    private static CMParser singleInstance = new CMParser();

    private CMParser(){

    }

    public static CMParser getCMParser(){
        return singleInstance;
    }

    public String parseToWebPage(String commonMarkString){

        Parser parser = Parser.builder().build();
        Node document = parser.parse(commonMarkString);
        HtmlRenderer renderer = HtmlRenderer.builder().build();

        return "<html>" + renderer.render(document) + "</html>";
    }
}
