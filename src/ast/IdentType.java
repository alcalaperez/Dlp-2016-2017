/**
 * @generated VGen 1.3.3
 */

package ast;

import visitor.*;

//	identType:tipo -> objeto:String

public class IdentType extends AbstractTipo {

	public IdentType(String objeto) {
		this.objeto = objeto;
	}

	public IdentType(Object objeto) {
		this.objeto = (objeto instanceof Token) ? ((Token)objeto).getLexeme() : (String) objeto;

		searchForPositions(objeto);	// Obtener linea/columna a partir de los hijos
	}

	public String getObjeto() {
		return objeto;
	}
	public void setObjeto(String objeto) {
		this.objeto = objeto;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}
	
	@Override
	public int getSize() {
		return size;
	}
	
	public void setSize(int size) {
		this.size=size;
	}
	
	private int size;
	private String objeto;
	private char sufijo;
	private String nombreMapl;

	@Override
	public char getSufijo() {
		return sufijo;
	}
	
	public void setSufijo(char sufijo) {
		this.sufijo = sufijo;
	}

	@Override
	public String getNombreMAPL() {
		return nombreMapl;
	}
	
	public void setMapl(String nombreMapl) {
		this.nombreMapl = nombreMapl;
	}

	
}

