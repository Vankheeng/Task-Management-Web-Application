import axiosClient from './axiosClient'

const teamApi = {
  getMyTeams: () => axiosClient.get('/teams'),
  createTeam: (data) => axiosClient.post('/teams', data),
  updateTeam: (id, data) => axiosClient.put(`/teams/${id}`, data),
  deleteTeam: (id) => axiosClient.delete(`/teams/${id}`),
}

export default teamApi;