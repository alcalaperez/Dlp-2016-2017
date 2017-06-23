/**
 * @generated VGen 1.3.3
 */

package visitor;

import ast.*;
import java.util.*;

/*
DefaultVisitor. Implementación base del visitor para ser derivada por nuevos visitor.
	No modificar esta clase. Para crear nuevos visitor usar el fichero "_PlantillaParaVisitors.txt".
	DefaultVisitor ofrece una implementación por defecto de cada nodo que se limita a visitar los nodos hijos.
*/
public class DefaultVisitor implements Visitor {

	//	class Programa { List<Definicion> definiciones; }
	public Object visit(Programa node, Object param) {
		visitChildren(node.getDefiniciones(), param);
		return null;
	}

	//	class DefStruct { String nombre;  List<Definicion> definiciones; }
	public Object visit(DefStruct node, Object param) {
		visitChildren(node.getDefiniciones(), param);
		return null;
	}

	//	class DefVariableLocal { String nombre;  Tipo tipo; }
	public Object visit(DefVariableLocal node, Object param) {
		if (node.getTipo() != null)
			node.getTipo().accept(this, param);
		return null;
	}

	//	class DefVariableGlobal { String nombre;  Tipo tipo; }
	public Object visit(DefVariableGlobal node, Object param) {
		if (node.getTipo() != null)
			node.getTipo().accept(this, param);
		return null;
	}

	//	class Campo { String nombre;  Tipo tipo; }
	public Object visit(Campo node, Object param) {
		if (node.getTipo() != null)
			node.getTipo().accept(this, param);
		return null;
	}

	//	class DefMetodo { String nombre;  List<Parametro> parametro;  Tipo tipo;  List<CuerpoMetodo> cuerpo; }
	public Object visit(DefMetodo node, Object param) {
		visitChildren(node.getParametro(), param);
		if (node.getTipo() != null)
			node.getTipo().accept(this, param);
		visitChildren(node.getCuerpo(), param);
		return null;
	}

	//	class IntType {  }
	public Object visit(IntType node, Object param) {
		return null;
	}

	//	class FloatType {  }
	public Object visit(FloatType node, Object param) {
		return null;
	}

	//	class IdentType { String objeto; }
	public Object visit(IdentType node, Object param) {
		return null;
	}

	//	class CharType {  }
	public Object visit(CharType node, Object param) {
		return null;
	}

	//	class ArrayType { int indice;  Tipo tipo; }
	public Object visit(ArrayType node, Object param) {
		if (node.getTipo() != null)
			node.getTipo().accept(this, param);
		return null;
	}

	//	class While { Expresion condicion;  List<CuerpoMetodo> cuerpo; }
	public Object visit(While node, Object param) {
		if (node.getCondicion() != null)
			node.getCondicion().accept(this, param);
		visitChildren(node.getCuerpo(), param);
		return null;
	}

	//	class If { Expresion condicion;  List<CuerpoMetodo> cuerpo; }
	public Object visit(If node, Object param) {
		if (node.getCondicion() != null)
			node.getCondicion().accept(this, param);
		visitChildren(node.getCuerpo(), param);
		return null;
	}

	//	class IfElse { Expresion condicion;  List<CuerpoMetodo> cuerpoIf;  List<CuerpoMetodo> cuerpoElse; }
	public Object visit(IfElse node, Object param) {
		if (node.getCondicion() != null)
			node.getCondicion().accept(this, param);
		visitChildren(node.getCuerpoIf(), param);
		visitChildren(node.getCuerpoElse(), param);
		return null;
	}

	//	class Return { Expresion expresion; }
	public Object visit(Return node, Object param) {
		if (node.getExpresion() != null)
			node.getExpresion().accept(this, param);
		return null;
	}

	//	class Asigna { Expresion left;  Expresion right; }
	public Object visit(Asigna node, Object param) {
		if (node.getLeft() != null)
			node.getLeft().accept(this, param);
		if (node.getRight() != null)
			node.getRight().accept(this, param);
		return null;
	}

	//	class Print { Expresion expresionImprimir; }
	public Object visit(Print node, Object param) {
		if (node.getExpresionImprimir() != null)
			node.getExpresionImprimir().accept(this, param);
		return null;
	}

	//	class Read { Expresion expresionLeer; }
	public Object visit(Read node, Object param) {
		if (node.getExpresionLeer() != null)
			node.getExpresionLeer().accept(this, param);
		return null;
	}

	//	class InvocarMetodo { String nombreMetodo;  List<Expresion> expresion; }
	public Object visit(InvocarMetodo node, Object param) {
		visitChildren(node.getExpresion(), param);
		return null;
	}

	//	class Param { String nombre;  Tipo tipo; }
	public Object visit(Param node, Object param) {
		if (node.getTipo() != null)
			node.getTipo().accept(this, param);
		return null;
	}

	//	class ExprAritmetica { Expresion left;  String operador;  Expresion right; }
	public Object visit(ExprAritmetica node, Object param) {
		if (node.getLeft() != null)
			node.getLeft().accept(this, param);
		if (node.getRight() != null)
			node.getRight().accept(this, param);
		return null;
	}

	//	class ExprComparadora { Expresion left;  String operador;  Expresion right; }
	public Object visit(ExprComparadora node, Object param) {
		if (node.getLeft() != null)
			node.getLeft().accept(this, param);
		if (node.getRight() != null)
			node.getRight().accept(this, param);
		return null;
	}

	//	class ExprComparadoraNot { Expresion right; }
	public Object visit(ExprComparadoraNot node, Object param) {
		if (node.getRight() != null)
			node.getRight().accept(this, param);
		return null;
	}

	//	class ExprAccesoObjeto { Expresion left;  String variable; }
	public Object visit(ExprAccesoObjeto node, Object param) {
		if (node.getLeft() != null)
			node.getLeft().accept(this, param);
		return null;
	}

	//	class ExprArray { Expresion left;  Expresion indice; }
	public Object visit(ExprArray node, Object param) {
		if (node.getLeft() != null)
			node.getLeft().accept(this, param);
		if (node.getIndice() != null)
			node.getIndice().accept(this, param);
		return null;
	}

	//	class ExprInvocarMetodo { String metodo;  List<Expresion> expresion; }
	public Object visit(ExprInvocarMetodo node, Object param) {
		visitChildren(node.getExpresion(), param);
		return null;
	}

	//	class Variable { String nombre; }
	public Object visit(Variable node, Object param) {
		return null;
	}

	//	class LiteralInt { String valor; }
	public Object visit(LiteralInt node, Object param) {
		return null;
	}

	//	class LiteralReal { String valor; }
	public Object visit(LiteralReal node, Object param) {
		return null;
	}

	//	class LiteralChar { String valor; }
	public Object visit(LiteralChar node, Object param) {
		return null;
	}

	//	class Cast { Tipo tipo;  Expresion valor; }
	public Object visit(Cast node, Object param) {
		if (node.getTipo() != null)
			node.getTipo().accept(this, param);
		if (node.getValor() != null)
			node.getValor().accept(this, param);
		return null;
	}
	
	// Método auxiliar -----------------------------
	protected void visitChildren(List<? extends AST> children, Object param) {
		if (children != null)
			for (AST child : children)
				child.accept(this, param);
	}

	@Override
	public Object visit(VoidType voidType, Object param) {
		// TODO Auto-generated method stub
		return null;
	}
}
