package recruitSystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

 @RestController
 public class RecruitProcessController {
  @Autowired
  RecruitProcessRepository recruitProcessRepository;

  @PostMapping("/recruitProcesses/recruitProcessCreation")
  RecruitProcess recruitProcessInsert(@RequestBody RecruitProcessCreated recruitProcessCreated) {
   RecruitProcess recruitProcess = new RecruitProcess();
   recruitProcess.setAppId(recruitProcessCreated.getAppId());
   recruitProcess.setUserId(recruitProcessCreated.getUserId());
   recruitProcess.setRecruitId(recruitProcessCreated.getRecruitId());
   recruitProcess.setProcessResult(recruitProcessCreated.getProcessResult());

   return recruitProcessRepository.save(recruitProcess);

  }


 }
