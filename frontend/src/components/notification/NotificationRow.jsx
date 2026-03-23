import { HiCheckCircle, HiTrash } from 'react-icons/hi'
import dayjs from 'dayjs'
import { NOTIFICATION_TYPE } from '../../utils/constants'

const TYPE_COLORS = {
  TASK_ASSIGNED: 'bg-blue-100 text-blue-700',
  TASK_UNASSIGNED: 'bg-slate-100 text-slate-600',
  TASK_UPDATED: 'bg-yellow-100 text-yellow-700',
  TASK_STATUS_UPDATED: 'bg-purple-100 text-purple-700',
  TASK_COMMENT: 'bg-green-100 text-green-700',
  TEAM_ADDED: 'bg-primary-light text-primary',
  TASK_OVERDUE: 'bg-red-100 text-red-700',
}

export default function NotificationRow({ notification, onMarkRead, onDelete }) {
  const { id, message, isRead, createdAt, type, task } = notification

  return (
    <div className={`grid grid-cols-12 items-center px-4 py-3 border-b border-border last:border-0 transition-colors
      ${!isRead ? 'bg-primary-light/40' : 'hover:bg-surface'}`}
    >
      {/* Message */}
      <div className="col-span-6">
        <p className={`text-sm ${!isRead ? 'font-medium text-slate-800' : 'text-slate-600'}`}>
          {message}
        </p>
        <div className="flex items-center gap-2 mt-0.5">
          {type && (
            <span className={`badge text-xs ${TYPE_COLORS[type] || 'bg-slate-100 text-slate-500'}`}>
              {NOTIFICATION_TYPE[type] || type}
            </span>
          )}
          {task && (
            <span className="text-xs text-slate-400">· {task.title}</span>
          )}
        </div>
      </div>

      {/* Created at */}
      <div className="col-span-3 text-right text-sm text-slate-500">
        {dayjs(createdAt).format('D/M/YYYY')}
      </div>

      {/* Mark as read */}
      <div className="col-span-2 flex justify-end">
        <button
          onClick={() => !isRead && onMarkRead(id)}
          disabled={isRead}
          className={`transition-colors ${isRead ? 'text-green-400 cursor-default' : 'text-slate-300 hover:text-green-500'}`}
          title={isRead ? 'Already read' : 'Mark as read'}
        >
          <HiCheckCircle className="text-xl" />
        </button>
      </div>

      {/* Delete */}
      <div className="col-span-1 flex justify-end">
        <button
          onClick={() => onDelete(id)}
          className="text-slate-400 hover:text-red-500 transition-colors"
        >
          <HiTrash />
        </button>
      </div>
    </div>
  )
}