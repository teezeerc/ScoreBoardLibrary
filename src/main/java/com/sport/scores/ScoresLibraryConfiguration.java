package com.sport.scores;

import com.sport.scores.service.ScoreBoardService;
import com.sport.scores.service.ScoreBoardServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan
@Configuration
public class ScoresLibraryConfiguration {

    @Bean
    ScoreBoardService getScoreService(){
        return new ScoreBoardServiceImpl();
    }
}
