%{
package sintactico;

import java.io.*;
import java.util.*;
import ast.*;
import main.*;
%}
%right '=' 
%left '+' '-' 
%left '*' '/'
%left 'AND' 'OR' 'NOT'
%left 'MAYORIGUAL' 'MENORIGUAL' 'EQUALS' 'NOTEQUALS' '<' '>'
%left '.'
%nonassoc 'IDENT' '[' ']'
%%


programa: definiciones															{ raiz = new Programa($1); }	
	;
	
definiciones: 																	{ $$ = new ArrayList(); }	
	| definiciones definicion													{ $$ = $1; ((List)$1).add($2); }
	;
		
definicion: estructura					{ $$ = $1; }
	| metodo							{ $$ = $1; }
	| defVariableGlobal					{ $$ = $1; }
	;
	
	
estructura: 'STRUCT' 'IDENT' '{'campos'}'';'					{ $$ = new DefStruct($2, $4); }
	;
	
campos: 														{ $$ = new ArrayList(); }
	| campos campo												{ $$ = $1; ((List)$1).add($2); }
	;
	
campo: 'IDENT'':' tipo ';'										{ $$ = new Campo($1, $3); }
	;
	
defVariableGlobal: 'VAR''IDENT'':' tipo ';'								{ $$ = new DefVariableGlobal($2, $4); }
	;

metodo: 'IDENT''('parametros')'':' tipo '{' cuerpoMetodo '}'					{ $$ = new DefMetodo($1, $3, $6, $8); }
	| 'IDENT''('parametros')' '{' cuerpoMetodo '}'								{ $$ = new DefMetodo($1, $3, $6); }
	;

parametros: 										{ $$ = new ArrayList(); }
	| parametroP									{ $$ = $1; }
	;
	
parametrosLlamada: 									{ $$ = new ArrayList(); }
	| parametroLlamadaP								{ $$ = $1; }
	;	
	
parametroP:	parametro							{ $$ =  new ArrayList(); ((List)$$).add($1);}
	| parametroP ',' parametro					{ $$ = $1; ((List)$1).add($3); }
	;

parametroLlamadaP:	parametroLlamada			{ $$ =  new ArrayList(); ((List)$$).add($1);}
	| parametroLlamadaP ',' parametroLlamada	{ $$ = $1; ((List)$1).add($3); }
	;
		
parametro: 'IDENT'':'tipo						{ $$ = new Param($1, $3); }
	;
	
parametroLlamada: expr							{ $$ = $1; }
	;
	
cuerpoMetodo: 									{ $$ = new ArrayList(); }		
	| cuerpoMetodo cuerpo						{ $$ = $1; ((List)$1).add($2); }
	;

cuerpo: 'VAR''IDENT'':' tipo ';'																{ $$ = new DefVariableLocal($2, $4); }
	| 'WHILE' '('expr')' '{' cuerpoMetodo '}'													{ $$ = new While($3, $6); }
	| 'IF' '('expr')' '{' cuerpoMetodo '}' 														{ $$ = new If($3, $6); }
	| 'IF' '('expr')' '{' cuerpoMetodo '}' 'ELSE' '{' cuerpoMetodo '}'							{ $$ = new IfElse($3, $6, $10); }
	| 'RETURN'';'																				{ $$ = new Return(new VoidType()); }
	| 'RETURN' expr ';'		 																	{ $$ = new Return($2); }
	|  expr '=' expr ';'		 																{ $$ = new Asigna($1, $3); }
	| 'PRINT' expr ';'																		    { $$ = new Print($2); }
	| 'READ' expr ';'																			{ $$ = new Read($2); }
	| 'IDENT' '(' parametrosLlamada ')' ';' 													{ $$ = new InvocarMetodo($1, $3);}	
	;
 
	
tipo: 'INT' 							{ $$ = new IntType(); }
	| 'CHAR' 							{ $$ = new CharType(); }
	| 'FLOAT' 							{ $$ = new FloatType(); }
	| 'IDENT'							{ $$ = new IdentType($1); }
	| '[''LITENT'']' tipo 				{ $$ = new ArrayType($2, $4); }
	;
	
expr: 'LITENT' 							{ $$ = new LiteralInt($1); }
	| 'LITREAL' 						{ $$ = new LiteralReal($1); }
	| 'IDENT'							{ $$ = new Variable($1); }
	| 'CAST' '<' tipo '>' expr 			{ $$ = new Cast($3, $5); }
	| 'CHARACTER' 						{ $$ = new LiteralChar($1); }
	| expr '+' expr						{ $$ = new ExprAritmetica($1, "+", $3); }
	| expr '-' expr	            		{ $$ = new ExprAritmetica($1, "-", $3); }
	| expr '*' expr						{ $$ = new ExprAritmetica($1, "*", $3); }
	| expr '/' expr						{ $$ = new ExprAritmetica($1, "/", $3); }
	| '(' expr ')'						{ $$ = $2; }
	| expr 'AND' expr					{ $$ = new ExprComparadora($1, "AND", $3); }
	| expr 'OR' expr					{ $$ = new ExprComparadora($1, "OR", $3); }
	| expr 'EQUALS' expr				{ $$ = new ExprComparadora($1, "EQUALS", $3); }
	| expr 'NOTEQUALS' expr				{ $$ = new ExprComparadora($1, "NOTEQUALS", $3); }
	| 'NOT' expr 						{ $$ = new ExprComparadoraNot($2);}
	| expr '<' expr						{ $$ = new ExprComparadora($1, "<", $3); }
	| expr '>' expr						{ $$ = new ExprComparadora($1, ">", $3); }
	| expr '.' 'IDENT'					{ $$ = new ExprAccesoObjeto($1, $3); }
	| expr '['expr']' 					{ $$ = new ExprArray($1, $3); }
	| expr 'MAYORIGUAL' expr			{ $$ = new ExprComparadora($1, "MAYORIGUAL", $3); }
	| expr 'MENORIGUAL' expr			{ $$ = new ExprComparadora($1, "MENORIGUAL", $3); }
	| 'IDENT' '('parametrosLlamada')' 	{ $$ = new ExprInvocarMetodo($1, $3); }
	;
	
%%
public Parser(Yylex lex, GestorErrores gestor, boolean debug) {
	this(debug);
	this.lex = lex;
	this.gestor = gestor;
}

// Métodos de acceso para el main -------------
public int parse() { return yyparse(); }
public AST getAST() { return raiz; }

// Funciones requeridas por Yacc --------------
void yyerror(String msg) {
	Token lastToken = (Token) yylval;
	gestor.error("Sintáctico", "Token = " + lastToken.getToken() + ", lexema = \"" + lastToken.getLexeme() + "\". " + msg, lastToken.getStart());
}

int yylex() {
	try {
		int token = lex.yylex();
		yylval = new Token(token, lex.lexeme(), lex.line(), lex.column());
		return token;
	} catch (IOException e) {
		return -1;
	}
}

private Yylex lex;
private GestorErrores gestor;
private AST raiz;