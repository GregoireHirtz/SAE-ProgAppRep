import {loadResource} from "./Loader.js";
import {addMarkerRestau} from "./map.js";

export async function loadRestaurant() {
    try {
        //Les ressources pour les informations générales des stations
        const restaurant = await loadResource("");

        // on ajoute chaque station à la carte
        addMarkerRestau(restaurant)

    } catch (error) {
        console.error('Une erreur s\'est produite lors du chargement et de l\'affichage des markers :', error);
    }
}