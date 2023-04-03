/// fix the bug in the following method
public void describeTo(Description description) {
    description.appendText("same(");
    appendQuoting(description);
    description.appendText(wanted.toString()); // buggy line is here
    
    appendQuoting(description);
    description.appendText(")");
}

/// Change the buggy line to fix the bug:
public void describeTo(Description description) {
    description.appendText("same(");
    appendQuoting(description);
