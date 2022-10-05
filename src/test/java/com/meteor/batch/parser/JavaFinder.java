package com.meteor.batch.parser;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.google.common.collect.Lists;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JavaFinder {

    private final String findPackageName = ".dto.";
    public static final JavaFinder INSTANCE = new JavaFinder();

    public List<String> findJava(String fileOrDir) {
        return findJava(new File(fileOrDir));
    }

    private List<String> findJava(File fileOrDir) {
        if (fileOrDir.isDirectory()) {
            return Arrays.stream(fileOrDir.listFiles())
                         .map(file -> findJava(file))
                         .flatMap(list -> list.stream())
                         .collect(Collectors.toList());
        }

        if (!fileOrDir.getName().endsWith(".java")) {
            return Lists.newArrayList();
        }

        return findJavaInner(fileOrDir).map(inner -> Lists.newArrayList(inner)).orElse(Lists.newArrayList());
    }

    private Optional<String> findJavaInner(File file) {
        final JavaModel javaModel = JavaParser.INSTANCE.parser(file);
        final Map<String, ImportDeclaration> importDeclarationMap = getDtoImportedCollect(javaModel);
        //import 문에 dto 없으면 스킵
        if (importDeclarationMap.isEmpty()) {return Optional.empty();}

        StringJoiner stringJoiner = new StringJoiner("\n");
        stringJoiner.add(typeDeclarationLogging(javaModel));

        //dto memberVariable check
        final List<String> dtoVariableList = getDtoMemberVariable(javaModel, importDeclarationMap);

        if (dtoVariableList.isEmpty()) {return Optional.empty();}

        dtoVariableList.forEach(logging -> stringJoiner.add(logging));

        return Optional.of(stringJoiner.toString());
    }

    private List<String> getDtoMemberVariable(JavaModel javaModel,
                                              Map<String, ImportDeclaration> importDeclarationMap) {
        return javaModel.fieldDeclaration()
                        .stream()
                        .flatMap(type -> {
                            final ImportDeclaration importDeclaration = importDeclarationMap.get(
                                    type.getType().toString());
                            if (importDeclaration == null) {
                                return Stream.empty();
                            }
                            return Stream.of(fieldDeclarationLogging(importDeclaration, type));
                        })
                        .collect(Collectors.toList());
    }

    private Map<String, ImportDeclaration> getDtoImportedCollect(JavaModel javaModel) {
        return javaModel.imports(
                                importDeclaration -> importDeclaration.getName()
                                                                      .getFullyQualifiedName()
                                                                      .contains(findPackageName)
                        )
                        .stream()
                        .collect(Collectors.toMap(
                                importDeclaration -> {
                                    final String
                                            fullyQualifiedName = importDeclaration.getName()
                                                                                  .getFullyQualifiedName();
                                    final int lastIndexOf = fullyQualifiedName.lastIndexOf(".");

                                    return fullyQualifiedName.substring(lastIndexOf + 1);
                                },
                                Function.identity()
                        ));
    }

    private String fieldDeclarationLogging(ImportDeclaration importDeclaration,
                                           FieldDeclaration fieldDeclaration) {

        return new StringBuilder()
                .append("    ")
                .append(importDeclaration.getName().getFullyQualifiedName())
                .append("::")
                .append(fieldDeclaration.fragments()
                                        .get(0)
                                        .toString())
                .toString();
    }

    //find  '.dto.' ]com.meteor.batch.parser.TestVariableClass1
    private String typeDeclarationLogging(JavaModel javaModel) {
        final TypeDeclaration typeDeclaration = javaModel.typeDeclaration();

        return new StringBuilder()
                .append("find  '")
                .append(findPackageName)
                .append("' ] ")
                .append(javaModel.getPackage())
                .append(".")
                .append(typeDeclaration.getName().getFullyQualifiedName())
                .toString();
    }

}
