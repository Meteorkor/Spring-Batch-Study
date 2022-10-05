package com.meteor.batch.parser;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JavaParserTest {

    @Test
    void parseTest() {
        File testSrc = new File(
                "/Users/user/dev/git/study/Spring-Batch-Study/src/test/java/com/meteor/batch/parser/TestVariableClass1.java");

        final JavaParser javaFinder = JavaParser.INSTANCE;
        final JavaModel javaModel = javaFinder.parser(testSrc);

        Assertions.assertEquals(
                "com.meteor.batch.parser.dto.OutTestImpl\n"
                + "com.meteor.batch.parser.dto.RequestIn"
                ,
                javaModel.imports().stream()
                         .map(importDeclaration -> importDeclaration.getName().getFullyQualifiedName())
                         .collect(Collectors.joining("\n"))
        );

        final TypeDeclaration typeDeclaration = javaModel.typeDeclaration();
        Assertions.assertEquals("TestVariableClass1", typeDeclaration.getName().getIdentifier());

        final List<FieldDeclaration> fieldDeclarations = javaModel.bodyDeclarations();

        //        private static OutTestImpl outTestStatic;
        {
            final FieldDeclaration fieldDeclaration = fieldDeclarations.get(0);
            final String fieldName = fieldDeclaration.fragments().get(0).toString();
            Assertions.assertEquals("outTestStatic", fieldName);
            if (fieldDeclaration.getType() instanceof SimpleType) {
                SimpleType simpleType = (SimpleType) fieldDeclaration.getType();
                Assertions.assertEquals("OutTestImpl", simpleType.getName()
                                                                 .getFullyQualifiedName());
            }
        }
//        private RequestIn requestIn;
        {
            final FieldDeclaration fieldDeclaration = fieldDeclarations.get(1);
            final String fieldName = fieldDeclaration.fragments().get(0).toString();
            Assertions.assertEquals("requestIn", fieldName);
            if (fieldDeclaration.getType() instanceof SimpleType) {
                SimpleType simpleType = (SimpleType) fieldDeclaration.getType();
                Assertions.assertEquals("RequestIn", simpleType.getName()
                                                               .getFullyQualifiedName());
            }
        }
        //        private OutTestImpl outTest;
        {
            final FieldDeclaration fieldDeclaration = fieldDeclarations.get(2);
            final String fieldName = fieldDeclaration.fragments().get(0).toString();
            Assertions.assertEquals("outTest", fieldName);
            if (fieldDeclaration.getType() instanceof SimpleType) {
                SimpleType simpleType = (SimpleType) fieldDeclaration.getType();
                Assertions.assertEquals("OutTestImpl", simpleType.getName()
                                                                 .getFullyQualifiedName());
            }
        }

    }

}
