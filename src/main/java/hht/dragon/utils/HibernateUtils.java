package hht.dragon.utils;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.internal.SessionFactoryImpl;

/**
 * Description.
 *
 * @author: huang
 * Date: 17-11-5
 */
public class HibernateUtils {

    private static SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    private HibernateUtils() {}

    public static SessionFactory get() {
        return sessionFactory;
    }
}
