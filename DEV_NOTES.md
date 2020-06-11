# Arquitectura de la aplicacion

Se planteo la siguiente arquitectura para la aplicacion:

	-- MineSweeper-API
    |-- config
    |-- controller
    |-- entities
    |   |-- dtos
    |   `-- model
    |-- handler
    |-- exception
    |-- mapper
    |-- repository
    |-- service
    `-- util

## Patrones de diseño utilizado

	- Dtos
	- Builder
	- Strategy

## Frameworks utilizados
	- Swagger: Para documentar los endpoints
	- MapStruct: Para hacer mapeos entre clases
	- Spring-security: para gestionar las cuentas de usuarios y sesiones
	- String-Jpa: para gestionar los datos en la db
	- String-boot: para construir la api

## Consideraciones

- Se utilizo una authenticacion de tipo bearer presente en el header de cada request. La aplicacion tiene la funcion de ser Authorization server y Resource server, con los tokens validos cargados en memoria

## Funcionalidades

### USUARIO

#### Registrar un usuario

POST: https://minesweeper-api-springboot.herokuapp.com/minesweeper/auth/register

Request:

```
{
  "password": "string",
  "username": "string"
}
```

Response:

```
{
  "authToken": "string",
  "refreshToken": "string"
}
```

#### Loguear un usuario

POST: https://minesweeper-api-springboot.herokuapp.com/minesweeper/auth/login

Request:

```
{
  "password": "string",
  "username": "string"
}
```

Response:

```
{
  "authToken": "string",
  "refreshToken": "string"
}
```

### JUEGO

Todas las request deben ser enviadas con una cabecera 'Authenticacion' con el valor 'Bearer ' + el authorization token obtenido

#### Crear un nuevo juego

POST: https://minesweeper-api-springboot.herokuapp.com/minesweeper/game

Request:

```
{
  "bombs": 0,
  "column": 0,
  "row": 0
}
```

Response:

```
{
  "board": [
    [
      0
    ]
  ],
  "createdDate": "2020-06-11T21:06:34.386Z",
  "gameId": 0,
  "gameStatus": "GAME_OVER",
  "timeSpend": 0
}
```
##### Consideraciones:
gameStatus: 
 - GAME_OVER: Juego perdido
 - PLAYING: El juego no termino, se puede seguir jugando
 - WIN: El juego ganado
timeSpend: tiempo en segundos

#### Actualizar un juego

PUT: https://minesweeper-api-springboot.herokuapp.com/minesweeper/game

Request:

```
{
  "column": 0,
  "gameId": 0,
  "row": 0,
  "shootType": "FLAG"
}
```
##### Consideraciones

shootType:
	-FLAG: para poner una bandera/marca en alguna casilla
	-DISCOVER: para descubrir una casilla

Response:

```
{
  "board": [
    [
      0
    ]
  ],
  "createdDate": "2020-06-11T21:06:34.386Z",
  "gameId": 0,
  "gameStatus": "WIN",
  "timeSpend": 0
}
```

##### Consideraciones:
gameStatus: 
 - GAME_OVER: Juego perdido
 - PLAYING: El juego no termino, se puede seguir jugando
 - WIN: El juego ganado
timeSpend: tiempo en segundos

#### Obtener un tableros especifico

GET: https://minesweeper-api-springboot.herokuapp.com/minesweeper/game/{gameId}

Response:

```
{
  "board": [
    [
      0
    ]
  ],
  "createdDate": "2020-06-11T21:06:34.386Z",
  "gameId": 0,
  "gameStatus": "PLAYING",
  "timeSpend": 0
}
```

##### Consideraciones:
gameStatus: 
 - GAME_OVER: Juego perdido
 - PLAYING: El juego no termino, se puede seguir jugando
 - WIN: El juego ganado
timeSpend: tiempo en segundos

#### Obtener todos los tableros del jugador

GET:https://minesweeper-api-springboot.herokuapp.com/minesweeper/game

Response:

```
{
  "board": [
    [
      0
    ]
  ],
  "createdDate": "2020-06-11T21:06:34.386Z",
  "gameId": 0,
  "gameStatus": "PLAYING",
  "timeSpend": 0
}
```

##### Consideraciones:
gameStatus: 
 - GAME_OVER: Juego perdido
 - PLAYING: El juego no termino, se puede seguir jugando
 - WIN: El juego ganado
timeSpend: tiempo en segundos
