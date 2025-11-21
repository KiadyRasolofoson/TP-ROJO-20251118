import { createRouter, createWebHistory } from 'vue-router'

import Semestre from '../views/Semestre.vue'
import Etudiant from '../views/Etudiant.vue'
import DetailNotes from '../views/DetailNotes.vue'
import Login from '../views/Login.vue'
import FicheEtudiant from '../views/FicheEtudiant.vue'
import NotesNiveau from '../views/NotesNiveau.vue'

const routes = [

    {
        path: '/',
        name: 'Semestre',
        component: Semestre
    },
    {
        path: '/login',
        name: 'Login',
        component: Login
    },
    {
        path: '/semestre/:id/etudiants',
        name: 'EtudiantsSemestre',
        component: Etudiant,
        props: true
    },
    {
        path: '/etudiant/:id/notes/:semestreId',
        name: 'DetailNotes',
        component: DetailNotes,
        props: true
    },
    {
        path: '/etudiant/:id/fiche',
        name: 'Fiche',
        component: FicheEtudiant,
        props: true
    },
    {
        path: '/etudiant/:id/niveau/:niveauId',
        name: 'NotesNiveau',
        component: NotesNiveau,
        props: true
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router