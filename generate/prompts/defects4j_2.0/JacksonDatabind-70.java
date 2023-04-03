/// fix the bug in the following method
public void remove(SettableBeanProperty propToRm) {
    ArrayList<SettableBeanProperty> props = new ArrayList<SettableBeanProperty>(_size);
    String key = getPropertyName(propToRm);
    boolean found = false;
    for (int i = 1, end = _hashArea.length; i < end; i += 2) {
        SettableBeanProperty prop = (SettableBeanProperty) _hashArea[i];
        if (prop == null) {
            continue;
        }
        if (!found) {
            found = key.equals(prop.getName()); // buggy line is here
            if (found) {
                _propsInOrder[_findFromOrdered(prop)] = null;
                continue;
            }
        }
        props.add(prop);
    }
    if (!found) {
        throw new NoSuchElementException("No entry '" + propToRm.getName() + "' found, can't remove");
    }
    init(props);
}

/// Change the buggy line to fix the bug:
public void remove(SettableBeanProperty propToRm) {
    ArrayList<SettableBeanProperty> props = new ArrayList<SettableBeanProperty>(_size);
    String key = getPropertyName(propToRm);
    boolean found = false;
    for (int i = 1, end = _hashArea.length; i < end; i += 2) {
        SettableBeanProperty prop = (SettableBeanProperty) _hashArea[i];
        if (prop == null) {
            continue;
        }
        if (!found) {
