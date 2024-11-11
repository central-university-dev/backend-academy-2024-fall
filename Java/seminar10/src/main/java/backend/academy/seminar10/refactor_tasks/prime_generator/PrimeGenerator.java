package backend.academy.seminar10.refactor_tasks.prime_generator;

public class PrimeGenerator {
    int[] generate(int count) {
        final int ORDMAX = 30;
        int[] MULT = new int[ORDMAX + 1];

        int ORD = 2;
        int primeSquare = 9;

        int[] primes = new int[count];
        primes[0] = 2;

        int currentPrime = 1;
        int currentPrimeIdx = 0;
        while (currentPrimeIdx < count - 1) {
            boolean isPrime;
            do {
                currentPrime += 2;
                if (currentPrime == primeSquare) {
                    ORD++;
                    primeSquare = primes[ORD] * primes[ORD];
                    MULT[ORD - 1] = currentPrime;
                }

                int N = 2;
                isPrime = true;

                while (N < ORD && isPrime) {
                    while (MULT[N] < currentPrime) {
                        MULT[N] += primes[N] + primes[N];
                    }
                    if (MULT[N] == currentPrime) {
                        isPrime = false;
                    }
                    N++;
                }
            } while (!isPrime);

            currentPrimeIdx++;
            primes[currentPrimeIdx] = currentPrime;
        }

        return primes;
    }
}
