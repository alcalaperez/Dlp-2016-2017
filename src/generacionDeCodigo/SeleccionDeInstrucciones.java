package generacionDeCodigo;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

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
import ast.Print;
import ast.Programa;
import ast.Read;
import ast.Return;
import ast.Tipo;
import ast.Variable;
import ast.VoidType;
import ast.While;
import semantico.ContextMap;
import visitor.DefaultVisitor;

public class SeleccionDeInstrucciones extends DefaultVisitor {
	// Si se hace return vacio no se generara ret automaticamente al acabar el
	// main
	private boolean generadoRetorno = false;
	private int numIf = 0;
	private int numIfElse = 0;
	private int numWhile = 0;
	private int numVarLocal = 0;

	enum Funcion {
		DIRECCION, VALOR
	}

	public SeleccionDeInstrucciones(Writer writer, String sourceFile) {
		this.writer = new PrintWriter(writer);
		this.sourceFile = sourceFile;

		instruccion.put("*", "mul");
		instruccion.put("+", "add");
		instruccion.put("-", "sub");
		instruccion.put("/", "div");
		instruccion.put("!", "not");
		instruccion.put("AND", "and");
		instruccion.put("OR", "or");
		instruccion.put("NOTEQUALS", "ne");
		instruccion.put(">", "gt");
		instruccion.put("<", "lt");
		instruccion.put("MAYORIGUAL", "ge");
		instruccion.put("MENORIGUAL", "le");
		instruccion.put("EQUALS", "eq");

	}

	// class Programa { List<Definicion> definiciones; }
	public Object visit(Programa node, Object param) {

		genera("#source \"" + sourceFile + "\"");
		genera("CALL main");
		genera("HALT");

		if (node.getDefiniciones() != null)
			for (Definicion child : node.getDefiniciones())
				child.accept(this, param);

		return null;
	}

	// class DefMetodo { String nombre; List<Parametro> parametro; Tipo tipo;
	// List<CuerpoMetodo> cuerpo; }
	public Object visit(DefMetodo node, Object param) {
		genera(node.getNombre() + ":");

		varsL.set();
		params.set();

		genera("enter " + obtenerTamañoVariablesLocalesPorMetodo(node));
		if (node.getParametro() != null)
			for (Param child : node.getParametro())
				child.accept(this, param);

		if (node.getTipo() != null)
			node.getTipo().accept(this, param);

		if (node.getCuerpo() != null) {
			for (CuerpoMetodo child : node.getCuerpo()) {
				child.setMetodoActual(node);
				child.accept(this, param);
			}
		}

		if (node.getTipo().getClass() == VoidType.class && !generadoRetorno) {
			genera("ret " + 0 + ", " + tamañoLocalesActual + ", " + tamañoParamActual);
		}

		varsL.reset();
		params.reset();
		tamañoLocalesActual = 0;
		tamañoParamActual = 0;
		generadoRetorno = false;

		genera("\n");
		return null;
	}

	public Object visit(DefStruct node, Object param) {
		genera("#type " + node.getNombre() + ": {");

		for (Definicion child : node.getDefiniciones()) {
			genera("\t" + ((Campo) child).getNombre() + ":" + ((Campo) child).getTipo().getNombreMAPL());
		}

		genera("}");

		structs.put(node.getNombre(), node);
		return null;
	}

	// class DefinicionVariable { String nombre; Tipo tipo; }
	public Object visit(DefVariableLocal node, Object param) {
		if (node.getTipo().getClass() == IdentType.class) {
			genera("#VAR " + node.getNombre() + numVarLocal + ":" + ((IdentType) node.getTipo()).getObjeto() );
			numVarLocal++;
		} else if (node.getTipo().getClass() == ArrayType.class) {
			genera("#VAR " + node.getNombre() + numVarLocal + ":" + ((ArrayType) node.getTipo()).getIndice() + "*" + 
					((IdentType)((ArrayType) node.getTipo()).getTipo()).getObjeto());
			numVarLocal++;
		} else {
			genera("#VAR " + node.getNombre() + numVarLocal + ":" + node.getTipo().getNombreMAPL());
			numVarLocal++;
		}

		varsL.put(node.getNombre(), node);
		tamañoLocalesActual += node.getTipo().getSize();
		return null;
	}

	// class DefVariableGlobal { String nombre; Tipo tipo; }
	public Object visit(DefVariableGlobal node, Object param) {
		if (node.getTipo().getClass() == IdentType.class) {
			genera("#VAR " + node.getNombre() + ":" + ((IdentType) node.getTipo()).getObjeto());
		} else if (node.getTipo().getClass() == ArrayType.class) {
			genera("#VAR " + node.getNombre() + ":" + ((ArrayType) node.getTipo()).getIndice() + "*" + 
		((IdentType)((ArrayType) node.getTipo()).getTipo()).getObjeto());

		} else {
			genera("#VAR " + node.getNombre() + ":" + node.getTipo().getNombreMAPL());
		}
		varsG.put(node.getNombre(), node);

		return null;
	}

