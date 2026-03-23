import axiosClient from './axiosClient'

const userApi = {
  register: (data) => axiosClient.post('/users', data),
  getMyInfo: () => axiosClient.get('/users/my-infor'),
  updateProfile: (data) => axiosClient.put('/users/update', data),
  searchByUsername: (username) => axiosClient.get(`/users/username/${username}`),
  seacrhByEmail: (email) => axiosClient.get(`/users/email/${email}`),
}

export default userApi