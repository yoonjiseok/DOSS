package com.example.moveon.web.dto.ranking;

import java.util.Objects;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Ranking {
    private Long id;
    private Long currentPixelCount;
    private Long rank;

    public static Ranking from(ZSetOperations.TypedTuple<String> typedTuple, Long rank) {
        return new Ranking(
                Long.parseLong(Objects.requireNonNull(typedTuple.getValue())),
                Objects.requireNonNull(typedTuple.getScore()).longValue(),
                rank
        );
    }
}
