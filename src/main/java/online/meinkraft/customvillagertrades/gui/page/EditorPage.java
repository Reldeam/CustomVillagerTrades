package online.meinkraft.customvillagertrades.gui.page;

import online.meinkraft.customvillagertrades.gui.Editor;

public class EditorPage extends Page {

    public EditorPage(Editor editor, String title) {
        super(editor, title);
    }

    public Editor getEditor() {
        return (Editor) getGUI();
    }
    
}
