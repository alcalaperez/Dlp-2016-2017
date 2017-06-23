/**
 * @generated VGen 1.3.3
 */

package ast;

import visitor.*;

//	exprComparadoraNot:expresion -> right:expresion

public class ExprComparadoraNot extends AbstractExpresion {

	public ExprComparadoraNot(Expresion right) {
		this.right = right;

		searchForPositions(right);	// Obtener linea/columna a partir de los hijos
	}

	public ExprComparadoraNot(Object right) {
		this.right = (Expresion) right;

		searchForPositions(right);	// Obtener linea/columna a partir de los hijos
	}

	public Expresion getRight() {
		return right;
	}
	public void setRight(Expresion right) {
		this.right = right;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private Expresion right;
}

