# Prices — servicio de tarifas (prueba técnica Inditex)

## Problema a resolver

Dado un producto, una cadena (`brandId`) y una fecha de aplicación, el servicio debe devolver
el precio de venta que corresponde. En el sistema puede haber varias tarifas de un mismo
producto y cadena cuyos rangos de fechas se solapan; cuando eso ocurre, gana la tarifa con
mayor `PRIORITY`. Se expone un único endpoint:

```
GET /prices?applicationDate={fecha}&productId={id}&brandId={id}
```

que devuelve la tarifa aplicable (identificador de tarifa, fechas de vigencia, precio y
moneda) o un error si los parámetros no son válidos o no existe ninguna tarifa aplicable.

## Tecnologías

- **Java 21 + Spring Boot 4.1.1** (Spring Framework 7, Jakarta EE): es el stack que pide el
  enunciado y el esqueleto de partida; Java 21 da acceso a `record` para los Value Objects,
  clave para modelar el dominio sin boilerplate.
- **Spring Web MVC, Spring Data JPA, Flyway, H2 (en memoria)**: MVC y Data JPA son los
  adaptadores REST/persistencia; Flyway versiona el esquema y siembra los datos del
  enunciado de forma reproducible en cada arranque; H2 en memoria evita depender de un
  motor de base de datos externo para una prueba técnica.
- **Spring Security**: aunque el endpoint es público de lectura, se usa para dejar la
  política de acceso (qué es público y qué no) declarada y verificable en vez de confiar
  en que nadie exponga por error un endpoint nuevo.
- **springdoc-openapi + Swagger UI, contrato OpenAPI escrito a mano**: se escribe el
  contrato primero (contract-first) para que sea la fuente de verdad del API, no un
  efecto secundario de las anotaciones del controlador; Swagger UI simplemente lo sirve.
- **openapi-generator-maven-plugin**: genera la interfaz del controlador y los DTOs a
  partir de ese contrato, para que sea imposible que el código y el contrato se
  desincronicen con el tiempo.
- **JUnit 5, AssertJ, Mockito, MockMvc, `@DataJpaTest`**: permiten probar cada capa en el
  nivel que le corresponde (dominio en aislamiento con Mockito, persistencia contra un H2
  real con `@DataJpaTest`, HTTP real con MockMvc) sin tener que levantar toda la pila para
  cada tipo de test.
- **Jacoco**: cobertura objetiva y verificable en el propio build (`mvn verify` falla si
  baja del umbral), en vez de depender de que alguien revise manualmente qué se ha
  probado.
- **Docker (build multi-stage) y GitHub Actions**: permiten ejecutar y desplegar el
  servicio de forma reproducible fuera de la máquina de quien lo ha desarrollado, y
  validar cada cambio automáticamente antes de fusionarlo.
- **Spring Boot Actuator**: se añade únicamente para exponer `/actuator/health`, que
  permite a un orquestador (Docker, Kubernetes, un balanceador...) comprobar si el
  servicio está vivo y listo para recibir tráfico sin tener que llamar al propio
  endpoint de negocio. Solo se expone `health`; el resto de endpoints de Actuator
  quedan desactivados por defecto para no filtrar información interna.

## Arquitectura

El proyecto sigue una arquitectura **hexagonal** con **DDD ligero**. Se ha elegido esta
arquitectura, y no una capa única controlador→servicio→repositorio, porque la regla de
negocio real de este ejercicio (qué tarifa gana cuando varias se solapan) es justo lo que
más va a cambiar o a probarse con más detalle, y aquí vive en clases de dominio que no
importan nada de Spring ni de JPA: se pueden testear con JUnit puro, sin arrancar
contexto ni tocar una base de datos, y se podría cambiar el framework web o el motor de
persistencia sin tocar una sola línea de esa lógica. Con una única capa, esa regla habría
quedado mezclada con anotaciones de Spring y detalles de JPA, más difícil de aislar y de
razonar. Todo esto se organiza en un único bounded context (*Pricing*) y módulo (`price`):

