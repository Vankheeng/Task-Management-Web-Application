import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { HiPencil, HiTrash, HiSave, HiX } from 'react-icons/hi'
import dayjs from 'dayjs'

export default function TaskListRow({ taskList, projectId, onUpdate, onDelete }) {
  const [editing, setEditing] = useState(false)
  const [name, setName] = useState(taskList.name)
  const navigate = useNavigate()

  const handleSave = async () => {
    await onUpdate(taskList.id, { name, projectId })
    setEditing(false)
  }

  const handleCancel = () => {
    setName(taskList.name)
    setEditing(false)
  }

  return (
    <div className="grid grid-cols-12 items-center px-4 py-3 border-b border-border last:border-0 hover:bg-surface transition-colors">
      {/* Name */}
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
            onClick={() => navigate(`/task-lists/${taskList.id}`)}
            className="text-primary font-medium text-sm hover:underline text-left"
          >
            {taskList.name}
          </button>
        )}
      </div>

      {/* Update */}
      <div className="col-span-2 flex justify-end">
        {editing ? (
          <button onClick={handleSave} className="text-primary hover:text-primary-dark">
            <HiSave className="text-lg" />
          </button>
        ) : (
          <button
            onClick={e => { e.stopPropagation(); setEditing(true) }}
            className="text-slate-400 hover:text-primary transition-colors"
          >
            <HiPencil className="text-lg" />
          </button>
        )}
      </div>

      {/* Delete */}
      <div className="col-span-2 flex justify-end">
        {editing ? (
          <button onClick={handleCancel} className="text-slate-400 hover:text-slate-600">
            <HiX className="text-lg" />
          </button>
        ) : (
          <button
            onClick={e => { e.stopPropagation(); onDelete(taskList) }}
            className="text-slate-400 hover:text-red-500 transition-colors"
          >
            <HiTrash className="text-lg" />
          </button>
        )}
      </div>

      {/* Created at */}
      <div className="col-span-2 text-right text-sm text-slate-500">
        {taskList.createdAt ? dayjs(taskList.createdAt).format('D/M/YYYY') : '—'}
      </div>
    </div>
  )
}