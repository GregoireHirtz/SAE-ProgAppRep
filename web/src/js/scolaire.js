import {loadResource} from "./Loader.js";
import {addMarkerEtab} from "./map.js";

export async function loadScolaire() {
    try {
        //On récupère les données des incidents
        const etablissements = await loadResource("https://data.enseignementsup-recherche.gouv.fr/api/explore/v2.1/catalog/datasets/fr-esr-implantations_etablissements_d_enseignement_superieur_publics/records?limit=50&refine=localisation%3A\"Alsace%20-%20Champagne-Ardenne%20-%20Lorraine>Nancy-Metz>Meurthe-et-Moselle>Nancy\"");

        // on ajoute chaques incidents à la carte
        addMarkerEtab(etablissements)

    } catch (error) {
        console.error('Une erreur s\'est produite lors du chargement et de l\'affichage des markers :', error);
    }
}