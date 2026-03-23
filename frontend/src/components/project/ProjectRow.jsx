import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { HiPencil, HiTrash, HiSave, HiX } from 'react-icons/hi'
import dayjs from 'dayjs'

export default function ProjectRow({ project, teamId, onUpdate, onDelete }) {
  const [editing, setEditing] = useState(false)
  const [name, setName] = useState(project.name)
  const navigate = useNavigate()

  const handleSave = async () => {
    await onUpdate(project.id, { name, teamId })
    setEditing(false)
  }

  const handleCancel = () => {
    setName(project.name)
    setEditing(false)
  }

  return (
    <div className="grid grid-cols-12 items-center px-4 py-3 border-b border-border last:border-0 hover:bg-surface transition-colors">
      <div className="col-span-6">
        {editing ? (
          <input
            className="input py-1 text-sm"
            value={name}
            onChange={e => setName(e.target.value)}
            onKeyDown={e => { if (e.key === 'Enter') handleSave(); if (e.key === 'Escape') handleCancel() }}
            autoFocus
            onClick={e => e.stopPropagation()}
          />
        ) : (
          <button
            onClick={() => navigate(`/projects/${project.id}`)}
            className="text-primary font-medium text-sm hover:underline text-left"
          >
            {project.name}
          </button>
        )}
      </div>

      <div className="col-span-4 text-sm text-slate-500">
        {dayjs(project.createdAt).format('D/M/YYYY')}
      </div>

      <div className="col-span-2 flex justify-end gap-2">
        {editing ? (
          <>
            <button onClick={handleSave} className="text-primary hover:text-primary-dark">
              <HiSave className="text-lg" />
            </button>
            <button onClick={handleCancel} className="text-slate-400 hover:text-slate-600">
              <HiX className="text-lg" />
            </button>
          </>
        ) : (
          <>
            <button
              onClick={e => { e.stopPropagation(); setEditing(true) }}
              className="text-slate-400 hover:text-primary transition-colors"
            >
              <HiPencil className="text-lg" />
            </button>
            <button
              onClick={e => { e.stopPropagation(); onDelete(project) }}
              className="text-slate-400 hover:text-red-500 transition-colors"
            >
              <HiTrash className="text-lg" />
            </button>
          </>
        )}
      </div>
    </div>
  )
}