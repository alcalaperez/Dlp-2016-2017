/**
 * @generated VGen 1.3.3
 */

package ast;

import visitor.*;

//	cast:expresion -> tipo:tipo  valor:expresion

public class Cast extends AbstractExpresion {

	public Cast(Tipo tipo, Expresion valor) {
		this.tipo = tipo;
		this.valor = valor;

		searchForPositions(tipo, valor);	// Obtener linea/columna a partir de los hijos
	}

	public Cast(Object tipo, Object valor) {
		this.tipo = (Tipo) tipo;
		this.valor = (Expresion) valor;

		searchForPositions(tipo, valor);	// Obtener linea/columna a partir de los hijos
	}

	public Tipo getTipo() {
		return tipo;
	}
	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public Expresion getValor() {
		return valor;
	}
	public void setValor(Expresion valor) {
		this.valor = valor;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private Tipo tipo;
	private Expresion valor;
}

