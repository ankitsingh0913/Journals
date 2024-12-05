package com.xclone.learning.Scheduler;

import com.xclone.learning.Cache.AppCache;
import com.xclone.learning.Entity.JournalEntity;
import com.xclone.learning.Entity.User;
import com.xclone.learning.Repository.UserRepositoryIMPL;
import com.xclone.learning.Services.ExternalAPI.EmailService;
import com.xclone.learning.Services.SentimentAnalysisServices;
import com.xclone.learning.enums.Sentiment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserScheduler {
    @Autowired
    private EmailService emailService;

    @Autowired
    private UserRepositoryIMPL userRepository;

    @Autowired
    private SentimentAnalysisServices sentimentAnalysisServices;

    @Autowired
    private AppCache appCache;

    @Scheduled(cron = "0 0 9 * * SUN")
    public void fetchUserAndSendMail(){
        List<User> users = userRepository.getUserForSA();
        for (User user: users){
            List<JournalEntity> journalEntries = user.getJournalEntries();
            List<Sentiment> sentiments = journalEntries.stream().filter(x -> x.getDate().isAfter(LocalDateTime.now().minus(7, ChronoUnit.DAYS))).map(x->x.getSentiment()).collect(Collectors.toList());
            Map<Sentiment,Integer> sentimentsCount = new HashMap<>();
            for(Sentiment sentiment : sentiments){
                sentimentsCount.put(sentiment,sentimentsCount.getOrDefault(sentiment,0)+1);
            }
            Sentiment mostFrequentSentiment = null;
            int maxCount = 0;
            for(Map.Entry<Sentiment,Integer> entry : sentimentsCount.entrySet()){
                if (entry.getValue() > maxCount){
                    maxCount = entry.getValue();
                    mostFrequentSentiment = entry.getKey();
                }
            }
            if (mostFrequentSentiment != null){
                emailService.sendEmail(user.getEmail(),"Sentiment for last 7 days",mostFrequentSentiment.toString());
            }
        }
    }

    @Scheduled(cron = "0 0/5 * ? * *")
    public void clearAppCache(){
        appCache.init();
    }

}
