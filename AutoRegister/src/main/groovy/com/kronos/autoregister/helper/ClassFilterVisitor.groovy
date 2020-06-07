package com.kronos.autoregister.helper

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class ClassFilterVisitor extends ClassVisitor {
    private HashSet<String> classItems
    private HashSet<String> deleteItems

    ClassFilterVisitor(ClassVisitor classVisitor, HashSet<String> classItems, HashSet<String> deleteItems) {
        super(Opcodes.ASM6, classVisitor)
        this.classItems = classItems
        this.deleteItems = deleteItems
    }

    @Override
    MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        if (name == "register" && desc == "()V") {
            TryCatchMethodVisitor methodVisitor = new TryCatchMethodVisitor(super.visitMethod(access, name, desc, signature, exceptions),
                    classItems, deleteItems)
            return methodVisitor
        }
        return super.visitMethod(access, name, desc, signature, exceptions)
    }

}