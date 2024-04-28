package com.sport.scores.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Team {
    @EqualsAndHashCode.Include
    private final String name;
    @Setter
    @Builder.Default
    private int score = 0;
}
