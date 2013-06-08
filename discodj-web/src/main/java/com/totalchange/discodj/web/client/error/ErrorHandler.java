package com.totalchange.discodj.web.client.error;

import java.util.Set;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import com.totalchange.discodj.web.client.views.ErrorView;

@Singleton
public class ErrorHandler {
    private ErrorView errorView;
    private ErrorConstants errorConstants;
    private Validator validator;

    @Inject
    public ErrorHandler(ErrorView errorView, ErrorConstants errorConstants,
            Validator validator) {
        this.errorView = errorView;
        this.errorConstants = errorConstants;
        this.validator = validator;
    }

    private String stackTraceToString(Throwable th) {
        StringBuilder str = new StringBuilder();
        String nl = "\n";

        if (th.getMessage() != null) {
            str.append(th.getMessage());
        }

        if (th.getStackTrace() != null) {
            for (StackTraceElement st : th.getStackTrace()) {
                str.append(nl);
                str.append(st.toString());
            }
        }

        if (th.getCause() != null) {
            str.append(nl + nl);
            str.append("Caused by: ");
            str.append(stackTraceToString(th.getCause()));
        }

        return str.toString();
    }

    public void argh(String msg) {
        errorView.setMessage(msg);
        errorView.setErrorMessage(null);
        errorView.setStackTrace(null);
        errorView.show();
    }

    public void argh(String msg, Throwable th) {
        errorView.setMessage(msg);
        errorView.setErrorMessage(th.getMessage());
        errorView.setStackTrace(stackTraceToString(th));
        errorView.show();
    }

    public boolean validatedOk(Object obj) {
        Set<ConstraintViolation<Object>> violations = validator.validate(obj);
        if (violations.size() > 0) {
            StringBuilder err = new StringBuilder();
            for (ConstraintViolation<Object> violation : violations) {
                if (err.length() > 0) {
                    err.append("\n\n");
                }
                err.append(violation.getMessage());
            }

            argh(err.toString());
            return false;
        } else {
            return true;
        }
    }

    public void loadingError(Throwable th) {
        argh(errorConstants.loadingErrorMessage(), th);
    }
}
