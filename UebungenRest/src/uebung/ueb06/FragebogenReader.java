package uebung.ueb06;

import java.io.*;
import java.util.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;

/**
 * Einlesen einer XML-Datei, die einen Fragebogen repräsentiert.
 * @author barth
 */
public class FragebogenReader {

	private static Document parseXMLFile(InputStream is) {
		Document doc = null;
	    try {
	    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
	    	DocumentBuilder builder = factory.newDocumentBuilder(); 
	    	doc = builder.parse(is);
	    } catch (Exception e) {
	    	throw new RuntimeException("FragebogenReader::cannot read XML stream");
	    }
	    return doc;
	}
	
	/**
	 * Liest Fragebogen-Datei ein und gibt Map von Fragen auf Map von 
	 * Antwort auf Punkte zurück.
	 * @param is Eingabestrom von dem die XML-Datei gelesen wird
	 * @return Fragebogen (Frage-> (Antwort, Punkte)*)*
	 */
	public static SortedMap<String, SortedMap<String, Integer>> getFragebogen(InputStream is) {
		SortedMap<String, SortedMap<String, Integer>> ret = new TreeMap<String, SortedMap<String, Integer>>();
		Document doc = parseXMLFile(is);
		NodeList cn = doc.getElementsByTagName("frage");
		for (int i=0; i < cn.getLength(); i+=1) {
			Node n = cn.item(i);
			String frage = n.getAttributes().getNamedItem("text").getTextContent();
			SortedMap<String, Integer> msi = new TreeMap<String, Integer>();
			NodeList nl = n.getChildNodes();
			for (int j=0; j < nl.getLength(); j+=1) {
				Node a = nl.item(j);
				if (a.getNodeName().equals("antwort"))  {
					int wert = Integer.parseInt(a.getAttributes().getNamedItem("wert").getTextContent());
					String antwort = a.getTextContent().trim();
					msi.put(antwort, wert);
				}
			}
			ret.put(frage, msi);
		}
		return ret;
	}
	
	/**
	 * Einfacher Test des Fragebogenreaders. Nicht im Servlet-Container
	 * zu verwenden!
	 * @param args
	 */
	public static void main(String[] args) {
		InputStream is;
		try {
			is = new FileInputStream("WebContent/WEB-INF/fragebogen.xml");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		SortedMap<String, SortedMap<String, Integer>> fragen = getFragebogen(is);
		for (String frage : fragen.keySet()) {
			System.out.println(frage);
			Map<String, Integer> antworten = fragen.get(frage);
			for (String antwort : antworten.keySet()) {
				int wert = antworten.get(antwort);
				System.out.format("%4d %s\n", wert, antwort);
			}
		}
	}

}
