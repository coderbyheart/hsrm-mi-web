package de.medieninf.webanw.ueb9;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;


@Entity(name="comment")
public class BlogComment implements Serializable {
	private static final long serialVersionUID = 4023539446332479303L;
	
	protected long commentId;
	protected int version;
	protected String content;
	protected BlogEntry entry;
	protected Date date;

	public BlogComment() {
		this.content = "";
	}

    @Id
    @SequenceGenerator(name="BlogCommentIdGen", sequenceName="comment_id_seq",
            allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="BlogCommentIdGen")
    @Column(name="id")
	public long getCommentId() {
		return commentId;
	}
	public void setCommentId(long commentId) {
		this.commentId = commentId;
	}
	
    @Version
    @Column(name="version")
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}

    @Column(name="content", nullable=false)	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@ManyToOne
	@JoinColumn(name="blog_id")
	public BlogEntry getEntry() {
		return entry;
	}
	public void setEntry(BlogEntry entry) {
		this.entry = entry;
	}

    @Column(name="date", nullable=false)
    @Temporal(TemporalType.TIMESTAMP)	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (commentId ^ (commentId >>> 32));
		result = prime * result + ((content == null) ? 0 : content.hashCode());
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
		BlogComment other = (BlogComment) obj;
		if (commentId != other.commentId)
			return false;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (version != other.version)
			return false;
		return true;
	}
	
}
