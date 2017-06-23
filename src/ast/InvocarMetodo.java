/**
 * @generated VGen 1.3.3
 */

package ast;

import java.util.*;
import visitor.*;

//	invocacion:cuerpoMetodo -> nombreMetodo:String  expresion:expresion*

public class InvocarMetodo extends AbstractCuerpoMetodo {

	public InvocarMetodo(String nombreMetodo, List<Expresion> expresion) {
		this.nombreMetodo = nombreMetodo;
		this.expresion = expresion;

		searchForPositions(expresion);	// Obtener linea/columna a partir de los hijos
	}

	@SuppressWarnings("unchecked")
	public InvocarMetodo(Object nombreMetodo, Object expresion) {
		this.nombreMetodo = (nombreMetodo instanceof Token) ? ((Token)nombreMetodo).getLexeme() : (String) nombreMetodo;
		this.expresion = (List<Expresion>) expresion;

		searchForPositions(nombreMetodo, expresion);	// Obtener linea/columna a partir de los hijos
	}

	public String getNombreMetodo() {
		return nombreMetodo;
	}
	public void setNombreMetodo(String nombreMetodo) {
		this.nombreMetodo = nombreMetodo;
	}

	public List<Expresion> getExpresion() {
		return expresion;
	}
	public void setExpresion(List<Expresion> expresion) {
		this.expresion = expresion;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private String nombreMetodo;
	private List<Expresion> expresion;
	
}

