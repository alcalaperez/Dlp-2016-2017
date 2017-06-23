/**
 * @generated VGen 1.3.3
 */

package visitor;

import ast.*;

public interface Visitor {
	public Object visit(Programa node, Object param);
	public Object visit(DefStruct node, Object param);
	public Object visit(DefVariableLocal node, Object param);
	public Object visit(DefVariableGlobal node, Object param);
	public Object visit(Campo node, Object param);
	public Object visit(DefMetodo node, Object param);
	public Object visit(IntType node, Object param);
	public Object visit(FloatType node, Object param);
	public Object visit(IdentType node, Object param);
	public Object visit(CharType node, Object param);
	public Object visit(ArrayType node, Object param);
	public Object visit(While node, Object param);
	public Object visit(If node, Object param);
	public Object visit(IfElse node, Object param);
	public Object visit(Return node, Object param);
	public Object visit(Asigna node, Object param);
	public Object visit(Print node, Object param);
	public Object visit(Read node, Object param);
	public Object visit(InvocarMetodo node, Object param);
	public Object visit(Param node, Object param);
	public Object visit(ExprAritmetica node, Object param);
	public Object visit(ExprComparadora node, Object param);
	public Object visit(ExprComparadoraNot node, Object param);
	public Object visit(ExprAccesoObjeto node, Object param);
	public Object visit(ExprArray node, Object param);
	public Object visit(ExprInvocarMetodo node, Object param);
	public Object visit(Variable node, Object param);
	public Object visit(LiteralInt node, Object param);
	public Object visit(LiteralReal node, Object param);
	public Object visit(LiteralChar node, Object param);
	public Object visit(Cast node, Object param);
	public Object visit(VoidType voidType, Object param);
}
