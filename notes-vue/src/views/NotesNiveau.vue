<template>
    <div class="notes-niveau">
        <div class="header">
            <router-link :to="`/etudiant/${studentId}/fiche`" class="back-btn">
                ← Retour à la fiche étudiant
            </router-link>
            <h1>Notes Licence {{ niveauId }} - {{ data?.student?.firstname }} {{ data?.student?.name }}</h1>
        </div>

        <!-- Loading state -->
        <div v-if="loading" class="loading">
            Chargement des notes du niveau L{{ niveauId }}...
        </div>

        <!-- Error state -->
        <div v-if="error" class="error">
            Erreur: {{ error }}
        </div>

        <!-- Success state -->
        <div v-if="!loading && !error && data" class="niveau-content">
            <!-- En-tête étudiant -->
            <div class="student-header">
                <div class="student-info">
                    <h2>{{ data.student.firstname }} {{ data.student.name }}</h2>
                    <p>Licence {{ niveauId }} - {{ getNiveauDescription }}</p>
                </div>
                <div class="niveau-stats">
                    <div class="stat">
                        <span class="stat-value">{{ data.semesters.length }}</span>
                        <span class="stat-label">Semestres</span>
                    </div>
                    <div class="stat">
                        <span class="stat-value">{{ totalMatieres }}</span>
                        <span class="stat-label">Matières</span>
                    </div>
                    <div class="stat">
                        <span class="stat-value">{{ totalCredits }}</span>
                        <span class="stat-label">Crédits</span>
                    </div>
                </div>
            </div>

            <!-- Navigation entre semestres -->
            <div class="semester-nav">
                <button v-for="(semester, index) in data.semesters" :key="index"
                    :class="['semester-tab', { active: activeSemesterIndex === index }]"
                    @click="activeSemesterIndex = index">
                    {{ semester.semester.label }}
                </button>
            </div>

            <!-- Contenu du semestre actif -->
            <div v-if="activeSemester" class="semester-content">
                <!-- Statistiques du semestre -->
                <div class="semester-stats">
                    <div class="stat-card">
                        <div class="stat-value">{{ activeSemester.grades.length }}</div>
                        <div class="stat-label">Matières</div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-value">{{ currentSemesterCredits }}</div>
                        <div class="stat-label">Crédits</div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-value">{{ moyenneSemestre }}/20</div>
                        <div class="stat-label">Moyenne</div>
                    </div>
                    <div class="stat-card">
                        <div class="stat-value">{{ matieresValidees }}</div>
                        <div class="stat-label">Validées</div>
                    </div>
                </div>

                <!-- Liste des notes du semestre -->
                <div class="grades-section">
                    <h3>Détail des notes - {{ activeSemester.semester.label }}</h3>

                    <div class="grades-list">
                        <div v-for="note in activeSemester.grades" :key="note.idGrade" class="grade-card">
                            <div class="subject-main">
                                <h4>{{ note.subject.title }}</h4>
                                <div class="subject-meta">
                                    <span class="ue">UE: {{ note.subject.ue }}</span>
                                    <span class="credit">{{ note.subject.credit }} crédits</span>
                                </div>
                            </div>
                            <div class="grade-details">
                                <div :class="['grade-value', getGradeClass(note.grade)]">
                                    {{ note.grade }}/20
                                </div>
                                <div class="grade-status">
                                    <span v-if="note.grade >= 10" class="status-pass">✓ Validé</span>
                                    <span v-else class="status-fail">✗ Non validé</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Vue d'ensemble de tous les semestres -->
            <div class="overview-section">
                <h2>Vue d'ensemble Licence {{ niveauId }}</h2>
                <div class="overview-grid">
                    <div v-for="semester in data.semesters" :key="semester.semester.label" class="overview-card">
                        <div class="overview-header">
                            <h4>{{ semester.semester.label }}</h4>
                            <span class="semester-credits">{{ getSemesterCredits(semester) }} crédits</span>
                        </div>
                        <div class="overview-grades">
                            <div class="overview-average">
                                <span class="average-label">Moyenne:</span>
                                <span :class="['average-value', getGradeClass(getSemesterAverage(semester))]">
                                    {{ getSemesterAverage(semester).toFixed(2) }}/20
                                </span>
                            </div>
                            <div class="overview-stats">
                                <span class="stat">{{ semester.grades.length }} matières</span>
                                <span class="stat">{{ getValidatedCount(semester) }} validées</span>
                            </div>
                        </div>
                        <router-link
                            :to="`/etudiant/${studentId}/notes/${semester.semester.label.replace('Semester ', '')}`"
                            class="overview-link">
                            Voir détail
                        </router-link>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import apiService from '../services/api'

