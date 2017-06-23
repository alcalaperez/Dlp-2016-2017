//### This file created by BYACC 1.8(/Java extension  1.14)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "sintac.y"
package sintactico;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ast.AST;
import ast.ArrayType;
import ast.Asigna;
import ast.Campo;
import ast.Cast;
import ast.CharType;
import ast.DefMetodo;
import ast.DefStruct;
import ast.DefVariableGlobal;
import ast.DefVariableLocal;
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
import ast.Token;
import ast.Variable;
import ast.VoidType;
import ast.While;
//#line 24 "Parser.java"
import main.GestorErrores;




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//## **user defined:Object
String   yytext;//user variable to return contextual strings
Object yyval; //used to return semantic vals from action routines
Object yylval;//the 'lval' (result) I got from yylex()
Object valstk[] = new Object[YYSTACKSIZE];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
final void val_init()
{
  yyval=new Object();
  yylval=new Object();
  valptr=-1;
}
final void val_push(Object val)
{
  try {
    valptr++;
    valstk[valptr]=val;
  }
  catch (ArrayIndexOutOfBoundsException e) {
    int oldsize = valstk.length;
    int newsize = oldsize*2;
    Object[] newstack = new Object[newsize];
    System.arraycopy(valstk,0,newstack,0,oldsize);
    valstk = newstack;
    valstk[valptr]=val;
  }
}
final Object val_pop()
{
  return valstk[valptr--];
}
final void val_drop(int cnt)
{
  valptr -= cnt;
}
final Object val_peek(int relative)
{
  return valstk[valptr-relative];
}
//#### end semantic value section ####
public final static short AND=257;
public final static short OR=258;
public final static short NOT=259;
public final static short MAYORIGUAL=260;
public final static short MENORIGUAL=261;
public final static short EQUALS=262;
public final static short NOTEQUALS=263;
public final static short IDENT=264;
public final static short STRUCT=265;
public final static short VAR=266;
public final static short WHILE=267;
public final static short IF=268;
public final static short ELSE=269;
public final static short RETURN=270;
public final static short PRINT=271;
public final static short READ=272;
public final static short INT=273;
public final static short CHAR=274;
public final static short FLOAT=275;
public final static short LITENT=276;
public final static short LITREAL=277;
public final static short CAST=278;
public final static short CHARACTER=279;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    0,    1,    1,    2,    2,    2,    3,    6,    6,    7,
    5,    4,    4,    9,    9,   12,   12,   11,   11,   13,
   13,   14,   15,   10,   10,   17,   17,   17,   17,   17,
   17,   17,   17,   17,   17,    8,    8,    8,    8,    8,
   16,   16,   16,   16,   16,   16,   16,   16,   16,   16,
   16,   16,   16,   16,   16,   16,   16,   16,   16,   16,
   16,   16,
};
final static short yylen[] = {                            2,
    1,    0,    2,    1,    1,    1,    6,    0,    2,    4,
    5,    9,    7,    0,    1,    0,    1,    1,    3,    1,
    3,    3,    1,    0,    2,    5,    7,    7,   11,    2,
    3,    4,    3,    3,    5,    1,    1,    1,    1,    4,
    1,    1,    1,    5,    1,    3,    3,    3,    3,    3,
    3,    3,    3,    3,    2,    3,    3,    3,    4,    3,
    3,    4,
};
final static short yydefred[] = {                         2,
    0,    0,    0,    0,    0,    3,    4,    5,    6,    0,
    0,    0,    0,    0,    0,   18,    8,    0,    0,    0,
    0,    0,   39,    0,   36,   37,   38,    0,   22,   24,
    0,   19,    0,    0,    9,    0,   11,    0,    0,    0,
    7,    0,    0,    0,   13,    0,    0,    0,    0,    0,
    0,    0,   41,   42,    0,   45,    0,   25,   24,    0,
   40,    0,    0,    0,    0,    0,    0,    0,   30,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   10,
    0,    0,    0,   20,    0,    0,   50,    0,    0,   31,
   33,   34,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   58,    0,   12,    0,
    0,    0,    0,    0,    0,    0,   32,   59,   62,   35,
   21,   26,   24,   24,    0,    0,    0,   27,    0,    0,
   24,    0,   29,
};
final static short yydgoto[] = {                          1,
    2,    6,    7,    8,    9,   22,   35,   28,   14,   38,
   15,   92,   93,   16,   94,   57,   58,
};
final static short yysindex[] = {                         0,
    0, -239,  -31, -251, -250,    0,    0,    0,    0, -245,
 -103,  -30,  -28,  -10,  -11,    0,    0,  -88,  -88,  -35,
 -245, -114,    0, -242,    0,    0,    0,  -22,    0,    0,
  -88,    0,  -20,  -17,    0,  -58,    0,  -40,  -84,  -88,
    0,  -88,   56,    9,    0, -213,   56,   13,   14,   50,
   56,   56,    0,    0,   -5,    0,  453,    0,    0,   -1,
    0,   19,  -45,   56,    4,  400,   56,   56,    0,  460,
  483,  490,  -88,   56,   56,   56,   56,   56,   56,   56,
   56,   56,   56,   56,   56,   56, -201,   56,  -24,    0,
   56,   25,   21,    0,  773,  -88,    0,  425,  432,    0,
    0,    0,   20,  513,  122,  122,  357,  357,  -45,  -45,
  -44,  -44,  -44,  -44,  -44,  -44,    0,  551,    0,   48,
   32,   56,   33,  -26,  -25,   56,    0,    0,    0,    0,
    0,    0,    0,    0,  -44,   -8,    8,    0, -176,  -21,
    0,   24,    0,
};
final static short yyrindex[] = {                         0,
    0,   95,    0,    0,    0,    0,    0,    0,    0,   58,
    0,    0,    0,    0,   59,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  719,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   79,  -37,   62,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
   62,    0,   63,    0,  -23,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  148,  813,  112,  808,  780,  801,
  101,  136,  307,  329,  364,  371,    0,    0,    0,    0,
  740,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,  393,    0,    0,    0,   40,    0,
    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,    0,    0,    0,    0,    0,    0,   10,    0,  -47,
    0,   16,    0,   84,  -14,   -7,    0,
};
final static int YYTABLESIZE=1059;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         47,
   87,   87,   24,   55,   55,   55,   55,   55,   10,   55,
   34,   89,   11,   12,   85,   47,   86,   23,   13,   17,
   23,   55,   31,   55,    3,    4,    5,   18,   29,   19,
   20,   47,   21,   36,   42,   63,   37,   40,   59,   66,
   39,   41,   70,   71,   72,   88,   88,   47,   64,   60,
   65,   61,   67,   68,   73,   55,   95,   90,   91,   98,
   99,   96,  117,   47,  122,  121,  104,  105,  106,  107,
  108,  109,  110,  111,  112,  113,  114,  115,  116,   28,
  118,  126,  103,   95,   45,  136,  137,   30,  129,   47,
  130,  132,  140,  142,    1,   47,  133,  134,   14,   15,
  119,  141,   16,   17,   32,  123,  120,  131,   69,    0,
    0,    0,    0,    0,   95,    0,  138,    0,  135,   43,
   43,   43,   43,   43,   43,   43,    0,    0,    0,    0,
    0,    0,  139,    0,    0,    0,    0,   43,   43,   43,
   43,   60,   60,   60,   60,   60,    0,   60,  143,   33,
    0,    0,   48,   48,   48,   48,   48,    0,   48,   60,
   60,   60,   60,   77,   28,    0,    0,   87,   78,   43,
   48,   43,   48,    0,    0,   23,   61,   61,   61,   61,
   61,   85,   61,   86,   25,   26,   27,    0,   46,    0,
   46,   46,   46,   60,   61,   61,   61,   61,    0,    0,
    0,    0,    0,    0,   48,    0,   46,    0,   46,    0,
    0,    0,   88,    0,   81,   82,   83,   84,   43,   55,
   55,    0,    0,   44,    0,   46,   48,   49,   61,   50,
   51,   52,    0,    0,   43,   53,   54,   55,   56,   44,
   46,   46,   48,   49,    0,   50,   51,   52,    0,    0,
   43,   53,   54,   55,   56,   44,    0,   46,   48,   49,
    0,   50,   51,   52,    0,    0,   43,   53,   54,   55,
   56,   44,    0,   46,   48,   49,    0,   50,   51,   52,
    0,    0,   43,   53,   54,   55,   56,   44,    0,   46,
   48,   49,    0,   50,   51,   52,    0,    0,   28,   53,
   54,   55,   56,   28,    0,   28,   28,   28,   43,   28,
   28,   28,    0,   62,   43,   28,   28,   28,   28,   62,
    0,    0,    0,    0,    0,   53,   54,   55,   56,    0,
    0,   53,   54,   55,   56,   43,   43,    0,   43,   43,
   43,   43,    0,    0,    0,    0,    0,   53,   53,   53,
   53,   53,    0,   53,    0,    0,    0,   60,   60,    0,
   60,   60,   60,   60,    0,   53,   53,   53,   53,   54,
   54,   54,   54,   54,    0,   54,    0,    0,   79,   80,
    0,   81,   82,   83,   84,    0,    0,   54,   54,   54,
   54,    0,   61,   61,    0,   61,   61,   61,   61,   53,
    0,    0,   87,    0,   56,   56,   56,   56,   56,    0,
   56,   57,   57,   57,   57,   57,   85,   57,   86,    0,
    0,   54,   56,   56,   56,   56,    0,    0,    0,   57,
   57,   57,   57,   44,   44,   44,   44,   44,    0,   44,
   97,   77,   75,    0,   76,   87,   78,   88,    0,    0,
    0,   44,   44,   44,   44,    0,   56,    0,    0,   85,
    0,   86,    0,   57,    0,  124,   77,   75,    0,   76,
   87,   78,  125,   77,   75,    0,   76,   87,   78,    0,
    0,    0,    0,    0,   85,   44,   86,    0,    0,    0,
   88,   85,    0,   86,   77,   75,    0,   76,   87,   78,
    0,   77,   75,    0,   76,   87,   78,    0,    0,    0,
    0,    0,   85,   74,   86,   88,    0,    0,  100,   85,
    0,   86,   88,    0,   77,   75,    0,   76,   87,   78,
    0,   77,   75,    0,   76,   87,   78,    0,    0,    0,
    0,  101,   85,   88,   86,    0,    0,    0,  102,   85,
   88,   86,    0,    0,   77,   75,    0,   76,   87,   78,
    0,    0,    0,   53,   53,    0,   53,   53,   53,   53,
    0,  127,   85,   88,   86,    0,    0,    0,    0,    0,
   88,    0,    0,    0,    0,   54,   54,    0,   54,   54,
   54,   54,   77,   75,    0,   76,   87,   78,    0,    0,
    0,    0,    0,   88,    0,    0,    0,    0,    0,    0,
   85,    0,   86,   79,   80,    0,   81,   82,   83,   84,
   56,   56,    0,   56,   56,   56,   56,   57,   57,    0,
   57,   57,   57,   57,    0,    0,    0,    0,    0,    0,
    0,   88,    0,  128,    0,    0,    0,    0,    0,   44,
   44,    0,   44,   44,   44,   44,   79,   80,    0,   81,
   82,   83,   84,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   79,   80,    0,   81,   82,   83,   84,   79,   80,
    0,   81,   82,   83,   84,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   79,
   80,    0,   81,   82,   83,   84,   79,   80,    0,   81,
   82,   83,   84,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   79,
   80,    0,   81,   82,   83,   84,   79,   80,    0,   81,
   82,   83,   84,    0,    0,    0,    0,    0,    0,    0,
   43,   43,    0,   43,   43,   43,    0,    0,    0,   79,
   80,    0,   81,   82,   83,   84,    0,    0,   43,   43,
   43,   62,   62,    0,   62,   62,   62,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   62,
   62,   62,    0,    0,    0,    0,    0,   79,   80,   43,
   81,   82,   83,   84,   77,   75,    0,   76,   87,   78,
   51,   51,   51,   51,   51,    0,   51,    0,    0,    0,
   62,    0,   85,    0,   86,    0,    0,    0,   51,    0,
   51,   52,   52,   52,   52,   52,    0,   52,   49,   49,
   49,   49,   49,   47,   49,   47,   47,   47,    0,   52,
    0,   52,    0,   88,    0,    0,   49,    0,   49,    0,
    0,   47,   51,   47,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   52,    0,    0,    0,    0,    0,    0,
   49,    0,    0,    0,    0,   47,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,   43,   43,    0,   43,   43,
   43,   43,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,   62,   62,    0,   62,
   62,   62,   62,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   79,
   80,    0,   81,   82,   83,   84,   51,   51,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,   52,   52,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         40,
   46,   46,   91,   41,   42,   43,   44,   45,   40,   47,
  125,   59,  264,  264,   60,   40,   62,   41,  264,  123,
   44,   59,   58,   61,  264,  265,  266,   58,   19,   58,
   41,   40,   44,  276,   93,   43,   59,   58,  123,   47,
   31,   59,   50,   51,   52,   91,   91,   40,   40,   40,
  264,   42,   40,   40,   60,   93,   64,   59,   40,   67,
   68,   58,  264,   40,   44,   41,   74,   75,   76,   77,
   78,   79,   80,   81,   82,   83,   84,   85,   86,   40,
   88,   62,   73,   91,  125,  133,  134,  123,   41,   40,
   59,   59,  269,  141,    0,   40,  123,  123,   41,   41,
  125,  123,   41,   41,   21,   96,   91,  122,   59,   -1,
   -1,   -1,   -1,   -1,  122,   -1,  125,   -1,  126,   41,
   42,   43,   44,   45,   46,   47,   -1,   -1,   -1,   -1,
   -1,   -1,  125,   -1,   -1,   -1,   -1,   59,   60,   61,
   62,   41,   42,   43,   44,   45,   -1,   47,  125,  264,
   -1,   -1,   41,   42,   43,   44,   45,   -1,   47,   59,
   60,   61,   62,   42,  125,   -1,   -1,   46,   47,   91,
   59,   93,   61,   -1,   -1,  264,   41,   42,   43,   44,
   45,   60,   47,   62,  273,  274,  275,   -1,   41,   -1,
   43,   44,   45,   93,   59,   60,   61,   62,   -1,   -1,
   -1,   -1,   -1,   -1,   93,   -1,   59,   -1,   61,   -1,
   -1,   -1,   91,   -1,  260,  261,  262,  263,  259,  257,
  258,   -1,   -1,  264,   -1,  266,  267,  268,   93,  270,
  271,  272,   -1,   -1,  259,  276,  277,  278,  279,  264,
   93,  266,  267,  268,   -1,  270,  271,  272,   -1,   -1,
  259,  276,  277,  278,  279,  264,   -1,  266,  267,  268,
   -1,  270,  271,  272,   -1,   -1,  259,  276,  277,  278,
  279,  264,   -1,  266,  267,  268,   -1,  270,  271,  272,
   -1,   -1,  259,  276,  277,  278,  279,  264,   -1,  266,
  267,  268,   -1,  270,  271,  272,   -1,   -1,  259,  276,
  277,  278,  279,  264,   -1,  266,  267,  268,  259,  270,
  271,  272,   -1,  264,  259,  276,  277,  278,  279,  264,
   -1,   -1,   -1,   -1,   -1,  276,  277,  278,  279,   -1,
   -1,  276,  277,  278,  279,  257,  258,   -1,  260,  261,
  262,  263,   -1,   -1,   -1,   -1,   -1,   41,   42,   43,
   44,   45,   -1,   47,   -1,   -1,   -1,  257,  258,   -1,
  260,  261,  262,  263,   -1,   59,   60,   61,   62,   41,
   42,   43,   44,   45,   -1,   47,   -1,   -1,  257,  258,
   -1,  260,  261,  262,  263,   -1,   -1,   59,   60,   61,
   62,   -1,  257,  258,   -1,  260,  261,  262,  263,   93,
   -1,   -1,   46,   -1,   41,   42,   43,   44,   45,   -1,
   47,   41,   42,   43,   44,   45,   60,   47,   62,   -1,
   -1,   93,   59,   60,   61,   62,   -1,   -1,   -1,   59,
   60,   61,   62,   41,   42,   43,   44,   45,   -1,   47,
   41,   42,   43,   -1,   45,   46,   47,   91,   -1,   -1,
   -1,   59,   60,   61,   62,   -1,   93,   -1,   -1,   60,
   -1,   62,   -1,   93,   -1,   41,   42,   43,   -1,   45,
   46,   47,   41,   42,   43,   -1,   45,   46,   47,   -1,
   -1,   -1,   -1,   -1,   60,   93,   62,   -1,   -1,   -1,
   91,   60,   -1,   62,   42,   43,   -1,   45,   46,   47,
   -1,   42,   43,   -1,   45,   46,   47,   -1,   -1,   -1,
   -1,   -1,   60,   61,   62,   91,   -1,   -1,   59,   60,
   -1,   62,   91,   -1,   42,   43,   -1,   45,   46,   47,
   -1,   42,   43,   -1,   45,   46,   47,   -1,   -1,   -1,
   -1,   59,   60,   91,   62,   -1,   -1,   -1,   59,   60,
   91,   62,   -1,   -1,   42,   43,   -1,   45,   46,   47,
   -1,   -1,   -1,  257,  258,   -1,  260,  261,  262,  263,
   -1,   59,   60,   91,   62,   -1,   -1,   -1,   -1,   -1,
   91,   -1,   -1,   -1,   -1,  257,  258,   -1,  260,  261,
  262,  263,   42,   43,   -1,   45,   46,   47,   -1,   -1,
   -1,   -1,   -1,   91,   -1,   -1,   -1,   -1,   -1,   -1,
   60,   -1,   62,  257,  258,   -1,  260,  261,  262,  263,
  257,  258,   -1,  260,  261,  262,  263,  257,  258,   -1,
  260,  261,  262,  263,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   91,   -1,   93,   -1,   -1,   -1,   -1,   -1,  257,
  258,   -1,  260,  261,  262,  263,  257,  258,   -1,  260,
  261,  262,  263,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,  257,  258,   -1,  260,  261,  262,  263,  257,  258,
   -1,  260,  261,  262,  263,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,
  258,   -1,  260,  261,  262,  263,  257,  258,   -1,  260,
  261,  262,  263,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,
  258,   -1,  260,  261,  262,  263,  257,  258,   -1,  260,
  261,  262,  263,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   42,   43,   -1,   45,   46,   47,   -1,   -1,   -1,  257,
  258,   -1,  260,  261,  262,  263,   -1,   -1,   60,   61,
   62,   42,   43,   -1,   45,   46,   47,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   60,
   61,   62,   -1,   -1,   -1,   -1,   -1,  257,  258,   91,
  260,  261,  262,  263,   42,   43,   -1,   45,   46,   47,
   41,   42,   43,   44,   45,   -1,   47,   -1,   -1,   -1,
   91,   -1,   60,   -1,   62,   -1,   -1,   -1,   59,   -1,
   61,   41,   42,   43,   44,   45,   -1,   47,   41,   42,
   43,   44,   45,   41,   47,   43,   44,   45,   -1,   59,
   -1,   61,   -1,   91,   -1,   -1,   59,   -1,   61,   -1,
   -1,   59,   93,   61,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   93,   -1,   -1,   -1,   -1,   -1,   -1,
   93,   -1,   -1,   -1,   -1,   93,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,  257,  258,   -1,  260,  261,
  262,  263,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,  257,  258,   -1,  260,
  261,  262,  263,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,
  258,   -1,  260,  261,  262,  263,  257,  258,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,   -1,
   -1,   -1,   -1,   -1,   -1,   -1,   -1,  257,  258,
};
}
final static short YYFINAL=1;
final static short YYMAXTOKEN=279;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,"'('","')'","'*'","'+'","','",
"'-'","'.'","'/'",null,null,null,null,null,null,null,null,null,null,"':'","';'",
"'<'","'='","'>'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
"'['",null,"']'",null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,"'{'",null,"'}'",null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,"\"AND\"","\"OR\"","\"NOT\"",
"\"MAYORIGUAL\"","\"MENORIGUAL\"","\"EQUALS\"","\"NOTEQUALS\"","\"IDENT\"",
"\"STRUCT\"","\"VAR\"","\"WHILE\"","\"IF\"","\"ELSE\"","\"RETURN\"","\"PRINT\"",
"\"READ\"","\"INT\"","\"CHAR\"","\"FLOAT\"","\"LITENT\"","\"LITREAL\"",
"\"CAST\"","\"CHARACTER\"",
};
final static String yyrule[] = {
"$accept : programa",
"programa : definiciones",
"definiciones :",
"definiciones : definiciones definicion",
"definicion : estructura",
"definicion : metodo",
"definicion : defVariableGlobal",
"estructura : \"STRUCT\" \"IDENT\" '{' campos '}' ';'",
"campos :",
"campos : campos campo",
"campo : \"IDENT\" ':' tipo ';'",
"defVariableGlobal : \"VAR\" \"IDENT\" ':' tipo ';'",
"metodo : \"IDENT\" '(' parametros ')' ':' tipo '{' cuerpoMetodo '}'",
"metodo : \"IDENT\" '(' parametros ')' '{' cuerpoMetodo '}'",
"parametros :",
"parametros : parametroP",
"parametrosLlamada :",
"parametrosLlamada : parametroLlamadaP",
"parametroP : parametro",
"parametroP : parametroP ',' parametro",
"parametroLlamadaP : parametroLlamada",
"parametroLlamadaP : parametroLlamadaP ',' parametroLlamada",
"parametro : \"IDENT\" ':' tipo",
"parametroLlamada : expr",
"cuerpoMetodo :",
"cuerpoMetodo : cuerpoMetodo cuerpo",
"cuerpo : \"VAR\" \"IDENT\" ':' tipo ';'",
"cuerpo : \"WHILE\" '(' expr ')' '{' cuerpoMetodo '}'",
"cuerpo : \"IF\" '(' expr ')' '{' cuerpoMetodo '}'",
"cuerpo : \"IF\" '(' expr ')' '{' cuerpoMetodo '}' \"ELSE\" '{' cuerpoMetodo '}'",
"cuerpo : \"RETURN\" ';'",
"cuerpo : \"RETURN\" expr ';'",
"cuerpo : expr '=' expr ';'",
"cuerpo : \"PRINT\" expr ';'",
"cuerpo : \"READ\" expr ';'",
"cuerpo : \"IDENT\" '(' parametrosLlamada ')' ';'",
"tipo : \"INT\"",
"tipo : \"CHAR\"",
"tipo : \"FLOAT\"",
"tipo : \"IDENT\"",
"tipo : '[' \"LITENT\" ']' tipo",
"expr : \"LITENT\"",
"expr : \"LITREAL\"",
"expr : \"IDENT\"",
"expr : \"CAST\" '<' tipo '>' expr",
"expr : \"CHARACTER\"",
"expr : expr '+' expr",
"expr : expr '-' expr",
"expr : expr '*' expr",
"expr : expr '/' expr",
"expr : '(' expr ')'",
"expr : expr \"AND\" expr",
"expr : expr \"OR\" expr",
"expr : expr \"EQUALS\" expr",
"expr : expr \"NOTEQUALS\" expr",
"expr : \"NOT\" expr",
"expr : expr '<' expr",
"expr : expr '>' expr",
"expr : expr '.' \"IDENT\"",
"expr : expr '[' expr ']'",
"expr : expr \"MAYORIGUAL\" expr",
"expr : expr \"MENORIGUAL\" expr",
"expr : \"IDENT\" '(' parametrosLlamada ')'",
};

