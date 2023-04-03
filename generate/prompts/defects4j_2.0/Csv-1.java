/// fix the bug in the following method
@Override
public int read() throws IOException {
    int current = super.read();
    if (current == '\n') { // buggy line is here
        lineCounter++;
    }
    lastChar = current;
    return lastChar;
}

/// Change the buggy line to fix the bug:
@Override
public int read() throws IOException {
    int current = super.read();
