import {loadResource} from "./Loader.js";
import {addMarkerRestau} from "./map.js";

export async function loadRestaurant() {
    try {
        //On récupère les données associées aux restaurants
        //const restaurant = await loadResource("http://localhost:8080/restaurants");

        // on ajoute chaque restaurant à la carte
        addMarkerRestau(restaurants)

    } catch (error) {
        console.error('Une erreur s\'est produite lors du chargement et de l\'affichage des markers :', error);
    }
}
const restaurants = [
    {"numrestau":1,"nom":"Le Petit Parisien","latitude":48.6366,"longitude":6.29},
    {"numrestau":2,"nom":"The London Pub","latitude":48.6666,"longitude":6.2},
    {"numrestau":3,"nom":"La Bella  Italia","latitude":48.6966,"longitude":6.23},
    {"numrestau":4,"nom":"Tokyo Sushi House","latitude":48.766,"longitude":6.2}
];

export async function fetchReservationDetails(idRestaurant, nbpers, date) {
    try {
        const response = await fetch(`http://localhost:8080/tables/${idRestaurant}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                nbpers: nbpers,
                date: date  // Assurez-vous que date est au format ISO 8601
            })
        });

        if (!response.ok) {
            throw new Error('Erreur lors de la récupération des détails de réservation');
        }

        const reservation = await response.json();
        console.log('Détails de réservation récupérés :', reservation);
        return reservation;

    } catch (error) {
        console.error('Erreur lors de la récupération des détails de réservation :', error);
        throw error;
    }
}


export async function finalizeReservation(nom, prenom, telephone, reservationDetails) {
    try {
        const response = await fetch(`/tables`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                nom: nom,
                prenom: prenom,
                telephone: telephone,
                ticket: reservationDetails
            })
        });

        if (!response.ok) {
            throw new Error('Erreur lors de la finalisation de la réservation');
        }

        const confirmation = await response.json();
        console.log('Réservation finalisée avec succès :', confirmation);
        return confirmation;

    } catch (error) {
        console.error('Erreur lors de la finalisation de la réservation :', error);
        throw error; // Propager l'erreur pour gérer dans le contexte approprié
    }
}

export async function ReservationFormSubmit(idRestaurant) {
    try {
        // Récupérer les valeurs du formulaire
        const nbPersonnes = document.getElementById('nbPersonnes').value;
        const dateHeure = document.getElementById('dateHeure').value;

        // Appeler fetchReservationDetails avec les données récupérées
        const reservationDetails = await fetchReservationDetails(idRestaurant, nbPersonnes, dateHeure);

        // Afficher les détails de la réservation (exemple : console.log)
        console.log('Détails de réservation :', reservationDetails);

        // Ajoutez ici le code pour finaliser l'affichage ou le traitement des détails de réservation

    } catch (error) {
        console.error('Erreur lors de la soumission du formulaire de réservation :', error);
        // Gérer l'erreur de manière appropriée, par exemple afficher un message à l'utilisateur
    }
}