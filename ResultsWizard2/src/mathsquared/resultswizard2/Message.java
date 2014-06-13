/**
 * 
 */
package mathsquared.resultswizard2;

import java.util.Arrays;
import java.util.Map;

/**
 * Represents one of several possible messages sent between the GUI and display units of the application.
 * 
 * <p>
 * An instance of this enum should be sent with all messages between these two components specifying the details of the message.
 * </p>
 * 
 * @author MathSquared
 * 
 */
@SuppressWarnings("rawtypes")
public enum Message {
    POISON(), ADD(Slide[].class, String.class), REMOVE(String.class), RETR_SLIDES(), RESP_SLIDES(String.class, Slide[].class, true), TICKER(String.class), RETR_TICKER(), RESP_TICKER(String.class);

    private Class[] types;
    private Class mappedKey;
    private Class mappedValue;

    /**
     * Constructs a Message representing a message that should be sent with parameters of the given type.
     * 
     * @param types the parameters of the types that should be sent
     */
    private Message (Class... types) {
        this.types = types;
    }

    /**
     * Constructs a Message representing a message that should be sent with a Map between two types as well as parameters of several other types.
     * 
     * <p>
     * This constructor sets the list of types as if calling {@link #Message(Class...)} with {@link Map}<code>.class</code> prepended to the argument list.
     * </p>
     * 
     * @param mappedKey the key that should be used in the Map
     * @param mappedValue the value that should be used in the Map
     * @param rando unused
     * @param types the parameters of the types that should be sent
     */
    private Message (Class mappedKey, Class mappedValue, boolean rando, Class... types) {
        // Prepend Map to the list of types
        Class[] typesPrime = new Class[types.length + 1];
        typesPrime[0] = Map.class;
        System.arraycopy(types, 0, typesPrime, 1, types.length);
        this.types = typesPrime;
        this.mappedKey = mappedKey;
        this.mappedValue = mappedValue;
    }

    /**
     * Returns a list of parameter types for the given message.
     * 
     * @return an array of parameter types
     */
    public Class[] getTypes () {
        return Arrays.copyOf(types, types.length);
    }

    /**
     * Returns the key that should be used in a Map.
     * 
     * @return the key for the map, or null if none is specified
     */
    public Class getMappedKey () {
        return mappedKey;
    }

    /**
     * Returns the value that should be used in a Map.
     * 
     * @return the value for the map, or null if none is specified
     */
    public Class getMappedValue () {
        return mappedValue;
    }
}
