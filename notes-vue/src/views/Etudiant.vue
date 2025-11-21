<template>
    <div class="etudiants-semestre">
        <div class="header">
            <router-link to="/semestre" class="back-btn">← Retour aux semestres</router-link>
            <h1>Étudiants du Semestre {{ semestreId }}</h1>
        </div>

        <div v-if="loading" class="loading">
            Chargement des étudiants...
        </div>

        <div v-if="error" class="error">
            Erreur: {{ error }}
        </div>

        <div v-if="!loading && !error" class="etudiants-list">
            <div v-if="etudiants.length === 0" class="no-data">
                Aucun étudiant trouvé pour ce semestre.
            </div>

            <div v-else>
                <div class="stats">
                    <p>Total d'étudiants: {{ etudiants.length }}</p>
                </div>

                <div class="etudiants-grid">
                    <div v-for="etudiant in etudiants" :key="etudiant.idStudent" class="etudiant-card">
                        <div class="etudiant-info">
                            <!-- Lien cliquable sur le nom de l'étudiant -->
                            <router-link :to="`/etudiant/${etudiant.idStudent}/fiche`" class="student-name-link">
                                <h3>{{ etudiant.firstname }} {{ etudiant.name }}</h3>
                            </router-link>

                            <p><strong>ID:</strong> {{ etudiant.idStudent }}</p>
                            <p v-if="etudiant.birth"><strong>Date de naissance:</strong> {{ formatDate(etudiant.birth)
                            }}</p>

                            <!-- Affichage des moyennes par semestre -->
                            <div class="averages-section">
                                <h4>Moyennes par semestre:</h4>
                                <div class="semester-averages">
                                    <div v-for="(moyenne, semestre) in etudiant.averages" :key="semestre"
                                        class="semester-average">
                                        <span class="semester-label">{{ semestre }}:</span>
                                        <router-link v-if="moyenne !== null"
                                            :to="`/etudiant/${etudiant.idStudent}/notes/${semestre.replace('S', '')}`"
                                            :class="getGradeClass(moyenne)" class="grade-value clickable-grade">
                                            {{ moyenne }}/20
                                        </router-link>
                                        <span v-else class="no-grade">-</span>
                                    </div>
                                </div>

                                <!-- Moyenne générale -->
                                <div class="general-average">
                                    <strong>Moyenne générale:</strong>
                                    <span :class="getGradeClass(etudiant.generalAverage)" class="general-grade">
                                        {{ etudiant.generalAverage }}/20
                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import apiService from '../services/api'

export default {
    name: 'EtudiantsSemestre',
    data() {
        return {
            etudiants: [],
            loading: false,
            error: null,
            semestreId: null
        }
    },
    async mounted() {
        this.semestreId = this.$route.params.id
        await this.fetchEtudiants()
    },
    methods: {
        async fetchEtudiants() {
            this.loading = true
            this.error = null

            try {
                // Récupération des étudiants depuis l'API
                const response = await apiService.getEtudiantsBySemestre(this.semestreId)
                this.etudiants = response.data || []

                if (!this.etudiants || this.etudiants.length === 0) {
                    this.etudiants = this.getMockEtudiants()
                }
            } catch (err) {
                console.error('Error fetching students:', err)
                this.error = 'Impossible de charger la liste des étudiants'
                this.etudiants = this.getMockEtudiants()
            } finally {
                this.loading = false
            }
        },

        getMockEtudiants() {
            return [
                {
                    idStudent: 1,
                    name: "Dupont",
                    firstname: "Jean",
                    birth: "2000-05-15",
                    averages: {
                        "S1": 14.5,
                        "S2": 15.2,
                        "S3": 13.8,
                        "S4": 16.0
                    },
                    generalAverage: 14.88
                },
                {
                    idStudent: 2,
                    name: "Martin",
                    firstname: "Marie",
                    birth: "1999-08-22",
                    averages: {
                        "S1": 16.2,
                        "S2": 15.8,
                        "S3": 17.1,
                        "S4": 16.5
                    },
                    generalAverage: 16.4
                }
            ]
        },

        formatDate(dateString) {
            if (!dateString) return 'Non spécifié'
            const date = new Date(dateString)
            return date.toLocaleDateString('fr-FR')
        },

        getGradeClass(average) {
            if (average >= 16) return 'excellent';
            if (average >= 14) return 'very-good';
            if (average >= 12) return 'good';
            if (average >= 10) return 'passable';
            return 'fail';
        }
    }
}
</script>

<style scoped>
.etudiants-semestre {
    max-width: 1000px;
    margin: 0 auto;
    padding: 20px;
}

.header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 30px;
    flex-wrap: wrap;
    gap: 20px;
}

.back-btn {
    background: #6c757d;
    color: white;
    padding: 10px 16px;
    text-decoration: none;
    border-radius: 4px;
    font-weight: 500;
    transition: background-color 0.2s;
}

.back-btn:hover {
    background: #5a6268;
}

h1 {
    color: #2c3e50;
    margin: 0;
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

.no-data {
    text-align: center;
    padding: 40px;
    font-size: 16px;
    color: #666;
    background: #f8f9fa;
    border-radius: 8px;
}

.stats {
    background: #e9ecef;
    padding: 15px;
    border-radius: 8px;
    margin-bottom: 20px;
    text-align: center;
}

.stats p {
    margin: 0;
    font-weight: 500;
    color: #495057;
}

.etudiants-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
    gap: 20px;
}

.etudiant-card {
    background: white;
    border: 1px solid #e9ecef;
    border-radius: 8px;
    padding: 20px;
    transition: transform 0.2s, box-shadow 0.2s;
}

.etudiant-card:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.student-name-link {
    text-decoration: none;
    color: inherit;
    display: block;
}

.student-name-link h3 {
    margin: 0 0 15px 0;
    color: #2c3e50;
    font-size: 1.2em;
    transition: color 0.2s;
}

.student-name-link:hover h3 {
    color: #42b983;
}

.etudiant-info p {
    margin: 8px 0;
    color: #6c757d;
    font-size: 0.95em;
}

.etudiant-info strong {
    color: #495057;
}

.averages-section {
    margin-top: 15px;
    padding-top: 15px;
    border-top: 1px solid #e9ecef;
}

.averages-section h4 {
    margin: 0 0 10px 0;
    color: #495057;
    font-size: 1em;
}

.semester-averages {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 8px;
    margin-bottom: 15px;
}

.semester-average {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.semester-label {
    font-weight: 500;
    color: #6c757d;
}

.clickable-grade {
    text-decoration: none;
    padding: 2px 6px;
    border-radius: 3px;
    font-weight: bold;
    font-size: 0.9em;
    transition: all 0.2s;
    display: inline-block;
}

.clickable-grade:hover {
    transform: scale(1.05);
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.general-average {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-top: 10px;
    border-top: 1px dashed #dee2e6;
    font-weight: 500;
}

.general-grade {
    padding: 4px 8px;
    border-radius: 4px;
    font-weight: bold;
}

/* Classes de couleur pour les notes */
.excellent {
    color: #28a745;
    background-color: #d4edda;
}

.very-good {
    color: #20c997;
    background-color: #d1f2eb;
}

.good {
    color: #17a2b8;
    background-color: #d1ecf1;
}

.passable {
    color: #ffc107;
    background-color: #fff3cd;
}

.fail {
    color: #dc3545;
    background-color: #f8d7da;
}

.no-grade {
    color: #6c757d;
    font-style: italic;
}
</style>