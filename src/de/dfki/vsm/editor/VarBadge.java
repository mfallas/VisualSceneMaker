package de.dfki.vsm.editor;

import de.dfki.vsm.editor.event.VariableChangedEvent;
import static de.dfki.vsm.editor.util.Preferences.sSHOW_VARIABLE_BADGE_ON_WORKSPACE;
import de.dfki.vsm.model.sceneflow.SuperNode;
import de.dfki.vsm.model.sceneflow.definition.VarDef;
import de.dfki.vsm.model.sceneflow.graphics.node.Position;
import de.dfki.vsm.util.TextFormat;
import de.dfki.vsm.util.evt.EventListener;
import de.dfki.vsm.util.evt.EventObject;
import de.dfki.vsm.util.log.LOGDefaultLogger;
import de.dfki.vsm.util.tpl.TPLTuple;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.font.TextLayout;
import java.text.AttributedString;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import javax.swing.JComponent;

/**
 * @author Gregor Mehlmann
 * @author Patrick Gebhard
 */
public class VarBadge extends JComponent implements EventListener, Observer {

    public static class Entry {

        public SuperNode mSuperNode;
        public boolean mHasChanged;
        public String mConcrete;
        public String mFormatted;
        public AttributedString mAttributed;

        public Entry(SuperNode superNode, boolean hasChanged, String concrete, String formatted, AttributedString attributed) {
            mSuperNode = superNode;
            mHasChanged = hasChanged;
            mConcrete = concrete;
            mFormatted = formatted;
            mAttributed = attributed;
        }

        public String getVarName() {
            String[] s = mConcrete.split(" ");
            if (s.length > 2) {
                return s[1];
            } else {
                return "";
            }
        }

        public String getVarType() {
            String[] s = mConcrete.split(" ");
            if (s.length > 2) {
                return s[0];
            } else {
                return "";
            }
        }
    }
    //
    private final Vector<Entry> mEntryList = new Vector<Entry>();
    // The supernode
    private final SuperNode mSuperNode;
    // TODO: Make format of variable badge as global preferences
    private final int mPositionOffset = 10;
    // interaction flags
    // TODO: Make private
    public boolean mSelected;
    public boolean mDragged;
    //
    private final LOGDefaultLogger mLogger = LOGDefaultLogger.getInstance();

    public VarBadge(SuperNode superNode) {
        // mWorkSpace = ws;
        mSuperNode = superNode;
        // Initialize the entry list
        SuperNode parentNode = mSuperNode;
        mEntryList.clear();
        while (parentNode != null) {
            Vector<VarDef> varDefList = parentNode.getVarDefList();
            for (VarDef varDef : varDefList) {
                mEntryList.add(new Entry(
                        parentNode,
                        false,
                        varDef.getConcreteSyntax(),
                        varDef.getFormattedSyntax(),
                        TextFormat.fillWithAttributes(varDef.getFormattedSyntax()).getSecond()));
            }
            parentNode = parentNode.getParentNode();
        }
        // Initialize size and location
        setSize(new Dimension(1, 1));
        setLocation(
                superNode.getVariableBadge().getPosition().getXPos(),
                superNode.getVariableBadge().getPosition().getYPos());
    }

    private Dimension computeTextRectSize(Graphics2D graphics) {
        int width = 0, height = 0;
        for (Entry entry : mEntryList) {
            TextLayout textLayout = new TextLayout(
                    entry.mAttributed.getIterator(),
                    graphics.getFontRenderContext());
            int advance = (int) textLayout.getVisibleAdvance();
            if (advance > width) {
                width = advance;
            }
            int currentAll = (int) (textLayout.getAscent() + textLayout.getDescent() + textLayout.getLeading());
            height = height + currentAll;
        }
        return new Dimension(width + 2 * mPositionOffset, height + 2 * mPositionOffset);
    }

    public boolean containsPoint(Point p) {
        return getBounds().contains(p.x, p.y);
    }

