import { createContext, useContext, useState, useEffect, useCallback } from 'react'
import notificationApi from '../api/notificationApi'
import { tokenUtils } from '../utils/tokenUtils'

export const NotificationContext = createContext(null)

export function NotificationProvider({ children }) {
  const [notifications, setNotifications] = useState([])
  const [unreadCount, setUnreadCount] = useState(0)
  const [page, setPage] = useState(0)
  const [totalPages, setTotalPages] = useState(1)
  const [filter, setFilter] = useState(null)

  const fetchNotifications = useCallback(async (p = 0, isRead = null) => {
    try {
      const params = { page: p, size: 10 }
      if (isRead !== null) params.isRead = isRead
      const res = await notificationApi.getAll(params)
      const data = res.result
      if (p === 0) setNotifications(data.content || [])
      else setNotifications(prev => [...prev, ...(data.content || [])])
      setTotalPages(data.totalPages || 1)
      setPage(p)
    } catch {}
  }, [])

  const fetchUnreadCount = useCallback(async () => {
    try {
      const res = await notificationApi.getAll({ page: 0, size: 1, isRead: false })
      setUnreadCount(res.result?.totalElements || 0)
    } catch {}
  }, [])

  useEffect(() => {
    if (tokenUtils.exists()) {
      fetchNotifications(0, null)
      fetchUnreadCount()
      // Poll every 30s
      const interval = setInterval(fetchUnreadCount, 30000)
      return () => clearInterval(interval)
    }
  }, [fetchNotifications, fetchUnreadCount])

  const markAsRead = async (id) => {
    await notificationApi.markAsRead(id)
    setNotifications(prev => prev.map(n => n.id === id ? { ...n, isRead: true } : n))
    setUnreadCount(prev => Math.max(0, prev - 1))
  }

  const markAllAsRead = async () => {
    await notificationApi.markAllAsRead()
    setNotifications(prev => prev.map(n => ({ ...n, isRead: true })))
    setUnreadCount(0)
  }

  const deleteNotification = async (id) => {
    const n = notifications.find(x => x.id === id)
    await notificationApi.delete(id)
    setNotifications(prev => prev.filter(x => x.id !== id))
    if (n && !n.isRead) setUnreadCount(prev => Math.max(0, prev - 1))
  }

  const applyFilter = (isRead) => {
    setFilter(isRead)
    fetchNotifications(0, isRead)
  }

  const loadMore = () => {
    if (page + 1 < totalPages) fetchNotifications(page + 1, filter)
  }

  return (
    <NotificationContext.Provider value={{
      notifications, unreadCount, page, totalPages, filter,
      fetchNotifications, fetchUnreadCount,
      markAsRead, markAllAsRead, deleteNotification,
      applyFilter, loadMore,
    }}>
      {children}
    </NotificationContext.Provider>
  )
}

export const useNotification = () => {
  const ctx = useContext(NotificationContext)
  if (!ctx) throw new Error('useNotification must be inside NotificationProvider')
  return ctx
}