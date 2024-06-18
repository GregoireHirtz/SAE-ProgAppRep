import {loadVelo} from "./velo.js";
import {loadRestaurant,finalizeReservation,ReservationFormSubmit} from "./Restaurant.js";
import {displayPlatsParCategorie,displayReservationForm} from "./ui.js";
import {loadResource} from "./Loader.js";
import {loadHazards} from "./hazards.js";
import {loadScolaire} from "./scolaire.js";
import {loadMeteo} from "./meteo.js";


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
        L.marker([station.lat, station.lon], {icon: veloIcon}).addTo(map)
            .bindPopup(popupContent);
    });
}



export function addMarkerRestau(restaurants) {
    suppmarker();
    restaurants.forEach(restaurant => {
        var popupContent = '<b>' + restaurant.nom + '</b><br>' +
            '<label for="nbPersonnes">Nombre de personnes :</label>' +
            '<input type="number" id="nbPersonnes" name="nbPersonnes" value="1"><br>' +
            '<label for="dateHeure">Date et heure :</label>' +
            '<input type="datetime-local" id="dateHeure" name="dateHeure"><br>' +
            '<button class="reserve-btn" onclick=\'openLightbox(' + JSON.stringify(restaurant) + ')\'>Réserver</button>';
        // Ajout du marker correspondant à la latitude et à la longitude du restaurant
        L.marker([restaurant.latitude, restaurant.longitude], {icon: restauIcon}).addTo(map)
            .bindPopup(popupContent);
    });
}


export function addMarkerEtab(etablissements) {
    suppmarker();
    etablissements.results.forEach(infos => {
        const popupContent = `
        <h2>${infos.implantation_lib}</h2>
        <p><strong>Type:</strong> ${infos.bcnag_n_type_uai_libelle_edition}</p>
        <p><strong>Adresse:</strong> ${infos.adresse_uai}, ${infos.code_postal_uai} ${infos.localite_acheminement_uai}</p>
        <p><strong>Département:</strong> ${infos.dep_nom}</p>
        <p><strong>Région:</strong> ${infos.reg_nom}</p>
        <p><strong>Date d'ouverture:</strong> ${infos.date_ouverture}</p>
        <p><strong>Effectif:</strong> ${infos.effectif}</p>
    `;
        L.marker([infos.coordonnees.lat, infos.coordonnees.lon], {icon: scolaireIcon}).addTo(map)
            .bindPopup(popupContent);
    });
}

export async function addMarkerIncidents(incidents) {
    try {
        suppmarker();
        incidents.incidents.forEach(incident => {
            const { polyline, location_description, street } = incident.location;
            const { description, starttime, endtime } = incident;

            // On récupère la latitude et la longitude
            const [lat, lon] = polyline.split(' ').map(Number);

            // on créer une popup avec des informations de l'incident
            const popupContent = `
                <b>${description}</b><br>
                <b>Localisation:</b> ${location_description}<br>
                <b>Rue:</b> ${street}<br>
                <b>Début:</b> ${new Date(starttime).toLocaleString()}<br>
                <b>Fin:</b> ${new Date(endtime).toLocaleString()}
            `;
            // on ajoute le marker
            L.marker([lat, lon], {icon: incidentIcon}).addTo(map)
                .bindPopup(popupContent);
        });
    } catch (error) {
        console.error("Erreur lors de la récupération des incidents :", error);
    }
}

function suppmarker() {
    map.eachLayer(function (layer) {
        if (layer instanceof L.Marker) {
            map.removeLayer(layer);
        }
    });
}


createMap()
loadMeteo()

document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('velo').addEventListener('click', loadVelo);
    document.getElementById('reset').addEventListener('click', suppmarker);
    document.getElementById('restaurants').addEventListener('click', loadRestaurant);
    document.getElementById('incidents').addEventListener('click', loadHazards);
    document.getElementById('scolaire').addEventListener('click', loadScolaire);

    window.openLightbox=openLightbox;
    window.closeLightbox=closeLightbox;
});


//--------------------------LightBox-----------------------------
// Fonction pour ouvrir la lightbox
export function openLightbox(restaurant) {
    //ReservationFormSubmit(restaurant.numrestau)
    const lightbox = document.getElementById('lightbox');
    displayReservationForm(restaurant)
    displayPlatsParCategorie(restaurant.numrestau) //restaurant.numrestau

    const lightboxContent = document.getElementById('lightbox-content');
    lightbox.style.display = 'block';
}

// Fonction pour fermer la lightbox
function closeLightbox() {
    const lightbox = document.getElementById('lightbox');
    lightbox.style.display = 'none';
}


var restauIcon = L.icon({
    iconUrl: 'img/restaurant.png', // Chemin vers votre image de marqueur rouge
    iconSize: [31, 51], // Taille de l'icône
    iconAnchor: [12, 41], // Point d'ancrage de l'icône
    popupAnchor: [1, -34], // Point d'ancrage de la popup

});

var incidentIcon = L.icon({
    iconUrl: 'img/incidents.png', // Chemin vers votre image de marqueur rouge
    iconSize: [31, 51], // Taille de l'icône
    iconAnchor: [12, 41], // Point d'ancrage de l'icône
    popupAnchor: [1, -34], // Point d'ancrage de la popup

});

var scolaireIcon = L.icon({
    iconUrl: 'img/scolaire.png', // Chemin vers votre image de marqueur rouge
    iconSize: [31, 51], // Taille de l'icône
    iconAnchor: [12, 41], // Point d'ancrage de l'icône
    popupAnchor: [1, -34], // Point d'ancrage de la popup

});
var veloIcon = L.icon({
    iconUrl: 'img/velo.png', // Chemin vers votre image de marqueur rouge
    iconSize: [31, 51], // Taille de l'icône
    iconAnchor: [12, 41], // Point d'ancrage de l'icône
    popupAnchor: [1, -34], // Point d'ancrage de la popup

});





