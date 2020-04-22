package recruitSystem;

import recruitSystem.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{
    
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverAppCreated_UserNotice(@Payload AppCreated appCreated){

        if(appCreated.isMe()){
            System.out.println("##### appCreated UserNotice : 지원등록 되었음 " + appCreated.toJson());
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverAppUpdted_UserNotice(@Payload AppUpdted appUpdted){

        if(appUpdted.isMe()){
            System.out.println("##### appUpdted UserNotice : 지원서 수정되었음 " + appUpdted.toJson());
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverRecruitProcessCreated_UserNotice(@Payload RecruitProcessCreated recruitProcessCreated){

        if(recruitProcessCreated.isMe()){
            System.out.println("##### recruitProcessCreated UserNotice : 전형 결과가 나왔습니다. " + recruitProcessCreated.toJson());
        }
    }

}
