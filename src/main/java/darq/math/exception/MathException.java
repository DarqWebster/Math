package darq.math.exception;

/**
 *
 * @author Craig.Webster
 */
public class MathException extends Exception {

	public MathException() {
	}

	public MathException(String message) {
		super(message);
	}

	public MathException(String message, Throwable cause) {
		super(message, cause);
	}

	public MathException(Throwable cause) {
		super(cause);
	}
}
