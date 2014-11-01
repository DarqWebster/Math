package darq.math.util;

/**
 *
 * @author Craig.Webster
 */
public class IntegerIterator {
	/**
	 * The integer from which this iterator starts iterating over, inclusive.
	 */
	public final int start;
	/**
	 * The integer from which this iterator ends iterating over, exclusive.
	 */
	public final int end;
	/**
	 * The increment or decrement applied to the iterator at each step.
	 */
	public final int delta;
	/**
	 * The current value of the iterator at any time.
	 * Will be "before" {@link #start} before the first call to {@link #next()};
	 */
	private int current;
	
	/**
	 * Constructs an IntegerIterator that iterates by 1, 
	 * from <code>start</code> (inclusive) to <code>end</code> (exclusive).
	 * @param start
	 * @param end 
	 */
	public IntegerIterator(int start, int end) {
		this(start, end, 1);
	}
	
	/**
	 * Constructs an IntegerIterator that iterates by <code>step</code>, 
	 * from <code>start</code> (inclusive) to <code>end</code> (exclusive).
	 * @param start
	 * @param end
	 * @param step 
	 */
	public IntegerIterator(int start, int end, int step) {
		this.start = start;
		this.end = end;
		this.delta = Math.abs(step) * (end < start ? -1 : +1);
		
		this.current = this.start - this.delta;
	}

	/**
	 * Returns <code>true</code> if at least one integer exists that is
	 * larger than the last value of <code>next()<code> by <code>delta</code> 
	 * and strictly before <code>end</code>.
	 * Returns <code>false</code> otherwise.
	 * @return 
	 */
	public boolean hasNext() {
		if (delta < 0) {
			return (end - current) < delta;
		} else {
			return (end - current) > delta;
		}
	}

	/**
	 * Returns the next integer,
	 * larger than the last value of <code>next()</code> by <code>delta</code>.
	 * @return 
	 */
	public int next() {
		current = current + delta;
		return current;
	}
}