    @Override
    public synchronized void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        if (sSHOW_VARIABLE_BADGE_ON_WORKSPACE && !mEntryList.isEmpty()) {
            // Enable antialiasing
            Graphics2D graphics = (Graphics2D) g;
            graphics.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            // Compute the size of the variable badge
            Dimension dimension = computeTextRectSize(graphics);
            setSize(dimension);

            // draw background
            graphics.setColor(new Color(100, 100, 100, 100));
            graphics.fillRoundRect(0, 0, dimension.width, dimension.height, 5, 5);

            // Draw the variables
            graphics.setStroke(new BasicStroke(1.5f));
            graphics.setColor(Color.BLACK);
            // Draw Type Definitions and Variable Definition

            int currentDrawingOffset = 0;
            for (Entry entry : mEntryList) {
                AttributedString attributedString = entry.mAttributed;
                TextLayout textLayout = new TextLayout(
                        attributedString.getIterator(),
                        graphics.getFontRenderContext());
                currentDrawingOffset = currentDrawingOffset + (int) textLayout.getAscent();

                graphics.drawString(
                        attributedString.getIterator(),
                        mPositionOffset,
                        mPositionOffset + currentDrawingOffset);
                currentDrawingOffset = currentDrawingOffset + (int) textLayout.getLeading() + (int) textLayout.getDescent();
            }
        }
    }

    private void updateVariable(TPLTuple<String, String> varVal) {
        //System.err.println("updateVariable");
        for (Entry entry : mEntryList) {
            String var = entry.getVarName();// the name of the current variable
            String typ = entry.getVarType();
            if (var.equals(varVal.getFirst())) {
                TPLTuple<String, AttributedString> formatedPair
                        = TextFormat.fillWithAttributes("#r#" + typ + " " + var + " = " + varVal.getSecond());
                entry.mFormatted = formatedPair.getFirst();
                entry.mAttributed = formatedPair.getSecond();
                entry.mHasChanged = true;
            }
        }
    }

    private boolean containsEntryFor(String varName) {
        for (Entry entry : mEntryList) {
            if (entry.getVarName().equals(varName)) {
                return true;
            }
        }
        return false;
    }

    public void update(Observable o, Object obj) {
        //mLogger.message("VarBadge.update(" + obj + ")");

        // Clear the entry list
        mEntryList.clear();
        // Recompute the entry list
        SuperNode parentNode = mSuperNode;
        while (parentNode != null) {
            for (VarDef varDef : parentNode.getVarDefList()) {
                String varName = varDef.getName();
                // if (!containsEntryFor(varName)) {
                mEntryList.add(new Entry(
                        parentNode,
                        false,
                        varDef.getConcreteSyntax(),
                        varDef.getFormattedSyntax(),
                        TextFormat.fillWithAttributes(varDef.getFormattedSyntax()).getSecond()));
                //}

            }
            parentNode = parentNode.getParentNode();
        }
    }

    public synchronized void update(EventObject event) {
        if (event instanceof VariableChangedEvent) {
            updateVariable(((VariableChangedEvent) event).getVarValue());
            //Editor.getInstance().update();
            revalidate();
            repaint();
        }

    }

    // TODO: do we need this?
    public void mouseClicked(MouseEvent event) {
        mSelected = true;
    }

    // TODO: do we need this?
    public void mousePressed(MouseEvent e) {
        mSelected = true;
    }

    // TODO: do we need this?
    public void mouseReleased(MouseEvent e) {
        mDragged = false;
    }

    // TODO: do we need this?
    public void deSelect() {
        mSelected = false;
        mDragged = false;
    }

    public void updateLocation(Point vector) {
        // Set new location
        setLocation(new Point(
                getLocation().x + vector.x,
                getLocation().y + vector.y));
        // Set the location on data model
        mSuperNode.getVariableBadge().setPosition(
                new Position(getLocation().x, getLocation().y));
    }
}
