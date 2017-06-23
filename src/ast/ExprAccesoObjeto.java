/**
 * @generated VGen 1.3.3
 */

package ast;

import visitor.*;

//	exprAccesoObjeto:expresion -> left:expresion  variable:String

public class ExprAccesoObjeto extends AbstractExpresion {

	public ExprAccesoObjeto(Expresion left, String variable) {
		this.left = left;
		this.variable = variable;

		searchForPositions(left);	// Obtener linea/columna a partir de los hijos
	}

	public ExprAccesoObjeto(Object left, Object variable) {
		this.left = (Expresion) left;
		this.variable = (variable instanceof Token) ? ((Token)variable).getLexeme() : (String) variable;

		searchForPositions(left, variable);	// Obtener linea/columna a partir de los hijos
	}

	public Expresion getLeft() {
		return left;
	}
	public void setLeft(Expresion left) {
		this.left = left;
	}

	public String getVariable() {
		return variable;
	}
	public void setVariable(String variable) {
		this.variable = variable;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private Expresion left;
	private String variable;
}

