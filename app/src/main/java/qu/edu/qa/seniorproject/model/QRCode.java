package qu.edu.qa.seniorproject.model;

import java.util.Random;

public class QRCode {
    private int code=52719763;
    private String service;// leave send
    private User destination;

    public QRCode(String service, User destination) {
        Random random = new Random();
        code = random.nextInt(99999)+11111;
        this.service = service;
        this.destination = destination;
    }

    public QRCode(){
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public User getDestination() {
        return destination;
    }

    public void setDestination(User destination) {
        this.destination = destination;
    }
}
