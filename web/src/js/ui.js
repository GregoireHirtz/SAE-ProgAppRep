// Fonction pour afficher les plats par catégorie pour un restaurant donné
import {loadResource} from "./Loader.js";
import { config } from './config.js';


export async function displayPlatsParCategorie(idRestaurant) {
    try {
        // Filtre les plats par catégorie
        const plats = await loadResource(`${config.API_BASE_URL}plats/${idRestaurant}`);

        const categories = [
            { type: "Entrée", items: plats.filter(plat => plat.type === "Entrée") },
            { type: "Plat", items: plats.filter(plat => plat.type === "Plat") },
            { type: "Viande", items: plats.filter(plat => plat.type === "Viande") },
            { type: "Poisson", items: plats.filter(plat => plat.type === "Poisson") },
            { type: "Dessert", items: plats.filter(plat => plat.type === "Dessert") },
            { type: "Fromage", items: plats.filter(plat => plat.type === "Fromage") }
        ];

        // Compile le modèle Handlebars
        const source = document.getElementById('menu-template').innerHTML;
        const template = Handlebars.compile(source);

        // Génère le HTML
        const html = template({ categories });

        // Insère le HTML généré dans la page
        document.getElementById('menu').innerHTML = html;

    } catch (error) {
        console.error('Erreur lors de la récupération des plats :', error);
    }
}

export function displayReservationForm(restaurant) {
    const lightbox = document.getElementById('lightbox');
    const reservationContent = document.getElementById('reservation');
    console.log(restaurant)

    // Génération du contenu de la lightbox avec les détails du restaurant et le formulaire de réservation
    reservationContent.innerHTML = `
            <h2>Reservation :  ${restaurant.nom}</h2>
            <form id="reservation-form">
                <label for="nom">Nom :</label>
                <input type="text" id="nom" name="nom" required><br>
                <label for="prenom">Prénom :</label>
                <input type="text" id="prenom" name="prenom" required><br>
                <label for="telephone">Numéro de téléphone :</label>
                <input type="tel" id="telephone" name="telephone" required><br>
                <button type="submit" class="reserve-btn" onclick="handleReservationSubmit('click')">Valider</button>
            </form>
        `;

    lightbox.style.display = 'block'; // Affiche la lightbox

}

export function displayTemperature(temperature) {
    const meteoContainer = document.getElementById('meteo');

    // Efface le contenu précédent
    meteoContainer.innerHTML = '';

    // Crée un élément pour afficher la température
    const tempElement = document.createElement('p');
    if(temperature>10) {
        tempElement.textContent = `🔆 Température actuelle : ${temperature} °C`;
    }


    // Ajoute l'élément à la fin du container météo
    meteoContainer.appendChild(tempElement);
}
