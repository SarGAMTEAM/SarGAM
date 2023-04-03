/// fix the bug in the following method
class HtmlTreeBuilder extends TreeBuilder {

private HtmlTreeBuilderState state; // the current state
private HtmlTreeBuilderState originalState; // original / marked state

private boolean baseUriSetFromDoc = false;
private Element headElement; // the current head element
private FormElement formElement; // the current form element
private Element contextElement; // fragment parse context -- could be null even if fragment parsing
private DescendableLinkedList<Element> formattingElements = new DescendableLinkedList<Element>(); // active (open) formatting elements
private List<Token.Character> pendingTableCharacters = new ArrayList<Token.Character>(); // chars in table to be shifted out

private boolean framesetOk = true; // if ok to go into frameset
private boolean fosterInserts = false; // if next inserts should be fostered
private boolean fragmentParsing = false; // if parsing a fragment of html

HtmlTreeBuilder() {}

@Override
Document parse(String input, String baseUri, ParseErrorList errors) {
    state = HtmlTreeBuilderState.Initial;
    return super.parse(input, baseUri, errors);
}

List<Node> parseFragment(String inputFragment, Element context, String baseUri, ParseErrorList errors) {
    // context may be null
    state = HtmlTreeBuilderState.Initial;
    initialiseParse(inputFragment, baseUri, errors);
    contextElement = context;
    fragmentParsing = true;
    Element root = null;

    if (context != null) {
        if (context.ownerDocument() != null) // quirks setup:
            doc.quirksMode(context.ownerDocument().quirksMode());

        // initialise the tokeniser state:
        String contextTag = context.tagName();
        if (StringUtil.in(contextTag, "title", "textarea"))
            tokeniser.transition(TokeniserState.Rcdata);
        else if (StringUtil.in(contextTag, "iframe", "noembed", "noframes", "style", "xmp"))
            tokeniser.transition(TokeniserState.Rawtext);
        else if (contextTag.equals("script"))
            tokeniser.transition(TokeniserState.ScriptData);
        else if (contextTag.equals(("noscript")))
            tokeniser.transition(TokeniserState.Data); // if scripting enabled, rawtext
        else if (contextTag.equals("plaintext"))
            tokeniser.transition(TokeniserState.Data);
        else
            tokeniser.transition(TokeniserState.Data); // default

        root = new Element(Tag.valueOf("html"), baseUri);
        doc.appendChild(root);
        stack.push(root);
        resetInsertionMode();

        // setup form element to nearest form on context (up ancestor chain). ensures form controls are associated
        // with form correctly
        Elements contextChain = context.parents();
        contextChain.add(0, context);
        for (Element parent: contextChain) {
            if (parent instanceof FormElement) {
                formElement = (FormElement) parent;
                break;
            }
        }
    }

    runParser();
    if (context != null)
        return root.childNodes();
    else
        return doc.childNodes();
}

@Override
protected boolean process(Token token) {
    currentToken = token;
    return this.state.process(token, this);
}

boolean process(Token token, HtmlTreeBuilderState state) {
    currentToken = token;
    return state.process(token, this);
}

void transition(HtmlTreeBuilderState state) {
    this.state = state;
}

HtmlTreeBuilderState state() {
    return state;
}

void markInsertionMode() {
    originalState = state;
}

HtmlTreeBuilderState originalState() {
    return originalState;
}

void framesetOk(boolean framesetOk) {
    this.framesetOk = framesetOk;
}

boolean framesetOk() {
    return framesetOk;
}

Document getDocument() {
    return doc;
}

String getBaseUri() {
    return baseUri;
}

void maybeSetBaseUri(Element base) {
    if (baseUriSetFromDoc) // only listen to the first <base href> in parse
        return;

    String href = base.absUrl("href");
    if (href.length() != 0) { // ignore <base target> etc
        baseUri = href;
        baseUriSetFromDoc = true;
        doc.setBaseUri(href); // set on the doc so doc.createElement(Tag) will get updated base, and to update all descendants
    }
}

boolean isFragmentParsing() {
    return fragmentParsing;
}

void error(HtmlTreeBuilderState state) {
    if (errors.canAddError())
        errors.add(new ParseError(reader.pos(), "Unexpected token [%s] when in state [%s]", currentToken.tokenType(), state));
}

Element insert(Token.StartTag startTag) {
    // handle empty unknown tags
    // when the spec expects an empty tag, will directly hit insertEmpty, so won't generate this fake end tag.
    if (startTag.isSelfClosing()) {
        Element el = insertEmpty(startTag);
        stack.add(el);
        // buggy line is here
        tokeniser.emit(new Token.EndTag(el.tagName()));  // ensure we get out of whatever state we are in. emitted for yielded processing
        return el;
    }
    
    Element el = new Element(Tag.valueOf(startTag.name()), baseUri, startTag.attributes);
    insert(el);
    return el;
    }
}

