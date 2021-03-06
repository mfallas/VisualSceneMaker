package de.dfki.vsm.model.visicon;

import de.dfki.vsm.util.ios.IndentWriter;
import de.dfki.vsm.model.ModelObject;
import de.dfki.vsm.util.xml.XMLParseAction;
import de.dfki.vsm.util.xml.XMLParseError;
import de.dfki.vsm.util.xml.XMLWriteError;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import org.w3c.dom.Element;

/**
 * @author Gregor Mehlmann
 */
public class VisiconObject implements ModelObject{

    // The Agent Entry List 
    private final ArrayList<VisiconAgent> mAgentList;

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    public VisiconObject() {
        mAgentList = new ArrayList<>();
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    public VisiconObject(final ArrayList<VisiconAgent> list) {
        mAgentList = list;
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    public final void append(final VisiconAgent entry) {
        mAgentList.add(entry);
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    public final void remove(final VisiconAgent entry) {
        mAgentList.remove(entry);
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    public final ArrayList<VisiconAgent> getAgentList() {
        return mAgentList;
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    public final ArrayList<VisiconAgent> copyAgentList() {
        // Construct A List Copy
        final ArrayList<VisiconAgent> copy
                = new ArrayList<>();
        // Copy Each Single Member
        for (final VisiconAgent entry : mAgentList) {
            copy.add(entry.getCopy());
        }
        // Return The Final Clone
        return copy;
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public final void writeXML(final IndentWriter stream) throws XMLWriteError {
        stream.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        stream.println("<Visicon>");
        stream.push();
        for (final VisiconAgent agent : mAgentList) {
            agent.writeXML(stream);
            stream.endl();
        }
        stream.pop();
        stream.println("</Visicon>");
        stream.flush();
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public final void parseXML(final Element element) throws XMLParseError {
        XMLParseAction.processChildNodes(element, new XMLParseAction() {

            @Override
            public void run(final Element element) throws XMLParseError {
                final String tag = element.getTagName();
                if (tag.equalsIgnoreCase("Agent")) {
                    final VisiconAgent agent = new VisiconAgent();
                    agent.parseXML(element);
                    mAgentList.add(agent);
                }
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public final String toString() {
        // Create A Byte Array Stream
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        // Initialize The Indent Writer
        final IndentWriter stream = new IndentWriter(buffer);
        try {
            // Write Object
            writeXML(stream);
        } catch (XMLWriteError exc) {
            // mLogger.failure(exc.toString());
        }
        // Cleanup Stream and Writer
        stream.flush();
        stream.close();
        // Return String Representation
        try {
            return buffer.toString("UTF-8");
        } catch (Exception exc) {
            return buffer.toString();
        }
    }

    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////
    @Override
    public VisiconObject getCopy() {
        return new VisiconObject(copyAgentList());
    }
}
