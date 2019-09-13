package splendor.common.util;

import splendor.common.util.Constants.ProtocolAction;
import java.io.Serializable;

public class ProtocolMessage implements Serializable {
    private final ProtocolAction messageType;
    private final String message;
    private final boolean valid;

    public ProtocolMessage(ProtocolAction messageType, String message, boolean valid) {
        this.messageType = messageType;
        this.message = encode(message);
        this.valid = valid;
    }

    public ProtocolMessage(ProtocolAction messageType, String message) {
        this.messageType = messageType;
        this.message = message;
        this.valid = false;
    }

    public ProtocolAction getMessageType() {
        return messageType;
    }

    public String getMessage() {
        return message;
    }

    public boolean isValid() {
        return valid;
    }

    private String encode(String s) {
        return s;
    }
}
