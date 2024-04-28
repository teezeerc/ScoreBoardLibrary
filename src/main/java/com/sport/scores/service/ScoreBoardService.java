package com.sport.scores.service;

import com.sport.scores.exception.InvalidMatchState;
import com.sport.scores.exception.InvalidScore;
import com.sport.scores.model.Match;

import java.util.List;

public interface ScoreBoardService {

    void startMatch(Match match) throws InvalidMatchState;

    void finishMatch(Match match) throws InvalidMatchState;

    void updateScore(Match match, int homeTeamScore, int awayTeamScore) throws InvalidScore, InvalidMatchState;

    List<Match> getOngoingMatchesOrdered();
}
