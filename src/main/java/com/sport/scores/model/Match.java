package com.sport.scores.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Comparator;

@Builder
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Match implements Comparable<Match>{
    @EqualsAndHashCode.Include
    private final Team homeTeam;
    @EqualsAndHashCode.Include
    private final Team awayTeam;
    @Setter
    @Builder.Default
    private LocalDateTime startDateTime = null;
    @Setter
    @Builder.Default
    private LocalDateTime finishDateTime = null;

    private int getTotalScore(){
        return getHomeTeam().getScore() + getAwayTeam().getScore();
    }
    @Override
    public int compareTo(Match other) {
        return Comparator.comparing(Match::getTotalScore)
                .thenComparing(Match::getStartDateTime)
                .reversed()
                .compare(this, other);
    }
}
