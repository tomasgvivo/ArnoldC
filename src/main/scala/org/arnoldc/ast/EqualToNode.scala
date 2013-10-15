package org.arnoldc.ast

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes._
import org.objectweb.asm.Label

case class EqualToNode(operand1: AstNode, operand2: AstNode) extends ExpressionNode{
  def generate(mv: MethodVisitor) {

    val notEqual = new Label()
    val conclude = new Label()
    operand1.generate(mv)
    operand2.generate(mv)
    mv.visitJumpInsn(IF_ICMPNE , notEqual)
    mv.visitInsn(ICONST_1)
    mv.visitJumpInsn(GOTO, conclude)
    mv.visitLabel(notEqual)
    mv.visitFrame(F_SAME, 0, null, 0, null)
    mv.visitInsn(ICONST_0)
    mv.visitLabel(conclude)
    mv.visitFrame(F_SAME1, 0, null, 1, Array(INTEGER))
  }
}
