/**
 * @generated VGen 1.3.3
 */

package ast;

import java.util.*;
import visitor.*;

//	ifElse:cuerpoMetodo -> condicion:expresion  cuerpoIf:cuerpoMetodo*  cuerpoElse:cuerpoMetodo*

public class IfElse extends AbstractCuerpoMetodo {

	public IfElse(Expresion condicion, List<CuerpoMetodo> cuerpoIf, List<CuerpoMetodo> cuerpoElse) {
		this.condicion = condicion;
		this.cuerpoIf = cuerpoIf;
		this.cuerpoElse = cuerpoElse;

		searchForPositions(condicion, cuerpoIf, cuerpoElse);	// Obtener linea/columna a partir de los hijos
	}

	@SuppressWarnings("unchecked")
	public IfElse(Object condicion, Object cuerpoIf, Object cuerpoElse) {
		this.condicion = (Expresion) condicion;
		this.cuerpoIf = (List<CuerpoMetodo>) cuerpoIf;
		this.cuerpoElse = (List<CuerpoMetodo>) cuerpoElse;

		searchForPositions(condicion, cuerpoIf, cuerpoElse);	// Obtener linea/columna a partir de los hijos
	}

	public Expresion getCondicion() {
		return condicion;
	}
	public void setCondicion(Expresion condicion) {
		this.condicion = condicion;
	}

	public List<CuerpoMetodo> getCuerpoIf() {
		return cuerpoIf;
	}
	public void setCuerpoIf(List<CuerpoMetodo> cuerpoIf) {
		this.cuerpoIf = cuerpoIf;
	}

	public List<CuerpoMetodo> getCuerpoElse() {
		return cuerpoElse;
	}
	public void setCuerpoElse(List<CuerpoMetodo> cuerpoElse) {
		this.cuerpoElse = cuerpoElse;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private Expresion condicion;
	private List<CuerpoMetodo> cuerpoIf;
	private List<CuerpoMetodo> cuerpoElse;
	
}

