/**
 * @generated VGen 1.3.3
 */

package ast;

import visitor.*;

//	charType:tipo -> 

public class CharType extends AbstractTipo {

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}
	public int getSize() {
		return 1;
	}

	@Override
	public char getSufijo() {
		return 'b';
	}

	@Override
	public String getNombreMAPL() {
		return "byte";
	}
}

