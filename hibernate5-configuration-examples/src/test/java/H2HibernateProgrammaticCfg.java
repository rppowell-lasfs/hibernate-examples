import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Properties;

import static org.testng.AssertJUnit.assertEquals;

public class H2HibernateProgrammaticCfg {

    /*
    // https://mvnrepository.com/artifact/org.hibernate/hibernate-core
    compile group: 'org.hibernate', name: 'hibernate-core', version: '5.2.17.Final'

    // https://mvnrepository.com/artifact/com.h2database/h2
    testCompile group: 'com.h2database', name: 'h2', version: '1.4.197'
    */

    private SessionFactory factory;

    @BeforeClass
    public void setup() {
        Properties prop= new Properties();
        prop.setProperty("hibernate.connection.driver_class", "org.h2.Driver");
        prop.setProperty("hibernate.connection.url", "jdbc:h2:./testdb.h2");
        prop.setProperty("hibernate.connection.username", "sa");
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
