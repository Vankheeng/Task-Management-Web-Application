import axiosClient from './axiosClient'

const taskListApi = {
  getByProjectId: (projectId) => axiosClient.get(`/task-lists/project/${projectId}`),
  create: (data) => axiosClient.post('/task-lists', data),
  update: (id, data) => axiosClient.put(`/task-lists/${id}`, data),
  delete: (id) => axiosClient.delete(`/task-lists/${id}`),
}

export default taskListApi;