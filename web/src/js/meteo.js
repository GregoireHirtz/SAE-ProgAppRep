import {loadResource} from "./Loader.js";
import {addMarkerRestau} from "./map.js";

export async function loadMeteo() {
    try {
        //on récupère les données météo
        const meteo = await loadResource("https://www.infoclimat.fr/public-api/gfs/json?_ll=48.67103,6.15083&_auth=ARsDFFIsBCZRfFtsD3lSe1Q8ADUPeVRzBHgFZgtuAH1UMQNgUTNcPlU5VClSfVZkUn8AYVxmVW0Eb1I2WylSLgFgA25SNwRuUT1bPw83UnlUeAB9DzFUcwR4BWMLYwBhVCkDb1EzXCBVOFQoUmNWZlJnAH9cfFVsBGRSPVs1UjEBZwNkUjIEYVE6WyYPIFJjVGUAZg9mVD4EbwVhCzMAMFQzA2JRMlw5VThUKFJiVmtSZQBpXGtVbwRlUjVbKVIuARsDFFIsBCZRfFtsD3lSe1QyAD4PZA%3D%3D&_c=19f3aa7d766b6ba91191c8be71dd1ab2");
        console.log(meteo)

    } catch (error) {
        console.error('Une erreur s\'est produite lors du chargement et de l\'affichage des markers :', error);
    }
}


// Exemple de JSON reçu
const jsonResponse = `{
    "request_state": 200,
    "request_key": "fd543c77e33d6c8a5e218e948a19e487",
    "message": "OK",
    "model_run": "20",
    "source": "internal:GFS:1",
    "data": {
        "2024-06-17 23:00:00": { "temperature": { "2m": 290.2 } },
        "2024-06-18 02:00:00": { "temperature": { "2m": 289 } },
        "2024-06-18 05:00:00": { "temperature": { "2m": 288.4 } },
        "2024-06-18 08:00:00": { "temperature": { "2m": 289.3 } },
        "2024-06-18 11:00:00": { "temperature": { "2m": 290.5 } },
        "2024-06-18 14:00:00": { "temperature": { "2m": 293.9 } },
        "2024-06-18 17:00:00": { "temperature": { "2m": 296.4 } },
        "2024-06-18 20:00:00": { "temperature": { "2m": 294.2 } },
        "2024-06-18 23:00:00": { "temperature": { "2m": 289.5 } },
        "2024-06-19 02:00:00": { "temperature": { "2m": 288.4 } }
    }
}`;

/*function afficherTemperatureActuelle() {
    // Parser le JSON
    const meteoData = JSON.parse(jsonResponse).data;

    // Obtenir l'heure actuelle et la formater
    const dateActuelle = new Date();
    const arrondirHeure = new Date(dateActuelle.setMinutes(0, 0, 0)); // Arrondir à l'heure complète la plus proche
    console.log(arrondirHeure)

    console.log(meteoData[arrondirHeure])
    // Vérifier si les données pour l'heure actuelle existent
    if (meteoData[arrondirHeure]) {
        console.log(`La température actuelle à ${arrondirHeure} est de ${meteoData[arrondirHeure].temperature['2m']} K.`);
    } else {
        console.log('Données météorologiques non disponibles pour l\'heure actuelle.');
    }
}

afficherTemperatureActuelle();*/