//#line 120 "sintac.y"
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
//#line 540 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 19 "sintac.y"
{ raiz = new Programa(val_peek(0)); }
break;
case 2:
//#line 22 "sintac.y"
{ yyval = new ArrayList(); }
break;
case 3:
//#line 23 "sintac.y"
{ yyval = val_peek(1); ((List)val_peek(1)).add(val_peek(0)); }
break;
case 4:
//#line 26 "sintac.y"
{ yyval = val_peek(0); }
break;
case 5:
//#line 27 "sintac.y"
{ yyval = val_peek(0); }
break;
case 6:
//#line 28 "sintac.y"
{ yyval = val_peek(0); }
break;
case 7:
//#line 32 "sintac.y"
{ yyval = new DefStruct(val_peek(4), val_peek(2)); }
break;
case 8:
//#line 35 "sintac.y"
{ yyval = new ArrayList(); }
break;
case 9:
//#line 36 "sintac.y"
{ yyval = val_peek(1); ((List)val_peek(1)).add(val_peek(0)); }
break;
case 10:
//#line 39 "sintac.y"
{ yyval = new Campo(val_peek(3), val_peek(1)); }
break;
case 11:
//#line 42 "sintac.y"
{ yyval = new DefVariableGlobal(val_peek(3), val_peek(1)); }
break;
case 12:
//#line 45 "sintac.y"
{ yyval = new DefMetodo(val_peek(8), val_peek(6), val_peek(3), val_peek(1)); }
break;
case 13:
//#line 46 "sintac.y"
{ yyval = new DefMetodo(val_peek(6), val_peek(4), val_peek(1)); }
break;
case 14:
//#line 49 "sintac.y"
{ yyval = new ArrayList(); }
break;
case 15:
//#line 50 "sintac.y"
{ yyval = val_peek(0); }
break;
case 16:
//#line 53 "sintac.y"
{ yyval = new ArrayList(); }
break;
case 17:
//#line 54 "sintac.y"
{ yyval = val_peek(0); }
break;
case 18:
//#line 57 "sintac.y"
{ yyval =  new ArrayList(); ((List)yyval).add(val_peek(0));}
break;
case 19:
//#line 58 "sintac.y"
{ yyval = val_peek(2); ((List)val_peek(2)).add(val_peek(0)); }
break;
case 20:
//#line 61 "sintac.y"
{ yyval =  new ArrayList(); ((List)yyval).add(val_peek(0));}
break;
case 21:
//#line 62 "sintac.y"
{ yyval = val_peek(2); ((List)val_peek(2)).add(val_peek(0)); }
break;
case 22:
//#line 65 "sintac.y"
{ yyval = new Param(val_peek(2), val_peek(0)); }
break;
case 23:
//#line 68 "sintac.y"
{ yyval = val_peek(0); }
break;
case 24:
//#line 71 "sintac.y"
{ yyval = new ArrayList(); }
break;
case 25:
//#line 72 "sintac.y"
{ yyval = val_peek(1); ((List)val_peek(1)).add(val_peek(0)); }
break;
case 26:
//#line 75 "sintac.y"
{ yyval = new DefVariableLocal(val_peek(3), val_peek(1)); }
break;
case 27:
//#line 76 "sintac.y"
{ yyval = new While(val_peek(4), val_peek(1)); }
break;
case 28:
//#line 77 "sintac.y"
{ yyval = new If(val_peek(4), val_peek(1)); }
break;
case 29:
//#line 78 "sintac.y"
{ yyval = new IfElse(val_peek(8), val_peek(5), val_peek(1)); }
break;
case 30:
//#line 79 "sintac.y"
{ yyval = new Return(null); }
break;
case 31:
//#line 80 "sintac.y"
{ yyval = new Return(val_peek(1)); }
break;
case 32:
//#line 81 "sintac.y"
{ yyval = new Asigna(val_peek(3), val_peek(1)); }
break;
case 33:
//#line 82 "sintac.y"
{ yyval = new Print(val_peek(1)); }
break;
case 34:
//#line 83 "sintac.y"
{ yyval = new Read(val_peek(1)); }
break;
case 35:
//#line 84 "sintac.y"
{ yyval = new InvocarMetodo(val_peek(4), val_peek(2));}
break;
case 36:
//#line 88 "sintac.y"
{ yyval = new IntType(); }
break;
case 37:
//#line 89 "sintac.y"
{ yyval = new CharType(); }
break;
case 38:
//#line 90 "sintac.y"
{ yyval = new FloatType(); }
break;
case 39:
//#line 91 "sintac.y"
{ yyval = new IdentType(val_peek(0)); }
break;
case 40:
//#line 92 "sintac.y"
{ yyval = new ArrayType(val_peek(2), val_peek(0)); }
break;
case 41:
//#line 95 "sintac.y"
{ yyval = new LiteralInt(val_peek(0)); }
break;
case 42:
//#line 96 "sintac.y"
{ yyval = new LiteralReal(val_peek(0)); }
break;
case 43:
//#line 97 "sintac.y"
{ yyval = new Variable(val_peek(0)); }
break;
case 44:
//#line 98 "sintac.y"
{ yyval = new Cast(val_peek(2), val_peek(0)); }
break;
case 45:
//#line 99 "sintac.y"
{ yyval = new LiteralChar(val_peek(0)); }
break;
case 46:
//#line 100 "sintac.y"
{ yyval = new ExprAritmetica(val_peek(2), "+", val_peek(0)); }
break;
case 47:
//#line 101 "sintac.y"
{ yyval = new ExprAritmetica(val_peek(2), "-", val_peek(0)); }
break;
case 48:
//#line 102 "sintac.y"
{ yyval = new ExprAritmetica(val_peek(2), "*", val_peek(0)); }
break;
case 49:
//#line 103 "sintac.y"
{ yyval = new ExprAritmetica(val_peek(2), "/", val_peek(0)); }
break;
case 50:
//#line 104 "sintac.y"
{ yyval = val_peek(1); }
break;
case 51:
//#line 105 "sintac.y"
{ yyval = new ExprComparadora(val_peek(2), "AND", val_peek(0)); }
break;
case 52:
//#line 106 "sintac.y"
{ yyval = new ExprComparadora(val_peek(2), "OR", val_peek(0)); }
break;
case 53:
//#line 107 "sintac.y"
{ yyval = new ExprComparadora(val_peek(2), "EQUALS", val_peek(0)); }
break;
case 54:
//#line 108 "sintac.y"
{ yyval = new ExprComparadora(val_peek(2), "NOTEQUALS", val_peek(0)); }
break;
case 55:
//#line 109 "sintac.y"
{ yyval = new ExprComparadoraNot(val_peek(0));}
break;
case 56:
//#line 110 "sintac.y"
{ yyval = new ExprComparadora(val_peek(2), "<", val_peek(0)); }
break;
case 57:
//#line 111 "sintac.y"
{ yyval = new ExprComparadora(val_peek(2), ">", val_peek(0)); }
break;
case 58:
//#line 112 "sintac.y"
{ yyval = new ExprAccesoObjeto(val_peek(2), val_peek(0)); }
break;
case 59:
//#line 113 "sintac.y"
{ yyval = new ExprArray(val_peek(3), val_peek(1)); }
break;
case 60:
//#line 114 "sintac.y"
{ yyval = new ExprComparadora(val_peek(2), "MAYORIGUAL", val_peek(0)); }
break;
case 61:
//#line 115 "sintac.y"
{ yyval = new ExprComparadora(val_peek(2), "MENORIGUAL", val_peek(0)); }
break;
case 62:
//#line 116 "sintac.y"
{ yyval = new ExprInvocarMetodo(val_peek(3), val_peek(1)); }
break;
//#line 936 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
