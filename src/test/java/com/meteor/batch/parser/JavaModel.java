package com.meteor.batch.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class JavaModel {

    private final CompilationUnit compilationUnit;
    private TypeDeclaration typeDeclaration;

    private JavaModel(CompilationUnit compilationUnit) {
        this.compilationUnit = compilationUnit;
    }

    public static JavaModel getInstance(CompilationUnit compilationUnit) {
        return new JavaModel(compilationUnit);
    }

    public String getPackage() {
        return compilationUnit.getPackage().getName().getFullyQualifiedName();
    }

    public List<ImportDeclaration> imports() {
        return new ArrayList(compilationUnit.imports());
    }

    public List<ImportDeclaration> imports(Function<ImportDeclaration, Boolean> filter) {
        return imports().stream().filter(filter::apply).collect(Collectors.toList());
    }

    public TypeDeclaration typeDeclaration() {
        if (typeDeclaration == null) {
            this.typeDeclaration = (TypeDeclaration) compilationUnit.types().get(0);
        }

        return typeDeclaration;
    }

    public List<BodyDeclaration> bodyDeclarations() {
        return new ArrayList<BodyDeclaration>(typeDeclaration.bodyDeclarations());
    }

    public List<BodyDeclaration> bodyDeclarations(Function<BodyDeclaration, Boolean> filter) {
        return bodyDeclarations().stream().filter(filter::apply).collect(Collectors.toList());
    }

    public List<FieldDeclaration> fieldDeclaration() {
        return bodyDeclarations().stream()
                                 .flatMap(bodyDec -> {
                                     if (bodyDec instanceof FieldDeclaration) {
                                         return Stream.of(
                                                 (FieldDeclaration) bodyDec);
                                     }
                                     return Stream.empty();
                                 }).collect(Collectors.toList());
    }

    public List<FieldDeclaration> fieldDeclaration(Function<FieldDeclaration, Boolean> filter) {
        return fieldDeclaration().stream().filter(filter::apply).collect(Collectors.toList());
    }

}
