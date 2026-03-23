import { useState } from 'react'
import { HiUser, HiX, HiPlus } from 'react-icons/hi'

export default function TaskAssigneeList({ assignments = [], onRemove, onAdd }) {
  const [tooltip, setTooltip] = useState(null)

  return (
    <div className="flex flex-wrap gap-2">
      {assignments.map(a => (
        <div key={a.id} className="relative">
          <div
            className="flex items-center gap-1 badge bg-primary-light text-primary cursor-pointer select-none"
            onClick={() => setTooltip(tooltip === a.id ? null : a.id)}
          >
            <HiUser className="text-xs" />
            <span>{a.user?.fullName}</span>
            <button
              onClick={e => { e.stopPropagation(); onRemove(a.id) }}
              className="ml-1 text-slate-400 hover:text-red-500 transition-colors"
            >
              <HiX className="text-xs" />
            </button>
          </div>

          {/* Tooltip */}
          {tooltip === a.id && (
            <div
              className="absolute top-8 left-0 z-20 bg-white border border-border rounded-xl shadow-modal p-3 text-xs w-52 fade-in"
              onClick={e => e.stopPropagation()}
            >
              <p className="font-semibold text-slate-800 mb-1">{a.user?.fullName}</p>
              <p className="text-slate-500">📞 {a.user?.phoneNumber || '—'}</p>
              <p className="text-slate-500">✉️ {a.user?.email || '—'}</p>
              <p className="text-slate-500">🏷️ Role: Member</p>
            </div>
          )}
        </div>
      ))}

      {/* Add button */}
      <button
        onClick={onAdd}
        className="badge bg-primary text-white hover:bg-primary-dark transition-colors cursor-pointer"
        title="Assign member"
      >
        <HiPlus className="text-xs" />
      </button>
    </div>
  )
}