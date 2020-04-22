package recruitSystem;

public class RecruitUpdated extends AbstractEvent {

    private Long id;
    private String recruitName;

    public RecruitUpdated(){
        super();
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
