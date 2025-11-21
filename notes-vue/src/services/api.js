import { config } from '../config'

class ApiService {
    constructor() {
        this.baseURL = config.BASE_URL
    }

    async request(endpoint, options = {}) {
        const url = `${this.baseURL}${endpoint}`

        const defaultOptions = {
            headers: {
                ...config.DEFAULT_HEADERS,
                ...options.headers
            },
            timeout: config.TIMEOUT
        }

        try {
            const response = await fetch(url, { ...defaultOptions, ...options })

            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`)
            }

            return await response.json()
        } catch (error) {
            console.error('API request failed:', error)
            throw error
        }
    }

    // Méthode pour récupérer les semestres
    async getSemestres() {
        return this.request('/semesters')
    }

    // Méthode pour récupérer les étudiants d'un semestre
    async getEtudiantsBySemestre(semestreId) {
        return this.request(`/semesters/${semestreId}/students`)
    }

    async getNotesEtudiantSemestre(studentId, semesterId) {
        return this.request(`/students/${studentId}/grades/semester/${semesterId}`)
    }
}

export default new ApiService()