	// class Asigna { Expresion left; CuerpoMetodo cuerpoAAsignar; }
	public Object visit(Asigna node, Object param) {
		genera("#line " + node.getEnd().getLine());
		
		if (node.getLeft() != null) {
			node.getLeft().accept(this, Funcion.DIRECCION);
		}
		
		if (node.getRight() != null) {
			node.getRight().accept(this, Funcion.VALOR);
		}

		genera("store", node.getLeft().getTipo());

		return null;
	}

	// class While { Expresion condicion; List<CuerpoMetodo> cuerpo; }
	public Object visit(While node, Object param) {

		int numActualizadoWhiles = numWhile++; 
		genera("#line " + node.getEnd().getLine());
		genera("loop" + numActualizadoWhiles + ":");

		if (node.getCondicion() != null)
			node.getCondicion().accept(this, Funcion.VALOR);

		genera("jz finWhile" + numActualizadoWhiles);
		
		if (node.getCuerpo() != null)
			for (CuerpoMetodo child : node.getCuerpo())
				child.accept(this, param);
		
		genera("jmp loop" + numActualizadoWhiles + ":");

		genera("finWhile" + numActualizadoWhiles + ":");
		genera("\n");
		numWhile++;

		return null;
	}

	// class If { Expresion condicion; List<CuerpoMetodo> cuerpo; }
	public Object visit(If node, Object param) {
		genera("\n#line " + node.getEnd().getLine());

		int numActualizadoIfs = numIf++; 

		if (node.getCondicion() != null)
			node.getCondicion().accept(this, Funcion.VALOR);
		genera("jz finIf" + numActualizadoIfs);
		if (node.getCuerpo() != null)
			for (CuerpoMetodo child : node.getCuerpo())
				child.accept(this, param);
		genera("finIf"+ numActualizadoIfs + ":");
		genera("\n");

		return null;
	}

	// class IfElse { Expresion condicion; List<CuerpoMetodo> cuerpoIf;
	// List<CuerpoMetodo> cuerpoElse; }
	public Object visit(IfElse node, Object param) {
		int numActualizaoElses = numIfElse++; 
		
		// super.visit(node, param);
		genera("\n#line " + node.getEnd().getLine());

		if (node.getCondicion() != null)
			node.getCondicion().accept(this, Funcion.VALOR);
		genera("jz else1" + numActualizaoElses);

		if (node.getCuerpoIf() != null) {
			for (CuerpoMetodo child : node.getCuerpoIf()) {
				child.accept(this, param);
			}
		}

		genera("jmp finIfElse" + numActualizaoElses);
		genera("else1" + numActualizaoElses + ":");
		
		if (node.getCuerpoElse() != null)
			for (CuerpoMetodo child : node.getCuerpoElse())
				child.accept(this, param);
		
		genera("finIfElse" + numActualizaoElses + ":");
		genera("\n");
		return null;
	}

	// class Cast { Tipo tipo; Expresion valor; }
	public Object visit(Cast node, Object param) {

		// super.visit(node, param);
		if (node.getTipo() != null)
			node.getTipo().accept(this, param);

		if (node.getValor() != null)
			node.getValor().accept(this, Funcion.VALOR);

		genera(node.getValor().getTipo().getSufijo() + "2" + node.getTipo().getSufijo());

		node.setTipo(node.getTipo());

		return null;
	}

	// class Print { CuerpoMetodo cuerpoAImprimir; }
	public Object visit(Print node, Object param) {
		genera("#line " + node.getEnd().getLine());

		// super.visit(node, param);

		if (node.getExpresionImprimir() != null)
			node.getExpresionImprimir().accept(this, Funcion.VALOR);

		genera("out", node.getExpresionImprimir().getTipo());

		return null;
	}

	// class Read { CuerpoMetodo cuerpoALeer; }
	public Object visit(Read node, Object param) {

		// super.visit(node, param);
		genera("#line " + node.getEnd().getLine());

		if (node.getExpresionLeer() != null)
			node.getExpresionLeer().accept(this, param);

		genera("in" , node.getExpresionLeer().getTipo());
		genera("store", node.getExpresionLeer().getTipo());

		return null;
	}

