import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.text.BadLocationException;
import javax.swing.text.EditorKit;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

/**
 * Extraire les liens d'un document HTML
 */
public class VisitPage {
	
	static ArrayList<String> visitedPage = new ArrayList<String>();
	static ArrayList<String> listArticle = new ArrayList<String>(); 

    public static void main(String[] args) {
    	String mainURL = "http://regimepaleo.wordpress.com";
        parsePage(mainURL);
        visitedPage.add(mainURL);
        for (int i = 0; i < listArticle.size();i++){
        	if (!visitedPage.contains(listArticle.get(i))){
              parsePage(listArticle.get(i));
        	}
        }
        
        
        System.out.println("Nombre Total de pages trouvées:" + listArticle.size());
        
    }

	private static void parsePage(String mainURL) {
		try {
            //Charger la page
        	
        	
        	System.out.println("Visit Page:"+ mainURL);
            URL url = new URL(mainURL);
            URLConnection uconnection = url.openConnection();
            Reader rd = new InputStreamReader(uconnection.getInputStream());
            //lire le document HTML
            EditorKit kit = new HTMLEditorKit();
            HTMLDocument doc = (HTMLDocument) kit.createDefaultDocument();
            doc.putProperty("IgnoreCharsetDirective", new Boolean(true));
            kit.read(rd, doc, 0);
            //Parcourir la balise lien
            HTMLDocument.Iterator it = doc.getIterator(HTML.Tag.A);
            while (it.isValid()) {
                SimpleAttributeSet s = (SimpleAttributeSet) it.getAttributes();
                String link = (String) s.getAttribute(HTML.Attribute.HREF);
                if (link != null) {
                    //Afficher le lien trouvé
                	if (link.contains(mainURL)){
                		if (!link.contains(mainURL + "/category")){
                			if (!link.contains(mainURL +"/tag")){
                				if (!link.contains(mainURL + "/author")){
                					if (!link.contains("#comments")){
                						if (!link.contains("#respond")){
                							if (!link.contains("#comment")){
                								if (!link.contains("?share")){
                					if (!listArticle.contains(link)){
                				      listArticle.add(link);
                					}}
                						}}
                					}
                				}
                			}
                		}
                	}
                }
                it.next();
            }
            System.out.println(listArticle.size() + " pages trouvées");
            for (int i = 0; i < listArticle.size();i++){
            	System.out.println(listArticle.get(i));
            }
        } catch (BadLocationException ex) {
            Logger.getLogger(VisitPage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(VisitPage.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
}