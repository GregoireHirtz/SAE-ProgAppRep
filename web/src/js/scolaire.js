import {loadResource} from "./Loader.js";
import {addMarkerEtab} from "./map.js";
import { config } from './config.js';

export async function loadScolaire() {
    try {
        //On récupère les données des incidents
        const etablissements = await loadResource(`${config.API_SCOLAIRE_URL}`);

        // on ajoute chaques incidents à la carte
        addMarkerEtab(etablissements)

    } catch (error) {
        console.error('Une erreur s\'est produite lors du chargement et de l\'affichage des markers :', error);
    }
}