package fantastic.faniverse.Exception;

public class NoBidderException extends RuntimeException {
    public NoBidderException() {
        super("입찰자가 없어서 경매가 취소됩니다.");
    }
}