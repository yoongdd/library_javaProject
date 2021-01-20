package manager.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import manager.dto.MemberDTO;

public class MemberDAO {


	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@192.168.0.61:1521:xe";
	String username = "c##member";
	String password = "1234";
	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs;

	public MemberDAO() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	public void getConnection() {
		try {
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// -----------------삭제----------------------------
	public int deleteMember(MemberDTO memberDTO) {
		int su = 0;
		int seq = memberDTO.getSeq();
		getConnection();
		String sql = "delete MEMBER_TABLE where NO_SEQ=?";

		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, seq);
			su = pstmt.executeUpdate();
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
		return su;
	}

	// ---------------수정------------------
	public int updateMember(MemberDTO memberDTO) {
		int su = 0;
		getConnection();
		String sql = "update MEMBER_TABLE set"
		+ " id=?,"
		+ " name=?,"
		+ " birth=? ,"
		+ " email=? ,"
		+ " sex=?,"
		+ " state=? where seq=?";
		
		
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberDTO.getId());
			pstmt.setString(2, memberDTO.getName());
			pstmt.setString(3, memberDTO.getBirth());
			pstmt.setString(4, memberDTO.getEmail());
			pstmt.setInt(5, memberDTO.getSex());
			pstmt.setInt(6, memberDTO.getState());
			pstmt.setInt(7, memberDTO.getSeq());
			su = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (pstmt != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		return su;
	}

	// -------------------불러오기---------------
	public List<MemberDTO> getMemberList() {
		List<MemberDTO> list = new ArrayList<MemberDTO>();
		getConnection();
		String sql = "select*from MEMBER_TABLE";
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				MemberDTO memberDTO = new MemberDTO();
				memberDTO.setSeq(rs.getInt("NO_SEQ"));// seq
				memberDTO.setId(rs.getString("id"));// id
				memberDTO.setPassword(rs.getString("password"));
				memberDTO.setName(rs.getString("name"));
				memberDTO.setBirth(rs.getString("birth"));
				memberDTO.setEmail(rs.getString("email"));// email
				memberDTO.setSex(rs.getInt("sex"));// sex
				memberDTO.setStatus(rs.getInt("status"));// state 관리자
				memberDTO.setState(rs.getInt("state"));// status 연체
				list.add(memberDTO);
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

}
