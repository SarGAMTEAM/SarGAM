/// fix the bug in the following method
boolean matchesLetter() {
    if (isEmpty())
        return false;
    char c = input[pos];
    return (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z'); // buggy line is here
}

/// Change the buggy line to fix the bug:
boolean matchesLetter() {
    if (isEmpty())
        return false;
    char c = input[pos];
