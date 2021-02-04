package com.home.automation.homeboard.controller;

import com.home.automation.homeboard.data.ErrorData;
import com.home.automation.homeboard.exception.BaseModelNotFoundException;
import com.home.automation.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
// TODO: read: https://www.baeldung.com/exception-handling-for-rest-with-spring
// TODO: read: https://www.baeldung.com/rest-api-error-handling-best-practices#:~:text=The%20simplest%20way%20we%20handle,to%20authenticate%20with%20the%20server
public class ErrorHandlingController {

    private static final Logger logger = LoggerFactory.getLogger(ErrorHandlingController.class);
//    private HomeErrorReportClient errorReportClient;

    @ExceptionHandler(value = {BaseModelNotFoundException.class})
    public ResponseEntity<ErrorData> handleBaseModelNotFoundException(final BaseModelNotFoundException notFound) {
        // TODO: add a error service to record and treat all errors
        //        errorReportClient.reportError(errorData);
        logger.warn("The following not found error occurred: " + notFound.getMessage());
        return new ResponseEntity<>(createNotFoundErrorData(notFound), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ErrorData> handleBaseModelNotFoundException(final Exception exc) {
        // TODO: add a error service to record and treat all errors
        //        errorReportClient.reportError(errorData);
        logger.error("A general error occurred: " + exc.getMessage());
        return new ResponseEntity<>(createInternalErrorData(exc), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ErrorData createInternalErrorData(Exception e) {
        final ErrorData errorData = new ErrorData();
        errorData.setErrorMessage("Ceva-i dubas, vb te rog cu marius");
        errorData.setErrorId(CommonUtils.createRandomSixDigitsId());
        errorData.setErrorMessage(e.getMessage());
        return errorData;
    }

    private ErrorData createNotFoundErrorData(BaseModelNotFoundException e) {
        final ErrorData errorData = new ErrorData();
        errorData.setEntityId(e.getBaseModelId());
        errorData.setErrorMessage(e.getMessage());
        errorData.setErrorId(CommonUtils.createRandomSixDigitsId());
        if (e.getBaseModelClass() != null) {
            errorData.setEntityInstance(e.getBaseModelClass().getSimpleName());
        }

        return errorData;
    }
}
