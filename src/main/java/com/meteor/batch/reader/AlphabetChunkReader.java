package com.meteor.batch.reader;


import com.meteor.batch.common.AlphabetConst;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * List Item Reader 같은걸 쓰면 되긴하지만..
 * Scope와 다른 설정들을 사용하기 위해 굳이 별도 클래스 생성
 */
@Component
@StepScope
//@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class AlphabetChunkReader implements ItemReader<String> {

    //StepScope나 JobScope 일때 가능
    @Value("#{jobParameters}")
    private Map<String, Object> jobParameters;
    private List<String> strList = null;


    @Override
    public String read() {
        if (strList == null) {
            strList = new ArrayList<>(Arrays.asList(String.valueOf(jobParameters.get(AlphabetConst.KEY)).split(",")));
        }

        return strList.isEmpty() ? null : strList.remove(0);
    }
}