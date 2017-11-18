# SoccerStat

Lorsque vous lancez le logiciel, une page d'accueil s'affiche. Choisissez un document .csv à exécuter. Un fichier de données est disponible à l'adresse suivante : http://home.ifi.uio.no/paalh/dataset/alfheim/2013-11-03/zxy/2013-11-03_tromso_stromsgodset_first.csv 
Téléchargez ce fichier, qui correspond aux données collectées sur des joueurs de football pendant une mi-temps d'un match. Une fois téléchargé, sélectionnez ce fichier dans l'accueil de l'application. 

Une fois le fichier choisit, le chargement du fichier commence. Il peut prendre une dizaine de seconde, pendant lesquelles vous devez patienter. Une fois le chargement effectué, la représentation graphique des données commence. 

Vous pouvez sélectionner les joueurs à afficher dans la rubrique "Sélection". Pour choisir un joueur en particulier, choisissez "aucun joueur" puis choisissez à nouveau le numéro du joueur qui vous intéresse. 

Vous pouvez également choisir la vitesse de lecture des données à l'aide du curseur. La partie "caméra" permet de passer en mode "caméra embarquée" sur le joueur sélectionné. Des informations sur le joueur s'affichent alors. Vous pouvez revenir au mode par défaut en choisissant "Terrain".

Enfin, vous pouvez afficher les traces des joueurs en cliquant sur "Draw Lines". La couleur indique la vitesse du joueur. 

# Comment compiler

Le programme exécutable se trouve dans une archive .jar. Pour exécuter
sous Linux, ouvrez un terminal, placez vous dans le répertoire ou se
trouve l'archive "SoccerStat.jar" et exécutez la ligne de commande
suivante : "java -jar SoccerStat.jar"

Le temps de chargement du fichier .csv peut prendre 5 a 10 secondes et
le programme n'affiche pas la progression du chargement.

# JMonkey Engine

Informations et tutoriel disponible ici : https://www.lri.fr/~cfleury/teaching/et3-info/ProjetJavaIHM-2015/

