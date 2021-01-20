package manager.dto;

import java.util.Date;

public class LendDTO {

	private String id;
	private String name;
	private String email;
	private int code;
	private String title;
	private Date lendDate;
	private Date returnDate;
	private int lendCheck;
	private int state;

	// getter, setter
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getLendDate() {
		return lendDate;
	}

	public void setLendDate(Date lendDate) {
		this.lendDate = lendDate;
	}

	public Date getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Date returnDate) {
		this.returnDate = returnDate;
	}

	public int getLendCheck() {
		return lendCheck;
	}

	public void setLendCheck(int lendCheck) {
		this.lendCheck = lendCheck;
	}

	public int getState() { 
		Date today = new Date();
		if (this.returnDate != null) {
			if (today.after(this.returnDate)) {
				this.state = 1;
			}
		}
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}