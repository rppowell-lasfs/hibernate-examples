import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Properties;

import static org.testng.AssertJUnit.assertEquals;

public class SQLiteHibernateProgrammaticCfg {

    /*
    // https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc
    compile group: 'org.xerial', name: 'sqlite-jdbc', version: '3.21.0.1'
    // https://mvnrepository.com/artifact/com.zsoltfabok/sqlite-dialect
    compile group: 'com.zsoltfabok', name: 'sqlite-dialect', version: '1.0'
    */

    private SessionFactory factory;

    @BeforeClass
    public void setup() {
        Properties prop= new Properties();
        prop.setProperty("hibernate.connection.driver_class", "org.sqlite.JDBC");
        prop.setProperty("hibernate.connection.url", "jdbc:sqlite:testdb.sqlite");
        prop.setProperty("hibernate.connection.username", "");
        prop.setProperty("hibernate.connection.password", "");
        prop.setProperty("hibernate.SQLiteDialect", "org.hibernate.SQLiteDialect.H2Dialect");
        prop.setProperty("hibernate.show_sql", "true");
        prop.setProperty("hibernate.hbm2ddl.auto", "create-drop");

        factory = new Configuration()
                .addProperties(prop)
                .addAnnotatedClass(Message.class)
                .buildSessionFactory();
    }

    @Test
    public void saveMessage() {
        Message message = new Message("Hello, World!");
        try(Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(message);
            tx.commit();
        }
    }

    @Test(dependsOnMethods = "saveMessage")
    public void readMessage() {
        try(Session session = factory.openSession()) {
            List<Message> list = session.createQuery("from Message", Message.class).list();

            assertEquals(list.size(), 1);
            for(Message m : list) {
                System.out.println(m);
            }
        }
    }
}
