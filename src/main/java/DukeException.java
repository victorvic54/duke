public class DukeException extends Exception {
    /**
     * To handle several logic error from the program that is thrown to this exception.
     *
     * @param message the details that the developer used to notify the users of an error.
     */
    public DukeException(String message) {
        super(message);
    }
}