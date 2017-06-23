/**
 * @generated VGen 1.3.3
 */

package ast;

import visitor.*;

//	exprArray:expresion -> left:expresion  indice:expresion

public class ExprArray extends AbstractExpresion {

	public ExprArray(Expresion left, Expresion indice) {
		this.left = left;
		this.indice = indice;

		searchForPositions(left, indice);	// Obtener linea/columna a partir de los hijos
	}

	public ExprArray(Object left, Object indice) {
		this.left = (Expresion) left;
		this.indice = (Expresion) indice;

		searchForPositions(left, indice);	// Obtener linea/columna a partir de los hijos
	}

	public Expresion getLeft() {
		return left;
	}
	public void setLeft(Expresion left) {
		this.left = left;
	}

	public Expresion getIndice() {
		return indice;
	}
	public void setIndice(Expresion indice) {
		this.indice = indice;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private Expresion left;
	private Expresion indice;
}

