package recruitSystem;

public class UserCreated extends AbstractEvent {

    private Long id;
    private String userName;

    public UserCreated(){
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
