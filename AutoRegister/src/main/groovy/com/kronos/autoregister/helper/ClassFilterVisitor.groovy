package com.kronos.autoregister.helper

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class ClassFilterVisitor extends ClassVisitor {

    ClassFilterVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM5, classVisitor)
    }

    @Override
    MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        Log.info("* visitMethod *" + " , " + access + " , " + name + " , " + desc + " , " + signature + " , " + exceptions)
        return super.visitMethod(access, name, desc, signature, exceptions)
    }
}