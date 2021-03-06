/**
 * @generated VGen 1.3.3
 */

package ast;

import visitor.*;

//	floatType:tipo -> 

public class FloatType extends AbstractTipo {

	@Override
	public Object accept(Visitor v, Object param) { 
		return v.visit(this, param);
	}

	public int getSize() {
		return 4;
	}

	@Override
	public char getSufijo() {
		return 'f';
	}

	@Override
	public String getNombreMAPL() {
		return "real";
	}
}

