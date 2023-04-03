/// fix the bug in the following method
public void close() throws IOException {
    if (!this.closed) {
        // buggy line is here
        super.close();
        this.closed = true;
    }
}

/// Change the buggy line to fix the bug:
public void close() throws IOException {
    if (!this.closed) {
        