package com.sport.scores.service;

import com.sport.scores.exception.InvalidMatchState;
import com.sport.scores.exception.InvalidScore;
import com.sport.scores.model.Match;

import java.util.List;

public class ScoreBoardServiceImpl implements ScoreBoardService {

    @Override
    public void startMatch(Match match) throws InvalidMatchState {

    }

    @Override
    public void finishMatch(Match match) throws InvalidMatchState {

    }

    @Override
    public void updateScore(Match match, int homeTeamScore, int awayTeamScore) throws InvalidScore {

    }

    @Override
    public List<Match> getOngoingMatchesOrdered() {
        return null;
    }
}
