/**
 * @generated VGen 1.3.3
 */

package ast;

import visitor.Visitor;

//	defVariableLocal:cuerpoMetodo -> nombre:String  tipo:tipo

public class DefVariableLocal extends AbstractCuerpoMetodo {

	public DefVariableLocal(String nombre, Tipo tipo) {
		this.nombre = nombre;
		this.tipo = tipo;

		searchForPositions(tipo);	// Obtener linea/columna a partir de los hijos
	}

	public DefVariableLocal(Object nombre, Object tipo) {
		this.nombre = (nombre instanceof Token) ? ((Token)nombre).getLexeme() : (String) nombre;
		this.tipo = (Tipo) tipo;

		searchForPositions(nombre, tipo);	// Obtener linea/columna a partir de los hijos
	}

	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
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

	private String nombre;
	private Tipo tipo;
	private DefMetodo metodo;

	@Override
	public void setMetodoActual(DefMetodo node) {
		metodo = node;
		
	}

	@Override
	public DefMetodo getMetodoActual() {
		return metodo;
	}
	
	public int getDireccion() {
		return direccion;
	}

	public void setDireccion(int direccion) {
		this.direccion = direccion;
	}

	
	private int direccion;
}

