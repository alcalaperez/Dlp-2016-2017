/**
 * @generated VGen 1.3.3
 */

package ast;

import java.util.*;
import visitor.*;

//	defStruct:definicion -> nombre:String  definiciones:definicion*

public class DefStruct extends AbstractDefinicion {

	public DefStruct(String nombre, List<Definicion> definiciones) {
		this.nombre = nombre;
		this.definiciones = definiciones;

		searchForPositions(definiciones);	// Obtener linea/columna a partir de los hijos
	}

	@SuppressWarnings("unchecked")
	public DefStruct(Object nombre, Object definiciones) {
		this.nombre = (nombre instanceof Token) ? ((Token)nombre).getLexeme() : (String) nombre;
		this.definiciones = (List<Definicion>) definiciones;

		searchForPositions(nombre, definiciones);	// Obtener linea/columna a partir de los hijos
	}

	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Definicion> getDefiniciones() {
		return definiciones;
	}
	public void setDefiniciones(List<Definicion> definiciones) {
		this.definiciones = definiciones;
	}

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	private String nombre;
	private List<Definicion> definiciones;
	
}

