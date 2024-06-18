import {loadResource} from "./Loader.js";
import {addMarkerRestau} from "./map.js";
import {displayTemperature} from "./ui.js";

export async function loadMeteo() {
    try {
        //on récupère les données météo
        const meteo = await loadResource("https://www.infoclimat.fr/public-api/gfs/json?_ll=48.67103,6.15083&_auth=ARsDFFIsBCZRfFtsD3lSe1Q8ADUPeVRzBHgFZgtuAH1UMQNgUTNcPlU5VClSfVZkUn8AYVxmVW0Eb1I2WylSLgFgA25SNwRuUT1bPw83UnlUeAB9DzFUcwR4BWMLYwBhVCkDb1EzXCBVOFQoUmNWZlJnAH9cfFVsBGRSPVs1UjEBZwNkUjIEYVE6WyYPIFJjVGUAZg9mVD4EbwVhCzMAMFQzA2JRMlw5VThUKFJiVmtSZQBpXGtVbwRlUjVbKVIuARsDFFIsBCZRfFtsD3lSe1QyAD4PZA%3D%3D&_c=19f3aa7d766b6ba91191c8be71dd1ab2");
        const targetDate = getCurrentDateTime();
        displayWeatherData(meteo,targetDate)

    } catch (error) {
        console.error('Une erreur s\'est produite lors du chargement et de l\'affichage des markers :', error);
    }
}




function getCurrentDateTime(offsetHours = 0) {
    const now = new Date();
    now.setHours(now.getHours() + offsetHours);
    const year = now.getFullYear();
    let month = now.getMonth() + 1; // Les mois sont indexés de 0 à 11, donc on ajoute 1
    month = month < 10 ? `0${month}` : month; // Pour obtenir le format avec deux chiffres (par exemple, 05 pour mai)
    let day = now.getDate();
    day = day < 10 ? `0${day}` : day; // Pour obtenir le format avec deux chiffres (par exemple, 03 pour le 3ème jour du mois)
    let hours = now.getHours();
    hours = hours < 10 ? `0${hours}` : hours; // Formatage pour obtenir deux chiffres pour les heures

    return `${year}-${month}-${day} ${hours}:00:00`;
}
function addOneHour(dateString) {
    // Convertir la chaîne de caractères en objet Date
    const date = new Date(dateString);

    // Ajouter une heure à la date
    date.setHours(date.getHours() + 1);

    // Retourner la date formatée en chaîne de caractères
    return date.toISOString().replace('T', ' ').substr(0, 19); // Format YYYY-MM-DD HH:MM:SS
}
function displayWeatherData(data, initialDateC) {


    let humidite = 'Not available';
    let temperature = 'Not available';
    let windSpeed = 'Not available';

    let dateC = initialDateC;
    let dateEntry = data[dateC];

    // Continuer à chercher jusqu'à ce que des données soient trouvées
    const originalDate = new Date(initialDateC.replace(' ', 'T'));
    while (!dateEntry) {
        dateC = addOneHour(dateC);
        dateEntry = data[dateC];

        // Ajouter une condition pour éviter une boucle infinie (par exemple, arrêt après 24 heures de recherche)
        const newDate = new Date(dateC.replace(' ', 'T'));
        if (newDate - originalDate > 24 * 60 * 60 * 1000) { // 24 heures en millisecondes
            console.log('No weather data found within 24 hours range.');
            return null;
        }
    }


    if (dateEntry) {
        // Stocker l'humidité à 2m dans une variable
        if (dateEntry.humidite && dateEntry.humidite['2m']) {
            humidite = dateEntry.humidite['2m'];
        }

        // Stocker la température solaire dans une variable
        if (dateEntry.temperature && dateEntry.temperature['2m']) {
            temperature = dateEntry.temperature['2m'] -273; // on mets la température en degré celsius
        }

        // Stocker la vitesse du vent moyen dans une variable
        if (dateEntry.vent_moyen && dateEntry.vent_moyen['10m']) {
            windSpeed = dateEntry.vent_moyen['10m'];
        }


    } else {

        console.log(`Weather data not available for ${dateC}`);
    }

    // Retourner ou utiliser les variables comme nécessaire
    return displayTemperature(temperature,humidite,windSpeed)
}
