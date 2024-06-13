
import {loadVelo} from "./velo.js";
import {loadRestaurant} from "./Restaurant.js";


let map
function createMap() {
    map = L.map('map').setView([48.683331, 6.2], 13);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
    }).addTo(map);

    return map;
}
export function addMarkerStation(information,status) {
    suppmarker()
    information.data.stations.forEach(function(station) {
        // on fait le lien entre les ressources informations et status de la station
        var status_station = status.data.stations.find(s => s.station_id === station.station_id);
        var popupContent = '<b>' + station.name + '</b><br>' +
            'Adresse: ' + station.address + '<br>' +
            '---------------------------- <br> ' +
            'Velos disponible : ' + (status_station ? status_station.num_bikes_available : 'N/A') + "<br>"+
            'Places disponible : ' + (status_station ? status_station.num_docks_available : 'N/A');
        //on ajoute le marker qui correspond à la latitude et longitude de la station
        L.marker([station.lat, station.lon]).addTo(map)
            .bindPopup(popupContent);
    });
}

export function addMarkerRestau(restaurants) {
    restaurants.forEach(function(restaurant) {
        // on fait le lien entre les ressources informations et status de la station
        var popupContent = '<b>' + restaurant.nom + '</b><br>' +
            //on ajoute le marker qui correspond à la latitude et longitude de la station
            L.marker([restaurant.latitude, restaurant.longitude]).addTo(map)
                .bindPopup(popupContent);
    });
}


function suppmarker() {
    map.eachLayer(function (layer) {
        if (layer instanceof L.Marker) {
            map.removeLayer(layer);
        }
    });
}


createMap()

document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('velo').addEventListener('click', loadVelo);
    document.getElementById('reset').addEventListener('click', suppmarker);
    document.getElementById('restaurants').addEventListener('click', loadRestaurant);
});
