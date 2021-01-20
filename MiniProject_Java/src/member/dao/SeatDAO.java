package member.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import member.dto.SeatDTO;

public class SeatDAO {

	String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@192.168.0.61:1521:xe";
	private String user = "c##member";
	private String passwordDB = "1234";

	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	public SeatDAO() {
		try {

			Class.forName(driver);
			System.out.println("드라이버 로딩 성공");

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void getConnection() {
		try {
			conn = DriverManager.getConnection(url, user, passwordDB);
			System.out.println("접속성공");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public int seatWrite(SeatDTO seatDTO) {
		int su = 0;
		this.getConnection();
		String sql = "insert into seat values(?,?,?)";
		System.out.println("SEATWrite");

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, seatDTO.getSeat_num());
			pstmt.setInt(2, seatDTO.getSeat_check());
			pstmt.setString(3, seatDTO.getId());

			su = pstmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			try {

				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return su;

	}

	public int seatDelete(SeatDTO seatDTO) {
		int su = 0;
		this.getConnection();
		String sql = "delete seat where member_id =?";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, seatDTO.getId());

			su = pstmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			try {

				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return su;
	}

	public List<SeatDTO> getSeatList() {

		List<SeatDTO> list = new ArrayList<SeatDTO>();
		getConnection();
		String sql = "select * from seat";

		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				SeatDTO seatDTO = new SeatDTO();
				seatDTO.setSeat_num(rs.getInt("seat_num"));
				seatDTO.setSeat_check(rs.getInt("seat_check"));
				seatDTO.setId(rs.getString("member_id"));

				list.add(seatDTO);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			list = null;
		} finally {

			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		return list;
	}

}
