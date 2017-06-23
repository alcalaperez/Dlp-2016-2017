package semantico;

import java.util.ArrayList;
import java.util.List;

import ast.AST;
import ast.ArrayType;
import ast.Asigna;
import ast.Campo;
import ast.Cast;
import ast.CharType;
import ast.CuerpoMetodo;
import ast.DefMetodo;
import ast.DefStruct;
import ast.DefVariableGlobal;
import ast.DefVariableLocal;
import ast.Definicion;
import ast.ExprAccesoObjeto;
import ast.ExprAritmetica;
import ast.ExprArray;
import ast.ExprComparadora;
import ast.ExprComparadoraNot;
import ast.ExprInvocarMetodo;
import ast.Expresion;
import ast.FloatType;
import ast.IdentType;
import ast.If;
import ast.IfElse;
import ast.IntType;
import ast.InvocarMetodo;
import ast.LiteralChar;
import ast.LiteralInt;
import ast.LiteralReal;
import ast.Param;
import ast.Position;
import ast.Print;
import ast.Read;
import ast.Return;
import ast.Tipo;
import ast.Variable;
import ast.VoidType;
import ast.While;
import main.GestorErrores;
import visitor.DefaultVisitor;

public class ComprobacionDeTipos extends DefaultVisitor {

	public ComprobacionDeTipos(GestorErrores gestor) {
		this.gestorErrores = gestor;
	}

	private List<DefMetodo> funciones = new ArrayList<DefMetodo>();
	private List<DefStruct> estructuras = new ArrayList<DefStruct>();
	private List<DefVariableGlobal> varsG = new ArrayList<DefVariableGlobal>();
	private ContextMap<String, DefVariableLocal> varsL = new ContextMap<String, DefVariableLocal>();
	
	public Object visit(DefMetodo node, Object param) {
		if (node.getTipo() != null) {
			predicado(!node.getTipo().getClass().equals(IdentType.class),
					"El tipo retornado  de una funcion no puede ser un objeto", node.getStart());
			predicado(!node.getTipo().getClass().equals(ArrayType.class),
					"El tipo retornado  de una funcion no puede ser un array", node.getStart());

		}

		funciones.add(node);

		if (node.getTipo() != null)
			node.getTipo().accept(this, param);

		if (node.getParametro() != null) {
			for (Param child : node.getParametro()) {
				child.accept(this, param);
			}
		}

		varsL.set();
		
		if (node.getCuerpo() != null) {
			for (CuerpoMetodo child : node.getCuerpo()) {
				child.setMetodoActual(node);
				child.accept(this, param);
			}
		}

		varsL.reset();
		return null;

	}

	// class DefStruct { String nombre; List<Definicion> definiciones; }
	public Object visit(DefStruct node, Object param) {

		if (node.getDefiniciones() != null)
			for (Definicion child : node.getDefiniciones())
				child.accept(this, param);

		estructuras.add(node);

		return null;
	}

	// class ExprInvocarMetodo { String nombreMetodo; List<Expr> parametros; }
	public Object visit(ExprInvocarMetodo node, Object param) {
		if (node.getExpresion() != null) {
			for (Expresion child : node.getExpresion()) {
				child.accept(this, param);
			}
		}

		DefMetodo mt = existMetodo(node.getMetodo());

		if (mt != null) {
			node.setTipo(mt.getTipo());
			predicado(node.getExpresion().size() == mt.getParametro().size(), "El numero de parametros no coincide",
					node.getStart());
			if (node.getExpresion().size() == mt.getParametro().size()) {
				for (int i = 0; i < node.getExpresion().size(); i++) {
					predicado(node.getExpresion().get(i).getTipo().getClass() == mt.getParametro().get(i).getTipo()
							.getClass(), "El parametro " + i + " no es del tipo correcto", node.getStart());
				}
			}
		}

		return null;
	}

	public Object visit(Param node, Object param) {
		predicado(node.getTipo().getClass() != ArrayType.class, "El parametro no puede ser un array", node.getStart());
		predicado(node.getTipo().getClass() != IdentType.class, "El parametro no puede ser una variable",
				node.getStart());
		varsL.put(node.getNombre(), new DefVariableLocal(node.getNombre(), node.getTipo()));

		return null;
	}

