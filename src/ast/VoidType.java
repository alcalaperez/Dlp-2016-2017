package ast;

import visitor.Visitor;

public class VoidType extends AbstractTipo {
	
	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}
	public int getSize() {
		return 0;
	}

	@Override
	public char getSufijo() {
		return ' ';
	}

	@Override
	public String getNombreMAPL() {
		return " ";
	}
}
