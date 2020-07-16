package com.meteor.batch.processor;

import com.meteor.batch.common.Pair;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@StepScope
@Component
public class LineToAlphabetProcessor implements ItemProcessor<String, Map<String, Long>> {
    @Override
    public Map<String, Long> process(String s) {
        Pair<String, Long> pair = Pair.<String, Long>builder()
                .left(String.valueOf((char) 1))
                .right(1L).build();

        return s.chars()
                .mapToObj(integer -> Pair.<String, Long>builder()
                        .left(String.valueOf((char) integer))
                        .right(1L).build())
                //.mapToObj(integer -> Pair.create(String.valueOf((char) integer), 1L))
                .collect(
                        Collectors.toMap(Pair::getLeft, Pair::getRight, Long::sum)
                );
    }
}