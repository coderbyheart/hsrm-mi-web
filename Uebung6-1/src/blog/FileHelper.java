package blog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Hilfsklasse zum Zugriff auf Dateien
 * @author pb
 *
 */
public class FileHelper {
	
	final static String pathSep = "/"; // File.pathSeparator;

	private final static Object lock = new Object(); // serialize all ops on that
	
	/**
	 * Speichern eines Strings in einer Datei.
	 * @param filename String Datei der Datei
	 * @param content String Inhalt der Datei
	 * @param forError String Text der im Fehlerfall in der Ausnahmemessage eingefügt wird
	 */
	public static void save(String filename, String content, String forError) {
		synchronized (lock) {
			File f = new File(filename);
			try {
				f.createNewFile();
			} catch (IOException e) {
				throw new RuntimeException(forError + ": cannot create file");
			}
			if (!f.canWrite()) {
				throw new RuntimeException(forError + ": cannot write");
			}
			try {
				FileOutputStream fos = new FileOutputStream(f);
				fos.write(content.getBytes());
				fos.flush();
				fos.close();
			} catch (IOException e) {
				throw new RuntimeException(forError + ": IOException " + e.getMessage());
			}	
		}
	}
	
	/**
	 * Laden des Inhalts einer Datei.
	 * @param filename String Datei der Datei
	 * @param forError String Text der im Fehlerfall in der Ausnahmemessage eingefügt wird
	 * @return String Inhalt der Datei
	 */
	public static String load(String filename, String forError) {
		synchronized (lock) {
			File f = new File(filename);
			if (!f.exists()) {
				throw new RuntimeException(forError + ": file " + filename
						+ " does not exist");
			}
			try {
				FileInputStream fis = new FileInputStream(f);
				byte[] a = new byte[(int) f.length()];
				fis.read(a);
				String s = new String(a);
				fis.close();
				return s;
			} catch (IOException e) {
				throw new RuntimeException(forError + ": file " + filename
						+ " IOException " + e.getMessage());
			}
		}
	}
	
	/**
	 * Überprüft ob Datei existiert
	 * @param filename String Datei, die zu überprüfen ist
	 * @return
	 */
	public static boolean exists(String filename) {
		synchronized (lock) {
			File f = new File(filename);
			return f.exists();
		}
	}

	/**
	 * Stellt sicher, dass ein Verzeichnis existert. Wenn es nicht existiert, dann
	 * wird versucht es anzulegen, wenn das schief geht, dann wird eine Ausnahme geworfen.
	 * @param directory String Verzeichnis
	 * @param forError String Text der im Fehlerfall in der Ausnahmemessage eingefügt wird
	 * @return String Verzeichnis
	 */
	public static String ensureDir(String directory, String forError) {
		synchronized (lock) {
			File f = new File(directory);
			if (!f.exists()) {
				if (!f.mkdirs()) {
					throw new RuntimeException(forError + ": cannot create directory" + directory);
				}
			}
			return directory;
		}
	}
	
	public static boolean delete(String filename) {
		synchronized (lock) {
			File f = new File(filename);
			if (!f.exists()) {
				System.out.println("file " + filename + " does not exist");
			}
			boolean ret = f.delete();
			// ok, i hate Java in that regard; it is possible that deletion fails
			// if that is the case we slow down and retry a couple of times
			if (!ret) {
				int tries = 3;
				int waitmsecs = 3;
				while (tries>0 && !ret) {
					System.out.println("Failed : try " + tries + " again after " + waitmsecs);
					try {
						Thread.sleep(waitmsecs);
					} catch (InterruptedException e) {
						// ignore
					}
					f = new File(filename);
					ret = f.delete();
					waitmsecs *= 2;
					tries -= 1;
				}
			}
			return ret;
		}
	}
	
	/**
	 * Gibt Liste von Dateinamen in einem Verzeichnis zurück, die eine bestimmte Endung haben.
	 * Wirft Ausnahme, falls Verzeichnigs nicht existiert oder nicht gesetzt ist.
	 * @param directory String Verzeichnisname
	 * @param extension String Endung
	 * @return List<String> Liste von Dateinamen
	 */
	public static List<String> getFilenamesDirectory(String directory, String extension) {
		synchronized (lock) {
			ensureDir(directory, "FileHelper::getFilenamesDirectory");
			ArrayList<String> als = new ArrayList<String>();
			File d = new File(directory);
			final String ext = extension;
			File[] entries = d.listFiles(new FilenameFilter() {
				public boolean accept(File dir, String name) {
					if (ext != null) {
						return name.endsWith(ext);
					} 
					return true;
				}
			});
			for (File f : entries) {
				als.add(f.getName());
			}
			Comparator<String> comparator = new Comparator<String>() {
				final int extlen = (ext == null) ? 0 : ext.length();
				public int compare(String s1, String s2) {
					try { // try numeric sort with numbers first
						long l1 = Long.parseLong((s1.substring(0,
								s1.length() - extlen)));
						long l2 = Long.parseLong((s2.substring(0,
								s2.length() - extlen)));
						return (int) (l1 - l2);
					} catch (NumberFormatException e) {
						// no number, then String
						return s1.compareTo(s2);
					}
				}
			};
			Collections.sort(als, comparator);
			return als;
		}
	}
	
}
