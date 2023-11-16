package com.bankaccount.service.enumeration;

public enum BusinessMessage {
    
    GENERICERROR(10000, "Generic error"),
    MALFORMEDURL(10001, "Malformed URL"),
    TIMEOUT(10002, "Timeout error"),
    ACCESSDENIED(10003, "Access denied"),
    INVALID_REQUEST(10004, "Invalid Request"),
    UNEXPECTED_RESPONSE(10005, "Unexpected response");

    private final int value;
    private final String message;
    
    BusinessMessage(int value, String message) {
        this.value = value;
        this.message = message;
    }
    
    public static BusinessMessage getEnum(int value) {
        for (BusinessMessage enm : BusinessMessage.values())
            if (value == enm.getValue())
                return enm;
        return null;
    }
    
    public int getValue() { return value; }
    
    public String getMessage() { return message; }
    
    public String toString() { return value + " - " + message; }
  
}
