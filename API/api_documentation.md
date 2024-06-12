# Doc de l'api

## Récupérer la liste des restaurants
### __GET__ /restaurants
### Résultat :
```json
[
  {"numrestau":1,"nom":"Le Petit Parisien","latitude":48.8566,"longitude":2.3522},
  {"numrestau":2,"nom":"The London Pub","latitude":51.5074,"longitude":-0.1278},
  {"numrestau":3,"nom":"La Bella  Italia","latitude":41.9028,"longitude":12.4964},
  {"numrestau":4,"nom":"Tokyo Sushi House","latitude":35.6895,"longitude":139.6917}
]
```

## Récupérer les informations d'un restaurant
### __GET__ /restaurants/{*idRestaurant*}
### Résultat :
```json
{
  "numrestau":1,
  "nom":"Le Petit Parisien",
  "latitude":48.8566,
  "longitude":2.3522
}
```

## Bloquer une réservation
### __POST__ /tables/{*idRestaurant*}
### Request body :
```json
{
  "nbpers":"2",
  "date":"2024-06-11"
}
```

### Résultat:
```json
{
  "numres":"1",
  "nbpers":"2",
  "date":"2024-06-11",
  "numrestau":"1"
}
```

## Finaliser la réservation
### __POST__ /tables
### Request body :
```json
{
  "nom":"Jean",
  "prenom":"Dupont",
  "telephone":"0606060606",
  "ticket":{
    "numres":"1",
    "nbpers":"2",
    "date":"2024-06-11",
    "numrestau":"1"
  }
}
```

## Récupérer les tables d'un restaurant
### __GET__ /tables/{*idRestaurant*}
### Résultat :
```json
[
  {"numtab":1,"nbplace":2, "numrestau": 1},
  {"numtab":2,"nbplace":4, "numrestau": 1},
  {"numtab":3,"nbplace":6, "numrestau": 1},
  {"numtab":4,"nbplace":8, "numrestau": 1},
  {"numtab":5,"nbplace":6, "numrestau": 1},
  {"numtab":6,"nbplace":2, "numrestau": 1}
]
```

## Récupérer les tables libres d'un restaurant à une date donnée
### __GET__ /tables/{*idRestaurant*}/{*date*}
### Résultat :
```json
[
  {"numtab":1,"nbplace":2, "numrestau": 1},
  {"numtab":3,"nbplace":6, "numrestau": 1},
  {"numtab":6,"nbplace":2, "numrestau": 1}
]
```