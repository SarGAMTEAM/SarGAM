/// fix the bug in the following method
private void inlineAliases(GlobalNamespace namespace) {
    Deque<Name> workList = new ArrayDeque<Name>(namespace.getNameForest());
    while (!workList.isEmpty()) {
        Name name = workList.pop();
        if (name.type == Name.Type.GET || name.type == Name.Type.SET) {
            continue;
        }
        if (name.globalSets == 1 && name.localSets == 0 && // buggy line is here
         name.aliasingGets > 0) {
            List<Ref> refs = Lists.newArrayList(name.getRefs());
            for (Ref ref : refs) {
                if (ref.type == Type.ALIASING_GET && ref.scope.isLocal()) {
                    if (inlineAliasIfPossible(ref, namespace)) {
                        name.removeRef(ref);
                    }
                }
            }
        }
        if ((name.type == Name.Type.OBJECTLIT || name.type == Name.Type.FUNCTION) && name.aliasingGets == 0 && name.props != null) {
            workList.addAll(name.props);
        }
    }
}

/// Change the buggy line to fix the bug:
private void inlineAliases(GlobalNamespace namespace) {
    Deque<Name> workList = new ArrayDeque<Name>(namespace.getNameForest());
    while (!workList.isEmpty()) {
        Name name = workList.pop();
        if (name.type == Name.Type.GET || name.type == Name.Type.SET) {
            continue;
        }
