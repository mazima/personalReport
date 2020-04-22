package recruitSystem;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="App_table")
public class App {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private Long userId;
    private Long recruitId;
    private String userName;
    private String schoolName;
    private String recruitName;

    @PostPersist
    public void onPostPersist(){
        AppCreated appCreated = new AppCreated();
        BeanUtils.copyProperties(this, appCreated);
        appCreated.publish();


    }

    @PostUpdate
    public void onPostUpdate(){
        AppUpdted appUpdted = new AppUpdted();
        BeanUtils.copyProperties(this, appUpdted);
        appUpdted.publish();


    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getRecruitId() {
        return recruitId;
    }

    public void setRecruitId(Long recruitId) {
        this.recruitId = recruitId;
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }
    public String getRecruitName() {
        return recruitName;
    }

    public void setRecruitName(String recruitName) {
        this.recruitName = recruitName;
    }




}
