package com.meteor.batch.basic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConditionTest {
    private int callCnt;

    @Test
    void conditionAndOr() {
        {
            boolean result = callLogFalse() && callLogFalse() || callLogTrue();
            Assertions.assertTrue(result);
//        0:false
//        1:true
        }
        callCnt = 0;
        {//위와 같음l
            boolean result = (callLogFalse() && callLogFalse()) || callLogTrue();
            Assertions.assertTrue(result);
//        0:false
//        1:true
        }
    }

    @Test
    void conditionAndOrAnd() {
        {
            boolean result = callLogFalse() && callLogFalse() || callLogTrue() && callLogFalse();
            Assertions.assertFalse(result);
//            0:false
//            1:true
//            2:false
        }
        callCnt = 0;
        {//위와 같음
            boolean result = (callLogFalse() && callLogFalse()) || (callLogTrue() && callLogFalse());
            Assertions.assertFalse(result);
//            0:false
//            1:true
//            2:false
        }
    }

    boolean callLogFalse() {
        System.out.println((callCnt++) + ":false");
        return false;
    }

    boolean callLogTrue() {
        System.out.println((callCnt++) + ":true");
        return true;
    }

}
