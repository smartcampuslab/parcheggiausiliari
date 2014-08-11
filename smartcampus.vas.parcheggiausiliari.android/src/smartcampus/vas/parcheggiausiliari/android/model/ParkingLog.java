package smartcampus.vas.parcheggiausiliari.android.model;

import java.util.Date;

public class ParkingLog{
	private String author;
	private Date time;
	public ParkingLog(String mAuthor, Date mTime) {
		super();
		this.author = mAuthor;
		this.time = mTime;
	}
	public String getAuthor() {
		return author;
	}
	public Date getTime() {
		return time;
	}
	
}