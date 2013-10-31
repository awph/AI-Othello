AI-Othello
==========

# Idée générale #

D'une manière générale, on a réduit au strict minimum le nombre d'instanciation d'objet. Dans alpha-beta, nous avons préféré utilisé des attributs ainsi que des getters pour que la classe Joueur puisse récupérer les coordonnées du coup à jouer. Le même principe a été utilisé dans la plus part des fonctions scores, pour éviter de retourner des tuples.

Dans la classe Board, nous minimisons le parcours du tableau 2D représentant le jeu : à chaque vérification d'un coup, nous partons du coup joué et vérifions les conditions pour que le coup soit valide (gauche, droite, haut, bas, ...). Une fois le coup joué, nous utilisons le même principe pour retourner les pièces.

Afin d'optimiser encore plus notre système, nous avons évité d'utiliser des objets, donc dans la fonction getAllPossibleMove, à la place d'instancier un ensemble de Move, nous utilisons un tableau d'entier dans lequel un coup est représenté par 2 indices (paire = colonne, impaire = ligne). 

Une autre information importante est le fait que nous gérerons très bien le cas d'othellier NxN, N>=4, il suffit de changer la constante BOARD_SIZE.

# Amélioration alpha-beta

L'apha-beta implémenté fonctionne parfaitement, mais il y aurait moyen de l'améliorer. Par exemple, il se peut qu'on ait déjà calculé le score de l'évaluation d'un état. Il serait intéressant de stocker ce résultat dans une table de hashage avec une fonction qu'on définirait à partir d'un état de jeu (position des pions sur le plateau) ainsi que la profondeur, afin qu'on puisse voir si le résultat a déjà été calculé, afin de gagner du temps.

# Fonction d'évaluation #

Pour commencer, nous avons représenté 4 groupes de scores différents :

- la parité, représentant si le joueur est le dernier à jouer;
- la mobilité, représentant les possibilités du joueur;
- la position, représentant la position des pions du joueur sur la grille;
- la stabilité, correspondant à la puissance des pions sur l'othellier (coin, bord, ....).

Pour chacun de ces groupes, des scores internes sont pris en comptes :

- Parité
	- Si le joueur est le dernier à jouer
	
- Mobilité
	- Les disques frontières (pions ayant au moins une case adjacente vide)
	- La mobilité, le nombre de coups possible
	
- Position
	- Position des pions du joueur sur la grille. Cette matrice évolue selon la prise des coins.
	
- Stabilité
	- Les coins
	- Les cases avoisinantes les coins
	- Les bords
	- Les pièces irréversibles (en plus des coins)
	- Différence des pièces sur le plateau
	
Dans ces scores internes au groupe de scores, il y a des ratios ainsi que des facteurs. Dans certains cas, l'on fait un réajustement.

Pour les groupes de scores, nous avons utilisé des facteurs, selon la profondeur de jeu et si l'on commence ou pas.

Dans cette fonction d'évaluation, nous prenons en compte l'avancement du jeu, car au début du jeu les priorités ne sont pas les mêmes qu'à la fin.

Au début du jeu, nous misons principalement sur la parité ainsi que la mobilité.
Pour la suite, nous réduisons au fur et à mesure de la partie, l'importance de la parité, mobilité et placement et prenons plus en compte le score de stabilité.

Quelques exceptions sont présentes
 - Dans le cas où l'on ne peut pas joueur de pièce, nous pénalisons grandement le score;
 - Dans le cas où la partie est finie, on retourne une valeur correspondante si l'on a gagné la partie ou pas.