export default {
    name: 'NotesNiveau',
    data() {
        return {
            data: null,
            loading: false,
            error: null,
            studentId: null,
            niveauId: null,
            activeSemesterIndex: 0
        }
    },
    computed: {
        activeSemester() {
            return this.data?.semesters[this.activeSemesterIndex]
        },
        getNiveauDescription() {
            const descriptions = {
                1: "Première année (S1 + S2)",
                2: "Deuxième année (S3 + S4)",
                3: "Troisième année (S5 + S6)"
            }
            return descriptions[this.niveauId] || `Niveau ${this.niveauId}`
        },
        totalMatieres() {
            if (!this.data) return 0
            return this.data.semesters.reduce((total, semester) => total + semester.grades.length, 0)
        },
        totalCredits() {
            if (!this.data) return 0
            return this.data.semesters.reduce((total, semester) => {
                return total + semester.grades.reduce((sum, grade) => sum + grade.subject.credit, 0)
            }, 0)
        },
        currentSemesterCredits() {
            if (!this.activeSemester) return 0
            return this.activeSemester.grades.reduce((sum, grade) => sum + grade.subject.credit, 0)
        },
        moyenneSemestre() {
            if (!this.activeSemester || !this.activeSemester.grades.length) return 0
            const sum = this.activeSemester.grades.reduce((acc, grade) => acc + grade.grade, 0)
            return (sum / this.activeSemester.grades.length).toFixed(2)
        },
        matieresValidees() {
            if (!this.activeSemester) return 0
            return this.activeSemester.grades.filter(grade => grade.grade >= 10).length
        },
        uesSemestre() {
            if (!this.activeSemester) return []

            const uesMap = {}
            this.activeSemester.grades.forEach(note => {
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

            return Object.values(uesMap).map(ue => ({
                ...ue,
                moyenne: ue.sommeNotes / (ue.totalCredits * this.activeSemester.grades.length > 0 ? this.activeSemester.grades.length : 1)
            }))
        }
    },
    async mounted() {
        this.studentId = this.$route.params.id
        this.niveauId = this.$route.params.niveauId
        await this.fetchNotesNiveau()
    },
    methods: {
        async fetchNotesNiveau() {
            this.loading = true
            this.error = null

            try {
                const response = await apiService.getNotesNiveau(this.studentId, this.niveauId)

                if (response.success) {
                    this.data = response.data
                    // Trier les semestres par ordre (S1, S2, etc.)
                    this.data.semesters.sort((a, b) => {
                        const numA = parseInt(a.semester.label.replace('Semester ', ''))
                        const numB = parseInt(b.semester.label.replace('Semester ', ''))
                        return numA - numB
                    })
                } else {
                    throw new Error(response.error || 'Erreur lors du chargement des notes')
                }
            } catch (err) {
                console.error('Error fetching level grades:', err)
                this.error = 'Impossible de charger les notes du niveau'
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
        },

        getSemesterCredits(semester) {
            return semester.grades.reduce((sum, grade) => sum + grade.subject.credit, 0)
        },

        getSemesterAverage(semester) {
            if (!semester.grades.length) return 0
            const sum = semester.grades.reduce((acc, grade) => acc + grade.grade, 0)
            return sum / semester.grades.length
        },

        getValidatedCount(semester) {
            return semester.grades.filter(grade => grade.grade >= 10).length
        }
    }
}
</script>

<style scoped>
.notes-niveau {
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
    align-items: center;
    background: #f8f9fa;
    padding: 20px;
    border-radius: 8px;
    margin-bottom: 30px;
    flex-wrap: wrap;
    gap: 20px;
}

.student-info h2 {
    margin: 0 0 5px 0;
    color: #2c3e50;
}

.student-info p {
    margin: 0;
    color: #6c757d;
}

.niveau-stats {
    display: flex;
    gap: 30px;
}

.stat {
    text-align: center;
}

.stat-value {
    display: block;
    font-size: 1.5em;
    font-weight: bold;
    color: #2c3e50;
}

.stat-label {
    font-size: 0.9em;
    color: #6c757d;
}

.semester-nav {
    display: flex;
    gap: 10px;
    margin-bottom: 20px;
    flex-wrap: wrap;
}

.semester-tab {
    background: #e9ecef;
    border: none;
    padding: 12px 20px;
    border-radius: 6px;
    cursor: pointer;
    transition: all 0.2s;
    font-weight: 500;
}

.semester-tab:hover {
    background: #dee2e6;
}

.semester-tab.active {
    background: #42b983;
    color: white;
}

.semester-content {
    background: white;
    border: 1px solid #e9ecef;
    border-radius: 8px;
    padding: 25px;
    margin-bottom: 30px;
}

.semester-stats {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
    gap: 15px;
    margin-bottom: 25px;
}

.stat-card {
    background: #f8f9fa;
    padding: 15px;
    border-radius: 6px;
    text-align: center;
}

.stat-card .stat-value {
    font-size: 1.8em;
    margin-bottom: 5px;
}

.stat-card .stat-label {
    font-size: 0.85em;
    color: #6c757d;
}

.grades-section {
    margin-bottom: 30px;
}

.grades-section h3 {
    margin-bottom: 20px;
    color: #2c3e50;
    border-bottom: 2px solid #42b983;
    padding-bottom: 10px;
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

.subject-main h4 {
    margin: 0 0 10px 0;
    color: #2c3e50;
}

.subject-meta {
    display: flex;
    gap: 15px;
    font-size: 0.9em;
}

.ue,
.credit {
    color: #6c757d;
}

.grade-details {
    text-align: right;
}

.grade-value {
    padding: 8px 12px;
    border-radius: 6px;
    font-weight: bold;
    font-size: 1.1em;
    margin-bottom: 5px;
    display: inline-block;
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
    margin-top: 30px;
}

.ue-summary h3 {
    margin-bottom: 20px;
    color: #2c3e50;
    border-bottom: 2px solid #42b983;
    padding-bottom: 10px;
}

.ue-list {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
    gap: 20px;
}

.ue-card {
    background: #f8f9fa;
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
    border-bottom: 1px solid #e9ecef;
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

.overview-section {
    background: white;
    border: 1px solid #e9ecef;
    border-radius: 8px;
    padding: 25px;
}

.overview-section h2 {
    margin: 0 0 20px 0;
    color: #2c3e50;
    border-bottom: 2px solid #42b983;
    padding-bottom: 10px;
}

.overview-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
    gap: 20px;
}

.overview-card {
    background: #f8f9fa;
    border: 1px solid #e9ecef;
    border-radius: 8px;
    padding: 20px;
    transition: transform 0.2s;
}

.overview-card:hover {
    transform: translateY(-2px);
}

.overview-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;
}

.overview-header h4 {
    margin: 0;
    color: #2c3e50;
}

.semester-credits {
    background: #e9ecef;
    padding: 4px 8px;
    border-radius: 4px;
    font-size: 0.8em;
    color: #495057;
}

.overview-grades {
    margin-bottom: 15px;
}

.overview-average {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 10px;
}

.average-label {
    font-weight: 500;
    color: #495057;
}

.average-value {
    padding: 4px 8px;
    border-radius: 4px;
    font-weight: bold;
}

.overview-stats {
    display: flex;
    justify-content: space-between;
    font-size: 0.9em;
    color: #6c757d;
}

.overview-link {
    display: block;
    text-align: center;
    background: #42b983;
    color: white;
    padding: 8px 16px;
    text-decoration: none;
    border-radius: 4px;
    font-size: 0.9em;
    transition: background-color 0.2s;
}

.overview-link:hover {
    background: #3aa876;
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

@media (max-width: 768px) {
    .student-header {
        flex-direction: column;
        align-items: flex-start;
    }

    .niveau-stats {
        width: 100%;
        justify-content: space-around;
    }

    .grade-card {
        flex-direction: column;
        align-items: flex-start;
        gap: 15px;
    }

    .grade-details {
        text-align: left;
        width: 100%;
    }
}
</style>