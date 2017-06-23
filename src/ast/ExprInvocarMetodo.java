/**
 * @generated VGen 1.3.3
 */

package ast;

import java.util.*;
import visitor.*;

//	exprInvocarMetodo:expresion -> metodo:String  expresion:expresion*

public class ExprInvocarMetodo extends AbstractExpresion {

	public ExprInvocarMetodo(String metodo, List<Expresion> expresion) {
		this.metodo = metodo;
		this.expresion = expresion;

		searchForPositions(expresion);	// Obtener linea/columna a partir de los hijos
	}

	@SuppressWarnings("unchecked")
	public ExprInvocarMetodo(Object metodo, Object expresion) {
		this.metodo = (metodo instanceof Token) ? ((Token)metodo).getLexeme() : (String) metodo;
		this.expresion = (List<Expresion>) expresion;

		searchForPositions(metodo, expresion);	// Obtener linea/columna a partir de los hijos
	}

	public String getMetodo() {
		return metodo;
	}
	public void setMetodo(String metodo) {
		this.metodo = metodo;
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

	private String metodo;
	private List<Expresion> expresion;
}

