package darq.math;

import darq.math.Utils;

/**
 *
 * @author Craig.Webster
 */
public class IntegerIterator {
	private int start;
	private int end;
	private int delta;
	private int current;
	
	public IntegerIterator(int start, int end) {
		this.start = start;
		this.end = end;
		
		int vector = end - start;
		this.delta = Utils.sign(vector);
		if (this.delta == 0) {
			this.delta = 1;
		}
		
		this.current = this.start - this.delta;
	}

	public boolean hasNext() {
		return ((current + delta - end) * delta) <= 0;
	}

	public int next() {
		return current += delta;
	}
}
