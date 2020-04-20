package qu.edu.qa.seniorproject.model;

import java.io.Serializable;
import java.util.Date;

public class Massage implements Serializable {
    private String topic;
    private String text;
    private String sender;
    private String resiver;
    private Date date;

    public Massage(String topic, String text, String sender, String resiver) {
        this.topic = topic;
        this.text = text;
        this.sender = sender;
        this.resiver = resiver;
        this.date = new Date(System.currentTimeMillis());
    }

    public Massage() {
        this.date = new Date(System.currentTimeMillis());

    }

    public String getTopic() {
        return topic;
    }

    @Override
    public String toString() {
        return "Massage{" +
                "topic='" + topic + '\'' +
                ", text='" + text + '\'' +
                ", sender='" + sender + '\'' +
                ", resiver='" + resiver + '\'' +
                '}';
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getResiver() {
        return resiver;
    }

    public void setResiver(String resiver) {
        this.resiver = resiver;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
