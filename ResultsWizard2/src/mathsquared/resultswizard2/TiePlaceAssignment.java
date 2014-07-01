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
}
