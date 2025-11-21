<template>
    <div class="fiche-etudiant">
        <div class="header">
            <router-link to="/semestre" class="back-btn">← Retour aux semestres</router-link>
            <h1>Fiche Étudiant</h1>
        </div>

        <!-- Loading state -->
        <div v-if="loading" class="loading">
            Chargement des informations de l'étudiant...
        </div>

        <!-- Error state -->
        <div v-if="error" class="error">
            Erreur: {{ error }}
        </div>

        <!-- Success state -->
        <div v-if="!loading && !error && data" class="student-content">
            <!-- Informations personnelles -->
            <div class="personal-info">
                <div class="info-card">
                    <h2>Informations Personnelles</h2>
                    <div class="info-grid">
                        <div class="info-item">
                            <label>ID:</label>
                            <span>{{ data.student.id }}</span>
                        </div>
                        <div class="info-item">
                            <label>Nom:</label>
                            <span>{{ data.student.name }}</span>
                        </div>
                        <div class="info-item">
                            <label>Prénom:</label>
                            <span>{{ data.student.firstname }}</span>
                        </div>
                        <div class="info-item">
                            <label>Date de naissance:</label>
                            <span>{{ formatDate(data.student.birth) }}</span>
                        </div>
                        <div class="info-item">
                            <label>Moyenne générale:</label>
                            <span :class="getGradeClass(data.generalAverage)" class="general-grade">
                                {{ data.generalAverage }}/20
                            </span>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Tableau des moyennes par semestre -->
            <div class="averages-section">
                <div class="section-header">
                    <h2>Moyennes par Semestre</h2>
                    <div class="summary">
                        <span class="summary-item">Semestres avec notes: {{ data.semestersWithGrades }}</span>
                    </div>
                </div>

                <div class="table-container">
                    <table class="averages-table">
                        <thead>
                            <tr>
                                <th>Semestre</th>
                                <th>Moyenne</th>
                                <th>Statut</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="(moyenne, semestre) in data.semesterAverages" :key="semestre"
                                :class="{ 'no-data': moyenne === null }">
                                <td class="semester-name">
                                    <strong>{{ semestre }}</strong>
                                </td>
                                <td class="grade-cell">
                                    <span v-if="moyenne !== null" :class="getGradeClass(moyenne)" class="grade-display">
                                        {{ moyenne }}/20
                                    </span>
                                    <span v-else class="no-grade">-</span>
                                </td>
                                <td class="status-cell">
                                    <span v-if="moyenne !== null" :class="getStatusClass(moyenne)" class="status-badge">
                                        {{ getStatusText(moyenne) }}
                                    </span>
                                    <span v-else class="status-unknown">Non noté</span>
                                </td>
                                <td class="actions-cell">
                                    <router-link v-if="moyenne !== null"
                                        :to="`/etudiant/${data.student.id}/notes/${semestre.replace('S', '')}`"
                                        class="details-btn">
                                        Voir détails
                                    </router-link>
                                    <span v-else class="no-action">-</span>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- Graphique des moyennes (optionnel) -->
            <div class="chart-section">
                <h2>Évolution des Moyennes</h2>
                <div class="chart-container">
                    <div class="chart-bars">
                        <div v-for="(moyenne, semestre) in data.semesterAverages" :key="semestre"
                            class="chart-bar-container">
                            <div class="semester-label">{{ semestre }}</div>
                            <div v-if="moyenne !== null" class="chart-bar-wrapper">
                                <div class="chart-bar" :style="{ height: calculateBarHeight(moyenne) + '%' }"
                                    :class="getGradeClass(moyenne)">
                                    <span class="bar-value">{{ moyenne }}</span>
                                </div>
                            </div>
                            <div v-else class="chart-bar-empty">
                                <span>-</span>
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
    name: 'FicheEtudiant',
    data() {
        return {
            data: null,
            loading: false,
            error: null,
            studentId: null
        }
    },
    async mounted() {
        this.studentId = this.$route.params.id
        await this.fetchStudentData()
    },
    methods: {
        async fetchStudentData() {
            this.loading = true
            this.error = null

            try {
                const response = await apiService.getAveragesEtudiant(this.studentId)

                if (response.success) {
                    this.data = response.data
                } else {
                    throw new Error(response.error || 'Erreur lors du chargement des données')
                }
            } catch (err) {
                console.error('Error fetching student data:', err)
                this.error = 'Impossible de charger les informations de l\'étudiant'
                // Données mockées pour le test
                this.data = this.getMockData()
            } finally {
                this.loading = false
            }
        },

        getMockData() {
            return {
                student: {
                    id: 1,
                    name: "Doe",
                    firstname: "John",
                    birth: "2002-05-20"
                },
                semesterAverages: {
                    "S1": 14.17,
                    "S2": 14.33,
                    "S3": 14.17,
                    "S4": 14.67
                },
                generalAverage: 14.34,
                semestersWithGrades: 4
            }
        },

        formatDate(dateString) {
            if (!dateString) return 'Non spécifié'
            const date = new Date(dateString)
            return date.toLocaleDateString('fr-FR')
        },

        getGradeClass(grade) {
            if (grade >= 16) return 'excellent';
            if (grade >= 14) return 'very-good';
            if (grade >= 12) return 'good';
            if (grade >= 10) return 'passable';
            return 'fail';
        },

        getStatusClass(grade) {
            if (grade >= 10) return 'status-pass';
            return 'status-fail';
        },

        getStatusText(grade) {
            if (grade >= 16) return 'Excellent';
            if (grade >= 14) return 'Très bien';
            if (grade >= 12) return 'Bien';
            if (grade >= 10) return 'Passable';
            return 'Insuffisant';
        },

        calculateBarHeight(grade) {
            // Convertir la note en pourcentage (0-20 → 0-100)
            return Math.min((grade / 20) * 100, 100);
        }
    }
}
</script>

