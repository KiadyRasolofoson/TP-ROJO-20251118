// Configuration de l'application
export const config = {
    // Utilise toujours le proxy en développement
    BASE_URL: '/api',
    TIMEOUT: 10000,
    DEFAULT_HEADERS: {
        'Content-Type': 'application/json',
        'Accept': 'application/json'
    }
}

console.log('API Base URL:', config.BASE_URL)

export default config