```
com.inditex.prices
├── shared/domain            DomainException (base de las excepciones de dominio)
└── price
    ├── domain               Modelo, factoría, puerto de repositorio y servicio de dominio.
    │                        Java puro: sin Spring, sin JPA, sin anotaciones.
    ├── application          Casos de uso: puerto de entrada, query/result intermedios
    │                        y el servicio que orquesta el caso de uso.
    └── infrastructure       Adaptadores: REST (controlador + DTOs generados + manejo
                             de errores), persistencia (JPA) y configuración (Spring).
```

La regla de dependencia es siempre hacia el dominio: `infrastructure` depende de
`application`, que depende de `domain`; el dominio no conoce ni Spring ni JPA ni el
contrato REST.

**Cómo se cubre cada pieza de DDD:**

- **Entidad / aggregate root**: `Price` encapsula sus invariantes en el constructor.
  `equals`/`hashCode` comparan todos sus campos (no solo cadena+producto+tarifa): el
  esquema no impone que esa combinación sea única (no hay `UNIQUE` en la tabla, solo un
  índice), y el servicio nunca compara una misma tarifa "antes y después" de un cambio
  —no hay caso de uso de escritura—, así que no hay ninguna necesidad real de tratarla
  como identidad de entidad; comparar por valor es más simple y no da por sentado algo
  que el esquema no garantiza.
- **Value Objects**: `BrandId`, `ProductId`, `PriceListId`, `Priority`, `Money` y
  `ApplicationPeriod`. Son `record` inmutables que validan sus propias reglas (por ejemplo,
  que un identificador sea positivo o que el fin del periodo no sea anterior al inicio).
- **Factoría**: `PriceFactory` es el punto único para construir un `Price` válido; lo usan
  tanto el mapper de persistencia como los tests.
- **Repositorio (puerto)**: `PriceRepository` se define en el dominio como interfaz; su
  implementación (`PriceRepositoryAdapter`) vive en infraestructura y traduce hacia/desde
  JPA.
- **Servicio de dominio**: `ApplicablePriceFinder` aplica la regla de negocio "si no hay
  tarifa aplicable, es un error de dominio" (`PriceNotFoundException`).
- **Casos de uso (application)**: `FindApplicablePriceUseCase` (puerto de entrada) y su
  implementación `FindApplicablePriceService`, con objetos intermedios propios
  (`FindApplicablePriceQuery` de entrada y `ApplicablePrice` de salida) para no filtrar
  DTOs de REST ni entidades JPA hacia el dominio. El paso de `Price` (dominio) a
  `ApplicablePrice` lo hace `ApplicablePriceMapper`, simétrico a `PricePersistenceMapper`
  (JPA → dominio) y `PriceRestMapper` (resultado → DTO REST): cada capa tiene su propio
  traductor y ninguna clase de orquestación (`FindApplicablePriceService`, `PriceController`)
  mapea nada por su cuenta.

**Eventos de dominio**: no se han implementado. El caso de uso es una consulta pura
(no hay una escritura ni un cambio de estado que otro módulo o sistema necesite conocer),
por lo que un evento de dominio (por ejemplo, `PriceAppliedEvent`) no tendría ningún
consumidor real y añadiría complejidad sin beneficio.

## Instalar, usar y probar

### Requisitos

Java 21 y, opcionalmente, Docker. El proyecto incluye el wrapper de Maven (`mvnw` /
`mvnw.cmd`), no es necesario tener Maven instalado.

### Compilar, generar el contrato y ejecutar los tests

```bash
./mvnw clean verify
```

Este comando:

1. Genera con openapi-generator, a partir de `src/main/resources/static/openapi.yaml`, la
   interfaz `PricesApi` y los DTOs `PriceResponse` / `ErrorResponse`.
