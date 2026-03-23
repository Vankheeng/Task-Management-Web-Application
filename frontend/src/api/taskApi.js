import axiosClient from './axiosClient'

const taskApi = {
  getByTaskListId: (taskListId) => axiosClient.get(`/tasks/task-list/${taskListId}`),
  getById: (id) => axiosClient.get(`/tasks/${id}`),
  getMyTasks: (start, end) => axiosClient.get(`/tasks/my-tasks?startDay=${start}&endDay=${end}`),
  create: (data) => axiosClient.post('/tasks', data),
  update: (id, data) => axiosClient.put(`/tasks/${id}`, data),
  delete: (id) => axiosClient.delete(`/tasks/${id}`),
}

export default taskApi