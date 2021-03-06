package de.dfki.vsm.runtime.error;

/**
 * An exception thrown if an interpreter thread has been terminated
 *
 * @author Gregor Mehlmann
 */
public class TerminatedException extends InterpreterException {

    public TerminatedException(Object obj, String msg) {
        super(obj, msg);
    }
}