2. Compila el proyecto.
3. Ejecuta los tests unitarios, de persistencia (`@DataJpaTest`) y de integración
   (`@SpringBootTest` + MockMvc), incluyendo los 5 casos del enunciado. Los tests que
   necesitan una tarifa de ejemplo usan un Object Mother (`PriceMother`, junto a `Price`
   en `price.domain.model`) en vez de repetir la construcción del agregado en cada test.
4. Genera el informe de cobertura y comprueba el umbral mínimo (90% de líneas, excluyendo
   el código generado por OpenAPI y la clase `PricesApplication`).

El informe de Jacoco queda en `target/site/jacoco/index.html`.

### Arrancar la aplicación

```bash
./mvnw spring-boot:run
```

Con la aplicación levantada en `http://localhost:8080`:

```bash
# Test 1 -> tarifa 1, 35.50 EUR
curl "http://localhost:8080/prices?applicationDate=2020-06-14T10:00:00&productId=35455&brandId=1"

# Test 2 -> tarifa 2, 25.45 EUR
curl "http://localhost:8080/prices?applicationDate=2020-06-14T16:00:00&productId=35455&brandId=1"

# Test 3 -> tarifa 1, 35.50 EUR
curl "http://localhost:8080/prices?applicationDate=2020-06-14T21:00:00&productId=35455&brandId=1"

# Test 4 -> tarifa 3, 30.50 EUR
curl "http://localhost:8080/prices?applicationDate=2020-06-15T10:00:00&productId=35455&brandId=1"

# Test 5 -> tarifa 4, 38.95 EUR
curl "http://localhost:8080/prices?applicationDate=2020-06-16T21:00:00&productId=35455&brandId=1"

# Fecha mal formada -> 400
curl -i "http://localhost:8080/prices?applicationDate=2020-06-14&productId=35455&brandId=1"

# Producto sin tarifa -> 404
curl -i "http://localhost:8080/prices?applicationDate=2020-06-14T10:00:00&productId=99999&brandId=1"

# Estado de salud de la aplicación (público, sin autenticación)
curl "http://localhost:8080/actuator/health"
```

Swagger UI queda disponible en `http://localhost:8080/swagger-ui.html`, sirviendo el
contrato real de `src/main/resources/static/openapi.yaml`.

Para inspeccionar la base de datos H2 en desarrollo, arrancar con el perfil `local`
(`./mvnw spring-boot:run -Dspring-boot.run.profiles=local`, o con la variable de entorno
`SPRING_PROFILES_ACTIVE=local`) y abrir `http://localhost:8080/h2-console` (JDBC URL
`jdbc:h2:mem:prices`, usuario `sa`, sin contraseña). La consola H2 está desactivada en el
resto de perfiles.

### Docker

```bash
docker build -t prices .
docker run -p 8080:8080 prices
```

La imagen se construye en dos etapas (`eclipse-temurin:21-jdk` para compilar,
`eclipse-temurin:21-jre` para ejecutar) y corre con un usuario no root.

## Decisiones adicionales

- **Query derivada frente a nativa**: se ha optado por una query derivada de Spring Data
  (`findFirstByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDescStartDateDesc`)
  en lugar de SQL nativo o JPQL. Con `findFirst...OrderBy...` el filtrado, la ordenación por
  prioridad y el `LIMIT 1` los resuelve la base de datos en una sola consulta, sin cargar en
  memoria las tarifas que se solapan para luego elegir la de mayor prioridad en Java. El
  nombre es largo, por lo que queda encapsulado en `PriceRepositoryAdapter`, que es el único
  punto del código que lo conoce.
- **Índice**: `(BRAND_ID, PRODUCT_ID, START_DATE, END_DATE)` cubre exactamente el filtro de
  la query (igualdad por cadena y producto, rango por fechas), evitando un escaneo completo
  de la tabla `PRICES` a medida que crece.
