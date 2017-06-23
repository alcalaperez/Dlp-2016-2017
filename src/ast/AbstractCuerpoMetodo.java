/**
 * @generated VGen 1.3.3
 */

package ast;

public abstract class AbstractCuerpoMetodo extends AbstractTraceable implements CuerpoMetodo {
	private DefMetodo metodo;

	@Override
	public void setMetodoActual(DefMetodo node) {
		metodo = node;
		
	}

	@Override
	public DefMetodo getMetodoActual() {
		return metodo;
	}
}

