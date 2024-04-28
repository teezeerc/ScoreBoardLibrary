package com.sport.scores.service;

import com.sport.scores.exception.InvalidMatchState;
import com.sport.scores.exception.InvalidScore;
import com.sport.scores.model.Match;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScoreBoardServiceImpl implements ScoreBoardService {

    private final Set<Match> matches = new HashSet<>();

    @Override
    public void startMatch(Match match) throws InvalidMatchState {
        validateMatch(match);
        if (matches.contains(match)) {
            throw new InvalidMatchState("Match already started");
        } else {
            match.setStartDateTime(LocalDateTime.now());
            matches.add(match);
        }
    }

    @Override
    public void finishMatch(Match match) throws InvalidMatchState {
        validateMatch(match);
        if (!matches.contains(match)) {
            throw new InvalidMatchState("Match doesn't exist, it was never started");
        } else {
            match.setFinishDateTime(LocalDateTime.now());
            matches.remove(match);
        }
    }


    @Override
    public void updateScore(Match match, int homeTeamScore, int awayTeamScore) throws InvalidScore, InvalidMatchState {
        validateMatch(match);
        validateScore(match, homeTeamScore, awayTeamScore);
        if (!matches.contains(match)) {
            throw new InvalidMatchState("Match doesn't exist, it was never started");
        } else {
            match.getHomeTeam().setScore(homeTeamScore);
            match.getAwayTeam().setScore(awayTeamScore);
        }
    }

    @Override
    public List<Match> getOngoingMatchesOrdered() {
        return null;
    }

    private void validateScore(Match match, int newHomeTeamScore, int newAwayTeamScore) throws InvalidScore {
        int currentHomeTeamScore = match.getHomeTeam().getScore();
        int currentAwayTeamScore = match.getAwayTeam().getScore();
        if(newHomeTeamScore<currentHomeTeamScore || newAwayTeamScore<currentAwayTeamScore){
            throw new InvalidScore("Scores for teams must be equal of greater than current scores and >=0");
        }

    }
    private void validateMatch(Match match) throws InvalidMatchState {
        if (match == null) {
            throw new InvalidMatchState("Match can't be null");
        }
        if (match.getAwayTeam() == null || match.getHomeTeam() == null) {
            throw new InvalidMatchState("Teams can't be null");
        }
        if (match.getAwayTeam().equals(match.getHomeTeam())) {
            throw new InvalidMatchState("Home team and away team must be different");
        }
    }

}
