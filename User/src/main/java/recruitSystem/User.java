package recruitSystem;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import recruitSystem.external.App;
import recruitSystem.external.AppService;

import java.util.List;

@Entity
@Table(name="User_table")
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String userName;

    @PostPersist
    public void onPostPersist(){
        UserCreated userCreated = new UserCreated();
        BeanUtils.copyProperties(this, userCreated);
        userCreated.publish();


    }

    @PostUpdate
    public void onPostUpdate(){
        UserUpdated userUpdated = new UserUpdated();
        BeanUtils.copyProperties(this, userUpdated);
        userUpdated.publish();



        App app = new App();
        System.out.println("mazima !!!!!!!!!!!! : "+this.getId());
        app.setUserName(this.getUserName());
        app.setUserId(this.getId());

        // mappings goes here
        Application.applicationContext.getBean(AppService.class).appNameModify(app);


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
