package com.kronos.autoregister.helper;

import com.kronos.autoregister.Constant;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.HashSet;

public class TryCatchMethodVisitor extends MethodVisitor {
    private HashSet<String> deleteItems;
    private HashSet<String> addItems;

    public TryCatchMethodVisitor(MethodVisitor mv, HashSet<String> addItems, HashSet<String> deleteItems) {
        super(Opcodes.ASM5, mv);
        this.deleteItems = deleteItems;
        this.addItems = addItems;
        if (this.addItems == null) {
            this.addItems = new HashSet<>();
        }
        if (this.deleteItems == null) {
            this.deleteItems = new HashSet<>();
        }
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        String className = owner + ".class";
        if (!deleteItems.contains(className)) {
            super.visitMethodInsn(opcode, owner, name, desc, itf);
        }
    }

    @Override
    public void visitCode() {
        super.visitCode();
        for (String input : addItems) {
            input = input.replace(".class", "");
            input = input.replace(".", "/");
            deleteItems.add(input + ".class");
            addTryCatchMethodInsn(Opcodes.INVOKESTATIC, input, Constant.REGISTER_CLASS_FUNCTION_CONST, "()V", false);
        }
    }


    public void addTryCatchMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
   /*     Label l0 = new Label();
        Label l1 = new Label();
        Label l2 = new Label();
        mv.visitTryCatchBlock(l0, l1, l2, "java/lang/Exception");*/
        mv.visitMethodInsn(opcode, owner, name, desc, itf);
       /* mv.visitLabel(l1);
        Label l3 = new Label();
        mv.visitJumpInsn(Opcodes.GOTO, l3);
        mv.visitLabel(l2);
        mv.visitVarInsn(Opcodes.ASTORE, 1);
        mv.visitLabel(l3);*/
    }
}
