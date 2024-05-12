# Proyecto de Procesamiento en Paralelo con KarelJRobot

Este proyecto consiste en desarrollar una simulación de procesamiento en paralelo utilizando KarelJRobot. El objetivo es crear un sistema con múltiples robots que trabajen de manera simultánea para recoger sirenas dispersas en un mundo virtual y llevarlas a un punto específico.

## Condiciones Generales

1. Sea \( r \) el número de robots a crear. \( r \) debe ser múltiplo de 2, es decir, no pueden haber tres robots en el mundo y la cantidad de sirenas en el mundo será \( r \times 100 \). Cuando se crea el robot, recibe de manera aleatoria su cantidad de sirenas máxima a transportar (1, 2, 4 ó 8).
2. Las sirenas se colocarán de manera aleatoria en cada posición del mundo hasta que se asignen las \( r \times 100 \).
3. Deben encontrar la manera que ningún robot esté con otro en la misma posición donde están recogiendo sirenas. De pasar esto, podrían generar problemas “inesperados” en los funcionamientos que no son del resorte de este curso. Cuando pasen a Sistemas Operativos aprenderán como resolver ese tipo de situaciones.
4. Para la ejecución del programa se debe recibir por parámetros de consola la cantidad de robots \( r \) que se tendrán. La cantidad de sirenas que puede transportar el robot (n) será generada de manera aleatoria para cada robot. El argumento a leer de la línea de comandos -r y debe venir acompañado de un número que debe ser 1, 2 ó 4 como se explicó en el punto 1 de condiciones generales.
5. Se puede agregar un argumento opcional que será -e y si está presente, debe asignarse la misma cantidad de sirenas a recoger a cada robot (asignada aleatoriamente pero la misma para todos).
