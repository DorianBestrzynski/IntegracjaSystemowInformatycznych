package com.zpi.notification.exceptions;

public class ExceptionsInfo {
    public final static String ILLEGAL_ARGUMENT_FEIGN = "In request there were illegal argument via feign";

    public final static String EXCEPTION_FEIGN = "There was exception while feign communication";

    public final static String RESOURCE_NOT_FOUND_FEIGN = "Resource was not found";

    public final static String UNPROCESSABLE_ENTITY_FEIGN = "Given entity was unprocessable";

    public final static String FORBIDDEN_FEIGN = "You do not have access to this resource or action";

    public final static String SERVICE_UNAVAILABLE_FEIGN = "Requested service is currently unavailable";

    public final static String UNAUTHORIZED_REQUEST_FEIGN = "Request was unauthorized ";

    public final static String GROUP_CREATION_VALIDATION_ERROR = "Error while validating group";

}
