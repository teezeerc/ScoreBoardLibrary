package com.sport.scores

import com.sport.scores.dict.TeamType
import com.sport.scores.exception.InvalidMatchState
import com.sport.scores.model.Match
import com.sport.scores.model.Team
import com.sport.scores.service.ScoreBoardService
import com.sport.scores.service.ScoreBoardServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest(classes = ScoresLibraryConfiguration.class)
class ScoreBoardServiceTest extends Specification {

    @Autowired(required = true)
    private ScoreBoardServiceImpl scoreBoardService

    def "after context is loaded, beans are available"() {
        expect: "the scoreService is not null"
        scoreBoardService
    }

    def "after starting a match it should be added to matches collection"() {
        when:
        Team team1 = Team.builder().name("TeamA").type(TeamType.HOME).build()
        Team team2 = Team.builder().name("TeamB").type(TeamType.AWAY).build()
        Match match = Match.builder().homeTeam(team1).awayTeam(team2).build()
        scoreBoardService.startMatch(match)
        then:
        scoreBoardService.matches.size() == 1
    }

    def "starting a match with two teams in the same role should throw exception"() {
        when:
        Team team1 = Team.builder().name("TeamA").type(TeamType.HOME).build()
        Team team2 = Team.builder().name("TeamB").type(TeamType.HOME).build()
        Match match = Match.builder().homeTeam(team1).awayTeam(team2).build()
        scoreBoardService.startMatch(match)
        then:
        thrown InvalidMatchState
    }

    def "starting a match that was already started should throw exception"() {
        when:
        Team team1 = Team.builder().name("TeamA").type(TeamType.HOME).build()
        Team team2 = Team.builder().name("TeamB").type(TeamType.AWAY).build()
        Match match = Match.builder().homeTeam(team1).awayTeam(team2).build()
        scoreBoardService.startMatch(match)
        scoreBoardService.startMatch(match)
        then:
        thrown InvalidMatchState
    }
}