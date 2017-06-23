package semantico;

import ast.ArrayType;
import ast.Campo;
import ast.CuerpoMetodo;
import ast.DefMetodo;
import ast.DefStruct;
import ast.DefVariableGlobal;
import ast.DefVariableLocal;
import ast.Definicion;
import ast.ExprInvocarMetodo;
import ast.Expresion;
import ast.IdentType;
import ast.InvocarMetodo;
import ast.Param;
import ast.Position;
import ast.Variable;
import main.GestorErrores;
import visitor.DefaultVisitor;


public class Identificacion extends DefaultVisitor {

	public Identificacion(GestorErrores gestor) {
		this.gestorErrores = gestor;
	}


	
	//	class DefVariableLocal { String nombre;  Tipo tipo; }
	public Object visit(DefVariableLocal node, Object param) {
		CuerpoMetodo definicion = variablesLocales.getFromTop(node.getNombre());
		Param params = parametros.getFromTop(node.getNombre());
		predicado(definicion == null, "Variable ya definida: " + node.getNombre(), node.getStart());
		predicado(params == null, "Variable ya definida en un parametro: " + node.getNombre(), node.getStart());
		variablesLocales.put(node.getNombre(), node);
		return null;
	}
	
	//	class DefVariableGlobal { String nombre;  Tipo tipo; }
	public Object visit(DefVariableGlobal node, Object param) {
		Definicion definicion = variablesGlobales.getFromTop(node.getNombre());
		predicado(definicion == null, "Variable ya definida: " + node.getNombre(), node.getStart());
		
		if(node.getTipo().getClass() == IdentType.class){
			Definicion estructura = estructuras.getFromTop(((IdentType) node.getTipo()).getObjeto());
			predicado(estructura != null, "Estructura no definida: " + ((IdentType) node.getTipo()).getObjeto(), node.getStart());
		}
		
		if(node.getTipo().getClass() == ArrayType.class){
			if(((ArrayType)node.getTipo()).getTipo().getClass() == IdentType.class){				
				String nombreObjeto = ((IdentType)((ArrayType)node.getTipo()).getTipo()).getObjeto();
				Definicion estructura = estructuras.getFromAny(nombreObjeto);
				predicado(estructura != null, "Estructura no definida: " + nombreObjeto, node.getStart());
			}
		}

		variablesGlobales.put(node.getNombre(), node);
		
		return null;
	}

	
	//	class DefStruct { String nombre;  List<Definicion> definiciones; }
	public Object visit(DefStruct node, Object param) {
		Definicion definicion = estructuras.getFromAny(node.getNombre());

		predicado(definicion == null, "Estructura ya definida: " + node.getNombre(), node.getStart());
		estructuras.put(node.getNombre(), node);

		campos.set();
		if (node.getDefiniciones() != null)
			for (Definicion child : node.getDefiniciones())
				child.accept(this, param);

		campos.reset();
		return null;
	}

	//	class DefMetodo { String nombre;  List<Parametro> parametro;  Tipo tipo;  List<CuerpoMetodo> cuerpo; }
	public Object visit(DefMetodo node, Object param) {
		Definicion definicionLocal = funciones.getFromTop(node.getNombre());
		predicado(definicionLocal == null, "Funcion ya definida: " + node.getNombre(), node.getStart());
		funciones.put(node.getNombre(), node);
		variablesLocales.set();
		parametros.set();

		if (node.getParametro() != null)
			for (Param child : node.getParametro())
				child.accept(this, param);

		if (node.getTipo() != null)
			node.getTipo().accept(this, param);

		if (node.getCuerpo() != null)
			for (CuerpoMetodo child : node.getCuerpo())
				child.accept(this, param);
		
		parametros.reset();
		variablesLocales.reset();
		return null;
	}
	
	//	class Variable { String nombre; }
	public Object visit(Variable node, Object param) {
		DefVariableLocal varLocal = variablesLocales.getFromTop(node.getNombre());
		DefVariableGlobal varGlobal = variablesGlobales.getFromAny(node.getNombre());
		Param params = parametros.getFromAny(node.getNombre());

		node.setLocal(varLocal);
		node.setGlobal(varGlobal);
		node.setParam(params);

		predicado(varLocal != null || varGlobal != null || params != null, "Variable no definida: " + node.getNombre(), node.getStart());
		return null;
	}
	

	//	class ExprInvocarMetodo { String metodo;  List<Expresion> expresion; }
	public Object visit(InvocarMetodo node, Object param) {
		if (node.getExpresion() != null)
			for (Expresion child : node.getExpresion())
				child.accept(this, param);
		
		
		// super.visit(node, param);
		Definicion definicionGlobal = funciones.getFromAny(node.getNombreMetodo());
		predicado(definicionGlobal != null, "Procedimiento no definido: " + node.getNombreMetodo(), node.getStart());

		return null;
	}
	
	
	//	class ExprInvocarMetodo { String metodo;  List<Expresion> expresion; }
	public Object visit(ExprInvocarMetodo node, Object param) {
		if (node.getExpresion() != null)
			for (Expresion child : node.getExpresion())
				child.accept(this, param);
		
		// super.visit(node, param);
		Definicion definicionGlobal = funciones.getFromAny(node.getMetodo());
		predicado(definicionGlobal != null, "Funcion no definida: " + node.getMetodo(), node.getStart());

		return null;
	}
	
	//	class Param { String nombre;  Tipo tipo; }
	public Object visit(Param node, Object param) {
		Param parametro = parametros.getFromAny(node.getNombre());
		predicado(parametro == null, "Parametro repetido: " + node.getNombre(), node.getStart());
		parametros.put(node.getNombre(), node);

		return null;
	}

	//	class Campo { String nombre;  Tipo tipo; }
	public Object visit(Campo node, Object param) {
		Campo campo = campos.getFromAny(node.getNombre());
		predicado(campo == null, "Campo repetido: " + node.getNombre(), node.getStart());
		campos.put(node.getNombre(), node);
		
		return null;
	}
	
	/**
	 * Método auxiliar opcional para ayudar a implementar los predicados de la Gramática Atribuida.
	 * 
	 * Ejemplo de uso:
	 * 	predicado(variables.get(nombre), "La variable no ha sido definida", expr.getStart());
	 * 	predicado(variables.get(nombre), "La variable no ha sido definida", null);
	 * 
	 * NOTA: El método getStart() indica la linea/columna del fichero fuente de donde se leyó el nodo.
	 * Si se usa VGen dicho método será generado en todos los nodos AST. Si no se quiere usar getStart() se puede pasar null.
	 * 
	 * @param condicion Debe cumplirse para que no se produzca un error
	 * @param mensajeError Se imprime si no se cumple la condición
	 * @param posicionError Fila y columna del fichero donde se ha producido el error. Es opcional (acepta null)
	 */
	private void predicado(boolean condicion, String mensajeError, Position posicionError) {
		if (!condicion)
			gestorErrores.error("Identificación", mensajeError, posicionError);
	}


	private GestorErrores gestorErrores;
	ContextMap<String, DefVariableGlobal> variablesGlobales = new ContextMap<String, DefVariableGlobal>();
	ContextMap<String, DefVariableLocal> variablesLocales= new ContextMap<String, DefVariableLocal>();
	ContextMap<String, DefMetodo> funciones = new ContextMap<String, DefMetodo>();
	ContextMap<String, DefStruct> estructuras = new ContextMap<String, DefStruct>();
	ContextMap<String, Param> parametros = new ContextMap<String, Param>();
	ContextMap<String, Campo> campos = new ContextMap<String, Campo>();

}
