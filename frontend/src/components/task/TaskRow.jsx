import { useNavigate } from 'react-router-dom'
import { HiTrash, HiCalendar } from 'react-icons/hi'
import dayjs from 'dayjs'

const PRIORITY_COLORS = {
  LOW: 'bg-green-100 text-green-700',
  MEDIUM: 'bg-yellow-100 text-yellow-700',
  HIGH: 'bg-red-100 text-red-700',
}

export default function TaskRow({ task, onDelete }) {
  const navigate = useNavigate()
  const isOverdue = task.deadline && dayjs(task.deadline).isBefore(dayjs(), 'day')

  const assignees = task.taskAssignments || []
  const firstAssignee = assignees[0]?.user?.fullName
  const extra = assignees.length > 1 ? `, +${assignees.length - 1} other` : ''

  return (
    <div className="grid grid-cols-12 items-center px-4 py-3 border-b border-border last:border-0 hover:bg-surface transition-colors">
      {/* Title */}
      <div className="col-span-3">
        <button
          onClick={() => navigate(`/tasks/${task.id}`)}
          className="text-primary font-medium text-sm hover:underline text-left line-clamp-1"
        >
          {task.title}
        </button>
      </div>

      {/* Assignees */}
      <div className="col-span-2 text-xs text-slate-500 truncate">
        {firstAssignee ? `${firstAssignee}${extra}` : <span className="text-slate-300">—</span>}
      </div>

      {/* Priority */}
      <div className="col-span-2">
        {task.priority
          ? <span className={`badge ${PRIORITY_COLORS[task.priority]}`}>{task.priority}</span>
          : <span className="text-slate-300 text-sm">—</span>
        }
      </div>

      {/* Deadline */}
      <div className={`col-span-2 flex items-center gap-1 text-sm ${isOverdue ? 'text-red-500 font-medium' : 'text-slate-500'}`}>
        {task.deadline ? (
          <>
            {dayjs(task.deadline).format('D/M/YYYY')}
            <HiCalendar className="text-xs opacity-60" />
          </>
        ) : <span className="text-slate-300">—</span>}
      </div>

      {/* Status */}
      <div className="col-span-2">
        {task.status
          ? <span className="badge bg-blue-100 text-blue-700">{task.status?.status || task.status?.name}</span>
          : <span className="text-slate-300 text-sm">—</span>
        }
      </div>

      {/* Created at */}
      <div className="col-span-1 text-xs text-slate-400">
        {dayjs(task.createdAt).format('D/M/YYYY')}
      </div>

      {/* Delete */}
      <div className="col-span-1 flex justify-end">
        <button
          onClick={e => { e.stopPropagation(); onDelete(task) }}
          className="text-slate-400 hover:text-red-500 transition-colors"
        >
          <HiTrash />
        </button>
      </div>
    </div>
  )
}