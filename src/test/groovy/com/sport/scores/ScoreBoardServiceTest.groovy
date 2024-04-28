package com.sport.scores


import com.sport.scores.service.ScoreBoardService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest(classes = ScoresLibraryConfiguration.class)
class ScoreBoardServiceTest extends Specification {

    @Autowired(required = true)
    private ScoreBoardService scoreBoardService;

    def "after context is loaded, beans are available"() {
        expect: "the scoreService is not null"
        scoreBoardService
    }
}