/// fix the bug in the following method
/**
    * Excel file format (using a comma as the value delimiter). Note that the actual value delimiter used by Excel is
    * locale dependent, it might be necessary to customize this format to accommodate to your regional settings.
    *
    * <p>
    * For example for parsing or generating a CSV file on a French system the following format will be used:
    * </p>
    *
    * <pre>
    * CSVFormat fmt = CSVFormat.EXCEL.withDelimiter(';');
    * </pre>
    *
    * <p>
    * Settings are:
    * </p>
    * <ul>
    *   <li>{@link #withDelimiter(char) withDelimiter(',')}</li>
    *   <li>{@link #withQuoteChar(String) withQuoteChar('"')}</li>
    *   <li>{@link #withRecordSeparator(String) withRecordSeparator("\r\n")}</li>
    *   <li>{@link #withIgnoreEmptyLines(boolean) withIgnoreEmptyLines(false)}</li>
    *   <li>{@link #withAllowMissingColumnNames(boolean) withAllowMissingColumnNames(true)}</li>
    * </ul>
    * <p>
    * Note: this is currently like {@link #RFC4180} plus {@link #withAllowMissingColumnNames(boolean) withAllowMissingColumnNames(true)}.
    * </p>
    */
public static final CSVFormat EXCEL = DEFAULT.withIgnoreEmptyLines(false); // buggy line is here

/// Change the buggy line to fix the bug:
/**
    * Excel file format (using a comma as the value delimiter). Note that the actual value delimiter used by Excel is
    * locale dependent, it might be necessary to customize this format to accommodate to your regional settings.
    *
    * <p>
    * For example for parsing or generating a CSV file on a French system the following format will be used:
    * </p>
    *
    * <pre>
    * CSVFormat fmt = CSVFormat.EXCEL.withDelimiter(';');
    * </pre>
    *
    * <p>
    * Settings are:
    * </p>
    * <ul>
    *   <li>{@link #withDelimiter(char) withDelimiter(',')}</li>
    *   <li>{@link #withQuoteChar(String) withQuoteChar('"')}</li>
    *   <li>{@link #withRecordSeparator(String) withRecordSeparator("\r\n")}</li>
    *   <li>{@link #withIgnoreEmptyLines(boolean) withIgnoreEmptyLines(false)}</li>
    *   <li>{@link #withAllowMissingColumnNames(boolean) withAllowMissingColumnNames(true)}</li>
    * </ul>
    * <p>
    * Note: this is currently like {@link #RFC4180} plus {@link #withAllowMissingColumnNames(boolean) withAllowMissingColumnNames(true)}.
    * </p>
    */
