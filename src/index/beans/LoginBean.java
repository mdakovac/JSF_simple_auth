package index.beans;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.hibernate.Session;
import org.hibernate.query.Query;

import authentication.BCrypt;
import entities.User;
import util.HibernateUtil;
import util.Message;

@ManagedBean(name = "login")
public class LoginBean {

	private String username;
	private String password;

	public LoginBean() {
		// System.out.println("LoginBean instantiated");
	}

	@Override
	public void finalize() {
		// System.out.println("LoginBean destroyed");
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String login() {
		FacesContext context = FacesContext.getCurrentInstance();

		Session session = HibernateUtil.getSession();

		session.beginTransaction();
		Query<User> query = session.createQuery("from User where username=?0", User.class);
		query.setParameter(0, this.username);

		List<User> u = query.getResultList();

		if (u.size() == 0) {
			Message.Display("Authentication Failed. Invalid credentials");
			return "index";
		}

		if (BCrypt.checkpw(this.password.trim(), u.get(0).getPassword().trim())) {
			context.getExternalContext().getSessionMap().put("user", username);
			return "home?faces-redirect=true";
		} else {
			Message.Display("Authentication Failed. Invalid credentials");
		}
		return "index";
	}

	public String logout() {
		System.out.println("logging out");
		FacesContext context = FacesContext.getCurrentInstance();
		context.getExternalContext().invalidateSession();

		return "index?faces-redirect=true";
	}
	/*
	 * public String login() { Session session = HibernateUtil.getSession();
	 * 
	 * session.beginTransaction(); // session.save(u);
	 * session.getTransaction().commit();
	 * 
	 * session.close();
	 * 
	 * return "index"; }
	 * 
	 * public void logout() {
	 * 
	 * }
	 */
}
