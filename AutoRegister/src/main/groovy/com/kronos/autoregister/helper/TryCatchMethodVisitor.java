package com.kronos.autoregister.helper;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class TryCatchMethodVisitor extends MethodVisitor {

    public TryCatchMethodVisitor(MethodVisitor mv) {
        super(Opcodes.ASM5, mv);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        addTry(opcode, owner, name, desc, itf);
    }

    private void addTry(int opcode, String owner, String name, String desc, boolean itf) {
        Log.info("addTry start");
        Label l0 = new Label();
        Label l1 = new Label();
        Label l2 = new Label();
        mv.visitTryCatchBlock(l0, l1, l2, "java/lang/Exception");
        mv.visitMethodInsn(opcode, owner, name, desc, itf);
        mv.visitLabel(l1);
        Label l3 = new Label();
        mv.visitJumpInsn(Opcodes.GOTO, l3);
        mv.visitLabel(l2);
        mv.visitVarInsn(Opcodes.ASTORE, 1);
        mv.visitLabel(l3);
        Log.info("addTry end");
    }
}
