package backend.academy.seminar10.refactor_tasks.prime_generator;

public class Runner {

    public static void main(String[] args) {
        PrimeGenerator gen = new PrimeGenerator();
//        LongStream primes = gen.generate(1000000000);

        final int numberOfPrimes = 1000;
        int[] primes = gen.generate(numberOfPrimes);

        PrimePrinter printer = new PrimePrinter(50, 4, printStream -> printStream.println("Hello world!"));
        printer.print(System.out, primes);
    }
}
