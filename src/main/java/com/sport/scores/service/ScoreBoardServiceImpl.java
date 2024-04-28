package com.sport.scores.service;

import com.sport.scores.exception.InvalidMatchState;
import com.sport.scores.exception.InvalidScore;
import com.sport.scores.model.Match;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class ScoreBoardServiceImpl implements ScoreBoardService {

    private final Set<Match> matches = new HashSet<>();
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();

    @Override
    public void startMatch(Match match) throws InvalidMatchState {
        validateMatch(match);
        writeLock.lock();
        try {
            if (matches.contains(match)) {
                throw new InvalidMatchState("Match already started");
            } else {
                match.setStartDateTime(LocalDateTime.now());
                matches.add(match);
            }
        } finally {
            writeLock.unlock();

        }
    }

    @Override
    public void finishMatch(Match match) throws InvalidMatchState {
        validateMatch(match);
        writeLock.lock();
        try {
            if (!matches.contains(match)) {
                throw new InvalidMatchState("Match doesn't exist, it was never started");
            } else {
                match.setFinishDateTime(LocalDateTime.now());
                matches.remove(match);
            }
        } finally {
            writeLock.unlock();

        }
    }


    @Override
    public void updateScore(Match match, int homeTeamScore, int awayTeamScore) throws InvalidScore, InvalidMatchState {
        validateMatch(match);
        validateScore(match, homeTeamScore, awayTeamScore);
        writeLock.lock();
        try {
            if (!matches.contains(match)) {
                throw new InvalidMatchState("Match doesn't exist, it was never started");
            } else {
                match.getHomeTeam().setScore(homeTeamScore);
                match.getAwayTeam().setScore(awayTeamScore);
            }
        } finally {
            writeLock.unlock();

        }
    }

    @Override
    public List<Match> getOngoingMatchesOrdered() {
        readLock.lock();
        try {
            List<Match> result = matches.stream()
                    .sorted(Comparator.comparing(Match::getTotalScore)
                            .thenComparing(Match::getStartDateTime))
                    .collect(Collectors.toList());
            Collections.reverse(result);
            return result;
        } finally {
            readLock.unlock();
        }
    }

    private void validateScore(Match match, int newHomeTeamScore, int newAwayTeamScore) throws InvalidScore {
        int currentHomeTeamScore = match.getHomeTeam().getScore();
        int currentAwayTeamScore = match.getAwayTeam().getScore();
        if (newHomeTeamScore < currentHomeTeamScore || newAwayTeamScore < currentAwayTeamScore) {
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
