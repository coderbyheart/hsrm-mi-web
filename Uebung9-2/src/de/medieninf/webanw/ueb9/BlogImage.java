package de.medieninf.webanw.ueb9;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;
import javax.persistence.*;

@Entity(name="image")
public class BlogImage implements Serializable {
	private static final long serialVersionUID = 7854256640930795458L;
	
	protected long imageId;
	protected int version;
	protected BlogEntry entry;
	protected byte[] blob;
	protected String type;
	protected Integer size;
	
	public BlogImage() {
		entry = null;
		blob = null;
		type = "";
		size = 0;
	}
	
    @Id
    @SequenceGenerator(name="BlogImageIdGen", sequenceName="image_id_seq",
            allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BlogImageIdGen")
    @Column(name="id")
	public long getImageId() {
		return imageId;
	}
	public void setImageId(long imageId) {
		this.imageId = imageId;
	}
	
    @Version
    @Column(name="version")
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	
	@OneToOne
	@JoinColumn(name="blog_id")
	public BlogEntry getEntry() {
		return entry;
	}
	public void setEntry(BlogEntry entry) {
		this.entry = entry;
		if (entry != null && entry.getImage() != this) {
			entry.setImage(this);
		}
	}
	
	@Lob
	@Column(name="image")
	public byte[] getBlob() {
		return blob;
	}
	public void setBlob(byte[] blob) {
		this.blob = blob;
	}

    @Column(name="type", nullable=false)	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
    @Column(name="size", nullable=false)
	public Integer getSize() {
		return size;
	}
	public void setSize(Integer size) {
		this.size = size;
	}

	/**
	 * Use that one to work images - good luck with direct manipulation.
	 * @return Image to retrieve
	 */
	@Transient
	public BufferedImage getImage() {
		try {
			BufferedImage bim = ImageIO.read(new ByteArrayInputStream(blob));
			return bim;
		} catch (IOException e) {
			System.err.println("IOExeption on reading image");
			e.printStackTrace(System.err);
			return null;
		}
	}
	/**
	 * Use that one to work with images - good luck with direct manipulation.
	 * @param img Image to add
	 */
	public void setImage(BufferedImage img) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			type = "png"; // so far we fix everything to png and don't worry
			ImageIO.write(img, type, bos);
			bos.toByteArray();
			blob = bos.toByteArray();
			size = blob.length;
		} catch (IOException e) {
			System.err.println("IOExeption on writing image");
			e.printStackTrace(System.err);
		}
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (imageId ^ (imageId >>> 32));
		result = prime * result + ((size == null) ? 0 : size.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + version;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BlogImage other = (BlogImage) obj;
		if (imageId != other.imageId)
			return false;
		if (size == null) {
			if (other.size != null)
				return false;
		} else if (!size.equals(other.size))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (version != other.version)
			return false;
		return true;
	}
	
}
