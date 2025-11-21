# TODO - Projet Web Services

## TP1a - WS des notes
- [ ] Créer WS pour renvoyer les notes d’un semestre (S1, S2, S3, S4)
- [ ] Créer WS pour renvoyer les notes d’une année (L1, L2, etc.)
- [ ] Utiliser le format JSON avec `status`, `data`, `error`
- [ ] Utiliser `camelCase`
- [ ] Ajouter un objet `meta`
## TP1b - Gestion des erreurs
- [ ] Ajouter la gestion des erreurs
- [ ] Gérer les cas :
   - [ ] Étudiant n’existe pas
   - [ ] Problème de connexion BDD
   - [ ] Erreur imprévue
   - [ ] Créer un code interne + message explicite pour chaque cas
## TP1c - Gestion du login
- [ ] Créer un WS d’authentification
- [ ] Fournir un token pour consulter les WSprécédents
- [ ] Gestion d’erreurs :
- [ ] Utilisateur non authentifié
## Déploiement & tests
- [ ] Préparer un environnement Docker fonctionnel
- [ ] Leprojet doit se lancer avec `docker compose up -d`
- [ ] Fournir des collectionsPostman pour tester tous les WS