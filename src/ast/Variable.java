/**
 * @generated VGen 1.3.3
 */

package ast;

import visitor.*;

//	variable:expresion -> nombre:String

public class Variable extends AbstractExpresion {

	public Variable(String nombre) {
		this.nombre = nombre;
	}

	public Variable(Object nombre) {
		this.nombre = (nombre instanceof Token) ? ((Token)nombre).getLexeme() : (String) nombre;

		searchForPositions(nombre);	// Obtener linea/columna a partir de los hijos
	}

	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	public CuerpoMetodo getLocal() {
		return local;
	}

	public void setLocal(CuerpoMetodo varLocal) {
		this.local = varLocal;
	}

	public DefVariableGlobal getGlobal() {
		return global;
	}

	public void setGlobal(DefVariableGlobal global) {
		this.global = global;
	}

	public Param getParam() {
		return param;
	}

	public void setParam(Param param) {
		this.param = param;
	}
	
	public AST getVariableAsociada() {
		if(param != null) {
			return param;
		} else if(local != null) {
			return local;
		} else {
			return global;
		}
	}


	private String nombre;
    private CuerpoMetodo local;
    private DefVariableGlobal global;
    private Param param;

}

