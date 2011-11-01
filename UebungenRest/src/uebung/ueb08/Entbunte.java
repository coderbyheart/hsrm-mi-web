package uebung.ueb08;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.*;
// hopefully no problems on the server
// (SUN) javavm might require -Djava.awt.headless=true
import java.awt.color.*;
import java.awt.image.*;

/**
 * Utilties to work with a directory containing PNG Files (.png)
 * @author pb
 */
public class Entbunte {

	static String imagePath;

	public Entbunte() {	}

	/**
	 * Sets the path where the images are located
	 * @param imagePath 
	 */
	public void setImagePath(String imagePath) {
		Entbunte.imagePath = imagePath;
	}
	
	/**
	 * Converts a BufferdImage to a grayscale image
	 * @param image BufferedImage to convert
	 * @return BufferedImage as grayscale
	 */
	static BufferedImage grayScale(BufferedImage image) {
		ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY); 
		BufferedImageOp op = new ColorConvertOp(cs, null); 
		return op.filter(image, null);
	}
	
	private static ArrayList<String> getFilenamesDirectory(String directory, String extension) {
		ArrayList<String> als = new ArrayList<String>();
		File d = new File(directory);
		final String ext = extension;
		File[] entries = d.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(ext);
			}			
		});
		if (entries == null)
			return als;
		for (File f:entries) {
			als.add(f.getName());
		}
		Comparator<String> comparator = new Comparator<String>() {
			public int compare(String s1, String s2) {
				return s1.compareTo(s2);
			}
		};
		Collections.sort(als, comparator);
		return als;
	}
	
	/**
	 * Directory Listing of png Files
	 * @return
	 */
	public List<String> getFileNames() {
		return getFilenamesDirectory(imagePath, ".png");
	}
	
	/**
	 * The number of IMG-Files with ending .png in the directory
	 * @return int number of files
	 */
	public int getSize() {
		return getFileNames().size();
	}
	
	/**
	 * Given the filename of a PNG-File loads and returns the image
	 * @param imageName String filename without path
	 * @return BufferedImage of read file
	 */
	public BufferedImage getImage(String imageName) {
		try {
			File file = new File(imagePath + imageName);
			BufferedImage image = ImageIO.read(file);
			return image;
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * Returns a mapping of the filenames to the file size.
	 * @return Map<String, Integer> imagename to fileSize in kByte
	 */
	public Map<String, Integer> getImageSizes() {
		Map<String, Integer> ret = new TreeMap<String, Integer>();
		for (String imageName: getFileNames()) {
			File file = new File(imagePath + imageName);
			ret.put(imageName, (int) (file.length()/1024));
		}
		return ret;
	}
		
}
