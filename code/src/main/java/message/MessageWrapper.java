package message;

public class MessageWrapper {

    private final String command;
    private final byte[] message;

    public MessageWrapper(String commad, byte[] message){
        this.command = commad;
        this.message = message;
    }

    public String getCommand() {
        return command;
    }

    public byte[] getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "MessageWrapper{" +
                "command='" + command + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
