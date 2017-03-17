import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.Assert;

import java.util.List;

public class PersistenceTest {
    SessionFactory factory;

    /* Hibernate 5
    @BeforeSuite
    public void setup() {
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        factory = new MetadataSources(registry)
                .buildMetadata()
                .buildSessionFactory();
    }
    */

    /* original Hibernate 4 example
    @BeforeClass
    public void setup() {
        Configuration configuration = new Configuration();
        configuration.configure();
        ServiceRegistryBuilder srBuilder = new ServiceRegistryBuilder();
        srBuilder.applySettings();
        ServiceRegistry serviceRegistry = srBuilder.buildServiceRegistry();
        factory = configuration.buildSessionFactory(serviceRegistry);

    }
    */

    @BeforeClass
    public void setup() {
        Configuration configuration = new Configuration();
        configuration.configure();
        StandardServiceRegistryBuilder ssrb = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
        factory = configuration.buildSessionFactory(ssrb.build());
    }

    /* Hibernate 5
    @Test
    public void saveMessage() {
        Message message = new Message("Hello, world");
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.persist(message);
            tx.commit();
        }
    }
    */

    @Test
    public void saveMessage() {
        Message message = new Message("Hello, world");
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        session.persist(message);
        tx.commit();
        session.close();
    }

    /* Hibernate 5
    @Test(dependsOnMethods = "saveMessage")
    public void readMessage() {
        try (Session session = factory.openSession()) {
            List<Message> list = session.createQuery("from Message",
                    Message.class).list();
            assertEquals(list.size(), 1);
            for (Message m : list) {
                System.out.println(m);
            }
        }
    }
    */

    @Test(dependsOnMethods = "saveMessage")
    public void readMessage() {
        Session session = factory.openSession();
        @SuppressWarnings("unchecked")
        List<Message> list = (List<Message>) session.createQuery("from Message").list();
        if (list.size() > 1) {
            Assert.fail("Message configuration in error; table should contain only one."
                    +" Set ddl to create-drop.");
        }
        if (list.size() == 0) {
            Assert.fail("Read of initial message failed; check saveMessage() for errors."
                    +" How did this test run?");
        }
        for (Message m : list) {
            System.out.println(m);
        }
        session.close();
    }
}