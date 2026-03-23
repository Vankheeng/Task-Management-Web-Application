import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import Layout from '../../components/common/Layout'
import { useNotification } from '../../context/NotificationContext'
import { HiTrash, HiCheckCircle, HiFilter, HiChevronLeft, HiChevronRight, HiExternalLink } from 'react-icons/hi'
import dayjs from 'dayjs'

const TYPE_COLORS = {
  TASK_ASSIGNED: 'bg-blue-100 text-blue-700',
  TASK_UNASSIGNED: 'bg-slate-100 text-slate-600',
  TASK_UPDATED: 'bg-yellow-100 text-yellow-700',
  TASK_STATUS_UPDATED: 'bg-purple-100 text-purple-700',
  TASK_COMMENT: 'bg-green-100 text-green-700',
  TEAM_ADDED: 'bg-primary-light text-primary',
  TASK_OVERDUE: 'bg-red-100 text-red-700',
}

export default function NotificationPage() {
  const navigate = useNavigate()
  const {
    notifications, totalPages, page, filter,
    markAsRead, markAllAsRead, deleteNotification, applyFilter, loadMore
  } = useNotification()
  const [showFilter, setShowFilter] = useState(false)

  const handleClickNotification = async (n) => {
    // Mark as read
    if (!n.isRead) await markAsRead(n.id)

    // Navigate to relevant page
    if (n.task?.id) {
      navigate(`/tasks/${n.task.id}`)
    } else if (n.team?.id) {
      localStorage.setItem('selectedTeamId', n.team.id)
      localStorage.setItem('selectedTeamName', n.team.name || '')
      navigate(`/teams/${n.team.id}`)
    }
  }

  return (
    <Layout>
      <div className="fade-in">
        <h1 className="font-display font-bold text-3xl text-primary mb-6">Notification</h1>

        <div className="card overflow-hidden">
          {/* Toolbar */}
          <div className="flex items-center justify-between px-4 py-3 border-b border-border">
            <div className="flex items-center gap-2 text-sm text-slate-500">
              <button
                onClick={() => {}}
                className={`hover:text-primary ${page === 0 ? 'opacity-30 cursor-default' : 'cursor-pointer'}`}
              >
                <HiChevronLeft />
              </button>
              <span>Page {page + 1}/{totalPages || 1}</span>
              <button
                onClick={loadMore}
                className={`hover:text-primary ${page + 1 >= totalPages ? 'opacity-30 cursor-default' : 'cursor-pointer'}`}
              >
                <HiChevronRight />
              </button>
            </div>

            <div className="flex items-center gap-3">
              <button onClick={markAllAsRead} className="text-sm text-primary hover:underline font-medium">
                Mark all as read
              </button>

              <div className="relative">
                <button onClick={() => setShowFilter(p => !p)} className="text-slate-400 hover:text-primary">
                  <HiFilter className="text-lg" />
                </button>
                {showFilter && (
                  <div className="absolute right-0 top-7 bg-white border border-border rounded-lg shadow-card z-10 min-w-40 overflow-hidden">
                    {[
                      { label: 'All notifications', value: null },
                      { label: 'Only unread', value: false },
                      { label: 'Only read', value: true },
                    ].map(f => (
                      <button key={String(f.value)}
                        onClick={() => { applyFilter(f.value); setShowFilter(false) }}
                        className={`w-full text-left px-4 py-2 text-sm hover:bg-surface transition-colors
                          ${filter === f.value ? 'text-primary font-medium bg-primary-light' : 'text-slate-600'}`}>
                        {f.label}
                      </button>
                    ))}
                  </div>
                )}
              </div>
            </div>
          </div>

          {/* Header */}
          <div className="grid grid-cols-12 px-4 py-2 bg-surface border-b border-border text-xs font-semibold text-slate-500 uppercase tracking-wide">
            <div className="col-span-6">Message</div>
            <div className="col-span-3 text-right">Created at</div>
            <div className="col-span-2 text-right">Mark read</div>
            <div className="col-span-1 text-right">Del</div>
          </div>

          {notifications.length === 0 ? (
            <div className="p-8 text-center text-slate-400 text-sm">No notifications</div>
          ) : (
            notifications.map(n => (
              <div key={n.id}
                className={`grid grid-cols-12 items-center px-4 py-3 border-b border-border last:border-0 transition-colors
                  ${!n.isRead ? 'bg-primary-light/40' : 'hover:bg-surface'}`}
              >
                {/* Message - clickable */}
                <div className="col-span-6">
                  <button
                    onClick={() => handleClickNotification(n)}
                    className="text-left w-full group"
                  >
                    <p className={`text-sm group-hover:underline ${!n.isRead ? 'font-medium text-slate-800' : 'text-slate-600'}`}>
                      {n.message}
                    </p>
                    <div className="flex items-center gap-2 mt-0.5">
                      {n.type && (
                        <span className={`badge text-xs ${TYPE_COLORS[n.type] || 'bg-slate-100 text-slate-500'}`}>
                          {n.type.replace(/_/g, ' ')}
                        </span>
                      )}
                      {(n.task || n.team) && (
                        <HiExternalLink className="text-xs text-slate-400" />
                      )}
                    </div>
                  </button>
                </div>

                {/* Date */}
                <div className="col-span-3 text-right text-sm text-slate-500">
                  {dayjs(n.createdAt).format('D/M/YYYY')}
                </div>

                {/* Mark read */}
                <div className="col-span-2 flex justify-end">
                  <button onClick={() => !n.isRead && markAsRead(n.id)} disabled={n.isRead}
                    className={`transition-colors ${n.isRead ? 'text-green-400 cursor-default' : 'text-slate-300 hover:text-green-500'}`}>
                    <HiCheckCircle className="text-xl" />
                  </button>
                </div>

                {/* Delete */}
                <div className="col-span-1 flex justify-end">
                  <button onClick={() => deleteNotification(n.id)}
                    className="text-slate-400 hover:text-red-500 transition-colors">
                    <HiTrash />
                  </button>
                </div>
              </div>
            ))
          )}
        </div>
      </div>
    </Layout>
  )
}