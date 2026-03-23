import axiosClient from './axiosClient'

const authApi = {
  login: (data) => axiosClient.post('/auth/log-in', data),
  logout: (token) => axiosClient.post('/auth/logout', { token }),
  introspect: (token) => axiosClient.post('/auth/introspect', { token }),
}

export default authApi