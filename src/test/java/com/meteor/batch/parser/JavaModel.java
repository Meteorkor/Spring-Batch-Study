package com.meteor.batch.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public List<FieldDeclaration> bodyDeclarations() {
        return new ArrayList<FieldDeclaration>(typeDeclaration.bodyDeclarations());
    }

    public List<FieldDeclaration> bodyDeclarations(Function<FieldDeclaration, Boolean> filter) {
        return bodyDeclarations().stream().filter(filter::apply).collect(Collectors.toList());
    }

}
