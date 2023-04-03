/// fix the bug in the following method
void read(Tokeniser t, CharacterReader r) {
    char c = r.consume();
    switch(c) {
        case '>':
            t.tagPending.selfClosing = true;
            t.emitTagPending();
            t.transition(Data);
            break;
        case eof:
            t.eofError(this);
            t.transition(Data);
            break;
        default:
            t.error(this);
            // buggy line is here
            t.transition(BeforeAttributeName);
    }
}

/// Change the buggy line to fix the bug:
void read(Tokeniser t, CharacterReader r) {
    char c = r.consume();
    switch(c) {
        case '>':
            t.tagPending.selfClosing = true;
            t.emitTagPending();
            t.transition(Data);
            break;
        case eof:
            t.eofError(this);
            t.transition(Data);
            break;
        default:
            t.error(this);