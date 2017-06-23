/**
 * @generated VGen 1.3.3
 */

package ast;

import visitor.*;

//	read:cuerpoMetodo -> expresionLeer:expresion

public class Read extends AbstractCuerpoMetodo {

	public Read(Expresion expresionLeer) {
		this.expresionLeer = expresionLeer;

		searchForPositions(expresionLeer);	// Obtener linea/columna a partir de los hijos
	}

	public Read(Object expresionLeer) {
		this.expresionLeer = (Expresion) expresionLeer;

		searchForPositions(expresionLeer);	// Obtener linea/columna a partir de los hijos
	}

	public Expresion getExpresionLeer() {
		return expresionLeer;
	}
	public void setExpresionLeer(Expresion expresionLeer) {
		this.expresionLeer = expresionLeer;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private Expresion expresionLeer;
	
}

