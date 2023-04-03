/// fix the bug in the following method
@Override
public Element clone() {
    Element clone = (Element) super.clone();
    clone.classNames(); // buggy line is here
    return clone;
}

/// Change the buggy line to fix the bug:
@Override
public Element clone() {
    Element clone = (Element) super.clone();
