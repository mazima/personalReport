package recruitSystem;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="RecruitProcessView_table")
public class RecruitProcessView {

        @Id
        @GeneratedValue(strategy=GenerationType.AUTO)
        private Long id;
        private Long userId;
        private Long recruitId;
        private Long appId;
        private String recruitName;
        private String userName;
        private String processResult;
        private String schoolName;


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
        public Long getAppId() {
            return appId;
        }

        public void setAppId(Long appId) {
            this.appId = appId;
        }
        public String getRecruitName() {
            return recruitName;
        }

        public void setRecruitName(String recruitName) {
            this.recruitName = recruitName;
        }
        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
        public String getProcessResult() {
            return processResult;
        }

        public void setProcessResult(String processResult) {
            this.processResult = processResult;
        }
        public String getSchoolName() {
            return schoolName;
        }

        public void setSchoolName(String schoolName) {
            this.schoolName = schoolName;
        }

}
