package recruitSystem;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecruitProcessViewRepository extends CrudRepository<RecruitProcessView, Long> {

    List<RecruitProcessView> findByRecruitId(Long recruitId);
    List<RecruitProcessView> findByAppId(Long appId);
    List<RecruitProcessView> findByUserId(Long userId);

}