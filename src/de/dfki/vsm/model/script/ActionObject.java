package de.dfki.vsm.model.script;

import de.dfki.vsm.util.ios.IndentWriter;
import de.dfki.vsm.util.xml.XMLParseAction;
import de.dfki.vsm.util.xml.XMLParseError;
import de.dfki.vsm.util.xml.XMLWriteError;
import java.util.HashMap;
import java.util.LinkedList;
import org.w3c.dom.Element;

/**
 * @author Gregor Mehlmann
 */
public final class ActionObject extends AbstractWord {

    // The Name Of The Action
    private String mName;
    // The Action Arguments
    private LinkedList<ActionFeature> mFeatureList
            = new LinkedList<>();

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    public ActionObject() {
        mName = null;
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    public ActionObject(
            final int lower,
            final int upper,
            final String name,
            final LinkedList<ActionFeature> list) {
        // Initialize The Boundary
        super(lower, upper);
        // Initialize The Members
        mName = name;
        // Initialize Fature List
        mFeatureList = list;
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    public final String getName() {
        return mName;
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    public final void setName(final String name) {
        mName = name;
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    public final LinkedList<ActionFeature> getFeatureList() {
        return mFeatureList;
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    public final void setFeatureList(final LinkedList<ActionFeature> list) {
        mFeatureList = list;
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    public final LinkedList<ActionFeature> copyFeatureList() {
        // Construct A List Copy
        final LinkedList<ActionFeature> copy
                = new LinkedList<>();
        // Copy Each Single Member
        for (final ActionFeature feature : mFeatureList) {
            copy.add(feature.getCopy());
        }
        // Return The Final Clone
        return copy;
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    public final ActionFeature getValueOf(final String key) {
        for (final ActionFeature feature : mFeatureList) {
            if (feature.getKey().equals(key)) {
                return feature;
            }
        }
        return null;
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public final String getText() {
        // Append The Identifier
        String result = "[" + mName;
        if (!mFeatureList.isEmpty()) {
            result += " ";
            for (int i = 0; i < mFeatureList.size(); i++) {
                // Append The Member String
                result += mFeatureList.get(i).getText();
                // Append A Whitespace Now
                if (i < mFeatureList.size() - 1) {
                    result += " ";
                }
            }
        }
        return result + "]";
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public final String getText(final HashMap<String, String> args) {
        // Append The Identifier
        String result = "[" + mName;
        if (!mFeatureList.isEmpty()) {
            result += " ";
            for (int i = 0; i < mFeatureList.size(); i++) {
                // Append The Member String
                result += mFeatureList.get(i).getText(args);
                // Append A Whitespace Now
                if (i < mFeatureList.size() - 1) {
                    result += " ";
                }
            }
        }
        return result + "]";
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public final void writeXML(final IndentWriter stream) throws XMLWriteError {
        stream.println("<ActionObject "
                + "lower=\"" + mLower + "\" "
                + "upper=\"" + mUpper + "\" "
                + "name=\"" + mName + "\">");
        stream.push();
        for (final ActionFeature feature : mFeatureList) {
            feature.writeXML(stream);
            if (!feature.equals(mFeatureList.getLast())) {
                stream.endl();
            }
        }
        stream.pop();
        stream.print("</ActionObject>");
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public final void parseXML(final Element element) throws XMLParseError {
        // Parse The Boundary
        mLower = Integer.parseInt(element.getAttribute("lower"));
        mUpper = Integer.parseInt(element.getAttribute("upper"));
        // Parse The Members
        mName = element.getAttribute("name");
        // Process The Child Nodes 
        XMLParseAction.processChildNodes(element, new XMLParseAction() {

            @Override
            public void run(Element element) throws XMLParseError {
                // Get The Child Tag Name
                final String name = element.getTagName();
                // Check The Child Tag Name
                if (name.equals("ActionFeature")) {
                    // Create A New Token Style 
                    final ActionFeature feature = new ActionFeature();
                    // Parse The New Token Style
                    feature.parseXML(element);
                    // Put The New Style To The Map
                    mFeatureList.add(feature);
                } else if (name.equals("ActionParam")) {
                    // Create A New Token Style 
                    final ActionParam feature = new ActionParam();
                    // Parse The New Token Style
                    feature.parseXML(element);
                    // Put The New Style To The Map
                    mFeatureList.add(feature);
                }
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public final ActionObject getCopy() {
        return new ActionObject(mLower, mUpper, mName, copyFeatureList());
    }
}
