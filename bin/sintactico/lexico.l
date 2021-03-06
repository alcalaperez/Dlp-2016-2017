/* -- No es necesario modificar esta parte ----------------------------------------------- */
package sintactico;


import java.io.*;
import main.*;
import ast.Position;
%%
%byaccj
%unicode
%line
%column
%public

%{
  public Yylex(Reader in, GestorErrores gestor) {
		this(in);
		this.gestor = gestor;
	}

	public int line() { return yyline + 1; }
	public int column() { return yycolumn + 1; }
	public String lexeme() { return yytext(); }

	// Traza para probar el l�xico de manera independiente al sint�ctico
	public static void main(String[] args) throws Exception {
		Yylex lex = new Yylex(new FileReader(Main.programa), new GestorErrores());
		int token;
		while ((token = lex.yylex()) != 0)
			System.out.println("\t[" + lex.line() + ":" + lex.column() + "] Token: " + token + ". Lexema: " + lex.lexeme());
	}

	private GestorErrores gestor;
%}

%%

/* -- Modificar aqu�. A�adir reglas en esta secci�n ----------------------------------------- */


[+\-*/.,:=;\[\](){}<>']  { return yytext().charAt(0); }

"<="			{ return Parser.MENORIGUAL; }
">="			{ return Parser.MAYORIGUAL; }
"&&"			{ return Parser.AND; }
"||"			{ return Parser.OR; }
"!"				{ return Parser.NOT; }
"!="			{ return Parser.NOTEQUALS; }
"=="			{ return Parser.EQUALS; }

int				{ return Parser.INT; }
char			{ return Parser.CHAR; }
float			{ return Parser.FLOAT; }

var					{ return Parser.VAR; }
struct				{ return Parser.STRUCT; }
return				{ return Parser.RETURN; }
print|printsp		{ return Parser.PRINT; }
read				{ return Parser.READ; }
while				{ return Parser.WHILE; }
cast				{ return Parser.CAST; }
if					{ return Parser.IF; }
else				{ return Parser.ELSE; }

\'(\\n| \\t|\\r|\\[0-256]|.)\'					{ return Parser.CHARACTER; }
[0-9]+     										{ return Parser.LITENT; }
[0-9]+\.[0-9]*									{ return Parser.LITREAL; }
[a-zA-Z][a-zA-Z0-9_�]*							{ return Parser.IDENT; }


"/*"~"*/"	{ }
"//".*		{ }

[ \n\r\t]     {  }

.		{ gestor.error("L�xico", "Cadena \"" + yytext() +"\" no reconocida.", new Position(line(), column())); }

