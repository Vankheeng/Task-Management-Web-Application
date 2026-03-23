import axiosClient from './axiosClient'

const statusApi = {
  getByProjectId: (projectId) => axiosClient.get(`/statuses/project/${projectId}`),
  create: (data) => axiosClient.post('/statuses', {
    status: data.status,
    projectId: data.projectId,
  }),
  update: (id, data) => axiosClient.put(`/statuses/${id}`, {
    status: data.status,
    projectId: data.projectId,
  }),
  delete: (id) => axiosClient.delete(`/statuses/${id}`),
}

export default statusApi