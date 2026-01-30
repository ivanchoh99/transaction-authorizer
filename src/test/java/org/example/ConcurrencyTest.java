package org.example;

import org.example.domain.Transaction;
import org.example.model.AccountDTO;
import org.example.model.RegisterResult;
import org.example.service.AccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class ConcurrencyTest {

    @Test
    @DisplayName("Debería mantener la integridad del saldo bajo alta concurrencia")
    void stressTestAtomicReference() throws InterruptedException {
        AccountService service = new AccountService();

        // 1. Inicializamos la cuenta con mucho saldo
        long saldoInicial = 100_000L;
        service.initAccount(new AccountDTO(true, saldoInicial));

        // 2. Configuramos el ataque de hilos
        int numeroDeHilos = 100; // 100 personas comprando a la vez
        int transaccionesPorHilo = 100; // Cada persona hace 100 compras
        int totalTransaccionesEsperadas = numeroDeHilos * transaccionesPorHilo;
        int montoPorTransaccion = 10;

        ExecutorService executor = Executors.newFixedThreadPool(numeroDeHilos);
        CountDownLatch latch = new CountDownLatch(1); // Para que todos arranquen al mismo tiempo
        AtomicInteger exitos = new AtomicInteger(0);

        System.out.println("🚀 Arrancando test de estrés con " + totalTransaccionesEsperadas + " transacciones...");

        for (int i = 0; i < totalTransaccionesEsperadas; i++) {
            executor.submit(() -> {
                try {
                    latch.await(); // Esperar la señal de salida
                    Transaction tx = new Transaction("Tienda-Test", montoPorTransaccion, Instant.parse("2026-01-29T21:00:00Z"));
                    RegisterResult response = service.processTransaction(tx);

                    if (response.violations().isEmpty()) {
                        exitos.incrementAndGet();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        // 3. ¡Fuego!
        long startTime = System.currentTimeMillis();
        latch.countDown(); // Libera todos los hilos al tiempo
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        long endTime = System.currentTimeMillis();

        // 4. Verificación de resultados
        long saldoFinalReal = service.getAccountRef().get().availableLimit();
        long saldoEsperado = saldoInicial - ((long) exitos.get() * montoPorTransaccion);

        System.out.println("--- RESULTADOS ---");
        System.out.println("Tiempo total: " + (endTime - startTime) + "ms");
        System.out.println("Transacciones exitosas: " + exitos.get());
        System.out.println("Saldo Final Real: " + saldoFinalReal);
        System.out.println("Saldo Final Esperado: " + saldoEsperado);

        if (saldoFinalReal == saldoEsperado) {
            System.out.println("✅ TEST PASADO: El AtomicReference mantuvo la integridad perfecta.");
        } else {
            System.out.println("❌ TEST FALLIDO: ¡Hubo pérdida de datos!");
        }
    }
}