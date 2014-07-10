/**
 * 
 */
package mathsquared.resultswizard2;

/**
 * Represents a method by which tied contestants are assigned a particular place. All contestants are assigned the place mandated by the TiePlaceAssignment for the event.
 * 
 * @author MathSquared
 * 
 */
public enum TiePlaceAssignment {
    /**
     * Assigns the place at the top of a range.
     */
    TOP {
        public int assignPlace (int placeOne, int placeTwo) {
            return (placeOne < placeTwo) ? placeOne : placeTwo; // return the LOWER number
        }
    },
    /**
     * Assigns the place at the bottom of a range.
     */
    BOTTOM {
        public int assignPlace (int placeOne, int placeTwo) {
            return (placeOne < placeTwo) ? placeTwo : placeOne; // return the HIGHER number
        }
    },
    /**
     * Assigns the place at the middle of a range, rounding towards the better (lesser) place.
     */
    MID_ROUND_BETTER {
        public int assignPlace (int placeOne, int placeTwo) {
            int high = (placeOne < placeTwo) ? placeOne : placeTwo;
            int low = (placeOne < placeTwo) ? placeTwo : placeOne;
            int diff = low - high; // the number of places under consideration minus one (fencepost effect)
            return high + diff / 2; // halfway through the range--since division truncates, this will round towards high
        }
    },
    /**
     * Assigns the place at the middle of a range, rounding towards the worse (greater) place.
     */
    MID_ROUND_WORSE {
        public int assignPlace (int placeOne, int placeTwo) {
            int high = (placeOne < placeTwo) ? placeOne : placeTwo;
            int low = (placeOne < placeTwo) ? placeTwo : placeOne;
            int diff = low - high; // the number of places under consideration minus one (fencepost effect)
            return low - diff / 2; // halfway through the range--since division truncates, this will round towards low
        }
    };

    // Default implementation
    /**
     * Given a range of places, assigns a place to all competitors tied within that range.
     * 
     * @param placeOne one inclusive end of the range
     * @param placeTwo another inclusive end of the range (need not be greater than placeOne)
     * @return the place to assign to the competitors in the given range
     */
    public int assignPlace (int placeOne, int placeTwo) {
        return 0;
    }

    /**
     * Returns a TiePlaceAssignment corresponding to the given character.
     * 
     * <p>
     * The scheme is as follows (case-insensitive):
     * </p>
     * 
     * <ul>
     * <li>b or l: {@link BOTTOM}</li>
     * <li>m: {@link MID_ROUND_BETTER}</li>
     * <li>w: {@link MID_ROUND_WORSE}</li>
     * <li>All others: {@link TOP}</li>
     * </ul>
     * 
     * @param from the character for which to return a TiePlaceAssignment
     * @return the corresponding TiePlaceAssignment
     */
    public static TiePlaceAssignment forChar (char from) {
        switch (Character.toLowerCase(from)) {
        case 't':
        case 'h': // high
            return TOP;
        case 'b':
        case 'l': // low
            return BOTTOM;
        case 'm':
            return MID_ROUND_BETTER;
        case 'w':
            return MID_ROUND_WORSE;
        default:
            return TOP;
        }
    }
}
