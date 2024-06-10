


// Initialisation de la carte
var map = L.map('map').setView([48.683331, 6.2], 13); // Coordonnées pour Paris, France

// Ajout de la couche de tuiles OpenStreetMap
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: 'Map data © <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
}).addTo(map);

loadMarker()
async function loadMarker() {
    try {
         const station_information = await loadResource("/station_information.json");
         const station_status = await loadResource("/station_status.json");

         addMarkers(station_information,station_status)

    } catch (error) {
        console.error('Une erreur s\'est produite lors du chargement et de l\'affichage de la galerie :', error);
    }
}
 async function loadResource(uri) {
    return new Promise((resolve, reject) => {
        fetch("https://transport.data.gouv.fr/gbfs/nancy/" + uri)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Problème de chargement de la ressource ');
                }
                return response.json();
            })
            .then(data => {
                resolve(data);
            })
            .catch(error => {
                reject(error);
            });
    });
}

// Fonction pour ajouter des marqueurs
function addMarkers(information,status) {
    information.data.stations.forEach(function(station) {
        var status_station = status.data.stations.find(s => s.station_id === station.station_id);
        var popupContent = '<b>' + station.name + '</b><br>' +
            'Adresse: ' + station.address + '<br>' +
            '---------------------------- <br> ' +
            'Velos disponible : ' + (status_station ? status_station.num_bikes_available : 'N/A') + "<br>"+
            'Places disponible : ' + (status_station ? status_station.num_docks_available : 'N/A');
        L.marker([station.lat, station.lon]).addTo(map)
            .bindPopup(popupContent);
    });
}