<style scoped>
.fiche-etudiant {
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

.personal-info {
    margin-bottom: 30px;
}

.info-card {
    background: white;
    border: 1px solid #e9ecef;
    border-radius: 8px;
    padding: 25px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.info-card h2 {
    margin: 0 0 20px 0;
    color: #2c3e50;
    border-bottom: 2px solid #42b983;
    padding-bottom: 10px;
}

.info-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    gap: 15px;
}

.info-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px 0;
    border-bottom: 1px solid #f8f9fa;
}

.info-item:last-child {
    border-bottom: none;
}

.info-item label {
    font-weight: 600;
    color: #495057;
}

.info-item span {
    color: #6c757d;
}

.general-grade {
    padding: 6px 12px;
    border-radius: 6px;
    font-weight: bold;
    font-size: 1.1em;
}

.averages-section {
    background: white;
    border: 1px solid #e9ecef;
    border-radius: 8px;
    padding: 25px;
    margin-bottom: 30px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    flex-wrap: wrap;
    gap: 15px;
}

.section-header h2 {
    margin: 0;
    color: #2c3e50;
    border-bottom: 2px solid #42b983;
    padding-bottom: 10px;
}

.summary {
    display: flex;
    gap: 20px;
}

.summary-item {
    background: #e9ecef;
    padding: 8px 12px;
    border-radius: 4px;
    font-size: 0.9em;
    color: #495057;
}

.table-container {
    overflow-x: auto;
}

.averages-table {
    width: 100%;
    border-collapse: collapse;
    background: white;
}

.averages-table th {
    background: #f8f9fa;
    padding: 15px;
    text-align: left;
    font-weight: 600;
    color: #495057;
    border-bottom: 2px solid #dee2e6;
}

.averages-table td {
    padding: 15px;
    border-bottom: 1px solid #e9ecef;
}

.averages-table tr:hover {
    background: #f8f9fa;
}

.averages-table tr.no-data {
    background: #f8f9fa;
    color: #6c757d;
}

.semester-name {
    font-weight: 500;
    color: #2c3e50;
}

.grade-display {
    padding: 6px 12px;
    border-radius: 6px;
    font-weight: bold;
    display: inline-block;
}

.status-badge {
    padding: 4px 8px;
    border-radius: 4px;
    font-size: 0.85em;
    font-weight: 500;
}

.status-pass {
    background: #d4edda;
    color: #155724;
}

.status-fail {
    background: #f8d7da;
    color: #721c24;
}

.status-unknown {
    color: #6c757d;
    font-style: italic;
}

.details-btn {
    background: #42b983;
    color: white;
    padding: 6px 12px;
    text-decoration: none;
    border-radius: 4px;
    font-size: 0.85em;
    transition: background-color 0.2s;
}

.details-btn:hover {
    background: #3aa876;
}

.no-action {
    color: #6c757d;
    font-style: italic;
}

.chart-section {
    background: white;
    border: 1px solid #e9ecef;
    border-radius: 8px;
    padding: 25px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.chart-section h2 {
    margin: 0 0 20px 0;
    color: #2c3e50;
    border-bottom: 2px solid #42b983;
    padding-bottom: 10px;
}

.chart-container {
    padding: 20px 0;
}

.chart-bars {
    display: flex;
    justify-content: space-around;
    align-items: flex-end;
    height: 200px;
    gap: 20px;
}

.chart-bar-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    flex: 1;
}

.semester-label {
    margin-bottom: 10px;
    font-weight: 500;
    color: #495057;
}

.chart-bar-wrapper {
    height: 150px;
    display: flex;
    align-items: flex-end;
    width: 60px;
}

.chart-bar {
    width: 100%;
    border-radius: 4px 4px 0 0;
    display: flex;
    align-items: flex-end;
    justify-content: center;
    transition: height 0.3s ease;
    position: relative;
}

.bar-value {
    color: white;
    font-weight: bold;
    font-size: 0.8em;
    margin-bottom: 5px;
    text-shadow: 1px 1px 1px rgba(0, 0, 0, 0.3);
}

.chart-bar-empty {
    height: 150px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #6c757d;
    font-style: italic;
}

/* Classes de couleur pour les notes */
.excellent {
    background: linear-gradient(to top, #28a745, #34ce57);
}

.very-good {
    background: linear-gradient(to top, #20c997, #3dd4ad);
}

.good {
    background: linear-gradient(to top, #17a2b8, #1fc8e3);
}

.passable {
    background: linear-gradient(to top, #ffc107, #ffd351);
}

.fail {
    background: linear-gradient(to top, #dc3545, #e4606d);
}

@media (max-width: 768px) {
    .info-grid {
        grid-template-columns: 1fr;
    }

    .section-header {
        flex-direction: column;
        align-items: flex-start;
    }

    .chart-bars {
        gap: 10px;
    }

    .chart-bar-wrapper {
        width: 40px;
    }
}
</style>