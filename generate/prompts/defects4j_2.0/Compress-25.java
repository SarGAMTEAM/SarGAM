/// fix the bug in the following method
public ZipArchiveInputStream(InputStream inputStream,
                                String encoding,
                                boolean useUnicodeExtraFields,
                                boolean allowStoredEntriesWithDataDescriptor) {
    zipEncoding = ZipEncodingHelper.getZipEncoding(encoding);
    this.useUnicodeExtraFields = useUnicodeExtraFields;
    in = new PushbackInputStream(inputStream, buf.capacity());
    this.allowStoredEntriesWithDataDescriptor =
        allowStoredEntriesWithDataDescriptor;
    // haven't read anything so far
    // buggy line is here
}

/// Change the buggy line to fix the bug:
public ZipArchiveInputStream(InputStream inputStream,
                                String encoding,
                                boolean useUnicodeExtraFields,
                                boolean allowStoredEntriesWithDataDescriptor) {
    zipEncoding = ZipEncodingHelper.getZipEncoding(encoding);
    this.useUnicodeExtraFields = useUnicodeExtraFields;
    in = new PushbackInputStream(inputStream, buf.capacity());
    this.allowStoredEntriesWithDataDescriptor =
        allowStoredEntriesWithDataDescriptor;
        