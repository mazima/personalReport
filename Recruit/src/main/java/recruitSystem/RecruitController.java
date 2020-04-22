package recruitSystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

 @RestController
 public class RecruitController {
  @Autowired
  RecruitRepository recruitRepository;

  @PostMapping("/recruits/recruitCreation")
  Recruit recruitInsert(@RequestBody RecruitCreated recruitCreated) {
   Recruit recruit = new Recruit();
   recruit.setRecruitName(recruitCreated.getRecruitName());

   return recruitRepository.save(recruit);
  }

  @RequestMapping(method= RequestMethod.PATCH, path="/recruits/recruitModify")
  public void recruitModify(@RequestBody RecruitUpdated recruitUpdated){
   Recruit recruit = new Recruit();
   recruit.setId(recruitUpdated.getId());
   recruit.setRecruitName(recruitUpdated.getRecruitName());
   recruitRepository.save(recruit);

  }
 }
