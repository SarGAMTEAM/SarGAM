/// fix the bug in the following method
static float toJavaVersionInt(String version) { // buggy line is here
s
    return toVersionInt(toJavaVersionIntArray(version, JAVA_VERSION_TRIM_SIZE));
}

/// Change the buggy line to fix the bug:
