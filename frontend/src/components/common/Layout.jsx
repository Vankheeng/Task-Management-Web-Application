import { useState, useEffect, useCallback } from 'react'
import { useParams, useLocation } from 'react-router-dom'
import Sidebar from './Sidebar'
import { useTeamTree } from '../../hooks/useTeam'
import { useTree } from '../../context/TreeContext'
import { useAuth } from '../../context/AuthContext'
import teamApi from '../../api/teamApi'

// Context chia sẻ collapsed state
import { createContext, useContext } from 'react'
export const SidebarContext = createContext({ collapsed: false })
export const useSidebar = () => useContext(SidebarContext)

export default function Layout({ children }) {
  const params = useParams()
  const location = useLocation()
  const { version } = useTree()
  const { user } = useAuth()
  const [collapsed, setCollapsed] = useState(false)

  const [teamId, setTeamId] = useState(() => localStorage.getItem('selectedTeamId') || '')
  const [teamName, setTeamName] = useState(() => localStorage.getItem('selectedTeamName') || '')

  useEffect(() => {
    if (params.teamId) setTeamId(params.teamId)
    setTeamName(localStorage.getItem('selectedTeamName') || '')
  }, [params.teamId, location.pathname])

  useEffect(() => {
    const storedTeamId = localStorage.getItem('selectedTeamId')
    if (!storedTeamId || !user) return
    teamApi.getMyTeams()
      .then(res => {
        const teams = res.result || []
        const stillMember = teams.some(t => t.id === storedTeamId)
        if (!stillMember) {
          localStorage.removeItem('selectedTeamId')
          localStorage.removeItem('selectedTeamName')
          localStorage.removeItem('selectedProjectId')
          localStorage.removeItem('selectedTeamRole')
          setTeamId('')
          setTeamName('')
        }
      })
      .catch(() => {})
  }, [user])

  const { treeData, refetch } = useTeamTree(teamId)

  useEffect(() => {
    if (teamId) refetch()
  }, [version, location.pathname])

  return (
    <SidebarContext.Provider value={{ collapsed }}>
      <div className="flex min-h-screen">
        <Sidebar
          treeData={treeData}
          selectedTeamName={teamName}
          selectedTeamId={teamId}
          collapsed={collapsed}
          onCollapse={setCollapsed}
        />
        <main className={`flex-1 p-8 min-h-screen transition-all duration-200 ${collapsed ? 'ml-16' : 'ml-60'}`}>
          {children}
        </main>
      </div>
    </SidebarContext.Provider>
  )
}