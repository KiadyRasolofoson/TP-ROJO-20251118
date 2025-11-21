<template>
    <div class="detail-notes">
        <div class="header">
            <router-link :to="`/semestre/${semestreId}/etudiants`" class="back-btn">← Retour aux étudiants</router-link>
            <h1>Détail des Notes</h1>
        </div>

        <!-- Loading state -->
        <div v-if="loading" class="loading">
            Chargement des notes...
        </div>

        <!-- Error state -->
        <div v-if="error" class="error">
            Erreur: {{ error }}
        </div>

        <!-- Success state -->
        <div v-if="!loading && !error && data" class="notes-content">
            <!-- En-tête étudiant et semestre -->
            <div class="student-header">
                <div class="student-info">
                    <h2>{{ data.student.firstname }} {{ data.student.name }}</h2>
                    <p>ID: {{ data.student.id }}</p>
                </div>
                <div class="semester-info">
                    <h3>{{ data.semester.label }}</h3>
                    <p>Semestre {{ data.semester.id }}</p>
                </div>
            </div>

            <!-- Statistiques -->
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-value">{{ notes.length }}</div>
                    <div class="stat-label">Matières</div>
                </div>
                <div class="stat-card">
                    <div class="stat-value">{{ moyenneGenerale }}/20</div>
                    <div class="stat-label">Moyenne</div>
                </div>
                <div class="stat-card">
                    <div class="stat-value">{{ totalCredits }}</div>
                    <div class="stat-label">Crédits</div>
                </div>
                <div class="stat-card">
                    <div class="stat-value">{{ notesSuperieures10.length }}</div>
                    <div class="stat-label">Matières ≥ 10</div>
                </div>
            </div>

            <!-- Liste des notes -->
            <div class="grades-section">
                <h3>Détail des notes par matière</h3>

                <div v-if="notes.length === 0" class="no-grades">
                    Aucune note disponible pour ce semestre.
                </div>

                <div v-else class="grades-list">
                    <div v-for="note in notes" :key="note.idGrade" class="grade-card">
                        <div class="subject-info">
                            <h4>{{ note.subject.title }}</h4>
                            <div class="subject-details">
                                <span class="ue">UE: {{ note.subject.ue }}</span>
                                <span class="credit">{{ note.subject.credit }} crédits</span>
                            </div>
                        </div>
                        <div class="grade-info">
                            <div :class="['grade-value', getGradeClass(note.grade)]">
                                {{ note.grade }}/20
                            </div>
                            <div class="grade-status">
                                <span v-if="note.grade >= 10" class="status-pass">Validé</span>
                                <span v-else class="status-fail">Non validé</span>
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
    name: 'DetailNotes',
    data() {
        return {
            data: null,
            loading: false,
            error: null,
            studentId: null,
            semestreId: null
        }
    },
    computed: {
        notes() {
            return this.data?.grades || []
        },
        moyenneGenerale() {
            if (!this.notes.length) return 0
            const sum = this.notes.reduce((acc, note) => acc + note.grade, 0)
            return (sum / this.notes.length).toFixed(2)
        },
        totalCredits() {
            return this.notes.reduce((acc, note) => acc + note.subject.credit, 0)
        },
        notesSuperieures10() {
            return this.notes.filter(note => note.grade >= 10)
        },
        ues() {
            const uesMap = {}

            this.notes.forEach(note => {
                const ueCode = note.subject.ue
                if (!uesMap[ueCode]) {
                    uesMap[ueCode] = {
                        nom: ueCode,
                        matieres: [],
                        totalCredits: 0,
                        sommeNotes: 0
                    }
                }

                uesMap[ueCode].matieres.push({
                    idSubject: note.subject.idSubject,
                    title: note.subject.title,
                    grade: note.grade,
                    credit: note.subject.credit
                })

                uesMap[ueCode].totalCredits += note.subject.credit
                uesMap[ueCode].sommeNotes += note.grade * note.subject.credit
            })

            // Calculer la moyenne pondérée pour chaque UE
            return Object.values(uesMap).map(ue => ({
                ...ue,
                moyenne: ue.sommeNotes / (ue.totalCredits * this.notes.length > 0 ? this.notes.length : 1)
            }))
        }
    },
    async mounted() {
        this.studentId = this.$route.params.id
        this.semestreId = this.$route.params.semestreId
        await this.fetchNotes()
    },
    methods: {
        async fetchNotes() {
            this.loading = true
            this.error = null

            try {
                const response = await apiService.getNotesEtudiantSemestre(this.studentId, this.semestreId)

                if (response.success) {
                    this.data = response.data
                } else {
                    throw new Error(response.error || 'Erreur lors du chargement des notes')
                }
            } catch (err) {
                console.error('Error fetching grades:', err)
                this.error = 'Impossible de charger les détails des notes'
            } finally {
                this.loading = false
            }
        },

        
        getGradeClass(grade) {
            if (grade >= 16) return 'excellent';
            if (grade >= 14) return 'very-good';
            if (grade >= 12) return 'good';
            if (grade >= 10) return 'passable';
            return 'fail';
        }
    }
}
</script>

