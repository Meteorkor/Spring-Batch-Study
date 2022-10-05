package com.meteor.batch.parser;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.ImportDeclaration;

import com.google.common.collect.Lists;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JavaFinder {

    public static final JavaFinder INSTANCE = new JavaFinder();

    public List<String> findJava(String fileOrDir) {
        return findJava(new File(fileOrDir));
    }

    public List<String> findJava(File fileOrDir) {

        if (fileOrDir.isDirectory()) {
            return Arrays.stream(fileOrDir.listFiles())
                         .filter(file -> file.getName().endsWith(".java"))
                         .map(this::findJavaInner)
                         .filter(Optional::isPresent)
                         .map(Optional::get)
                         .collect(Collectors.toList());
        } else {
            if (!fileOrDir.getName().endsWith(".java")) {
                throw new IllegalArgumentException("not java file");
            }
            final Optional<String> javaInner = findJavaInner(fileOrDir);
            return javaInner.map(inner -> Lists.newArrayList(inner)).orElse(Lists.newArrayList());
        }
    }

    private Optional<String> findJavaInner(File file) {

        final JavaModel javaModel = JavaParser.INSTANCE.parser(file);
        final Map<String, ImportDeclaration> importDeclarationMap = javaModel.imports(
                                                                                     importDeclaration -> importDeclaration.getName()
                                                                                                                           .getFullyQualifiedName()
                                                                                                                           .contains(".dto.")
                                                                             )
                                                                             .stream()
                                                                             .collect(Collectors.toMap(
                                                                                     importDeclaration -> {
                                                                                         final String
                                                                                                 fullyQualifiedName =
                                                                                                 importDeclaration.getName()
                                                                                                                  .getFullyQualifiedName();
                                                                                         final int lastIndexOf =
                                                                                                 fullyQualifiedName.lastIndexOf(
                                                                                                         ".");

                                                                                         return fullyQualifiedName.substring(
                                                                                                 lastIndexOf
                                                                                                 + 1);
                                                                                     },
                                                                                     Function.identity()
                                                                             ));
        //import 문에 dto 없으면 스킵
        if (importDeclarationMap.isEmpty()) {return Optional.empty();}

        //dto memberVariable check

        final List<String> dtoVariableList = javaModel.bodyDeclarations().stream().filter(
                                                          type -> importDeclarationMap.containsKey(type.getType().toString()))
                                                  .map(fieldDeclaration -> fieldDeclaration.getType()
                                                                                           .toString()
                                                                           + ":"
                                                                           + fieldDeclaration.fragments()
                                                                                             .get(0)
                                                                                             .toString())
                                                  .collect(Collectors.toList());

        if (dtoVariableList.isEmpty()) {return Optional.empty();}

        StringJoiner stringJoiner = new StringJoiner("\n");
        stringJoiner.add(javaModel.typeDeclaration().getName().getIdentifier());

//        stringJoiner.

//                System.out.println("=====" +);
        importDeclarationMap.entrySet().forEach(entry -> {
            System.out.println("entry.getKey() : " + entry.getKey());
            System.out.println("entry.getValue() : " + entry.getValue());
        });
        return Optional.of(stringJoiner.toString());
    }
}
