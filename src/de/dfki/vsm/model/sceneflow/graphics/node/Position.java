package de.dfki.vsm.model.sceneflow.graphics.node;

import de.dfki.vsm.model.sceneflow.Object;
import de.dfki.vsm.util.ios.IndentWriter;
import org.w3c.dom.Element;

/**
 * A position of a node
 *
 * @author Gregor Mehlmann
 */
public class Position extends Object {

    private int mXPos;
    private int mYPos;

    public Position(int xPos, int yPos) {
        mXPos = xPos;
        mYPos = yPos;
    }

    public Position() {
        mXPos = Integer.MIN_VALUE;
        mYPos = Integer.MIN_VALUE;
    }

    public void setXPos(int value) {
        mXPos = value;
    }

    public int getXPos() {
        return mXPos;
    }

    public void setYPos(int value) {
        mYPos = value;
    }

    public int getYPos() {
        return mYPos;
    }

    public String getAbstractSyntax() {
        return "Position(" + mXPos + "," + mYPos + ")";
    }

    public String getConcreteSyntax() {
        return getAbstractSyntax();
    }

    public String getFormattedSyntax() {
        return "";
    }

    public Position getCopy() {
        return new Position(mXPos, mYPos);
    }

    public void writeXML(IndentWriter out) {
        out.println("<Position x-pos=\"" + mXPos + "\" y-pos=\"" + mYPos + "\"/>");
    }

    public void parseXML(Element element) {
        mXPos = Integer.valueOf(element.getAttribute("x-pos"));
        mYPos = Integer.valueOf(element.getAttribute("y-pos"));
    }
}
