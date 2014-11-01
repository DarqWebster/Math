package darq.math.util;

import java.util.ArrayList;

/**
 *
 * @author Craig.Webster
 */
public class PrimeIterator {
	private ArrayList<Integer> primes;
	private int pointer;

	public PrimeIterator() {
		primes = new ArrayList<Integer>();
		// Only even prime.
		primes.add(2);
		// First odd prime.
		primes.add(3);
		
		pointer = -1;
	}

	public int next() {
		// If at the end of the list of known primes.
		if (pointer == primes.size() - 1) {
			// Calculate the next prime.
			calculateNext();
		}
		return primes.get(++pointer);
	}

	private void calculateNext() {
		int current = primes.get(pointer);
		while (true) {
			// All primes, except 2, are odd; don't consider even integers.
			current = current + 2;
			// Factors are less than or equal to x / 2.
			// Factors of odd values are less than or equal to x / 3.
			int lim = current / 3;
			
			// Check if a multiple of any known prime.
			boolean isPrime = true;
			for (int prime : primes) {
				if (prime > lim) {
					break;
				}
				if (current % prime == 0) {
					isPrime = false;
					break;
				}
			}
			
			// If not a multiple of any known prime.
			if (isPrime) {
				// Add to the list of known primes.
				primes.add(current);
				break;
			}
		}
	}

	public void reset() {
		pointer = 0;
	}
}
