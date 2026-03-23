import { useState, useEffect, useCallback } from 'react'
import taskApi from '../api/taskApi'
import taskAssignmentApi from '../api/taskAssignmentApi'

export function useTaskList(taskListId) {
  const [tasks, setTasks] = useState([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)

  const fetch = useCallback(async () => {
    if (!taskListId) return
    setLoading(true)
    setError(null)
    try {
      const res = await taskApi.getByTaskListId(taskListId)
      setTasks(res.result || [])
    } catch (e) {
      setError(e?.message || 'Failed to load tasks')
    } finally {
      setLoading(false)
    }
  }, [taskListId])

  useEffect(() => { fetch() }, [fetch])

  return { tasks, loading, error, refetch: fetch }
}

export function useTaskDetail(taskId) {
  const [task, setTask] = useState(null)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)

  const fetch = useCallback(async () => {
    if (!taskId) return
    setLoading(true)
    setError(null)
    try {
      const res = await taskApi.getById(taskId)
      setTask(res.result)
    } catch (e) {
      setError(e?.message || 'Failed to load task')
    } finally {
      setLoading(false)
    }
  }, [taskId])

  useEffect(() => { fetch() }, [fetch])

  return { task, loading, error, refetch: fetch }
}

export function useMyAssignments() {
  const [assignments, setAssignments] = useState([])
  const [loading, setLoading] = useState(false)

  const fetch = useCallback(async () => {
    setLoading(true)
    try {
      const res = await taskAssignmentApi.getMyTasks()
      setAssignments(res.result || [])
    } catch {} finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => { fetch() }, [fetch])

  return { assignments, loading, refetch: fetch }
}