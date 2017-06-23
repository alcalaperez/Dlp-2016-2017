/**
 * @generated VGen 1.3.3
 */

package ast;

public abstract class AbstractExpresion extends AbstractTraceable implements Expresion {
	@Override
	public Tipo getTipo() {
		return tipo;
	}

	@Override
	public void setTipo(Tipo tipo) {
		this.tipo = tipo;

	}
	
	@Override
	public boolean getModificable() {
		return b;
	}

	@Override
	public void setModificable(boolean b) {
		this.b = b;

	}

	private Tipo tipo;
	private boolean b;

}
