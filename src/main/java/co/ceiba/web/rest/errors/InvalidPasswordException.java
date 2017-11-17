package co.ceiba.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class InvalidPasswordException extends AbstractThrowableProblem {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8741754627414445912L;

	public InvalidPasswordException() {
        super(ErrorConstants.INVALID_PASSWORD_TYPE, "Incorrect password", Status.BAD_REQUEST);
    }
}
