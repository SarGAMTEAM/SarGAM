/// fix the bug in the following method
public final Object computeValue(EvalContext context) {
    return compute(args[0].computeValue(context), args[1].computeValue(context)) // buggy line is here
     ? Boolean.TRUE : Boolean.FALSE;
}

/// Change the buggy line to fix the bug:
public final Object computeValue(EvalContext context) {
