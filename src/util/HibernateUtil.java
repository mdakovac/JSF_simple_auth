package util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import entities.User;

public abstract class HibernateUtil {
	private static SessionFactory sessionFactory = null;

	public static Session getSession() {
		if (sessionFactory == null) {
			System.out.println("Creating a  new session factory.");
			sessionFactory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(User.class)
					.buildSessionFactory();
		}
		Session session = sessionFactory.openSession();
		return session;
	}
}
