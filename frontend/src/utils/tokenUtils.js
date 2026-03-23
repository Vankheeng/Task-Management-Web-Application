const TOKEN_KEY = 'token'
const TEAM_KEY = 'selectedTeamId'

export const tokenUtils = {
  get: () => localStorage.getItem(TOKEN_KEY),
  set: (token) => localStorage.setItem(TOKEN_KEY, token),
  remove: () => localStorage.removeItem(TOKEN_KEY),
  exists: () => !!localStorage.getItem(TOKEN_KEY),
}

export const teamUtils = {
  getSelectedTeamId: () => localStorage.getItem(TEAM_KEY),
  setSelectedTeamId: (id) => localStorage.setItem(TEAM_KEY, id),
  clear: () => localStorage.removeItem(TEAM_KEY),
}