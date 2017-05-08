package com.joeldholmes.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import io.katharsis.errorhandling.ErrorData;
import io.katharsis.errorhandling.ErrorResponse;
import io.katharsis.errorhandling.mapper.ExceptionMapper;

@Component
public class BaseApiExceptionMapper implements ExceptionMapper<APIException> {

	protected Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public ErrorResponse toErrorResponse(APIException exception) {
		
		if (exception.getCause() != null) {
			log.error("Exception occurred {}", exception.getCause());
		}
		return ErrorResponse.builder().
				setStatus(exception.getStatus().value())
				.setSingleErrorData(ErrorData.builder()
						.setCode(exception.getErrorCode())
						.setDetail(exception.getMessage()).build()).build();
				
	}

	@Override
	public APIException fromErrorResponse(ErrorResponse errorResponse) {
		return new APIException("ERROR");
	}

	@Override
	public boolean accepts(ErrorResponse errorResponse) {
		return errorResponse.getHttpStatus() == 500;
	}

}
