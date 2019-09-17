package splendor.common.util;

import splendor.common.util.Constants.ProtocolAction;
import java.io.Serializable;

public class ProtocolMessage implements Serializable {
    private final ProtocolAction messageType;
    private final byte[] message;
    private final boolean valid;

    public ProtocolMessage(ProtocolAction messageType, byte[] message, boolean valid) {
        this.messageType = messageType;
        this.message = message;
        this.valid = valid;
    }

    public ProtocolMessage(ProtocolAction messageType, byte[] message) {
        this.messageType = messageType;
        this.message = message;
        this.valid = false;
    }

    public ProtocolAction getMessageType() {
        return messageType;
    }

    public byte[] getMessage() {
        return message;
    }

    public boolean isValid() {
        return valid;
    }
}
