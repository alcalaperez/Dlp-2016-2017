package generacionDeCodigo;

import java.util.HashMap;
import java.util.Map;

import ast.ArrayType;
import ast.Campo;
import ast.CuerpoMetodo;
import ast.DefMetodo;
import ast.DefStruct;
import ast.DefVariableGlobal;
import ast.DefVariableLocal;
import ast.Definicion;
import ast.IdentType;
import ast.Param;
import ast.Programa;
import visitor.DefaultVisitor;

/**
 * Clase encargada de asignar direcciones a las variables
 */
public class GestionDeMemoria extends DefaultVisitor {

	int sumaTama�oVariablesGlobales = 0;
	int sumaTama�oVariablesLocales = 0;
	int sumaTama�oParametros = 4;
	Map<String, Integer> estructuras = new HashMap<String, Integer>();
	int indiceActual = 0;

	/*
	 * Poner aqu� los visit necesarios. Si se ha usado VGen solo hay que
	 * copiarlos de la clase 'visitor/_PlantillaParaVisitors.txt'.
	 */

	// public Object visit(Programa prog, Object param) {
	// ...
	// }

	// class Programa { List<Definicion> definiciones; }
	public Object visit(Programa node, Object param) {
		if (node.getDefiniciones() != null) {
			for (Definicion child : node.getDefiniciones()) {
				child.accept(this, param);

				if (child instanceof DefVariableGlobal) {
					((DefVariableGlobal) child).setDireccion(sumaTama�oVariablesGlobales);
					sumaTama�oVariablesGlobales += ((DefVariableGlobal) child).getTipo().getSize();

				}
			}
		}
		return null;
	}
	
	//	class DefMetodo { String nombre;  List<Parametro> parametro;  Tipo tipo;  List<CuerpoMetodo> cuerpo; }
	public Object visit(DefMetodo node, Object param) {

		// super.visit(node, param);

		if (node.getParametro() != null)
			for (int i = node.getParametro().size(); i > 0 ; i--){
				node.getParametro().get(i-1).accept(this, param);
			}

		if (node.getTipo() != null)
			node.getTipo().accept(this, param);

		if (node.getCuerpo() != null)
			for (CuerpoMetodo child : node.getCuerpo())
				child.accept(this, param);

		sumaTama�oVariablesLocales = 0;
		sumaTama�oParametros = 4;
		return null;
	}

	public Object visit(DefStruct node, Object param) {
		int sumaTama�oCamposStruct = 0;

		for (Definicion child : node.getDefiniciones()) {
			child.accept(this, param);
			((Campo) child).setDireccion(sumaTama�oCamposStruct);
			sumaTama�oCamposStruct += ((Campo) child).getTipo().getSize();
		}

		estructuras.put(node.getNombre(), sumaTama�oCamposStruct);

		return null;
	}

	// class ArrayType { int indice; Tipo tipo; }
	public Object visit(ArrayType node, Object param) {

		// super.visit(node, param);

		if (node.getTipo() != null){
			node.getTipo().accept(this, param);
		}
		return null;
	}
	
	//	class Param { String nombre;  Tipo tipo; }
	public Object visit(Param node, Object param) {

		// super.visit(node, param);

		if (node.getTipo() != null)
			node.getTipo().accept(this, param);

		node.setDireccion(sumaTama�oParametros);
		sumaTama�oParametros = sumaTama�oParametros + node.getTipo().getSize();

		return null;
	}
	
	//	class DefVariableLocal { String nombre;  Tipo tipo; }
	public Object visit(DefVariableLocal node, Object param) {
		// super.visit(node, param);
		if (node.getTipo() != null)
			node.getTipo().accept(this, param);
		sumaTama�oVariablesLocales = sumaTama�oVariablesLocales - node.getTipo().getSize();
		node.setDireccion(sumaTama�oVariablesLocales);
		return null;
	}

	// class IdentType { }
	public Object visit(IdentType node, Object param) {
		node.setSize(estructuras.get(node.getObjeto()));
		return null;
	}

}
