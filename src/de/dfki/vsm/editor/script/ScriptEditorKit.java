package de.dfki.vsm.editor.script;

import de.dfki.vsm.model.script.ScriptLexxer;
import de.dfki.vsm.util.syn.SyntaxEditorKit;

/**
 * @author Gregor Mehlmann
 */
public class ScriptEditorKit extends SyntaxEditorKit {

    public ScriptEditorKit() {
        super(new ScriptLexxer(true, true, true));
    }
}
