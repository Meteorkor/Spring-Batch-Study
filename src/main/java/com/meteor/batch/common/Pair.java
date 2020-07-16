package com.meteor.batch.common;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Pair<L, R> {
    private final L left;
    private final R right;
}