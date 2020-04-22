package recruitSystem;

public class RecruitCreated extends AbstractEvent {

    private Long id;
    private String recruitName;

    public RecruitCreated(){
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
