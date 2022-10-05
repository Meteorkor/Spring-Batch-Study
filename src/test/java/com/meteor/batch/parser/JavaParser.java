package com.meteor.batch.parser;

import java.io.File;
import java.nio.file.Files;
import java.util.stream.Collectors;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import lombok.SneakyThrows;

public class JavaParser {
    public static final JavaParser INSTANCE = new JavaParser();

    @SneakyThrows
    public JavaModel parser(File file) {
        final ASTParser astParser = ASTParser.newParser(AST.JLS18);
        astParser.setKind(ASTParser.K_COMPILATION_UNIT);
        final String collect = Files.readAllLines(file.toPath()).stream().collect(Collectors.joining("\n"));
        astParser.setSource(collect.toCharArray());

        return JavaModel.getInstance((CompilationUnit) astParser.createAST(null));
    }

}
