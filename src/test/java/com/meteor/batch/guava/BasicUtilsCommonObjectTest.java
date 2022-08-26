package com.meteor.batch.guava;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.common.base.MoreObjects;
import com.meteor.batch.reader.vo.Emp;

public class BasicUtilsCommonObjectTest {

    /**
     * When your object fields can be null, implementing Object.equals can be a pain,
     * because you have to check separately for null.
     * Using Objects.equal lets you perform equals checks in a null-sensitive way, without risking a NullPointerException.
     *
     * Note: The newly introduced Objects class in JDK 7 provides the equivalent Objects.equals method.
     */
    @Test
    void equalsTest() {
        //guava
        Assertions.assertTrue(com.google.common.base.Objects.equal("a", "a"));
        Assertions.assertFalse(com.google.common.base.Objects.equal(null, "a"));
        Assertions.assertFalse(com.google.common.base.Objects.equal("a", null));
        Assertions.assertTrue(com.google.common.base.Objects.equal(null, null));

        //jdk7 equals
        Assertions.assertTrue(java.util.Objects.equals("a", "a"));
        Assertions.assertFalse(java.util.Objects.equals(null, "a"));
        Assertions.assertFalse(java.util.Objects.equals("a", null));
        Assertions.assertTrue(java.util.Objects.equals(null, null));
    }

    @Test
    void hasCodeTest() {
        //guava
        Assertions.assertEquals(126145, com.google.common.base.Objects.hashCode("a", "b", "c"));

        //jdk7 hash
        Assertions.assertEquals(126145, java.util.Objects.hash("a", "b", "c"));
    }

    @Test
    void toStringTest() {
        Emp emp = Emp.builder()
                     .ename("kim")
                     .empno(1)
                     .build();
        {//손으로 작성
            final String toString = String.format("Emp{empno=%s, ename=%s}", emp.getEmpno(), emp.getEname());
            Assertions.assertEquals("Emp{empno=1, ename=kim}", toString);
        }
        {
            final String toString = MoreObjects.toStringHelper(Emp.class)
                                               .add("empno", emp.getEmpno())
                                               .add("ename", emp.getEname())
                                               .toString();
            Assertions.assertEquals("Emp{empno=1, ename=kim}", toString);
        }
    }

}
