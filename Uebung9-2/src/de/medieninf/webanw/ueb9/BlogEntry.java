package de.medieninf.webanw.ueb9;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity(name="blog")
public class BlogEntry implements Serializable {
	private static final long serialVersionUID = -447278896155632237L;

	protected long blogId;
	protected int version;
	protected String title;
	protected String content;
	protected Date date;
	protected BlogImage image;
	protected List<BlogComment> comments;
	
	public BlogEntry() {
		this.title = "";
		this.content = "";
		this.date = new java.util.Date();
		this.image = null;
		comments = new ArrayList<BlogComment>();
	}

    @Id
    @SequenceGenerator(name="BlogEntryIdGen", sequenceName="blog_id_seq",
            allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BlogEntryIdGen")
    @Column(name="id")
	public long getBlogId() {
		return blogId;
	}
	public void setBlogId(long blogId) {
		this.blogId = blogId;
	}

    @Version
    @Column(name="version")
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}

    @Column(name="title", nullable=false)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

    @Column(name="content", nullable=false)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
    @Column(name="date", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	@OneToOne(mappedBy="entry")
	public BlogImage getImage() {
		return image;
	}
	public void setImage(BlogImage image) {
		this.image = image;
		if (image != null && image.getEntry() != this) {
			image.setEntry(this);
		}
	}

	@OneToMany(mappedBy="entry")
	public List<BlogComment> getComments() {
		return comments;
	}
	public void setComments(List<BlogComment> comments) {
		this.comments = comments;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (blogId ^ (blogId >>> 32));
		result = prime * result
				+ ((comments == null) ? 0 : comments.hashCode());
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
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
		BlogEntry other = (BlogEntry) obj;
		if (blogId != other.blogId)
			return false;
		if (comments == null) {
			if (other.comments != null)
				return false;
		} else if (!comments.equals(other.comments))
			return false;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (version != other.version)
			return false;
		return true;
	};
	
}