	// class ExprArray { Expresion left; Expresion indice; }
	public Object visit(ExprArray node, Object param) {

		if (param == Funcion.VALOR) {
			visit(node, Funcion.DIRECCION);
			genera("load", node.getTipo());
		} else {
			node.getLeft().accept(this, Funcion.DIRECCION);
			node.getIndice().accept(this, Funcion.VALOR);

			
			genera("push " + (((ArrayType)node.getLeft().getTipo()).getTipo().getSize()));
			genera("mul");
			genera("add");
		}
		
		if (node.getLeft().getTipo() != null) {
			if (node.getLeft().getTipo().getClass() == ArrayType.class) {
				node.setTipo(((ArrayType) node.getLeft().getTipo()).getTipo());
			}
		}

		return null;
	}

	// class ExprLlamadaMetodo { String metodo; List<Expresion> expresion; }
	public Object visit(ExprInvocarMetodo node, Object param) {
		visitChildren(node.getExpresion(), param);
		genera("call " + node.getMetodo());
		return null;
	}

	// class ExprLlamadaMetodo { String metodo; List<Expresion> expresion; }
	public Object visit(InvocarMetodo node, Object param) {
		visitChildren(node.getExpresion(), Funcion.VALOR);
		genera("call " + node.getNombreMetodo());
		if (node.getMetodoActual().getTipo().getClass() != VoidType.class) {
			genera("pop ", node.getMetodoActual().getTipo());

		}
		return null;
	}

	// class Return { Expresion expresion; }
	public Object visit(Return node, Object param) {
		if (node.getExpresion() != null)
			node.getExpresion().accept(this, Funcion.VALOR);

		if (node.getMetodoActual() != null) {
			if (node.getMetodoActual().getTipo() != null) {
				genera("ret " + node.getMetodoActual().getTipo().getSize() + ", " + tamañoLocalesActual + ", "
						+ tamañoParamActual);

			} else {
				genera("ret " + 0 + ", " + tamañoLocalesActual + ", " + tamañoParamActual);
				generadoRetorno = true;
			}
		}
		return null;
	}

	// class ExprAritmetica { Expresion left; String operador; Expresion right;
	// }
	public Object visit(ExprAritmetica node, Object param) {

		// super.visit(node, param);
		assert (param == Funcion.VALOR);

		if (node.getLeft() != null)
			node.getLeft().accept(this, Funcion.VALOR);

		if (node.getRight() != null)
			node.getRight().accept(this, Funcion.VALOR);

		node.setTipo(node.getLeft().getTipo());
		genera(instruccion.get(node.getOperador()), node.getTipo());

		return null;
	}

	// class ExprAccesoObjeto { Expresion left; String variable; }
	public Object visit(ExprAccesoObjeto node, Object param) {

		if (param == Funcion.DIRECCION) {
			if (node.getLeft() != null) {
				node.getLeft().accept(this, Funcion.DIRECCION);
			}

			if (node.getLeft().getTipo().getClass() == IdentType.class) {
				Campo accedido = getVariableEstructura(((IdentType) node.getLeft().getTipo()).getObjeto(),
						node.getVariable());
				node.setTipo(accedido.getTipo());

			}

			DefStruct d = structs.getFromAny(((IdentType) node.getLeft().getTipo()).getObjeto());
			int tamañoTotalHastaCampo = 0;

			for (int i = 0; i < d.getDefiniciones().size(); i++) {
				if (((Campo) d.getDefiniciones().get(i)).getNombre().equals(node.getVariable())) {
					break;
				} else {
					tamañoTotalHastaCampo += ((Campo) d.getDefiniciones().get(i)).getTipo().getSize();
				}
			}

			genera("push " + tamañoTotalHastaCampo);
			genera("add");
		} else {
			visit(node, Funcion.DIRECCION);
			genera("load", node.getTipo());
		}

		return null;
	}

	// class ExprComparadora { Expresion left; String operador; Expresion right;
	// }
	public Object visit(ExprComparadora node, Object param) {

		assert (param == Funcion.VALOR);

		if (node.getLeft() != null)
			node.getLeft().accept(this, Funcion.VALOR);

		if (node.getRight() != null)
			node.getRight().accept(this, Funcion.VALOR);

		if (isOperadorLogico(node.getOperador())) {
			genera(instruccion.get(node.getOperador()));
			node.setTipo(new IntType());

		} else {
			genera(instruccion.get(node.getOperador()), node.getLeft().getTipo());
			node.setTipo(node.getLeft().getTipo());

		}

		return null;
	}
	
	//	class ExprComparadoraNot { Expresion right; }
	public Object visit(ExprComparadoraNot node, Object param) {
		if (node.getRight() != null)
			node.getRight().accept(this, Funcion.VALOR);

		genera(instruccion.get("!"));
		return null;
	}

