package com.sport.scores


import com.sport.scores.exception.InvalidMatchState
import com.sport.scores.exception.InvalidScore
import com.sport.scores.model.Match
import com.sport.scores.model.Team
import com.sport.scores.service.ScoreBoardServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest(classes = ScoresLibraryConfiguration.class)
class ScoreBoardServiceTest extends Specification {

    @Autowired(required = true)
    private ScoreBoardServiceImpl scoreBoardService

    def "after context is loaded, beans are available"() {
        expect: "the scoreBoardService is not null"
        scoreBoardService
    }

    def "after starting a match it should be added to matches collection"() {
        given:
        scoreBoardService.matches.clear()
        when:
        Team team1 = Team.builder().name("TeamA").build()
        Team team2 = Team.builder().name("TeamB").build()
        Match match = Match.builder().homeTeam(team1).awayTeam(team2).build()
        scoreBoardService.startMatch(match)
        then:
        scoreBoardService.matches.size() == 1
    }

    def "starting a match with two teams in the same role should throw exception"() {
        given:
        scoreBoardService.matches.clear()
        when:
        Team team1 = Team.builder().name("TeamA").build()
        Match match = Match.builder().homeTeam(team1).awayTeam(team1).build()
        scoreBoardService.startMatch(match)
        then:
        thrown InvalidMatchState
    }

    def "starting a match that was already started should throw exception"() {
        given:
        scoreBoardService.matches.clear()
        when:
        Team team1 = Team.builder().name("TeamA").build()
        Team team2 = Team.builder().name("TeamB").build()
        Match match = Match.builder().homeTeam(team1).awayTeam(team2).build()
        scoreBoardService.startMatch(match)
        scoreBoardService.startMatch(match)
        then:
        thrown InvalidMatchState
    }

    def "after finishing a match it should be removed from matches collection"() {
        given:
        scoreBoardService.matches.clear()
        when:
        Team team1 = Team.builder().name("TeamA").build()
        Team team2 = Team.builder().name("TeamB").build()
        Match match = Match.builder().homeTeam(team1).awayTeam(team2).build()
        scoreBoardService.startMatch(match)
        int afterStartCollectionSize = scoreBoardService.matches.size()
        scoreBoardService.finishMatch(match)
        int afterFinishCollectionSize = scoreBoardService.matches.size()
        then:
        afterStartCollectionSize == 1
        afterFinishCollectionSize == 0
    }

    def "finishing a match that was never started should throw exception"() {
        given:
        scoreBoardService.matches.clear()
        when:
        Team team1 = Team.builder().name("TeamA").build()
        Team team2 = Team.builder().name("TeamB").build()
        Match match = Match.builder().homeTeam(team1).awayTeam(team2).build()
        scoreBoardService.startMatch(match)
        Team otherTeam1 = Team.builder().name("OtherTeamA").build()
        Team otherTeam2 = Team.builder().name("OtherTeamB").build()
        Match otherMatch = Match.builder().homeTeam(otherTeam1).awayTeam(otherTeam2).build()
        scoreBoardService.finishMatch(otherMatch)
        then:
        thrown InvalidMatchState
    }

    def "updating a score for never started match should throw exception"() {
        given:
        scoreBoardService.matches.clear()
        when:
        Team team1 = Team.builder().name("TeamA").build()
        Team team2 = Team.builder().name("TeamB").build()
        Match match = Match.builder().homeTeam(team1).awayTeam(team2).build()
        scoreBoardService.updateScore(match, 0, 0)
        then:
        thrown InvalidMatchState
    }

    def "updating a score to a lower values than already present should throw exception"() {
        given:
        scoreBoardService.matches.clear()
        when:
        Team team1 = Team.builder().name("TeamA").build()
        Team team2 = Team.builder().name("TeamB").build()
        Match match = Match.builder().homeTeam(team1).awayTeam(team2).build()
        scoreBoardService.startMatch(match)
        scoreBoardService.updateScore(match, 3, 1)
        scoreBoardService.updateScore(match, 1, 1)
        then:
        thrown InvalidScore
    }

    def "updating a score multiple times should yield match with updated score"() {
        given:
        scoreBoardService.matches.clear()
        when:
        Team team1 = Team.builder().name("TeamA").build()
        Team team2 = Team.builder().name("TeamB").build()
        Match match = Match.builder().homeTeam(team1).awayTeam(team2).build()
        scoreBoardService.startMatch(match)
        scoreBoardService.updateScore(match, 3, 1)
        scoreBoardService.updateScore(match, 3, 2)
        scoreBoardService.updateScore(match, 3, 3)
        scoreBoardService.updateScore(match, 3, 4)
        then:
        scoreBoardService.matches.getAt(0).homeTeam.score == 3
        scoreBoardService.matches.getAt(0).awayTeam.score == 4
    }

    def "matches should be sorted by total score and for equal total scores should be sorted by most recent started"() {
        given:
        scoreBoardService.matches.clear()
        when:
        Match m1 = startAndUpdateMatch("Mexico","Canada",0,5)
        Match m2 = startAndUpdateMatch("Spain","Brazil",0,5)
        Match m3 = startAndUpdateMatch("Germany","France",2,2)
        Match m4 = startAndUpdateMatch("Uruguay","Italy",6,6)
        Match m5 = startAndUpdateMatch("Argentina","Australia",3,1)
        List<Match> sortedMatches = scoreBoardService.getOngoingMatchesOrdered()
        then:
        [m4, m2, m1, m5, m3] == sortedMatches
    }

    private Match startAndUpdateMatch(String homeTeamName, String awayTeamName, int homeTeamScore, int awayTeamScore){
        Team team1 = Team.builder().name(homeTeamName).build()
        Team team2 = Team.builder().name(awayTeamName).build()
        Match match = Match.builder().homeTeam(team1).awayTeam(team2).build()
        scoreBoardService.startMatch(match)
        scoreBoardService.updateScore(match, homeTeamScore, awayTeamScore)
        return match
    }
}