package com.sport.scores.service;

import com.sport.scores.exception.InvalidMatchState;
import com.sport.scores.exception.InvalidScore;
import com.sport.scores.model.Match;

import java.util.List;

public interface ScoreBoardService {

    /**
     * Method starts a match and ads it to ongoing games
     * @param match
     * @throws InvalidMatchState when match data is incorrect
     */
    void startMatch(Match match) throws InvalidMatchState;

    /**
     * Method marks match as finished and removes it from ongoing games
     * @param match
     * @throws InvalidMatchState when match data is incorrect
     */
    void finishMatch(Match match) throws InvalidMatchState;

    /**
     * Method updates score of ongoing game
     * @param match
     * @param homeTeamScore
     * @param awayTeamScore
     * @throws InvalidScore when score data is incorrect
     * @throws InvalidMatchState when match data is incorrect
     */
    void updateScore(Match match, int homeTeamScore, int awayTeamScore) throws InvalidScore, InvalidMatchState;

    /**
     * Method returns a list of ongoing games, sorted
     * @return list of ongoing games, highest total scores are first and if scores are equal, games started first are higher on a list
     */
    List<Match> getOngoingMatchesOrdered();
}
