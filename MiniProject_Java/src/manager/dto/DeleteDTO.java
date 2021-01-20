package manager.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DeleteDTO {
	
	private int code;
	private String title;
	private String author;
	private String publisher;
	private String genre; 
	static List<DeleteDTO> list = new ArrayList<DeleteDTO>();
	

	private Date deleteDate;
	
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
	public static List<DeleteDTO> getList() {
		return list;
	}
	public static void setList(List<DeleteDTO> list) {
		DeleteDTO.list = list;
	}
	
	public Date getDeleteDate() {
		return deleteDate;
	}
	public void setDeleteDate(Date deleteDate) {
		this.deleteDate = deleteDate;
	}
	
}
	