package com.kronos.autoregister.helper

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class ClassFilterVisitor extends ClassVisitor {
    private HashSet<String> classItems

    ClassFilterVisitor(ClassVisitor classVisitor, HashSet<String> classItems) {
        super(Opcodes.ASM5, classVisitor)
        this.classItems = classItems
    }

    @Override
    MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        if (name == "injectInit") {
            MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions)
            MethodVisitor mv = new MethodVisitor(Opcodes.ASM5, methodVisitor) {
            /*    @Override
                void visitCode() {
                    super.visitCode()
                    methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false)
                    methodVisitor.visitVarInsn(Opcodes.LSTORE, 1)
                }*/

                @Override
                void visitInsn(int opcode) {
                    if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN)
                            || opcode == Opcodes.ATHROW) {
                        classItems.each { String input ->
                            input = input.replace(".class", "")
                            Log.info("item:" + input)
                            methodVisitor.visitMethodInsn(Opcodes.INVOKESTATIC, input, "init", "()V;", false)
                        }
                    }
                    methodVisitor.visitInsn(opcode)
                }
            }
            return mv

        }
        return super.visitMethod(access, name, desc, signature, exceptions)
    }
}