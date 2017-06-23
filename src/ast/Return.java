/**
 * @generated VGen 1.3.3
 */

package ast;

import visitor.*;

//	return:cuerpoMetodo -> expresion:expresion

public class Return extends AbstractCuerpoMetodo {

	public Return(Expresion expresion) {
		this.expresion = expresion;

		searchForPositions(expresion);	// Obtener linea/columna a partir de los hijos
	}

	public Return(Object expresion) {
		this.expresion = (Expresion) expresion;

		searchForPositions(expresion);	// Obtener linea/columna a partir de los hijos
	}

	public Expresion getExpresion() {
		return expresion;
	}
	public void setExpresion(Expresion expresion) {
		this.expresion = expresion;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private Expresion expresion;
	
}

