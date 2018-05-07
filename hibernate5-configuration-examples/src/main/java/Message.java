import javax.persistence.*;

@Entity
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(nullable = false)
    public String text;

    public Message(String text) {
        this.text = text;
    }

    public Message() {
    }

    public void setId(long id) { this.id = id; }
    public long getId() { return id; }

    public void setText(String text) { this.text = text; }
    public String getText() { return text; }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + getId() +
                ", text='" + getText() + '\'' +
                '}';
    }
}
