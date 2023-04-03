/// fix the bug in the following method
public String html() {
    StringBuilder accum = new StringBuilder();
    html(accum);
    return accum.toString().trim(); // buggy line is here
}

/// Change the buggy line to fix the bug:
public String html() {
    StringBuilder accum = new StringBuilder();
    html(accum);
