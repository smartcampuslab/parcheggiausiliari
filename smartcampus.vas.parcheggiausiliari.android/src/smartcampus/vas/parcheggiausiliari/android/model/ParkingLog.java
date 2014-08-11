package smartcampus.vas.parcheggiausiliari.android.model;

import java.util.Date;


public class ParkingLog{
	private String mAuthor;
	private Date mTime;
	public ParkingLog(String mAuthor, Date mTime) {
		super();
		this.mAuthor = mAuthor;
		this.mTime = mTime;
	}
	public String getmAuthor() {
		return mAuthor;
	}
	public Date getmTime() {
		return mTime;
	}
	
}