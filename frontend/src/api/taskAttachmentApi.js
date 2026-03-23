import axiosClient from './axiosClient'

const taskAttachmentApi = {
  getByTaskId: (taskId) => axiosClient.get(`/task-attachments/task/${taskId}`),
  create: (data) => axiosClient.post('/task-attachments', data),
  update: (id, data) => axiosClient.put(`/task-attachments/${id}`, data),
  delete: (id) => axiosClient.delete(`/task-attachments/${id}`),
}

export default taskAttachmentApi;