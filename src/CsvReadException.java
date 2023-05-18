public class CsvReadException extends Exception {

    private String data;
    public CsvReadException(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return String.format("CsvReadException: %s\n", data);
    }
}
