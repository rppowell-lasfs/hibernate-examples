import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.AssertJUnit.assertEquals;

public class SQLiteHibernateXmlCfg {

    /*
    // https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc
    compile group: 'org.xerial', name: 'sqlite-jdbc', version: '3.21.0.1'
    // https://mvnrepository.com/artifact/com.zsoltfabok/sqlite-dialect
    compile group: 'com.zsoltfabok', name: 'sqlite-dialect', version: '1.0'
    */

    /*
    <?xml version="1.0"?>
    <!DOCTYPE hibernate-configuration PUBLIC
        "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
        "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
    <hibernate-configuration>
        <session-factory>
            <!--  Database connection settings  -->
            <property name="connection.driver_class">org.sqlite.JDBC</property>
            <property name="connection.url">jdbc:sqlite:testdb.sqlite</property>
            <property name="connection.username"></property>
            <property name="connection.password"></property>
            <property name="dialect">org.hibernate.dialect.SQLiteDialect</property>
            <!--  Echo all executed SQL to stdout  -->
            <property name="show_sql">true</property>
            <!--  Drop and re-create the database schema on startup  -->
            <property name="hbm2ddl.auto">create-drop</property>
            <mapping class="Message"/>
        </session-factory>
    </hibernate-configuration>
    */

    private SessionFactory factory;

    @BeforeClass
    public void setup() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("sqlite.hibernate.cfg.xml")
                .build();
        factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
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
