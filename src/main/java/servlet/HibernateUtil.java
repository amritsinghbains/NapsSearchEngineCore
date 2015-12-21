package servlet;

import org.hibernate.SessionFactory; 
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
 
public class HibernateUtil {
 
    private static final SessionFactory sessionFactory = buildSessionFactory();
 
    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml file:///home/amrit/Documents/playground/RESTfulExample/src/main/resources
            return new AnnotationConfiguration().configure()
                    .buildSessionFactory();
//            Configuration configuration = new Configuration().configure( "/home/amrit/Documents/playground/RESTfulExample/src/main/resources/hibernate.cfg.xml");
//            return new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
 
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
