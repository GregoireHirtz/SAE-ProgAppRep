#!/bin/bash

cd SAE_hirtz_mellano_naigeon_reveillard

echo "Démarrage API HTTP : port 8080"
java -jar api.jar localhost >/dev/null 2>&1 &
pid1=$!

echo "Démarrage RMI JAVA : annuaire port 1659"
java -jar rmi.jar >/dev/null 2>&1 &
pid2=$!

#echo "Démarrage du serveur HTTP sur le port 8000..."

cd ../web/src

#python3 -m http.server 8000 >/dev/null 2>&1 &
#pid3=$!

echo "PID: "$pid1 $pid2 #$pid3
echo "https://webetu.iutnc.univ-lorraine.fr/www/hirtz44u/web/src/"

stop_services() {
    echo " "
    echo "Arrêt des services..."
    kill $pid1
    kill $pid2
    #kill $pid3
    exit
}

trap stop_services SIGINT

while true; do
    read -p "Tapez 'quit' pour arrêter les services et terminer le script: " input
    if [ "$input" = "quit" ]; then
        stop_services
    else
        echo "Commande non reconnue. Tapez 'quit' pour arrêter les services."
    fi
done

wait