<style scoped>
.detail-notes {
    max-width: 1200px;
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

.student-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    background: #f8f9fa;
    padding: 20px;
    border-radius: 8px;
    margin-bottom: 30px;
}

.student-info h2 {
    margin: 0 0 10px 0;
    color: #2c3e50;
}

.semester-info h3 {
    margin: 0 0 5px 0;
    color: #495057;
}

.stats-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 20px;
    margin-bottom: 30px;
}

.stat-card {
    background: white;
    border: 1px solid #e9ecef;
    border-radius: 8px;
    padding: 20px;
    text-align: center;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.stat-value {
    font-size: 2em;
    font-weight: bold;
    color: #2c3e50;
    margin-bottom: 5px;
}

.stat-label {
    color: #6c757d;
    font-size: 0.9em;
}

.grades-section {
    margin-bottom: 40px;
}

.grades-section h3 {
    margin-bottom: 20px;
    color: #2c3e50;
}

.no-grades {
    text-align: center;
    padding: 40px;
    color: #6c757d;
    background: #f8f9fa;
    border-radius: 8px;
}

.grades-list {
    display: flex;
    flex-direction: column;
    gap: 15px;
}

.grade-card {
    display: flex;
    justify-content: space-between;
    align-items: center;
    background: white;
    border: 1px solid #e9ecef;
    border-radius: 8px;
    padding: 20px;
    transition: transform 0.2s, box-shadow 0.2s;
}

.grade-card:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.subject-info h4 {
    margin: 0 0 10px 0;
    color: #2c3e50;
}

.subject-details {
    display: flex;
    gap: 15px;
    font-size: 0.9em;
}

.ue,
.credit {
    color: #6c757d;
}

.grade-info {
    text-align: right;
}

.grade-value {
    padding: 8px 12px;
    border-radius: 6px;
    font-weight: bold;
    font-size: 1.1em;
    margin-bottom: 5px;
}

.grade-status {
    font-size: 0.9em;
}

.status-pass {
    color: #28a745;
    font-weight: 500;
}

.status-fail {
    color: #dc3545;
    font-weight: 500;
}

.ue-summary {
    margin-top: 40px;
}

.ue-summary h3 {
    margin-bottom: 20px;
    color: #2c3e50;
}

.ue-list {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap: 20px;
}

.ue-card {
    background: white;
    border: 1px solid #e9ecef;
    border-radius: 8px;
    padding: 20px;
}

.ue-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;
    padding-bottom: 10px;
    border-bottom: 1px solid #e9ecef;
}

.ue-header h4 {
    margin: 0;
    color: #2c3e50;
}

.ue-credits {
    background: #e9ecef;
    padding: 4px 8px;
    border-radius: 4px;
    font-size: 0.9em;
    color: #495057;
}

.ue-grades {
    margin-bottom: 15px;
}

.ue-grade-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 8px 0;
    border-bottom: 1px solid #f8f9fa;
}

.ue-grade-item:last-child {
    border-bottom: none;
}

.subject-name {
    color: #495057;
    font-size: 0.9em;
}

.subject-grade {
    padding: 2px 6px;
    border-radius: 3px;
    font-weight: bold;
    font-size: 0.9em;
}

.ue-average {
    padding-top: 10px;
    border-top: 1px dashed #dee2e6;
    text-align: center;
    font-weight: 500;
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
</style>