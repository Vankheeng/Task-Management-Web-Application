import axiosClient from './axiosClient'

const notificationApi = {
  getAll: (params) => axiosClient.get('/notifications', { params }),
  markAsRead: (id) => axiosClient.put(`/notifications/read/${id}`),
  markAllAsRead: () => axiosClient.put('/notifications/read-all'),
  delete: (id) => axiosClient.delete(`/notifications/${id}`),
}

export default notificationApi;