- **Desempate**: si dos tarifas del mismo producto y cadena tuvieran la misma prioridad y
  ambas aplicaran a la fecha solicitada, el resultado sería ambiguo por definición del
  enunciado. Para que la respuesta sea determinista (misma entrada, misma salida siempre)
  se añade un segundo criterio de orden, `StartDateDesc`, que en igualdad de prioridad
  favorece la tarifa más reciente.
- **`LocalDateTime` frente a `Instant`/`ZonedDateTime`**: `applicationDate` llega sin
  offset (`2020-06-14T10:00:00`) y `START_DATE`/`END_DATE` se guardan como `TIMESTAMP` sin
  zona. `LocalDateTime` es una fecha/hora "de pared", sin ningún vínculo con UTC ni con
  ninguna zona; usar `Instant` habría obligado a asumir una zona horaria que el enunciado
  no especifica en ningún sitio, solo para poder compararlo con datos que tampoco la
  tienen. Si el negocio fuera multi-zona (tiendas en países distintos, por ejemplo),
  `Instant` o `ZonedDateTime` sí tendrían sentido.
- **Sin logging propio más allá del que trae Spring Boot**: los caminos normales del
  endpoint (200, 400, 404) ya quedan completamente descritos por el código de estado HTTP
  y el `ErrorResponse` devuelto, que además están verificados por tests; loguear un 404
  no aportaría nada que esos tests no capturen ya, y en este ejercicio no hay ningún
  sistema (ELK, métricas, dashboards) que fuera a consumir esos logs. Añadir logging
  disperso por controlador/servicio/repositorio sin un consumidor real habría sido
  complejidad sin beneficio demostrable, el mismo criterio que se ha aplicado para no
  implementar eventos de dominio. La única excepción es el manejador genérico de
  `GlobalExceptionHandler` (`handleUnexpectedError`): el cliente recibe el mensaje
  genérico correcto, pero sin un `log.error(...)` del lado del servidor esa excepción
  sería invisible para quien opera el servicio en un despliegue real, así que sí se
  registra ahí (con la excepción completa, método y ruta de la petición), cubierto por
  `PriceControllerTest#returnsInternalServerErrorWithAGenericMessageOnAnUnexpectedFailure`.
- **Versión `1.0.0`**: primera versión estable, publicada como release
  (`v1.0.0`), que congela el contrato de API expuesto en `openapi.yaml`. A partir
  de aquí `main` avanza en `1.0.1-SNAPSHOT` para desarrollo futuro.
- **Configuración por perfil en ficheros separados**: `application.yaml` (común) +
  `application-local.yaml` (solo con el perfil `local`), en vez de un único fichero con
  varios documentos YAML separados por `---` y `spring.config.activate.on-profile`. Es la
  convención estándar de Spring Boot y evita mezclar en el mismo archivo configuraciones
  que nunca coexisten en el mismo arranque.
- **Seguridad**: el único dato que expone la API es un precio de catálogo, sin información
  personal ni de negocio sensible, y el endpoint es de solo lectura (`GET`). Por eso el
  filtro de seguridad es sin estado (`STATELESS`, sin sesión ni cookies), con CSRF
  desactivado —CSRF protege operaciones con estado de sesión vía navegador; una API GET sin
  cookies de autenticación no está expuesta a ese riesgo— y con una política de
  "todo denegado salvo lista blanca": `GET /prices`, Swagger y `/actuator/health` son
  públicos; el resto de rutas quedan denegadas por defecto para que añadir un endpoint
  nuevo no lo exponga sin decidirlo explícitamente. Esa lista blanca (ruta y, si aplica,
  método HTTP) está en `security.public-endpoints` dentro de `application.yaml`, no en
  el código: `SecurityConfiguration` solo sabe construir un `SecurityFilterChain` a partir
  de lo que declare la configuración. El perfil `local` añade `/h2-console/**` a esa misma
  lista en `application-local.yaml`.

## Licencia

Este proyecto se distribuye bajo licencia MIT. Ver el fichero `LICENSE`.
