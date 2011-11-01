package blog;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommentsPerDay {
	Date day;
	private String dayLink;
	int numComments;

	public CommentsPerDay(Date date) {
		day = date;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		setDayLink(df.format(day));
		numComments = 1;
	}

	public void setNumComments(int numComments) {
		this.numComments = numComments;
	}

	public int getNumComments() {
		return numComments;
	}

	public void inc() {
		numComments++;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	public Date getDay() {
		return day;
	}

	public void setDayLink(String dayLink) {
		this.dayLink = dayLink;
	}

	public String getDayLink() {
		return dayLink;
	}
}
