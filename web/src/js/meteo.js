import {loadResource} from "./Loader.js";
import {addMarkerRestau} from "./map.js";

export async function loadMeteo() {
    try {
        //Les ressources pour les informations générales des stations
        const restaurant = await loadResource("https://www.infoclimat.fr/public-api/gfs/json?_ll=48.67103,6.15083&_auth=ARsDFFIsBCZRfFtsD3lSe1Q8ADUPeVRzBHgFZgtuAH1UMQNgUTNcPlU5VClSfVZkUn8AYVxmVW0Eb1I2WylSLgFgA25SNwRuUT1bPw83UnlUeAB9DzFUcwR4BWMLYwBhVCkDb1EzXCBVOFQoUmNWZlJnAH9cfFVsBGRSPVs1UjEBZwNkUjIEYVE6WyYPIFJjVGUAZg9mVD4EbwVhCzMAMFQzA2JRMlw5VThUKFJiVmtSZQBpXGtVbwRlUjVbKVIuARsDFFIsBCZRfFtsD3lSe1QyAD4PZA%3D%3D&_c=19f3aa7d766b6ba91191c8be71dd1ab2");
        console.log(restaurant.temperature)
        displayWeatherData(restaurant)

    } catch (error) {
        console.error('Une erreur s\'est produite lors du chargement et de l\'affichage des markers :', error);
    }
}



function displayWeatherData(data) {
    const photoTemplate = document.getElementById('photoTemplate').innerHTML;

    // Compiler le template avec Handlebars
    const compiledTemplate = Handlebars.compile(photoTemplate);

    // Parcourir les données et générer le HTML pour chaque entrée
    for (const [timestamp, values] of Object.entries(data)) {
        const time = new Date(timestamp).toLocaleString('fr-FR', { hour: '2-digit', minute: '2-digit' });
        console.log(values)
        const temperature = values.temperature; // Adapter selon la structure de vos données

        // Générer le HTML pour chaque entrée
        const html = compiledTemplate({ time: time, temperature: temperature });

        // Insérer le HTML généré dans le DOM
        const photoElement = document.querySelector('#weather');
        photoElement.innerHTML += html;
    }
}