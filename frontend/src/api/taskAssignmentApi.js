import axiosClient from './axiosClient'

const taskAssignmentApi = {
  assign: (data) => axiosClient.post('/task-assignments', data),
  update: (id, data) => axiosClient.put(`/task-assignments/${id}`, data),
  remove: (id) => axiosClient.delete(`/task-assignments/${id}`),
}

export default taskAssignmentApi;