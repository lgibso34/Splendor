package splendor.common.util;

public class Message {
    private byte[] message;

    public Message(byte[] message){
        this.message = message;
    }

    public byte[] getMessage() {
        return message;
    }
}
