import axiosClient from './axiosClient'

const teamMemberApi = {
  getByTeamId: (teamId) => axiosClient.get(`/team-members/team/${teamId}`),
  addMember: (data) => axiosClient.post('/team-members', data),
  updateMember: (id, data) => axiosClient.put(`/team-members/${id}`, data),
  removeMember: (id) => axiosClient.delete(`/team-members/${id}`),
  searchUser: (username) => axiosClient.get(`/users/username/${username}`),
  searchByEmail: (email) => axiosClient.get(`/users/email/${email}`),
}

export default teamMemberApi;