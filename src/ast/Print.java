/**
 * @generated VGen 1.3.3
 */

package ast;

import visitor.*;

//	print:cuerpoMetodo -> expresionImprimir:expresion

public class Print extends AbstractCuerpoMetodo {

	public Print(Expresion expresionImprimir) {
		this.expresionImprimir = expresionImprimir;

		searchForPositions(expresionImprimir);	// Obtener linea/columna a partir de los hijos
	}

	public Print(Object expresionImprimir) {
		this.expresionImprimir = (Expresion) expresionImprimir;

		searchForPositions(expresionImprimir);	// Obtener linea/columna a partir de los hijos
	}

	public Expresion getExpresionImprimir() {
		return expresionImprimir;
	}
	public void setExpresionImprimir(Expresion expresionImprimir) {
		this.expresionImprimir = expresionImprimir;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private Expresion expresionImprimir;
	
}

