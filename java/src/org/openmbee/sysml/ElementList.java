package org.openmbee.sysml;

import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;

/**
 * A list of elements that carries the convenience members of its elements: {@code xs.findPorts()} is
 * the elements' {@code findPorts()} in list order, list results concatenated (duplicates kept) and
 * single results with the absent ones dropped; the result is again a typed list (a list of strings for
 * {@code findDocText}). If the member refuses for one element, the call refuses. Members that answer a
 * Boolean, a number or a literal stay on the elements.
 *
 * <p>This class carries the members every element has; the generated lists of
 * {@code org.openmbee.sysml.classes} ({@code PartUsageList}, {@code TypeList}, …) add those of their
 * element class, through one {@code …ListOps} interface per class that declares members. Every element
 * list a convenience member returns is one of them; any collection of elements can be lifted:
 * {@code new ElementList<>(xs)}, {@code new TypeList(types)}. Immutable.
 *
 * @param <T> the elements' class
 */
public class ElementList<T extends Element> extends AbstractList<T>
        implements RandomAccess, org.openmbee.sysml.classes.ElementListOps {
    private final List<T> elements;

    public ElementList(Collection<? extends T> elements) {
        this.elements = List.copyOf(elements);
    }

    @Override
    public T get(int index) {
        return elements.get(index);
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public List<? extends Element> $elements() {
        return elements;
    }
}
