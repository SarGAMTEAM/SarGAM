/// fix the bug in the following method
public String generateToolTipFragment(String toolTipText) {
    return " title=\"" + toolTipText // buggy line is here
     + "\" alt=\"\"";
}

/// Change the buggy line to fix the bug:
public String generateToolTipFragment(String toolTipText) {
