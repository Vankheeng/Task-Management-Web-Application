import axiosClient from './axiosClient'

const commentApi = {
  getByTaskId: (taskId) => axiosClient.get(`/comments/task/${taskId}`),
  create: (data) => axiosClient.post('/comments', data),
  update: (id, data) => axiosClient.put(`/comments/${id}`, data),
  delete: (id) => axiosClient.delete(`/comments/${id}`),
}

export default commentApi;