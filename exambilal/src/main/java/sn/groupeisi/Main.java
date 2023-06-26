package sn.groupeisi;

import org.hibernate.SessionFactory;
import sn.groupeisi.config.HibernateUtil;

public class Main {
    public static void main(String[] args) {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

        // Fermeture de la session factory
        sessionFactory.close();
    }
}
