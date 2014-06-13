/**
 * 
 */
package mathsquared.resultswizard2;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * Delivered from the GUI to the display or vice versa to allow inter-component communication.
 * 
 * @author MathSquared
 * 
 */
public class Command {
    private Message msg;
    private Object[] params;

    /**
     * Instantiates a Command requesting a particular behavior from the receiving component.
     * 
     * @param msg the {@link Message} describing this command
     * @param params the {@linkplain Message#getTypes() parameters} to this <code>Message</code>, which must match the types specified by <code>msg</code> in the same order
     * @throws IllegalArgumentException if the <code>params</code> given do not match the classes specified by <code>msg</code>, or (if type parameters for a {@link Map} are specified) the objects stored in the given Map do not match the type parameters mandated by <code>msg</code>
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public Command (Message msg, Object... params) {
        this.msg = msg;
        if (params.length != msg.getTypes().length) {
            throw new IllegalArgumentException("Improper number of parameters for Message " + msg);
        }
        for (int i = 0; i < params.length; i++) {
            if (!params[i].getClass().isInstance(msg.getTypes()[i])) {
                throw new IllegalArgumentException("Type mismatch in argument " + i + "for Message " + msg + "; " + params[i].getClass() + " given, " + msg.getTypes()[i] + " expected");
            }
        }

        // Quick and dirty check of type parameters for Map
        if (msg.getMappedKey() != null && msg.getMappedValue() != null && msg.getTypes()[0].equals(Map.class)) {
            Class k = msg.getMappedKey();
            Class v = msg.getMappedValue();
            Map toCheck = (Map) params[0];
            Iterator<Map.Entry> iter = toCheck.entrySet().iterator();
            Map.Entry entry = iter.next();
            if (!entry.getKey().getClass().isInstance(k)) {
                throw new IllegalArgumentException("Keys in given Map do not match type parameter specified by Message " + msg);
            }
            if (!entry.getValue().getClass().isInstance(v)) {
                throw new IllegalArgumentException("Values in given Map do not match type parameter specified by Message " + msg);
            }
        }

        this.params = Arrays.copyOf(params, params.length);
    }

    /**
     * Returns the {@link Message} transmitted by this Command.
     * 
     * @return the msg
     */
    public Message getMsg () {
        return msg;
    }

    /**
     * Returns the parameters passed to this Command, in the order they were specified.
     * 
     * @return the params
     */
    public Object[] getParams () {
        return Arrays.copyOf(params, params.length);
    }
}
