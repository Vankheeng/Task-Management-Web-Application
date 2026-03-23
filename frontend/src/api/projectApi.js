import axiosClient from './axiosClient'

const projectApi = {
  getByTeamId: (teamId) => axiosClient.get(`/projects/team/${teamId}`),
  create: (data) => axiosClient.post('/projects', data),
  update: (id, data) => axiosClient.put(`/projects/${id}`, data),
  delete: (id) => axiosClient.delete(`/projects/${id}`),
}

export default projectApi;