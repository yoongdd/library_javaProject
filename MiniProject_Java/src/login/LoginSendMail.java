package login;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;

public class LoginSendMail {
	private String code;

	public String getCode() {
		return code;
	}

	public void findPWMail(String id, String email, String getPassword) {

		String user = "bookmanagerbit@gmail.com";
		String password = "";

		// SMTP 서버 정보를 설정한다.
		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", 465);
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.ssl.enable", "true");
		prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");

		Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});

		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(user));

			message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
			message.setSubject("비트도서관 비밀번호 찾기  안내");
			message.setText(id + "님의 비밀번호는 " + getPassword + "입니다 ");

			Transport.send(message); //// 전송
			System.out.println("비밀번호 찾기 인증번호 발송 완료!" + getPassword);

		} catch (AddressException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
