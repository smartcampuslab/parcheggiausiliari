package eu.trentorise.smartcampus.parcheggiausiliari.model;

import java.util.Map;


public class LogObject {
	
	private String objId;
	private String type;
	private String author;
	private Map<String, Object> value;
	private String valueString;
	private String userAgencyId;
	private Long[] logPeriod;
	private boolean deleted;
	private String year;
	private String month;
	private String week_day;
	private String timeSlot;		//time of operation (start a slot of one hour)
	private boolean isHolyday;		//true if is an holyday day (in ita = festivo)
	private boolean isSystemLog;	//true if inserted with the form in parkingManagementWeb app
	private long time;
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getObjId() {
		return objId;
	}
	public void setObjId(String objId) {
		this.objId = objId;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}

	public Map<String, Object> getValue() {
		return value;
	}
	public void setValue(Map<String, Object> value) {
		this.value = value;
	}
	public String getValueString() {
		return valueString;
	}
	public void setValueString(String valueString) {
		this.valueString = valueString;
	}
	public String getUserAgencyId() {
		return userAgencyId;
	}
	public void setUserAgencyId(String userAgencyId) {
		this.userAgencyId = userAgencyId;
	}
	public Long[] getLogPeriod() {
		return logPeriod;
	}
	public void setLogPeriod(Long[] logPeriod) {
		this.logPeriod = logPeriod;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getWeek_day() {
		return week_day;
	}
	public void setWeek_day(String week_day) {
		this.week_day = week_day;
	}
	public String getTimeSlot() {
		return timeSlot;
	}
	public void setTimeSlot(String timeSlot) {
		this.timeSlot = timeSlot;
	}
	public boolean isHolyday() {
		return isHolyday;
	}
	public void setHolyday(boolean isHolyday) {
		this.isHolyday = isHolyday;
	}
	public boolean isSystemLog() {
		return isSystemLog;
	}
	public void setSystemLog(boolean isSystemLog) {
		this.isSystemLog = isSystemLog;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}


	
}