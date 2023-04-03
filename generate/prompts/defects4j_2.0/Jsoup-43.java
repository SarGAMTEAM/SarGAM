/// fix the bug in the following method
private static <E extends Element> Integer indexInList(Element search, List<E> elements) {
    Validate.notNull(search);
    Validate.notNull(elements);
    for (int i = 0; i < elements.size(); i++) {
        E element = elements.get(i);
        if (element.equals(search)) // buggy line is here
            return i;
    }
    return null;
}

/// Change the buggy line to fix the bug:
private static <E extends Element> Integer indexInList(Element search, List<E> elements) {
    Validate.notNull(search);
    Validate.notNull(elements);
    for (int i = 0; i < elements.size(); i++) {
        E element = elements.get(i);
