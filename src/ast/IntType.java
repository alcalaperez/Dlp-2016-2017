/**
 * @generated VGen 1.3.3
 */

package ast;

import visitor.*;

//	intType:tipo -> 

public class IntType extends AbstractTipo {

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}
	public int getSize() {
		return 2;
	}

	@Override
	public char getSufijo() {
		return 'i';
	}

	@Override
	public String getNombreMAPL() {
		return "int";
	}
	
}

