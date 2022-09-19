package com.meteor.batch;

import org.springframework.util.StopWatch;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestTimeUtils {

    public static long processTime(Runnable runnable) {
        StopWatch st = new StopWatch();
        st.start();
        runnable.run();
        st.stop();
        return st.getTotalTimeMillis();
    }
    
}
