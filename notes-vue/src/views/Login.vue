<template>
    <div class="login">
        <h1>Connexion</h1>

        <div v-if="error" class="error">{{ error }}</div>

        <form @submit.prevent="handleLogin" class="login-form">
            <label>
                Nom d'utilisateur
                <input v-model="username" type="text" required />
            </label>

            <label>
                Mot de passe
                <input v-model="password" type="password" required />
            </label>

            <button type="submit" class="btn-login">Se connecter</button>
        </form>
    </div>
</template>

<script>
import apiService from '../services/api'

export default {
    name: 'Login',
    data() {
        return {
            username: '',
            password: '',
            error: null,
            loading: false
        }
    },
    methods: {
        async handleLogin() {
            this.error = null
            this.loading = true
            try {
                const resp = await apiService.login(this.username, this.password)
                // resp should contain token
                if (resp && resp.token) {
                    localStorage.setItem('token', resp.token)
                    // Optionally store expiresAt
                    if (resp.expiresAt) localStorage.setItem('tokenExpiresAt', resp.expiresAt)
                    // Redirect to home (semestres)
                    this.$router.push('/semestre')
                } else {
                    this.error = 'Réponse inattendue du serveur'
                }
            } catch (err) {
                console.error('Login failed', err)
                if (err && err.message && err.message.includes('401')) this.error = 'Identifiants invalides'
                else this.error = 'Impossible de se connecter'
            } finally {
                this.loading = false
            }
        }
    }
}
</script>

<style scoped>
.login {
    max-width: 420px;
    margin: 40px auto;
    padding: 20px;
    background: #f8f9fa;
    border: 1px solid #e9ecef;
    border-radius: 8px;
}

h1 {
    text-align: center;
    color: #2c3e50;
    margin-bottom: 20px;
}

.login-form {
    display: flex;
    flex-direction: column;
    gap: 12px;
}

label {
    display: flex;
    flex-direction: column;
    font-weight: 500;
    color: #34495e;
}

input[type="text"], input[type="password"] {
    padding: 10px;
    border: 1px solid #ced4da;
    border-radius: 4px;
    margin-top: 6px;
}

.btn-login {
    background: #42b983;
    color: white;
    padding: 10px 16px;
    border: none;
    border-radius: 4px;
    font-weight: 600;
    cursor: pointer;
}

.btn-login:hover { background: #3aa876; }

.error {
    background: #fee;
    color: #c33;
    padding: 10px;
    border-radius: 4px;
    margin-bottom: 10px;
    text-align: center;
}
</style>
