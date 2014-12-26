/**
 *
 */
package trsit.cpay.web.validation;

import org.apache.wicket.validation.INullAcceptingValidator;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

/**
 * @author black
 *
 */
public class NotNullValidator implements INullAcceptingValidator<Object> {
    private final static IValidator<Object> instance = new NotNullValidator();

    private static final long serialVersionUID = 1L;


    public static IValidator<Object> instance() {
        return instance;
    }


    @Override
    public void validate(final IValidatable<Object> validatable) {
        if(validatable.getValue() == null) {
            validatable.error(new ValidationError("Should not be  null"));
        }

    }

}
