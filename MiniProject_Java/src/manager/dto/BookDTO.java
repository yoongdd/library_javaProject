package manager.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookDTO {

	private int code;
	private String title;
	private String author;
	private String publisher;
	private String genre;
	private int lendCheck;
	private Date lendDate;
	private Date returnDate;

	static List<BookDTO> list = new ArrayList<BookDTO>();
	String StringLend;

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

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public void setGenre(Object objectGerne) {
		this.genre = (String) objectGerne;
	}

	public int getLendCheck() {
		return lendCheck;
	}

	public String getStringLendCheck() {
		String lendString = null;

		if (lendCheck == 1)
			lendString = "대여 불가";
		else if (lendCheck == 0)
			lendString = "대여 가능";

		return lendString;
	}

	public void setLendCheck(int lendCheck) {
		this.lendCheck = lendCheck;
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

	public List<BookDTO> getList() {
		return list;
	}

	public static void setList(List<BookDTO> list) {
		BookDTO.list = list;
	}

	public String getStringLend() {
		return StringLend;
	}

	public void setStringLend(String stringLend) {
		StringLend = stringLend;
	}
}
