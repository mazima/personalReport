package recruitSystem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RestController
 public class AppController {
  @Autowired
  AppRepository appRepository;

  @PostMapping("/apps/appCreation")
  App appInsert(@RequestBody AppCreated appCreated) {
   App app = new App();
   app.setRecruitId(appCreated.getRecruitId());
   app.setRecruitName(appCreated.getRecruitName());
   app.setSchoolName(appCreated.getSchoolName());
   app.setUserId(appCreated.getUserId());
   app.setUserName(appCreated.getUserName());

   return appRepository.save(app);
  }

  @RequestMapping(method= RequestMethod.PATCH, path="/apps/appModify")
  public void appModify(@RequestBody AppUpdted appUpdted){
   App app = new App();
   app.setUserName(appUpdted.getUserName());
   app.setUserId(appUpdted.getUserId());
   app.setSchoolName(appUpdted.getSchoolName());
   app.setRecruitName(appUpdted.getRecruitName());
   app.setRecruitId(appUpdted.getRecruitId());
   app.setId(appUpdted.getId());
   appRepository.save(app);

  }

  @RequestMapping(method= RequestMethod.PATCH, path="/apps/appNameModify")
  public void appNameModify(@RequestBody App app){
   App tmp = new App();
   List<App> list = appRepository.findByUserId(app.getUserId());
   //List<RecruitProcessView> recruitProcessViewList = recruitProcessViewRepository.findByAppId(appUpdted.getId());
   for(App data : list){
    // view 객체에 이벤트의 eventDirectValue 를 set 함
    tmp.setId(data.getId());
    tmp.setRecruitId(data.getRecruitId());
    tmp.setRecruitName(data.getRecruitName());
    tmp.setSchoolName(data.getSchoolName());
    tmp.setUserName(app.getUserName());
    appRepository.save(tmp);
   }
  }




 }
