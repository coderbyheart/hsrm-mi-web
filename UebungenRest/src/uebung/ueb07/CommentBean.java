package uebung.ueb07;

import java.io.*;
import java.util.*;

/**
 * Repräsentation eines Kommentars mit Datum und Text 
 * @author pb
 */
public class CommentBean implements Serializable {
	
	final static String extension = ".comment";
	
	private Date date;
	private String text;
	
	/**
	 * Erstellt neuen Kommentar mit leerem Titel und aktuellem Datum
	 */
	public CommentBean() {
		date = new Date();
		text = "";
	}

	/**
	 * Erstellt neuen Kommentar mit angegebenem Text und aktuellem Datum
	 * @param s String Text des Kommentars
	 */
	public CommentBean(String s) {
		date = new Date();
		setText(s);
	}

	/**
	 * Gibt Erstelldatum des Kommentars zurück
	 * @return Date Erstelldatum
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Setzt das Erstelldatum des Kommentars
	 * @param date Datum Erstelldatum
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * Gibt Text des Kommentars zurück.
	 * @return String Text des Kommentars
	 */
	public String getText() {
		return text;
	}

	/**
	 * Setzt Text des Kommentars. Führendes und schließende Whitespaces werden entfernt.
	 * @param text Text des Kommentars
	 */
	public void setText(String text) {
		text = text.trim();
		this.text = text;
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof CommentBean)) {
			return false;
		}
		CommentBean b = (CommentBean) o;
		return (date.equals(b.date) && text.equals(b.text));
	}
	
	/**
	 * Speichert Kommentar im angegebenen Unterverzeichnis.
	 * Wird gespeichert unter path/date.comment, wobei date das
	 * Erstelldatum des Kommentars in long millis ist. Solange
	 * schon ein Eintrag existiert wird das Erstelldatum erhöht.
	 * Ist *nicht* thread safe. 
	 */
	public void save(String path) {
		File f;
		long millis = date.getTime();
		do {
			f = new File(path, "" + (millis++) + extension);
		} while (f.exists());
		millis -= 1;
		date = new Date(millis); // adjust date in case it got adjusted
		FileHelper.save(f.getAbsolutePath(), text, "CommentBean::save");
	}

	/**
	 * Laden eines Kommentars aus einer Datei mit angegebenen Dateinamen
	 * Bei Problemen wird Ausnahme geworfen
	 * @param filename String Dateiname
	 */
	public void load(String filename) {
		if (!filename.endsWith(extension)) {
			throw new RuntimeException("load: file " + filename + " wrong extension, " + extension + "needed");		
		}
		File f = new File(filename);
		String fn = f.getName();
		String smillis = fn.substring(0, fn.length()-extension.length());
		long millis = 0;
		try {
			millis = Long.parseLong(smillis);
		} catch (NumberFormatException e) {
			throw new RuntimeException("load: file " + filename + " wrong date in filename, " +e.getMessage());	
		}
		date = new Date(millis);
		text = FileHelper.load(filename, "CommentBean::load");
	}
}