/// Change the buggy line to fix the bug:
class HtmlTreeBuilder extends TreeBuilder {

private HtmlTreeBuilderState state; // the current state
private HtmlTreeBuilderState originalState; // original / marked state

private boolean baseUriSetFromDoc = false;
private Element headElement; // the current head element
private FormElement formElement; // the current form element
private Element contextElement; // fragment parse context -- could be null even if fragment parsing
private DescendableLinkedList<Element> formattingElements = new DescendableLinkedList<Element>(); // active (open) formatting elements
private List<Token.Character> pendingTableCharacters = new ArrayList<Token.Character>(); // chars in table to be shifted out

private boolean framesetOk = true; // if ok to go into frameset
private boolean fosterInserts = false; // if next inserts should be fostered
private boolean fragmentParsing = false; // if parsing a fragment of html

HtmlTreeBuilder() {}

@Override
Document parse(String input, String baseUri, ParseErrorList errors) {
    state = HtmlTreeBuilderState.Initial;
    return super.parse(input, baseUri, errors);
}

List<Node> parseFragment(String inputFragment, Element context, String baseUri, ParseErrorList errors) {
    // context may be null
    state = HtmlTreeBuilderState.Initial;
    initialiseParse(inputFragment, baseUri, errors);
    contextElement = context;
    fragmentParsing = true;
    Element root = null;

    if (context != null) {
        if (context.ownerDocument() != null) // quirks setup:
            doc.quirksMode(context.ownerDocument().quirksMode());

        // initialise the tokeniser state:
        String contextTag = context.tagName();
        if (StringUtil.in(contextTag, "title", "textarea"))
            tokeniser.transition(TokeniserState.Rcdata);
        else if (StringUtil.in(contextTag, "iframe", "noembed", "noframes", "style", "xmp"))
            tokeniser.transition(TokeniserState.Rawtext);
        else if (contextTag.equals("script"))
            tokeniser.transition(TokeniserState.ScriptData);
        else if (contextTag.equals(("noscript")))
            tokeniser.transition(TokeniserState.Data); // if scripting enabled, rawtext
        else if (contextTag.equals("plaintext"))
            tokeniser.transition(TokeniserState.Data);
        else
            tokeniser.transition(TokeniserState.Data); // default

        root = new Element(Tag.valueOf("html"), baseUri);
        doc.appendChild(root);
        stack.push(root);
        resetInsertionMode();

        // setup form element to nearest form on context (up ancestor chain). ensures form controls are associated
        // with form correctly
        Elements contextChain = context.parents();
        contextChain.add(0, context);
        for (Element parent: contextChain) {
            if (parent instanceof FormElement) {
                formElement = (FormElement) parent;
                break;
            }
        }
    }

    runParser();
    if (context != null)
        return root.childNodes();
    else
        return doc.childNodes();
}

@Override
protected boolean process(Token token) {
    currentToken = token;
    return this.state.process(token, this);
}

boolean process(Token token, HtmlTreeBuilderState state) {
    currentToken = token;
    return state.process(token, this);
}

void transition(HtmlTreeBuilderState state) {
    this.state = state;
}

HtmlTreeBuilderState state() {
    return state;
}

void markInsertionMode() {
    originalState = state;
}

HtmlTreeBuilderState originalState() {
    return originalState;
}

void framesetOk(boolean framesetOk) {
    this.framesetOk = framesetOk;
}

boolean framesetOk() {
    return framesetOk;
}

Document getDocument() {
    return doc;
}

String getBaseUri() {
    return baseUri;
}

void maybeSetBaseUri(Element base) {
    if (baseUriSetFromDoc) // only listen to the first <base href> in parse
        return;

    String href = base.absUrl("href");
    if (href.length() != 0) { // ignore <base target> etc
        baseUri = href;
        baseUriSetFromDoc = true;
        doc.setBaseUri(href); // set on the doc so doc.createElement(Tag) will get updated base, and to update all descendants
    }
}

boolean isFragmentParsing() {
    return fragmentParsing;
}

void error(HtmlTreeBuilderState state) {
    if (errors.canAddError())
        errors.add(new ParseError(reader.pos(), "Unexpected token [%s] when in state [%s]", currentToken.tokenType(), state));
}

Element insert(Token.StartTag startTag) {
    // handle empty unknown tags
    // when the spec expects an empty tag, will directly hit insertEmpty, so won't generate this fake end tag.
    if (startTag.isSelfClosing()) {
        Element el = insertEmpty(startTag);
        stack.add(el);
        