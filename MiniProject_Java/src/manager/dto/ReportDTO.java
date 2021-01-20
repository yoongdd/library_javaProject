package manager.dto;

public class ReportDTO {
	private int mon_seq;
	private int allBook;
	private int allLend;
	private int lengOk;
	private int addCount;
	private int deleteCount;
	private int lendCount;
	private int overCount;
	
	public int getMon_seq() {
		return mon_seq;
	}
	public void setMon_seq(int mon_seq) {
		this.mon_seq = mon_seq;
	}
	
	public int getAllBook() {
		return allBook;
	}
	public void setAllBook(int allBook) {
		this.allBook = allBook;
	}
	public int getAllLend() {
		return allLend;
	}
	public void setAllLend(int allLend) {
		this.allLend = allLend;
	}
	public int getLengOk() {
		return lengOk;
	}
	public void setLengOk(int lengOk) {
		this.lengOk = lengOk;
	}
	public int getAddCount() {
		return addCount;
	}
	public void setAddCount(int addCount) {
		this.addCount = addCount;
	}
	public int getDeleteCount() {
		return deleteCount;
	}
	public void setDeleteCount(int deleteCount) {
		this.deleteCount = deleteCount;
	}
	public int getLendCount() {
		return lendCount;
	}
	public void setLendCount(int lendCount) {
		this.lendCount = lendCount;
	}
	public int getOverCount() {
		return overCount;
	}
	public void setOverCount(int overCount) {
		this.overCount = overCount;
	}
	
}
