package darq.math.geometry;

import java.util.Objects;

/**
 *
 * @author Craig.Webster
 */
public class Segment {
	public final Point pS;
	public final Point pE;

	public Segment(Point pS, Point pE) {
		this.pS = pS;
		this.pE = pE;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 37 * hash + Objects.hashCode(this.pS);
		hash = 37 * hash + Objects.hashCode(this.pE);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Segment other = (Segment) obj;
		if (!Objects.equals(this.pS, other.pS)) {
			return false;
		}
		if (!Objects.equals(this.pE, other.pE)) {
			return false;
		}
		return true;
	}
	
	

	@Override
	public String toString() {
		return "Segment{pS=" + pS + ", pE=" + pE + '}';
	}
}
