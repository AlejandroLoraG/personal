### Desafío técnico – Desarrollo de API Java/Spring Boot

Objetivo:
Desarrollar una API REST utilizando Java y Spring Boot que, dado un par de
ubicaciones, devuelva la ruta más rápida (en términos de tiempo de viaje) desde una
ubicación de origen hasta una de destino.

#### Consideraciones

Carga de datos
  1. La API debe permitir cargar los tiempos de viaje entre ubicaciones a través de
  un archivo CSV. Cada fila del archivo representa una conexión unidireccional
  con el siguiente formato:

    ```markdown
      loc_start;loc_end;time
      R11;R12;20
      R12;R13;9
      R13;R12;11
      R13;R20;9
      R20;R13;11
      CP1;R11;84
      R11;CP1;92
      CP1;CP2;7
      CP2;CP1;10
      CP2;R20;67
      R20;CP2;60
    ```

    Donde:
    • loc_start: nombre de la ubicación de origen (String)
    • loc_end: nombre de la ubicación de destino (String)
    • time: tiempo de viaje n minutos (entero)

  2. La API deberá contar con un endpoint para recibir los tiempos de viaje
  entregados en el archivo csv. El modo en que se carga este archivo (por
  ejemplo, como archivo adjunto, contenido plano o JSON) queda a criterio del
  desarrollador.


#### Tiempos de viaje

  1. La API deberá contar con un endpoint que, dado un par de ubicaciones (origen
  y destino), devuelva la ruta más rápida entre ambas, así como el tiempo total
  de viaje. Ejemplo de respuesta esperada:

    ```json
      {
      "ruta": ["CP1", "CP2", "R20"],
      "tiempoTotal": 74
      }
    ```

  2. Las ubicaciones de origen y de destino deberán estar en la respuesta.

  3. El cálculo de la ruta debe ejecutarse en menos de 300ms por solicitud,
  independientemente del par de ubicaciones consultado.

#### Otras consideraciones

  1. El sistema debe poder operar con archivos de hasta 10.000 filas sin
  degradación significativa del rendimiento.
  2. La solución debe estar implementada en Java 21+ utilizando el framework
  Spring Boot.
  3. Se valorará el uso de estructuras de datos eficientes y un diseño de código
  limpio y mantenible.
  4. Se valorará la entrega de pruebas unitarias.
  5. Se valorará también la entrega de una imagen Docker con la API.