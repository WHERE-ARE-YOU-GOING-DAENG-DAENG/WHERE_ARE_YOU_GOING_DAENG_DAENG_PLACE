package com.daengdaeng_eodiga.project.Global.exception;

import com.daengdaeng_eodiga.project.Global.enums.ErrorCode;

public class EventTicketLimitException extends BusinessException {
	public EventTicketLimitException() {
		super(
			ErrorCode.EVENT_TICKET_LIMIT_EXCEEDED.getErrorCode(),
			ErrorCode.EVENT_TICKET_LIMIT_EXCEEDED.getMessage()
		);
	}
}