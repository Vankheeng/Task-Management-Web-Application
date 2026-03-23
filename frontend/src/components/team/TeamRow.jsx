import { useState, useEffect, useCallback } from 'react'
import teamApi from '../api/teamApi'
import teamMemberApi from '../api/teamMemberApi'
import projectApi from '../api/projectApi'
import taskListApi from '../api/taskListApi'
import taskApi from '../api/taskApi'

export function useMyTeams() {
  const [teams, setTeams] = useState([])
  const [loading, setLoading] = useState(false)

  const fetch = useCallback(async () => {
    setLoading(true)
    try {
      const res = await teamApi.getMyTeams()
      setTeams(res.result || [])
    } catch {} finally { setLoading(false) }
  }, [])

  useEffect(() => { fetch() }, [fetch])

  return { teams, loading, refetch: fetch }
}

export function useTeamMembers(teamId) {
  const [members, setMembers] = useState([])
  const [loading, setLoading] = useState(false)

  const fetch = useCallback(async () => {
    if (!teamId) return
    setLoading(true)
    try {
      const res = await teamMemberApi.getByTeamId(teamId)
      setMembers(res.result || [])
    } catch {} finally { setLoading(false) }
  }, [teamId])

  useEffect(() => { fetch() }, [fetch])

  return { members, loading, refetch: fetch }
}

export function useTeamTree(teamId) {
  const [treeData, setTreeData] = useState({ projects: [] })
  const [loading, setLoading] = useState(false)

  const fetch = useCallback(async () => {
    if (!teamId) return
    setLoading(true)
    try {
      const projectsRes = await projectApi.getByTeamId(teamId)
      const projects = projectsRes.result || []

      const projectsWithLists = await Promise.all(
        projects.map(async (p) => {
          try {
            const tlRes = await taskListApi.getByProjectId(p.id)
            const taskLists = tlRes.result || []
            const tlWithTasks = await Promise.all(
              taskLists.map(async (tl) => {
                try {
                  const tRes = await taskApi.getByTaskListId(tl.id)
                  return { ...tl, tasks: tRes.result || [] }
                } catch { return { ...tl, tasks: [] } }
              })
            )
            return { ...p, taskLists: tlWithTasks }
          } catch { return { ...p, taskLists: [] } }
        })
      )
      setTreeData({ projects: projectsWithLists })
    } catch {} finally { setLoading(false) }
  }, [teamId])

  useEffect(() => { fetch() }, [fetch])

  return { treeData, loading, refetch: fetch }
}