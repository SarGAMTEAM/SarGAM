/// fix the bug in the following method
public static String encodeBase64String(byte[] binaryData) {
    return StringUtils.newStringUtf8(encodeBase64(binaryData, true)); // buggy line is here
}

/// Change the buggy line to fix the bug:
public static String encodeBase64String(byte[] binaryData) {
