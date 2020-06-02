package com.kronos.autoregister.helper

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

import java.awt.Label

class ClassFilterVisitor extends ClassVisitor {
    private HashSet<String> classItems

    ClassFilterVisitor(ClassVisitor classVisitor, HashSet<String> classItems) {
        super(Opcodes.ASM5, classVisitor)
        this.classItems = classItems
    }

    @Override
    void visitEnd() {
        super.visitEnd()
    }

    @Override
    MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        Log.info("name:$name  desc:$desc ")
        if (name == "injectInit" && desc == "()V") {
            MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions)
            MethodVisitor mv = new MethodVisitor(Opcodes.ASM5, methodVisitor) {
                @Override
                void visitCode() {
                    classItems.each { String input ->
                        input = input.replace(".class", "")
                        Log.info("item:" + input)
                        mv.visitMethodInsn(Opcodes.INVOKESTATIC, input, "init", "()V", false)
                    }
                    super.visitCode()
                }
            }
            return mv

        }
        return super.visitMethod(access, name, desc, signature, exceptions)
    }
}