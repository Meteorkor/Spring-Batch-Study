package com.meteor.batch.guava;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.google.common.base.Optional;

/**
 * Many of the cases where programmers use null is to indicate some sort of absence: perhaps where there might have been a value, there is none, or one could not be found.
 * For example, Map.get returns null when no value is found for a key.
 */
public class BasicUtilsNullOptionalTest {
    //java.util.Optional
    //com.google.common.base.Optional

    @Test
    void guavaOptional() {
        {//absent
            final com.google.common.base.Optional<Object> absentOptional = Optional.absent();
            Assertions.assertFalse(absentOptional.isPresent());
            Assertions.assertEquals("DUMMY", absentOptional.or("DUMMY"));
            Assertions.assertEquals("DUMMY", absentOptional.transform(val -> "TRANS").or("DUMMY"));

            //java.lang.IllegalStateException: Optional.get() cannot be called on an absent value
            Assertions.assertThrows(IllegalStateException.class, () -> {
                absentOptional.get();
            });
            Assertions.assertNull(absentOptional.orNull());
            Assertions.assertEquals(java.util.Optional.empty(), absentOptional.toJavaUtil());
        }
        {//of("DATA")
            final String data = "DATA";
            final com.google.common.base.Optional<Object> dataOptional = Optional.of(data);
            Assertions.assertTrue(dataOptional.isPresent());
            Assertions.assertEquals(data, dataOptional.or("DUMMY"));
            Assertions.assertEquals("TRANS", dataOptional.transform(val -> "TRANS").or("DUMMY"));
            Assertions.assertEquals(data, dataOptional.get());
            Assertions.assertEquals(data, dataOptional.orNull());
            Assertions.assertEquals(java.util.Optional.of(data), dataOptional.toJavaUtil());
        }
        {//fromNullable(null)
            final com.google.common.base.Optional<Object> nullOptional = Optional.fromNullable(null);
            Assertions.assertFalse(nullOptional.isPresent());
            Assertions.assertEquals("DUMMY", nullOptional.or("DUMMY"));
            Assertions.assertEquals("DUMMY", nullOptional.transform(val -> "TRANS").or("DUMMY"));

            //java.lang.IllegalStateException: Optional.get() cannot be called on an absent value
            Assertions.assertThrows(IllegalStateException.class, () -> {
                nullOptional.get();
            });
            Assertions.assertNull(nullOptional.orNull());
            Assertions.assertEquals(java.util.Optional.empty(), nullOptional.toJavaUtil());
        }

    }

    @Test
    void jdkOptional() {
        {//empty()
            final java.util.Optional<Object> absentOptional = java.util.Optional.empty();
            Assertions.assertFalse(absentOptional.isPresent());
            Assertions.assertEquals("DUMMY", absentOptional.orElse("DUMMY"));
            Assertions.assertEquals("DUMMY", absentOptional.map(val -> "TRANS").orElse("DUMMY"));

            //java.lang.IllegalStateException: Optional.get() cannot be called on an absent value
            Assertions.assertThrows(NoSuchElementException.class, () -> {
                absentOptional.get();
            });
            Assertions.assertNull(absentOptional.orElse(null));

            //additional methods
            //jdk9
//            Assertions.assertEquals(
//                    java.util.Optional.of("DUMMY"),
//                    absentOptional.or(() -> {
//                        return java.util.Optional.of("DUMMY");
//                    })
//            );

            //jdk9
//            Assertions.assertThrows(UnsupportedOperationException.class, () -> {
//                absentOptional.ifPresentOrElse(value -> {
//                    //consume value Processing
//                }, () -> {
//                    throw new UnsupportedOperationException();
//                });
//            });
        }
        {//of("DATA")
            final String data = "DATA";
            final java.util.Optional<Object> dataOptional = java.util.Optional.of(data);
            Assertions.assertTrue(dataOptional.isPresent());
            Assertions.assertEquals(data, dataOptional.orElse("DUMMY"));
            Assertions.assertEquals("TRANS", dataOptional.map(val -> "TRANS").orElse("DUMMY"));
            Assertions.assertEquals(data, dataOptional.get());
            Assertions.assertEquals(data, dataOptional.orElse(null));

            //additional methods
            //jdk9
//            Assertions.assertEquals(
//                    java.util.Optional.of(data),
//                    dataOptional.or(() -> {
//                        return java.util.Optional.of("DUMMY");
//                    })
//            );

            //jdk9
//            dataOptional.ifPresentOrElse(value -> {
//                //consume value Processing
//            }, () -> {
//                throw new UnsupportedOperationException();
//            });

        }
        {//fromNullable(null)
            final java.util.Optional<Object> nullOptional = java.util.Optional.ofNullable(null);
            Assertions.assertFalse(nullOptional.isPresent());
            Assertions.assertEquals("DUMMY", nullOptional.orElse("DUMMY"));
            Assertions.assertEquals("DUMMY", nullOptional.map(val -> "TRANS").orElse("DUMMY"));

            //java.lang.IllegalStateException: Optional.get() cannot be called on an absent value
            Assertions.assertThrows(NoSuchElementException.class, () -> {
                nullOptional.get();
            });
            Assertions.assertNull(nullOptional.orElse(null));

            //addtional methods
            //jdk9
//            Assertions.assertEquals(
//                    java.util.Optional.of("DUMMY"),
//                    nullOptional.or(() -> {
//                        return java.util.Optional.of("DUMMY");
//                    })
//            );

            //jdk9
//            Assertions.assertThrows(UnsupportedOperationException.class, () -> {
//                nullOptional.ifPresentOrElse(value -> {
//                    //consume value Processing
//                }, () -> {
//                    throw new UnsupportedOperationException();
//                });
//            });
        }

    }

}
