/**
 * @generated VGen 1.3.3
 */

package ast;

import visitor.*;

//	arrayType:tipo -> indice:int  tipo:tipo

public class ArrayType extends AbstractTipo {

	public ArrayType(int indice, Tipo tipo) {
		this.indice = indice;
		this.tipo = tipo;

		searchForPositions(tipo);	// Obtener linea/columna a partir de los hijos
	}

	public ArrayType(Object indice, Object tipo) {
		this.indice = (indice instanceof Token) ? Integer.parseInt(((Token)indice).getLexeme()) : (Integer) indice;
		this.tipo = (Tipo) tipo;

		searchForPositions(indice, tipo);	// Obtener linea/columna a partir de los hijos
	}

	public int getIndice() {
		return indice;
	}
	public void setIndice(int indice) {
		this.indice = indice;
	}

	public Tipo getTipo() {
		return tipo;
	}
	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}
	

	public int getSize() {
		return indice*tipo.getSize();
	}

	@Override
	public char getSufijo() {		
		return tipo.getSufijo();
	}

	@Override
	public String getNombreMAPL() {
		return tipo.getNombreMAPL();
	}

	private int indice;
	private Tipo tipo;
}

