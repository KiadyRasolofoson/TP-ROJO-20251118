<template>
    <div class="semestre">
        <h1>Liste des Semestres</h1>

        <!-- Loading state -->
        <div v-if="loading" class="loading">
            Chargement des semestres...
        </div>

        <!-- Error state -->
        <div v-if="error" class="error">
            Erreur: {{ error }}
        </div>

        <!-- Success state -->
        <div v-if="!loading && !error" class="semestre-list">
            <div v-for="semestre in semestres" :key="semestre.idSemester" class="semestre-card">
                <div class="semestre-info">
                    <h3>{{ semestre.label }}</h3>
                    <p>ID: {{ semestre.idSemester }}</p>
                    <p>Code: S{{ semestre.idSemester }}</p>
                </div>

                <router-link :to="`/semestre/${semestre.idSemester}/etudiants`" class="btn-etudiants">
                    Voir étudiants
                </router-link>
            </div>
        </div>
    </div>
</template>

<script>
import apiService from '../services/api'

export default {
    name: 'Semestre',
    data() {
        return {
            semestres: [],
            loading: false,
            error: null
        }
    },
    async mounted() {
        await this.fetchSemestres()
    },
    methods: {
        async fetchSemestres() {
            this.loading = true
            this.error = null

            try {
                // Récupération des données depuis l'API
                const data = await apiService.getSemestres()
                
                // Transformation des données pour utiliser la structure de l'API
                this.semestres = data.map(semestre => ({
                    idSemester: semestre.idSemester,
                    label: semestre.label,
                    code: `S${semestre.idSemester}`,
                }))
            } catch (err) {
                console.error('Error fetching semestres:', err)
                this.error = 'Impossible de charger les semestres'
                // Utilisation des données statiques en cas d'erreur
                this.semestres = this.getStaticSemestres()
            } finally {
                this.loading = false
            }
        },

    }
}
</script>

<style scoped>
.semestre {
    max-width: 800px;
    margin: 0 auto;
    padding: 20px;
}

h1 {
    text-align: center;
    color: #2c3e50;
    margin-bottom: 30px;
}

.loading {
    text-align: center;
    padding: 40px;
    font-size: 18px;
    color: #666;
}

.error {
    background: #fee;
    color: #c33;
    padding: 15px;
    border-radius: 4px;
    text-align: center;
    margin: 20px 0;
}

.semestre-list {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap: 20px;
}

.semestre-card {
    background: #f8f9fa;
    border: 1px solid #e9ecef;
    border-radius: 8px;
    padding: 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    transition: transform 0.2s, box-shadow 0.2s;
}

.semestre-card:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.semestre-info h3 {
    margin: 0 0 10px 0;
    color: #2c3e50;
}

.semestre-info p {
    margin: 5px 0;
    color: #6c757d;
}

.btn-etudiants {
    background: #42b983;
    color: white;
    padding: 10px 16px;
    text-decoration: none;
    border-radius: 4px;
    font-weight: 500;
    transition: background-color 0.2s;
}

.btn-etudiants:hover {
    background: #3aa876;
}
</style>