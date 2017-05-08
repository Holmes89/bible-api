package com.joeldholmes.exceptions;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import io.katharsis.errorhandling.ErrorData;
import io.katharsis.errorhandling.ErrorResponse;
import io.katharsis.errorhandling.mapper.ExceptionMapper;

@Component
public class ApiMultipleExceptionMapper implements ExceptionMapper<ApiMultipleException> {

	@Override
	public ErrorResponse toErrorResponse(ApiMultipleException exception) {
			
		List<ErrorData> errorData = new ArrayList<ErrorData>();
		String code = exception.getCode();
		
		for(String message: exception.getMessageArray()){
			errorData.add(ErrorData.builder().setCode(code).setDetail(message).build());
		}
		
		return ErrorResponse.builder().
				setStatus(exception.getStatus().value())
				.setErrorData(errorData).build();
				
	}

	@Override
	public ApiMultipleException fromErrorResponse(ErrorResponse errorResponse) {
		return new ApiMultipleException("ERROR");
	}

	@Override
	public boolean accepts(ErrorResponse errorResponse) {
		return errorResponse.getHttpStatus() == 500;
	}

}
