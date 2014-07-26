/**
 * 
 */
package mathsquared.resultswizard2;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a list of {@link Slide}s. Subinterfaces will carry different types of metadata (e.g. {@linkplain EventResults event results}) that are represented by the slides.
 * 
 * @author MathSquared
 * 
 */
public interface SlideList extends List<Slide>, Serializable {

}
