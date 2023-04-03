/// fix the bug in the following method
public void captureArgumentsFrom(Invocation i) {
    int k = 0;
    for (Matcher m : matchers) {
        if (m instanceof CapturesArguments) { // buggy line is here
        
            ((CapturesArguments) m).captureFrom(i.getArguments()[k]);
        }
        k++;
    }
}

/// Change the buggy line to fix the bug:
public void captureArgumentsFrom(Invocation i) {
    int k = 0;
    for (Matcher m : matchers) {
