import {addMarkerIncidents} from "./map.js";
import {loadResource} from "./Loader.js";

export async function loadHazards() {
    try {
        //On récupère les données des incidents
        const incidents = await loadResource("js/incidents.json"); //http://localhost:8080/hazards

        // on ajoute chaques incidents à la carte
        addMarkerIncidents(incidents)

    } catch (error) {
        console.error('Une erreur s\'est produite lors du chargement et de l\'affichage des markers :', error);
    }
}
