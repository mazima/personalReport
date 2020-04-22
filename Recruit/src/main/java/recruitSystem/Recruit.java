package recruitSystem;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="Recruit_table")
public class Recruit {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String recruitName;

    @PostPersist
    public void onPostPersist(){
        RecruitCreated recruitCreated = new RecruitCreated();
        BeanUtils.copyProperties(this, recruitCreated);
        recruitCreated.publish();


    }

    @PostUpdate
    public void onPostUpdate(){
        RecruitUpdated recruitUpdated = new RecruitUpdated();
        BeanUtils.copyProperties(this, recruitUpdated);
        recruitUpdated.publish();


    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getRecruitName() {
        return recruitName;
    }

    public void setRecruitName(String recruitName) {
        this.recruitName = recruitName;
    }




}
