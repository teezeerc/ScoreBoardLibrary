package com.sport.scores.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Match {
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

}
