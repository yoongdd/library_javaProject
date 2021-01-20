package manager.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import manager.dto.BookDTO;
import manager.dto.LendDTO;
import manager.dto.LendJoinDTO;

public class LendDAO {

	// XML
	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@192.168.0.61:1521:xe";
	private String user = "c##member";
	private String password = "1234";
	private String sql;
	private int code;
	private String title;
	private String author;
	private String publisher;
	private String genre;// getter setter도 전부 추가

	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	public LendDAO() {

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void getConnection() {
		try {
			conn = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<LendDTO> getLendList() {

		List<LendDTO> list = new ArrayList<LendDTO>();

		getConnection();
		sql = "select * from lend_table";

		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				// 한줄을 불러와 friendDTO를 생성시켜 list에 추가
				LendDTO lendDTO = new LendDTO();
				lendDTO.setId(rs.getString("id"));
				lendDTO.setName(rs.getString("name"));
				lendDTO.setEmail(rs.getString("email"));
				lendDTO.setCode(rs.getInt("code"));
				lendDTO.setTitle(rs.getString("title"));
				lendDTO.setLendDate(rs.getDate("lendDate"));
				lendDTO.setReturnDate(rs.getDate("returnDate"));
				lendDTO.setLendCheck((rs.getInt("lendCheck")));
				lendDTO.setState((rs.getInt("state")));

				list.add(lendDTO);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			list = null; // 예외가 발생하면 list에 잘못된 data가 저장되므로 list를 null로 초기화 시켜준다
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	// 도서 대여
		public void lendInsert(LendDTO lendDTO) {

			getConnection();

			sql = "insert into lend_table values(seq_lend.nextval,?,?,?,?,?,sysdate,sysdate+14,?,?)";

			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, lendDTO.getId());
				pstmt.setString(2, lendDTO.getName());
				pstmt.setString(3, lendDTO.getEmail());
				pstmt.setInt(4, lendDTO.getCode());
				pstmt.setString(5, lendDTO.getTitle());
				pstmt.setInt(6, lendDTO.getLendCheck());
				pstmt.setInt(7, lendDTO.getState());

				pstmt.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (pstmt != null)
						pstmt.close();
					if (conn != null)
						conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		// 도서대여 정보 삭제
		public void lendDelete(LendDTO lendDTO) {

			getConnection();
			sql = "delete lend_table where code = ?";

			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, lendDTO.getCode());

				pstmt.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (pstmt != null)
						pstmt.close();
					if (conn != null)
						conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		// 도서 대여여부 변경
		public void lendCheckUpdate(BookDTO bookDTO) {

			getConnection();
			sql = "update book_table set lendCheck=1 where code = ?";

			try {
				pstmt = conn.prepareStatement(sql);

				pstmt.setInt(1, bookDTO.getCode());

				pstmt.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (pstmt != null)
						pstmt.close();
					if (conn != null)
						conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		// 도서 대여여부 변경
		public void returnCheckUpdate(BookDTO bookDTO) {

			getConnection();
			sql = "update book_table set lendCheck=0 where code = ?";

			try {
				pstmt = conn.prepareStatement(sql);

				pstmt.setInt(1, bookDTO.getCode());

				pstmt.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				try {
					if (pstmt != null)
						pstmt.close();
					if (conn != null)
						conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	public List<LendJoinDTO> getLendJoin(String id) {
		getConnection();
		List<LendJoinDTO> list = new ArrayList<LendJoinDTO>(); 
		sql = "select lend_table.code , lend_table.name, lend_table.title , book_table.author , book_table.publisher, book_table.genre"
				+ ",lend_table.returndate from book_table, lend_table where lend_table.code = book_table.code and lend_table.id= ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				LendJoinDTO lendJoinDTO = new LendJoinDTO();
				lendJoinDTO.setCode(Integer.parseInt(rs.getString("code")));
				lendJoinDTO.setName(rs.getString("name"));
				lendJoinDTO.setTitle(rs.getString("title"));
				lendJoinDTO.setAuthor(rs.getString("author"));
				lendJoinDTO.setPublisher(rs.getString("publisher"));
				lendJoinDTO.setGenre(rs.getString("genre"));
				lendJoinDTO.setReturnDate(rs.getDate("returndate")); //0519추가
				
				list.add(lendJoinDTO);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

}