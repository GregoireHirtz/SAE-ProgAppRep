// Fonction pour afficher les plats par catégorie pour un restaurant donné

const plats = [
    {"numplat":1,"libelle":"assiette de crudités","type":"Entrée","prixunit":90},
    {"numplat":2,"libelle":"tarte de saison","type":"Dessert","prixunit":90},
    {"numplat":3,"libelle":"sorbet mirabelle","type":"Dessert","prixunit":90},
    {"numplat":4,"libelle":"filet de boeuf","type":"Viande","prixunit":90},
    {"numplat":5,"libelle":"salade verte","type":"Entrée","prixunit":90},
    {"numplat":6,"libelle":"chevre chaud","type":"Entrée","prixunit":90},
    {"numplat":7,"libelle":"pate lorrain","type":"Entrée","prixunit":90},
    {"numplat":8,"libelle":"saumon fumé","type":"Entrée","prixunit":90},
    {"numplat":9,"libelle":"entrecote printaniere","type":"Viande","prixunit":90},
    {"numplat":10,"libelle":"gratin dauphinois","type":"Plat","prixunit":90},
    {"numplat":11,"libelle":"brochet à l'oseille","type":"Poisson","prixunit":90},
    {"numplat":12,"libelle":"gigot d'agneau","type":"Viande","prixunit":90},
    {"numplat":13,"libelle":"crème caramel","type":"Dessert","prixunit":90},
    {"numplat":14,"libelle":"munster au cumin","type":"Fromage","prixunit":90},
    {"numplat":15,"libelle":"filet de sole au beurre","type":"Poisson","prixunit":90},
    {"numplat":16,"libelle":"fois gras de lorraine","type":"Entrée","prixunit":90},
    {"numplat":17,"libelle":"tarte aux pommes","type":"Dessert","prixunit":90},
    {"numplat":18,"libelle":"tarte aux mirabelles","type":"Dessert","prixunit":90},
    {"numplat":19,"libelle":"tarte aux quetsches","type":"Dessert","prixunit":90},
    {"numplat":20,"libelle":"tarte aux myrtilles","type":"Dessert","prixunit":90}
];

export async function displayPlatsParCategorie(idRestaurant) {
    try {
        // Filtre les plats par catégorie
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