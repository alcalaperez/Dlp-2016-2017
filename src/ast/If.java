/**
 * @generated VGen 1.3.3
 */

package ast;

import java.util.*;
import visitor.*;

//	if:cuerpoMetodo -> condicion:expresion  cuerpo:cuerpoMetodo*

public class If extends AbstractCuerpoMetodo {

	public If(Expresion condicion, List<CuerpoMetodo> cuerpo) {
		this.condicion = condicion;
		this.cuerpo = cuerpo;

		searchForPositions(condicion, cuerpo);	// Obtener linea/columna a partir de los hijos
	}

	@SuppressWarnings("unchecked")
	public If(Object condicion, Object cuerpo) {
		this.condicion = (Expresion) condicion;
		this.cuerpo = (List<CuerpoMetodo>) cuerpo;

		searchForPositions(condicion, cuerpo);	// Obtener linea/columna a partir de los hijos
	}

	public Expresion getCondicion() {
		return condicion;
	}
	public void setCondicion(Expresion condicion) {
		this.condicion = condicion;
	}

	public List<CuerpoMetodo> getCuerpo() {
		return cuerpo;
	}
	public void setCuerpo(List<CuerpoMetodo> cuerpo) {
		this.cuerpo = cuerpo;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private Expresion condicion;
	private List<CuerpoMetodo> cuerpo;
	
}