	// class DefinicionVariable { String nombre; Tipo tipo; }
	public Object visit(DefVariableLocal node, Object param) {
		varsL.put(node.getNombre(), node);
		return null;
	}

	// class DefVariableGlobal { String nombre; Tipo tipo; }
	public Object visit(DefVariableGlobal node, Object param) {
		varsG.add(node);
		return null;
	}

	// class InvocarMetodo { String nombreMetodo; List<Expresion> expresion; }
	public Object visit(InvocarMetodo node, Object param) {
		if (node.getExpresion() != null) {
			for (Expresion child : node.getExpresion()) {
				child.accept(this, param);
			}
		}
		DefMetodo mt = existMetodo(node.getNombreMetodo());

		if (mt != null) {
			predicado(node.getExpresion().size() == mt.getParametro().size(), "El numero de parametros no coincide",
					node.getStart());
			if (node.getExpresion().size() == mt.getParametro().size()) {
				for (int i = 0; i < node.getExpresion().size(); i++) {
					predicado(
							node.getExpresion().get(i).getTipo().getClass() == mt.getParametro().get(i).getTipo()
									.getClass(),
							"El parametro " + (i + 1) + " no es del tipo correcto", node.getStart());
				}
			}
		}

		return null;
	}

	// class Return { Expresion expresion; }
	public Object visit(Return node, Object param) {

		if (node.getExpresion() != null)
			node.getExpresion().accept(this, param);

		if (node.getMetodoActual().getTipo() instanceof VoidType) {
			predicado(node.getExpresion() == null, "El return debe estar vacio en funciones void", node.getStart());
		} else if (node.getExpresion() != null) {
			predicado(node.getExpresion().getTipo().getClass() == node.getMetodoActual().getTipo().getClass(),
					"El return debe ser del mismo tipo que el metodo", node.getStart());
		} else {
			predicado(node.getMetodoActual().getTipo() instanceof VoidType,
					"El return debe ser del mismo tipo que el metodo", node.getStart());
		}

		return null;
	}

	// class While { Expresion condicion; List<CuerpoMetodo> cuerpo; }
	public Object visit(While node, Object param) {

		if (node.getCondicion() != null)
			node.getCondicion().accept(this, param);

		predicado(node.getCondicion().getTipo().getClass() == IntType.class,
				"La condicion del while debe ser un entero", node.getStart());

		return null;
	}

	// class If { Expresion condicion; List<CuerpoMetodo> cuerpo; }
	public Object visit(If node, Object param) {

		if (node.getCondicion() != null)
			node.getCondicion().accept(this, param);

		predicado(node.getCondicion().getTipo().getClass() == IntType.class, "La condicion del if debe ser un entero",
				node.getStart());

		return null;
	}

	// class IfElse { Expresion condicion; List<CuerpoMetodo> cuerpoIf;
	// List<CuerpoMetodo> cuerpoElse; }
	public Object visit(IfElse node, Object param) {

		if (node.getCondicion() != null)
			node.getCondicion().accept(this, param);

		predicado(node.getCondicion().getTipo().getClass() == IntType.class, "La condicion del if debe ser un entero",
				node.getStart());

		return null;
	}

	// class Print { CuerpoMetodo cuerpoAImprimir; }
	public Object visit(Print node, Object param) {
		if (node.getExpresionImprimir() != null)
			node.getExpresionImprimir().accept(this, param);

		if (node.getExpresionImprimir().getTipo() != null) {
			predicado(node.getExpresionImprimir().getTipo().getClass() != IdentType.class,
					"La expresion no puede ser un objeto", node.getStart());

			predicado(node.getExpresionImprimir().getTipo().getClass() != VoidType.class,
					"El metodo que se quiere invocar no tiene retorno", node.getStart());
		}

		return null;

	}

	// class Read { CuerpoMetodo cuerpoALeer; }
	public Object visit(Read node, Object param) {
		if (node.getExpresionLeer() != null)
			node.getExpresionLeer().accept(this, param);

		predicado(node.getExpresionLeer().getModificable(), "La expresion debe ser modificable", node.getStart());

		predicado(node.getExpresionLeer().getTipo().getClass() != IdentType.class,
				"La expresion no puede ser un objeto", node.getStart());

		return null;
	}

