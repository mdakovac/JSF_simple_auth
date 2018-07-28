package index.beans;

import java.util.List;
import java.util.Set;

import javax.faces.bean.ManagedBean;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.Session;
import org.hibernate.query.Query;

import authentication.BCrypt;
import entities.User;
import util.HibernateUtil;
import util.Message;

@ManagedBean(name = "register")
public class RegisterBean {
	private String username = "";
	private String email = "";
	private String password1 = "";
	private String password2 = "";

	public RegisterBean() {
		//System.out.println("RegisterBean instantiated");
	}

	@Override
	public void finalize() {
		//System.out.println("RegisterBean destroyed");
	}

	// Getters and Setters
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword1() {
		return password1;
	}

	public void setPassword1(String password1) {
		this.password1 = password1;
	}

	public String getPassword2() {
		return password2;
	}

	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	public String validate() {

		// Check for validation violations
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();

		User u = new User(this.username, this.password1, this.email);

		Set<ConstraintViolation<User>> constraintViolations = validator.validate(u);

		if (!constraintViolations.isEmpty()) {
			for (ConstraintViolation<User> violation : constraintViolations) {
				String message = violation.getMessage();
				Message.Display(message);
			}
			return "index";
		}

		System.out.println(this.password1);
		System.out.println(this.password2);

		// Check if passwords match
		if (!this.password1.equals(this.password2)) {
			Message.Display("Passwords do not match!");
			return "index";
		}

		String hashedPassword = BCrypt.hashpw(password1, BCrypt.gensalt());
		u.setPassword(hashedPassword);

		Session session = HibernateUtil.getSession();

		Query<User> query = session.createQuery("from User where username=?0 or email=?1", User.class);
		query.setParameter(0, u.getUsername());
		query.setParameter(1, u.getEmail());
		List<User> userList = query.list();

		if (userList.size() > 0) {
			if (u.getUsername().equals(userList.get(0).getUsername())) {
				Message.Display("Username taken!");
			} else if (u.getEmail().equals(userList.get(0).getEmail())) {
				Message.Display("Email taken!");
			}
			
			session.close();
			return "index";
		}
		
		System.out.println("Inserting");
		session.beginTransaction();
		session.save(u);
		session.getTransaction().commit();
		session.close();
		System.out.println("Inserting done.");
		// log me in
		// redirect to success page

		return "index";

		/*
		 * CriteriaBuilder cb = session.getCriteriaBuilder(); CriteriaQuery<User> query
		 * = cb.createQuery(User.class); Root<User> employee = (Root<User>)
		 * query.from(User.class);
		 * query.select(employee).where(cb.equal(employee.get("email"), u.getEmail()));
		 * TypedQuery<User> typedQuery = session.createQuery(query);
		 * typedQuery.getResultList().forEach(System.out::println); session.close();
		 * return "index";
		 */
		/*
		 * 
		 * session.beginTransaction(); session.save(u);
		 * session.getTransaction().commit();
		 * 
		 * session.close();
		 * 
		 * System.out.println("Got here. Returning."); return "index";
		 */
	}
}
