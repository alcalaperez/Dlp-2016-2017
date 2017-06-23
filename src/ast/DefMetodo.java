/**
 * @generated VGen 1.3.3
 */

package ast;

import java.util.*;
import visitor.*;

//	defMetodo:definicion -> nombre:String  parametro:parametro*  tipo:tipo  cuerpo:cuerpoMetodo*

public class DefMetodo extends AbstractDefinicion {

	public DefMetodo(String nombre, List<Param> parametro, Tipo tipo, List<CuerpoMetodo> cuerpo) {
		this.nombre = nombre;
		this.parametro = parametro;
		this.tipo = tipo;
		this.cuerpo = cuerpo;

		searchForPositions(parametro, tipo, cuerpo);	// Obtener linea/columna a partir de los hijos
	}

	@SuppressWarnings("unchecked")
	public DefMetodo(Object nombre, Object parametro, Object tipo, Object cuerpo) {
		this.nombre = (nombre instanceof Token) ? ((Token)nombre).getLexeme() : (String) nombre;
		this.parametro = (List<Param>) parametro;
		this.tipo = (Tipo) tipo;
		this.cuerpo = (List<CuerpoMetodo>) cuerpo;

		searchForPositions(nombre, parametro, tipo, cuerpo);	// Obtener linea/columna a partir de los hijos
	}

	@SuppressWarnings("unchecked")
	public DefMetodo(Object nombre, Object parametro, Object cuerpo) {
		this.nombre = (nombre instanceof Token) ? ((Token)nombre).getLexeme() : (String) nombre;
		this.parametro = (List<Param>) parametro;
		this.tipo = new VoidType();
		this.cuerpo = (List<CuerpoMetodo>) cuerpo;

		searchForPositions(nombre, parametro, cuerpo);	// Obtener linea/columna a partir de los hijos
	}

	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Param> getParametro() {
		return parametro;
	}
	public void setParametro(List<Param> parametro) {
		this.parametro = parametro;
	}

	public Tipo getTipo() {
		return tipo;
	}
	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
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

	private String nombre;
	private List<Param> parametro;
	private Tipo tipo;
	private List<CuerpoMetodo> cuerpo;
}