	// class Asigna { Expresion left; CuerpoMetodo cuerpoAAsignar; }
	public Object visit(Asigna node, Object param) {

		if (node.getLeft() != null)
			node.getLeft().accept(this, param);

		if (node.getRight() != null)
			node.getRight().accept(this, param);

		predicado(node.getLeft().getModificable(), "La parte izquierda debe ser modificable", node.getStart());

		predicado(node.getLeft().getTipo().getClass() == node.getRight().getTipo().getClass(),
				"Las 2 partes de la asignacion deben ser del mismo tipo", node.getStart());

		predicado(node.getLeft().getTipo().getClass() != IdentType.class, "La parte izquierda no puede ser un objeto",
				node.getStart());

		return null;
	}
	
	private boolean isIntOrReal(Tipo t) {
		if(t.getClass() == IntType.class ||
				t.getClass() == FloatType.class) {
			return true;
		}
		return false;
	}

	public Object visit(ExprComparadora node, Object param) {
		node.setModificable(false);

		if (node.getLeft() != null)
			node.getLeft().accept(this, param);

		if (node.getRight() != null)
			node.getRight().accept(this, param);

		if (isOperadorLogico(node.getOperador())) {
			predicado(node.getLeft().getTipo().getClass() == IntType.class, 
					"La parte izquierda debe ser un entero",
					node.getStart());

			predicado(node.getRight().getTipo().getClass() == IntType.class,
					"La parte derecha debe ser un entero",
					node.getStart());
		} else {
			predicado(isIntOrReal(node.getLeft().getTipo()), "La parte izquierda debe ser un entero o real",
					node.getStart());

			predicado(isIntOrReal(node.getRight().getTipo()), "La parte derecha debe ser un entero o real",
					node.getStart());
		}
		
		predicado(node.getRight().getTipo().getClass() == node.getLeft().getTipo().getClass(), 
				"Las 2 partes deben ser del mismo tipo",
				node.getStart());

		node.setTipo(node.getLeft().getTipo());
		return null;
	}

	// class ExprAritmetica { Expresion left; String operador; Expresion right;
	// }
	public Object visit(ExprAritmetica node, Object param) {
		node.setModificable(false);

		if (node.getLeft() != null)
			node.getLeft().accept(this, param);

		if (node.getRight() != null)
			node.getRight().accept(this, param);

		predicado(node.getLeft().getTipo().getClass() == node.getRight().getTipo().getClass(),
				"Las dos partes deben ser del mismo tipo", node.getStart());

		predicado(node.getLeft().getTipo().getClass() != IdentType.class, "La parte izquierda no puede ser un objeto",
				node.getStart());

		predicado(node.getRight().getTipo().getClass() != IdentType.class, "La parte derecha no puede ser un objeto",
				node.getStart());

		node.setTipo(node.getLeft().getTipo());

		return null;
	}

	@Override
	public Object visit(ExprComparadoraNot node, Object param) {
		super.visit(node, param);
		predicado(node.getRight().getTipo().getClass() == IntType.class, "La parte derecha debe ser un entero",
				node.getStart());

		node.setTipo(new IntType());
		return null;
	}

	// class ExprAccesoObjeto { Expresion left; String variable; }
	public Object visit(ExprAccesoObjeto node, Object param) {
		node.setModificable(true);

		if (node.getLeft() != null) {
			node.getLeft().accept(this, param);
		}

		if (node.getLeft().getTipo().getClass() == IdentType.class) {
			Campo accedido = getVariableEstructura(((IdentType) node.getLeft().getTipo()).getObjeto(),
					node.getVariable());
			node.setTipo(accedido.getTipo());

		}

		return null;
	}

	// class ExprAccesoArray { Expresion left; Expresion indice; }
	public Object visit(ExprArray node, Object param) {
		node.setModificable(true);

		if (node.getLeft() != null) {
			node.getLeft().accept(this, param);
		}

		if (node.getIndice() != null)
			node.getIndice().accept(this, param);

		predicado(node.getIndice().getTipo().getClass() == IntType.class,
				"El indice del array debe ser un numero entero", node.getStart());

		if (node.getLeft().getTipo() != null) {
			predicado(node.getLeft().getTipo().getClass() == ArrayType.class, "El tipo de la variable no es un array",
					node.getStart());
			if(node.getLeft().getTipo().getClass() == ArrayType.class) {
				node.setTipo(((ArrayType) node.getLeft().getTipo()).getTipo());
			}
		}

		return null;
	}

