package co.ceiba.web.rest.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class EmailNotFoundException extends AbstractThrowableProblem {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4615209157995473438L;

	public EmailNotFoundException() {
        super(ErrorConstants.EMAIL_NOT_FOUND_TYPE, "Email address not registered", Status.BAD_REQUEST);
    }
}
