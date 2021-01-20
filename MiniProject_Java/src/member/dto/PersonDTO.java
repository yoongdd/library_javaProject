package member.dto;

import java.util.Calendar;

public class PersonDTO {
	
	 	private String id;
	 	
	 	private String password;
	 	private String name;
	 	private String birth;
	 	private String email;
	 	private int age;
	 	private int sex;
	 	private int state;
	    private int seatNum = -1; // 좌석번호 아무것도 선택 되어있지 않으면 -1이다. 
	 	// 일반 회원 - 정상 0 , 연체자 1
	    // 관리자 - 일반관리자0 , 채팅관리자 1
	    
	    private String[] borrow = new String[3]; //빌린책 리스트	    
	    private Calendar cal = Calendar.getInstance();
	    
	    
	    PersonDTO(){
	    	
	    }
	    public PersonDTO(String id, String password, String name, String birth, String email, int sex) {
	        this.id = id;
	        this.email = email;
	        this.birth = birth;
	        this.password = password;
	        this.name = name;
	        this.sex = sex;
	    }
	    //---------------------
	    private String address1;
	    private String address2;
	    private String phoneNum;
	    public String getAddress1() {
			return address1;
		}
		public void setAddress1(String address1) {
			this.address1 = address1;
		}
		public String getAddress2() {
			return address2;
		}
		public void setAddress2(String address2) {
			this.address2 = address2;
		}
		public String getPhoneNum() {
			return phoneNum;
		}
		public void setPhoneNum(String phoneNum) {
			this.phoneNum = phoneNum;
		}
		//-------------------------------
	    public String[] getBorrow() {
	        return borrow;
	    }
	    public void setBorrow(String[] borrow) {
	        this.borrow = borrow;
	    }
	   
	    
	    public void calcAge() {
	    	birth = this.birth.substring(0, 3);
	    	int year = cal.get(Calendar.YEAR);
	    	this.age = year-Integer.parseInt(birth);
	    }
		public String getId() {
			return id;
		}
		public void setSeatNum(int seatNum) {
			this.seatNum = seatNum;
		}
		public int getSeatNum() {
			return seatNum;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getBirth() {
			return birth;
		}
		public void setBirth(String birth) {
			this.birth = birth;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
		public int getSex() {
			return sex;
		}
		public void setSex(int sex) {
			this.sex = sex;
		}
		public int getState() {
			return state;
		}
		public void setState(int state) {
			this.state = state;
		}
		public Calendar getCal() {
			return cal;
		}
		public void setCal(Calendar cal) {
			this.cal = cal;
		}

}