	// class Cast { Tipo tipo; Expresion valor; }
	public Object visit(Cast node, Object param) {
		node.setModificable(false);
		super.visit(node, param);

		if (node.getTipo() != null)
			node.getTipo().accept(this, param);

		predicado(node.getTipo().getClass() != IdentType.class, "El tipo a castear no puede ser un objeto",
				node.getStart());

		predicado(node.getValor().getTipo().getClass() != IdentType.class,
				"La expresion a castear no puede ser un objeto", node.getStart());

		predicado(node.getValor().getTipo().getClass() != node.getTipo().getClass(),
				"La expresion a castear y el tipo casteado no puede ser el mismo", node.getStart());

		node.setTipo(node.getTipo());

		return null;
	}

	// class Variable { String nombre; }
	public Object visit(Variable node, Object param) {
		node.setTipo(new IdentType(node.getNombre()));
		node.setModificable(true);

		node.setTipo(comprobarTipoAsociadoAVariable(node));
		return null;
	}

	// class LiteralInt { String valor; }
	public Object visit(LiteralInt node, Object param) {
		node.setTipo(new IntType());
		node.setModificable(false);

		return null;
	}

	// class LiteralReal { String valor; }
	public Object visit(LiteralReal node, Object param) {
		node.setTipo(new FloatType());
		node.setModificable(false);

		return null;
	}

	// class LiteralChar { Expresion valor; }
	public Object visit(LiteralChar node, Object param) {
		node.setTipo(new CharType());
		node.setModificable(false);

		return null;
	}

	/**
	 * Método auxiliar opcional para ayudar a implementar los predicados de la
	 * Gramática Atribuida.
	 * 
	 * Ejemplo de uso (suponiendo implementado el método "esPrimitivo"):
	 * predicado(esPrimitivo(expr.tipo),
	 * "La expresión debe ser de un tipo primitivo", expr.getStart());
	 * predicado(esPrimitivo(expr.tipo),
	 * "La expresión debe ser de un tipo primitivo", null);
	 * 
	 * NOTA: El método getStart() indica la linea/columna del fichero fuente de
	 * donde se leyó el nodo. Si se usa VGen dicho método será generado en todos
	 * los nodos AST. Si no se quiere usar getStart() se puede pasar null.
	 * 
	 * @param condicion
	 *            Debe cumplirse para que no se produzca un error
	 * @param mensajeError
	 *            Se imprime si no se cumple la condición
	 * @param posicionError
	 *            Fila y columna del fichero donde se ha producido el error. Es
	 *            opcional (acepta null)
	 */
	private void predicado(boolean condicion, String mensajeError, Position posicionError) {
		if (!condicion)
			gestorErrores.error("Comprobación de tipos", mensajeError, posicionError);
	}

	private Tipo comprobarTipoAsociadoAVariable(Variable var) {
		AST defVariable = var.getVariableAsociada();
		if (defVariable instanceof Param) {
			return ((Param) defVariable).getTipo();
		} else if (defVariable instanceof DefVariableLocal) {
			return ((DefVariableLocal) defVariable).getTipo();
		} else {
			return ((DefVariableGlobal) defVariable).getTipo();
		}
	}

	private DefMetodo existMetodo(String nombreMetodo) {
		for (DefMetodo func : funciones) {
			if (func.getNombre().equals(nombreMetodo)) {
				return func;
			}
		}
		return null;
	}


	private boolean isOperadorLogico(String operador) {
		if (operador.equals("AND") || operador.equals("OR") || operador.equals("!")) {
			return true;
		}
		return false;
	}
	
	private Campo getVariableEstructura(String nombreEstructura, String nombreVariable) {
		Campo tip = null;
		for (DefStruct func : estructuras) {
			if (func.getNombre().equals(nombreEstructura)) {
				for (int i = 0; i < func.getDefiniciones().size(); i++) {
					if (((Campo) func.getDefiniciones().get(i)).getNombre().equals(nombreVariable)) {
						tip = (Campo) func.getDefiniciones().get(i);
					}
				}
			}
		}
		return tip;
	}

	private GestorErrores gestorErrores;
}
