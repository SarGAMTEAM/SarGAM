/// fix the bug in the following method
private void _verifySharing() {
    if (_hashShared) {
        _hashArea = Arrays.copyOf(_hashArea, _hashArea.length);
        _names = Arrays.copyOf(_names, _names.length);
        _hashShared = false;
        // buggy line is here
    }
    if (_needRehash) {
        rehash();
    }
}

/// Change the buggy line to fix the bug:
private void _verifySharing() {
    if (_hashShared) {
        _hashArea = Arrays.copyOf(_hashArea, _hashArea.length);
        _names = Arrays.copyOf(_names, _names.length);
        _hashShared = false;
        