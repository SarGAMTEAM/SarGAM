/// fix the bug in the following method
public Map<String, Integer> getHeaderMap() {
    return new LinkedHashMap<String, Integer>(this.headerMap); // buggy line is here
}

/// Change the buggy line to fix the bug:
public Map<String, Integer> getHeaderMap() {
