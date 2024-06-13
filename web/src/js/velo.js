import {loadResource} from "./Loader.js";
import {addMarkerStation} from "./map.js";

export async function loadVelo() {
    try {
        //Les ressources pour les informations générales des stations
        const station_information = await loadResource("https://transport.data.gouv.fr/gbfs/nancy/station_information.json");
        // Les ressources pour le status de chaque station
        const station_status = await loadResource("https://transport.data.gouv.fr/gbfs/nancy/station_status.json");

        // on ajoute chaque station à la carte
        addMarkerStation(station_information,station_status)

    } catch (error) {
        console.error('Une erreur s\'est produite lors du chargement et de l\'affichage des markers :', error);
    }
}