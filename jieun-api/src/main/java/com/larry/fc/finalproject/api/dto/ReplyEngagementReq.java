package com.larry.fc.finalproject.api.dto;

import com.larry.fc.finalproject.core.domain.RequestReplyType;

public class ReplyEngagementReq {
    private RequestReplyType type;    //REJECT, ACCEPT, RequestStatus 안쓴 이유는 request 안쓰는 타입이 하나 있어서

    public ReplyEngagementReq() {

    }

    public ReplyEngagementReq(RequestReplyType type) {
        this.type = type;
    }

    public RequestReplyType getType() {
        return type;
    }
}
