import { createContext, useContext, useState, useEffect } from 'react'
import authApi from '../api/authApi'
import userApi from '../api/userApi'
import { tokenUtils } from '../utils/tokenUtils'

export const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [user, setUser] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    if (tokenUtils.exists()) {
      userApi.getMyInfo()
        .then(res => setUser(res.result))
        .catch(() => tokenUtils.remove())
        .finally(() => setLoading(false))
    } else {
      setLoading(false)
    }
  }, [])

  const login = async (username, password) => {
    const res = await authApi.login({ username, password })
    tokenUtils.set(res.result.token)
    const info = await userApi.getMyInfo()
    setUser(info.result)
    localStorage.setItem("userInfo", JSON.stringify(info.result))
    return info.result
  }

  const logout = async () => {
    const token = tokenUtils.get()
    try { await authApi.logout(token) } catch {}
    tokenUtils.remove()
    localStorage.removeItem("userInfo")
    // Xóa toàn bộ team/project info khỏi localStorage
    localStorage.removeItem("selectedTeamId")
    localStorage.removeItem("selectedTeamName")
    localStorage.removeItem("selectedProjectId")
    setUser(null)
  }

  const updateUser = (data) => setUser(prev => ({ ...prev, ...data }))

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        <div className="w-8 h-8 border-2 border-primary/20 border-t-primary rounded-full animate-spin" />
      </div>
    )
  }

  return (
    <AuthContext.Provider value={{ user, loading, login, logout, updateUser }}>
      {children}
    </AuthContext.Provider>
  )
}

export const useAuth = () => {
  const ctx = useContext(AuthContext)
  if (!ctx) throw new Error('useAuth must be inside AuthProvider')
  return ctx
}