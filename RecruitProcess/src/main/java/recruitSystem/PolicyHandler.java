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

    @Autowired
    RecruitProcessRepository recruitProcessRepository;
    
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverAppCreated_RecruitProcessStart(@Payload AppCreated appCreated){

        if(appCreated.isMe()){
            RecruitProcess recruitProcess = new RecruitProcess();
            recruitProcess.setAppId(appCreated.getId());
            recruitProcess.setUserId(appCreated.getUserId());
            recruitProcess.setRecruitId(appCreated.getRecruitId());
            recruitProcess.setProcessResult("접수완료");

            recruitProcessRepository.save(recruitProcess);
            System.out.println("##### listener RecruitProcessStart : " + appCreated.toJson());
        }
    }

}
