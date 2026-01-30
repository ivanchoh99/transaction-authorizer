🚀 Transaction Authorizer

Este proyecto es una implementación de un motor de autorización de transacciones en tiempo real, desarrollado bajo los principios de simplicidad inmutable y concurrencia lock-free. Fue diseñado para procesar flujos continuos de eventos con alta eficiencia y precisión financiera.
🧠 Arquitectura y Principios de Diseño

El sistema sigue la filosofía de "Functional Core, Imperative Shell", separando la lógica de negocio pura de los efectos secundarios de entrada/salida.
1. Inmutabilidad en el Dominio

A diferencia de los modelos mutables tradicionales, la entidad Account se implementó utilizando Java Records.

    Cada transacción exitosa genera una nueva instancia del estado de la cuenta.

    Esto garantiza la integridad de los datos, facilita el testing y elimina efectos secundarios inesperados en entornos concurrentes.

2. Gestión de Estado Lock-Free (AtomicReference)

Para manejar un entorno de alto rendimiento sin los cuellos de botella del bloqueo tradicional (synchronized), se implementó un CAS Loop (Compare-And-Swap).
    Estrategia: El sistema lee el estado, calcula la transición de forma aislada y solo actualiza la referencia global si el estado base no ha sido modificado por otro hilo. Esto permite un paralelismo real a nivel de hardware.

3. Ventana Deslizante (Sliding Window)

Para validar las reglas de frecuencia e intervalo (2 minutos):

    Se utilizó una ArrayDeque para mantener un historial eficiente.

    Optimización: El sistema limpia automáticamente los registros fuera de la ventana de tiempo antes de cada validación, manteniendo una complejidad temporal de O(1) amortizado.

🛠️ Stack Tecnológico

    Lenguaje: Java 21 (Records, Pattern Matching, Instant API).

    Build System: Gradle.

    JSON Provider: Jackson (Estrategia de Kebab Case para compatibilidad con el contrato).

    Concurrencia: java.util.concurrent.atomic.AtomicReference.

🚦 Reglas de Negocio Implementadas

    account-already-initialized: Evita la creación duplicada de cuentas.

    account-not-initialized: Bloquea transacciones si no existe una cuenta activa.

    card-not-active: Rechaza operaciones si la tarjeta está inhabilitada.

    insufficient-limit: Valida que el monto no exceda el límite disponible.

    high-frequency-small-interval: Máximo 3 transacciones en un intervalo de 2 minutos.

    doubled-transaction: Detecta transacciones idénticas (mismo comercio y monto) en menos de 2 minutos.

🧪 Test de Estrés y Validación

El proyecto incluye una suite de pruebas de alta contención que lanza miles de transacciones simultáneas desde múltiples hilos.

    Objetivo: Verificar que el saldo final sea exacto (Integridad de Datos) y que el sistema no presente bloqueos (deadlocks).

    Resultado: El uso de concurrencia optimista demostró una consistencia del 100% con un rendimiento superior a las implementaciones basadas en bloqueos pesados.

🤖 Créditos y Colaboración (AI-Assisted Development)

Este proyecto fue desarrollado en una sesión de entrenamiento técnico intensivo asistida por Gemini (IA de Google).

    Rol de la IA: Actuó como Senior Tech Lead y Entrevistador Técnico, definiendo los requerimientos, realizando revisiones de código iterativas y desafiando las decisiones de diseño (como la transición de synchronized a AtomicReference).

    Aportes de Arquitectura: La IA guió la implementación hacia patrones funcionales y estructuras de datos eficientes para el manejo de ventanas de tiempo.

    Ejecución: El diseño final, la resolución de conflictos de lógica y la validación de los tests de estrés fueron liderados por el autor, utilizando la IA como mentor técnico avanzado.

⚖️ Trade-offs y Consideraciones

    In-Memory vs DB: Se optó por un estado en memoria para priorizar la latencia ultra-baja, asumiendo que el flujo de eventos es gestionado por una capa de transporte que garantiza el orden (como Kafka).

    Simplicidad: Se mantuvieron los DTOs y Mappers para desacoplar el contrato externo del modelo de dominio, asegurando que cambios internos no rompan la compatibilidad de la API.

🚀 Cómo ejecutarlo
Bash

# Construir el proyecto
./gradlew build

# Ejecutar con un archivo de operaciones
java -jar build/libs/authorizer.jar < operations.txt