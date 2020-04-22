package recruitSystem;

import recruitSystem.config.kafka.KafkaProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class RecruitProcessViewViewHandler {


    @Autowired
    private RecruitProcessViewRepository recruitProcessViewRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void whenAppCreated_then_CREATE_1 (@Payload AppCreated appCreated) {
        try {
            if (appCreated.isMe()) {
                // view 객체 생성
                RecruitProcessView recruitProcessView = new RecruitProcessView();
                // view 객체에 이벤트의 Value 를 set 함
                recruitProcessView.setUserName(appCreated.getUserName());
                recruitProcessView.setUserId(appCreated.getUserId());
                recruitProcessView.setRecruitId(appCreated.getRecruitId());
                recruitProcessView.setAppId(appCreated.getId());
                recruitProcessView.setRecruitName(appCreated.getRecruitName());
                recruitProcessView.setSchoolName(appCreated.getSchoolName());
                // view 레파지 토리에 save
                recruitProcessViewRepository.save(recruitProcessView);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @StreamListener(KafkaProcessor.INPUT)
    public void whenRecruitUpdated_then_UPDATE_1(@Payload RecruitUpdated recruitUpdated) {
        try {
            if (recruitUpdated.isMe()) {
                // view 객체 조회
                List<RecruitProcessView> recruitProcessViewList = recruitProcessViewRepository.findByRecruitId(recruitUpdated.getId());
                for(RecruitProcessView recruitProcessView : recruitProcessViewList){
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    recruitProcessView.setRecruitName(recruitUpdated.getRecruitName());
                    // view 레파지 토리에 save
                    recruitProcessViewRepository.save(recruitProcessView);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenAppUpdted_then_UPDATE_2(@Payload AppUpdted appUpdted) {
        try {
            if (appUpdted.isMe()) {
                // view 객체 조회
                List<RecruitProcessView> recruitProcessViewList = recruitProcessViewRepository.findByAppId(appUpdted.getId());
                for(RecruitProcessView recruitProcessView : recruitProcessViewList){
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    recruitProcessView.setUserName(appUpdted.getUserName());
                    recruitProcessView.setSchoolName(appUpdted.getSchoolName());
                    recruitProcessView.setRecruitName(appUpdted.getRecruitName());
                    // view 레파지 토리에 save
                    recruitProcessViewRepository.save(recruitProcessView);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenRecruitProcessCreated_then_UPDATE_3(@Payload RecruitProcessCreated recruitProcessCreated) {
        try {
            if (recruitProcessCreated.isMe()) {
                // view 객체 조회
                List<RecruitProcessView> recruitProcessViewList = recruitProcessViewRepository.findByAppId(recruitProcessCreated.getAppId());
                for(RecruitProcessView recruitProcessView : recruitProcessViewList){
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    recruitProcessView.setProcessResult(recruitProcessCreated.getProcessResult());
                    // view 레파지 토리에 save
                    recruitProcessViewRepository.save(recruitProcessView);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void whenUserUpdated_then_UPDATE_4(@Payload UserUpdated userUpdated) {
        try {
            if (userUpdated.isMe()) {
                // view 객체 조회
                List<RecruitProcessView> recruitProcessViewList = recruitProcessViewRepository.findByUserId(userUpdated.getId());
                for(RecruitProcessView recruitProcessView : recruitProcessViewList){
                    // view 객체에 이벤트의 eventDirectValue 를 set 함
                    recruitProcessView.setUserName(userUpdated.getUserName());
                    // view 레파지 토리에 save
                    recruitProcessViewRepository.save(recruitProcessView);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}