	// class Variable { String nombre; }
	public Object visit(Variable node, Object param) {
		DefVariableGlobal dg = comporbarExisteVaribleGlobal(node.getNombre());
		DefVariableLocal dl = comporbarExisteVaribleLocal(node.getNombre());
		Param p = comporbarExisteParametro(node.getNombre());

		if (((Funcion) param) == Funcion.VALOR) {
			visit(node, Funcion.DIRECCION);
			if (p != null) {
				genera("load", p.getTipo());
				node.setTipo(p.getTipo());

			} else if (dl != null) {
				genera("load", dl.getTipo());
				node.setTipo(dl.getTipo());

			} else if (dg != null) {
				genera("load", dg.getTipo());
				node.setTipo(dg.getTipo());

			}
		} else { // Funcion.DIRECCION
			assert (param == Funcion.DIRECCION);
			if (p != null) {
				genera("push BP");
				genera("push " + p.getDireccion());
				genera("add");
				node.setTipo(p.getTipo());

			} else if (dl != null) {
				genera("push BP");
				genera("push " + dl.getDireccion());
				genera("add");
				node.setTipo(dl.getTipo());

			} else if (dg != null) {
				genera("pusha " + dg.getDireccion());
				node.setTipo(dg.getTipo());

			}
		}
		return null;
	}

	// class Param { String nombre; Tipo tipo; }
	public Object visit(Param node, Object param) {
		params.put(node.getNombre(), node);
		tamañoParamActual += node.getTipo().getSize();
		return null;
	}

	// class LiteralInt { String valor; }
	public Object visit(LiteralInt node, Object param) {
		assert (param == Funcion.VALOR);
		genera("push " + node.getValor());
		node.setTipo(new IntType());

		return null;
	}

	// class LiteralReal { String valor; }
	public Object visit(LiteralReal node, Object param) {
		assert (param == Funcion.VALOR);
		genera("pushf " + node.getValor());
		node.setTipo(new FloatType());

		return null;
	}

	// class LiteralChar { Expresion valor; }
	public Object visit(LiteralChar node, Object param) {
		assert (param == Funcion.VALOR);
		int caracter = stringToChar(node.getValor());

		genera("pushb " +  (int) caracter);
		node.setTipo(new CharType());

		return null;
	}

	private Map<String, String> instruccion = new HashMap<String, String>();

	// Método auxiliar recomendado -------------
	private void genera(String instruccion) {
		writer.println(instruccion);
	}

	private void genera(String instruccion, Tipo tipo) {
		genera(instruccion + tipo.getSufijo());
	}

	private DefVariableGlobal comporbarExisteVaribleGlobal(String nombre) {
		DefVariableGlobal d = varsG.getFromAny(nombre);

		return d;
	}

	private DefVariableLocal comporbarExisteVaribleLocal(String nombre) {
		DefVariableLocal d = varsL.getFromAny(nombre);

		return d;
	}

	private Param comporbarExisteParametro(String nombre) {
		Param p = params.getFromAny(nombre);

		return p;
	}

	private char stringToChar(String s) {
		if(s.equals("'\\n'")) {
			return '\n';
		}  else if(s.equals("'\\t'")) {
			return '\t';
		} else if(s.equals("'\\r'")) {
			return '\r';
		}
		return s.replaceAll("\'", "").charAt(0);
	}

	private int obtenerTamañoVariablesLocalesPorMetodo(DefMetodo node) {
		int tam = 0;
		if (node.getCuerpo() != null) {
			for (CuerpoMetodo child : node.getCuerpo()) {
				if (child instanceof DefVariableLocal) {
					tam += ((DefVariableLocal) child).getTipo().getSize();
				}
			}
		}

		return tam;
	}

	private boolean isOperadorLogico(String operador) {
		if (operador.equals("AND") || operador.equals("OR") || operador.equals("!")) {
			return true;
		}
		return false;
	}

	private Campo getVariableEstructura(String nombreEstructura, String nombreVariable) {
		Campo tip = null;
		DefStruct struct = structs.getFromAny(nombreEstructura);

		for (int i = 0; i < struct.getDefiniciones().size(); i++) {
			if (((Campo) struct.getDefiniciones().get(i)).getNombre().equals(nombreVariable)) {
				tip = (Campo) struct.getDefiniciones().get(i);
			}
		}
		return tip;
	}

	private PrintWriter writer;
	private String sourceFile;
	private ContextMap<String, DefVariableGlobal> varsG = new ContextMap<String, DefVariableGlobal>();
	private ContextMap<String, DefVariableLocal> varsL = new ContextMap<String, DefVariableLocal>();
	private ContextMap<String, DefStruct> structs = new ContextMap<String, DefStruct>();
	private ContextMap<String, Param> params = new ContextMap<String, Param>();
	private int tamañoParamActual = 0;
	private int tamañoLocalesActual = 0;
}
