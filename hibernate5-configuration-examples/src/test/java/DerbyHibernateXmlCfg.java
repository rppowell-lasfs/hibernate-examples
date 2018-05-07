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

public class DerbyHibernateXmlCfg {

    /*
    // https://mvnrepository.com/artifact/org.apache.derby/derby
    testCompile group: 'org.apache.derby', name: 'derby', version: '10.14.2.0'
    */

    // derby.hibernate.cfg.zml
    /*
    <?xml version="1.0" encoding="UTF-8"?>
    <!DOCTYPE hibernate-configuration PUBLIC
        "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
        "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
    <hibernate-configuration>
        <session-factory>
            <!--  Database connection settings  -->
            <property name="connection.driver_class">org.apache.derby.jdbc.EmbeddedDriver</property>
            <!-- <property name="connection.url">jdbc:derby:memory:db;create=true</property> -->
            <property name="connection.url">jdbc:derby:testdb.derby;create=true</property>
            <property name="connection.username"/>
            <property name="connection.password"/>
            <property name="dialect">org.hibernate.dialect.DerbyTenSevenDialect</property>
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
                .configure("derby.hibernate.cfg.xml")
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
