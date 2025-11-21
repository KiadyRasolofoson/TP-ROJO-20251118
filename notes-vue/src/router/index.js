import { createRouter, createWebHistory } from 'vue-router'

import Semestre from '../views/Semestre.vue'
import Etudiant from '../views/Etudiant.vue'
import DetailNotes from '../views/DetailNotes.vue'
import Login from '../views/Login.vue'

const routes = [

    {
        path: '/semestre',
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
    }

]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router