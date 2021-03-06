package de.dfki.vsm.editor.script;

import de.dfki.vsm.editor.util.VisualisationTask;
import de.dfki.vsm.model.acticon.ActiconAction;
import de.dfki.vsm.model.configs.ProjectPreferences;
import de.dfki.vsm.model.gesticon.GesticonGesture;
import de.dfki.vsm.model.visicon.VisiconViseme;
import de.dfki.vsm.util.evt.EventCaster;
import de.dfki.vsm.util.evt.EventListener;
import de.dfki.vsm.util.evt.EventObject;
import de.dfki.vsm.util.log.LOGDefaultLogger;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.dnd.InvalidDnDOperationException;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import javax.swing.BorderFactory;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;

/**
 * @author Gregor Mehlmann
 */
public class ScriptEditorPane extends JEditorPane
        implements EventListener, Observer {

    // The System Logger
    private final LOGDefaultLogger mLogger
            = LOGDefaultLogger.getInstance();
    // The Event Caster
    private final EventCaster mEventCaster
            = EventCaster.getInstance();
    // Init Drag & Drop Support
    private DropTargetListener mDropListener;
    private DropTarget mDropTarget;
    private int mValidActions;
    // Activity monitor
    private Font mFont = new Font("Courier New", Font.PLAIN, 12);
    private final ProjectPreferences mPreferences;

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    // Construct The Text Pane
    public ScriptEditorPane(ProjectPreferences preferences) {
        mPreferences = preferences;
        mFont = new Font(mPreferences.sSCRIPT_FONT_TYPE, Font.PLAIN, mPreferences.sSCRIPT_FONT_SIZE);
        setFont(mFont);
        // Set Lexxer And Editor
        setEditorKit(new ScriptEditorKit());
        // Set An Empty Border
        setBorder(BorderFactory.createEmptyBorder());
        // Init Drag & Drop
        initDnDSupport();
        // Allow The Focus To Leave The Pane
        // Only If The Script Syntax Is Valid
        setInputVerifier(new InputVerifier() {

            @Override
            public boolean verify(final JComponent anInput) {
                // Check If The Script Is Valid
                return true;
            }
        });

        // Register Document Actions
        //registerKeyboardAction(getDocument().getUndoAction(), KeyStroke.getKeyStroke(
        //        KeyEvent.VK_Z, InputEvent.CTRL_MASK), JComponent.WHEN_FOCUSED);
        //registerKeyboardAction(getDocument().getRedoAction(), KeyStroke.getKeyStroke(
        //        KeyEvent.VK_Y, InputEvent.CTRL_MASK), JComponent.WHEN_FOCUSED);
        // Register As Event Listener
        mEventCaster.append(this);

    }

    /*
     // Paint The Text Pane
     @Override
     protected final synchronized void paintComponent(final Graphics graphics) {
     super.paintComponent(graphics);
        
     // If there are visualization tasks then visualize them
     if (!mVisualisationTasks.isEmpty()) {
     // TODO: why create() and clone()?
     Graphics2D graphics2D = (Graphics2D) graphics.create();
     ArrayList<ScriptEditPanel.HighlightTask> visTasks = (ArrayList<ScriptEditPanel.HighlightTask>) mVisualisationTasks.clone();

     for (ScriptEditPanel.HighlightTask highlightTask : visTasks) {
     // draw activity cue
     if (highlightTask.getActivityTime() > 20) {
     graphics2D.setColor(highlightTask.mColor);
     graphics2D.fillRect(
     highlightTask.getXPos(),
     highlightTask.getYPos(),
     highlightTask.getWidth(),
     highlightTask.getHeight());
     } else {
     // Draw the fading away of the highlight
     Color c = new Color(
     highlightTask.mColor.getRed(),
     highlightTask.mColor.getGreen(),
     highlightTask.mColor.getBlue(),
     highlightTask.mColor.getTransparency() - (highlightTask.mColor.getTransparency() - 5 * highlightTask.getActivityTime()));
     graphics2D.setColor(c);
     graphics2D.fillRect(
     highlightTask.getXPos(),
     highlightTask.getYPos(),
     highlightTask.getWidth(),
     highlightTask.getHeight());
     }
     }
     graphics2D.dispose();
     }
     }*/
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    // Init Drag & Drop Support
    private void initDnDSupport() {
        mValidActions = DnDConstants.ACTION_COPY;
        mDropListener = new DropTargetListener() {

            @Override
            public void dragEnter(final DropTargetDragEvent dtde) {
                // Do Nothing Here
            }

            @Override
            public void dragExit(final DropTargetEvent dte) {
                // Do Nothing Here
            }

            @Override
            public void dropActionChanged(final DropTargetDragEvent dtde) {
                // Do Nothing Here
            }

            @Override
            public void dragOver(final DropTargetDragEvent dtde) {
                Object data = null;
                DataFlavor flavor = null;
                try {
                    try {
                        flavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType);
                    } catch (ClassNotFoundException exc) {
                        exc.printStackTrace();
                    }
                    data = dtde.getTransferable().getTransferData(flavor);
                } catch (UnsupportedFlavorException | IOException exc) {
                    exc.printStackTrace();
                    dtde.rejectDrag();
                }

                if (data instanceof ActiconAction) {
                    dtde.acceptDrag(dtde.getDropAction());
                    setCaretPosition(viewToModel(dtde.getLocation()));

                } else if (data instanceof GesticonGesture) {
                    dtde.acceptDrag(dtde.getDropAction());
                    setCaretPosition(viewToModel(dtde.getLocation()));

                } else if (data instanceof VisiconViseme) {
                    dtde.acceptDrag(dtde.getDropAction());
                    setCaretPosition(viewToModel(dtde.getLocation()));

                } else {
                    dtde.rejectDrag();
                }
            }

            ////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////////////////////
            @Override
            public final void drop(final DropTargetDropEvent dtde) {
                Object data = null;
                DataFlavor flavor = null;
                try {
                    try {
                        // Get The Data Flavour
                        flavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType);
                    } catch (ClassNotFoundException exc) {
                        exc.printStackTrace();
                    }
                    // Transfer The Data
                    data = dtde.getTransferable().getTransferData(flavor);
                } catch (UnsupportedFlavorException | IOException exc) {
                    dtde.rejectDrop();
                }
                // Check The Data Type
                if (data instanceof ActiconAction) {
                    // Compute the Drop Position
                    int dropPosition = viewToModel(dtde.getLocation());
                    // Insert The Acticon Action
                    try {
                        // Cast The Data To An Action
                        ActiconAction action = (ActiconAction) data;
                        // Insert The Text In Document
                        getDocument().insertString(dropPosition, action.toScript(), null);
                        // Set The Caret Position
                        setCaretPosition(dropPosition);
                        // Accept the drop
                        dtde.acceptDrop(mValidActions);
                        dtde.getDropTargetContext().dropComplete(true);
                    } catch (BadLocationException | InvalidDnDOperationException exc) {
                        exc.printStackTrace();
                        // Reject The Drop
                        dtde.rejectDrop();
                    }
                } else if (data instanceof GesticonGesture) {
                    // Compute the Drop Position
                    int dropPosition = viewToModel(dtde.getLocation());
                    // Insert The Gesticon Gesture
                    try {
                        // Cast The Data To A Gesture
                        GesticonGesture gesture = (GesticonGesture) data;
                        // Insert The Text In Document
                        getDocument().insertString(dropPosition, gesture.toScript(), null);
                        // Set The Caret Position
                        setCaretPosition(dropPosition);
                        // Accept the drop
                        dtde.acceptDrop(mValidActions);
                        dtde.getDropTargetContext().dropComplete(true);
                    } catch (BadLocationException | InvalidDnDOperationException exc) {
                        exc.printStackTrace();
                        // Reject The Drop
                        dtde.rejectDrop();
                    }
                } else if (data instanceof VisiconViseme) {
                    // Compute the Drop Position
                    int modelDropPosition = viewToModel(dtde.getLocation());
                    // TODO: Do Something Here
                } else {
                    // Reject The Drop
                    dtde.rejectDrop();
                }
            }
        };
        // Set The Drop Target
        mDropTarget = new DropTarget(this, mDropListener);
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void update(final Observable obs, final Object obj) {
        // Do Nothing
        mFont = new Font(mPreferences.sSCRIPT_FONT_TYPE, Font.PLAIN, mPreferences.sSCRIPT_FONT_SIZE);        
        setFont(mFont);        
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public void update(final EventObject event) {
        // Do Nothing
        mFont = new Font(mPreferences.sSCRIPT_FONT_TYPE, Font.PLAIN, mPreferences.sSCRIPT_FONT_SIZE);        
        setFont(mFont);
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    private class HighlightTask extends VisualisationTask {

        private Color mColor = null;
        private Rectangle mRect = null;

        public HighlightTask(final int steps, final JComponent c, final Color col, final Rectangle rect) {
            super(steps, c);
            mColor = col;
            mRect = rect;
        }

        public int getXPos() {
            return mRect.x;
        }

        public int getYPos() {
            return mRect.y;
        }

        public int getWidth() {
            return mRect.width;
        }

        public int getHeight() {
            return mRect.height;
        }
    }
}
