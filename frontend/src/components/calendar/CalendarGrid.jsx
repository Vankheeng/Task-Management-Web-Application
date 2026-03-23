import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import dayjs from 'dayjs'

const DAYS = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']

// Màu theo status name
const getTaskColor = (task) => {
  const status = (task.status?.status || task.status?.name || '').toLowerCase()
  if (status.includes('done') || status.includes('complete')) return 'bg-green-500 hover:bg-green-600'
  if (status.includes('progress') || status.includes('doing')) return 'bg-yellow-500 hover:bg-yellow-600'
  if (status.includes('review')) return 'bg-purple-500 hover:bg-purple-600'
  if (status.includes('block') || status.includes('cancel')) return 'bg-red-400 hover:bg-red-500'
  return 'bg-primary hover:bg-primary-dark'
}

export default function CalendarGrid({ currentDate, tasks = [] }) {
  const navigate = useNavigate()
  const today = dayjs()
  const [expandedDay, setExpandedDay] = useState(null)

  const startOfMonth = currentDate.startOf('month')
  const daysInMonth = currentDate.daysInMonth()
  const startDay = startOfMonth.day() === 0 ? 6 : startOfMonth.day() - 1

  const getTasksForDay = (day) => {
    const date = currentDate.date(day).format('YYYY-MM-DD')
    return tasks.filter(t => t.deadline && t.deadline === date)
  }

  const isToday = (day) =>
    today.date() === day &&
    today.month() === currentDate.month() &&
    today.year() === currentDate.year()

  return (
    <div onClick={() => setExpandedDay(null)}>
      {/* Day headers */}
      <div className="grid grid-cols-7 mb-1">
        {DAYS.map(d => (
          <div key={d} className="text-center text-xs font-semibold text-slate-500 uppercase tracking-wider py-2">
            {d}
          </div>
        ))}
      </div>

      {/* Grid */}
      <div className="grid grid-cols-7 border-l border-t border-border rounded-xl overflow-hidden">
        {Array.from({ length: startDay }).map((_, i) => (
          <div key={`e-${i}`} className="border-r border-b border-border min-h-24 p-2 bg-slate-50/60" />
        ))}

        {Array.from({ length: daysInMonth }).map((_, i) => {
          const day = i + 1
          const dayTasks = getTasksForDay(day)
          const tod = isToday(day)
          const isExpanded = expandedDay === day
          const MAX_VISIBLE = 2
          const hiddenCount = dayTasks.length - MAX_VISIBLE

          return (
            <div key={day}
              className={`border-r border-b border-border min-h-24 p-2 transition-colors relative
                ${tod ? 'bg-primary-light/20' : 'hover:bg-surface'}`}
              onClick={e => e.stopPropagation()}
            >
              {/* Day number */}
              <div className={`text-sm font-medium mb-1 w-7 h-7 flex items-center justify-center rounded-full
                ${tod ? 'bg-primary text-white font-bold' : 'text-slate-600'}`}>
                {day}
              </div>

              {/* Visible tasks */}
              {dayTasks.slice(0, MAX_VISIBLE).map((t, idx) => (
                <button key={idx} onClick={() => navigate(`/tasks/${t.id}`)}
                  className={`w-full text-left text-xs px-1.5 py-0.5 rounded-md text-white mb-0.5 truncate transition-colors ${getTaskColor(t)}`}>
                  {t.title}
                </button>
              ))}

              {/* +N more button */}
              {hiddenCount > 0 && !isExpanded && (
                <button
                  onClick={e => { e.stopPropagation(); setExpandedDay(day) }}
                  className="text-xs text-primary hover:underline mt-0.5 font-medium">
                  +{hiddenCount} more
                </button>
              )}

              {/* Expanded popup — click task vẫn được */}
              {isExpanded && (
                <div
                  className="absolute top-0 left-0 z-30 bg-white border border-border rounded-xl shadow-modal p-3 min-w-40 w-48 fade-in"
                  onClick={e => e.stopPropagation()}
                >
                  <div className="flex items-center justify-between mb-2">
                    <span className="text-xs font-semibold text-slate-600">
                      {currentDate.date(day).format('D MMM')}
                    </span>
                    <button onClick={() => setExpandedDay(null)}
                      className="text-slate-400 hover:text-slate-600 text-xs">✕</button>
                  </div>
                  <div className="space-y-1">
                    {dayTasks.map((t, idx) => (
                      <button key={idx} onClick={() => navigate(`/tasks/${t.id}`)}
                        className={`w-full text-left text-xs px-2 py-1 rounded-lg text-white truncate transition-colors ${getTaskColor(t)}`}>
                        {t.title}
                      </button>
                    ))}
                  </div>
                </div>
              )}
            </div>
          )
        })}
      </div>
    </div>
  )
}