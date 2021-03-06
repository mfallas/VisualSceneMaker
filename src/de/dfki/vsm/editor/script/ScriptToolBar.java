package de.dfki.vsm.editor.script;

import de.dfki.vsm.editor.util.Preferences;
import de.dfki.vsm.model.configs.ProjectPreferences;
import de.dfki.vsm.util.ios.ResourceLoader;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JToolBar;

/**
 * @author Patrick Gebhard
 */
public class ScriptToolBar extends JToolBar {

    private JButton mGesticonButton;
    private ScriptEditorPanel mParent;
     private ProjectPreferences mPreferences;

    public ScriptToolBar(ScriptEditorPanel parent) {
        super("Scenes Tool Bar", JToolBar.HORIZONTAL);
        mParent = parent;
        mPreferences =  mParent.getPreferences();
        setFloatable(false);
        setRollover(true);
        setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
        initComponents();        
    }

    private void initComponents() {
        add(Box.createHorizontalStrut(2));
        mGesticonButton = add(new AbstractAction("ACTION_SHOW_ELEMENTS",
                Boolean.valueOf(mPreferences.getProperty("showsceneelements"))
                ? ResourceLoader.loadImageIcon("/res/img/new/less.png")
                : ResourceLoader.loadImageIcon("/res/img/new/more.png")) {

            public void actionPerformed(ActionEvent evt) {
                boolean state = mParent.showElementDisplay();
                changeGesticonDisplayButtonState(state);
                revalidate();
                repaint();
            }
        });
        sanitizeTinyButton(mGesticonButton);
        add(Box.createHorizontalGlue());
    }

    private void sanitizeTinyButton(JButton b) {
        Dimension bDim = new Dimension(22, 22);
        b.setMinimumSize(bDim);
        b.setMaximumSize(bDim);
        b.setPreferredSize(bDim);
        b.setOpaque(false);
        b.setBorder(BorderFactory.createEmptyBorder());
    }

    private void changeGesticonDisplayButtonState(boolean state) {
        if (state) {
            mGesticonButton.setIcon(ResourceLoader.loadImageIcon("/res/img/new/less.png"));
            mPreferences.setProperty("showsceneelements", "true");
            mPreferences.save(mParent.getPreferencesFileName());
 
        } else {
            mGesticonButton.setIcon(ResourceLoader.loadImageIcon("/res/img/new/more.png"));
            mPreferences.setProperty("showsceneelements", "false");
            mPreferences.save(mParent.getPreferencesFileName());
        }
    }
}
