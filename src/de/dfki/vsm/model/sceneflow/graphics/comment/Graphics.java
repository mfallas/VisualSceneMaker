package de.dfki.vsm.model.sceneflow.graphics.comment;

import de.dfki.vsm.model.sceneflow.Object;
import de.dfki.vsm.util.ios.IndentWriter;
import de.dfki.vsm.util.xml.XMLParseAction;
import de.dfki.vsm.util.xml.XMLParseError;
import org.w3c.dom.Element;

/**
 * The graphics information for an edge.
 *
 * @author Gregor Mehlmann
 */
public class Graphics extends Object {

    private Rect mRect;

    public Graphics() {
        mRect = new Rect();
    }

    public Graphics(Rect Rect) {
        mRect = Rect;
    }

    public void setRect(Rect value) {
        mRect = value;
    }

    public Rect getRect() {
        return mRect;
    }

    public String getAbstractSyntax() {
        return "Graphics(" + (mRect != null ? mRect.getAbstractSyntax() : "") + ")";
    }

    public String getConcreteSyntax() {
        return (mRect != null ? mRect.getConcreteSyntax() : "");
    }

    public String getFormattedSyntax() {
        return "";
    }

    public Graphics getCopy() {
        return new Graphics(mRect.getCopy());
    }

    public void writeXML(IndentWriter out) {
        out.println("<Graphics>").push();
        mRect.writeXML(out);
        out.pop().println("</Graphics>");
    }

    public void parseXML(Element element) throws XMLParseError {
        XMLParseAction.processChildNodes(element, "Rect", new XMLParseAction() {

            public void run(Element element) throws XMLParseError {
                mRect.parseXML(element);
            }
        });
    }
}
