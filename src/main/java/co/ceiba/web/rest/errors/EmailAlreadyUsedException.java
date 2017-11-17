package co.ceiba.web.rest.errors;

public class EmailAlreadyUsedException extends BadRequestAlertException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3769108551015936416L;

	public EmailAlreadyUsedException() {
        super(ErrorConstants.EMAIL_ALREADY_USED_TYPE, "Email address already in use", "userManagement", "emailexists");
    }
}
