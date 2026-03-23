import { useState } from 'react'
import taskApi from '../../api/taskApi'
import statusApi from '../../api/statusApi'
import taskAssignmentApi from '../../api/taskAssignmentApi'
import TaskAssigneeList from './TaskAssigneeList'
import AttachmentPanel from './AttachmentPanel'
import CommentPanel from './CommentPanel'
import AssignTaskModal from './AssignTaskModal'
import { HiPencil, HiSave, HiX } from 'react-icons/hi'
import dayjs from 'dayjs'
import { useEffect } from 'react'

const PRIORITY_COLORS = {
  LOW: 'bg-green-100 text-green-700',
  MEDIUM: 'bg-yellow-100 text-yellow-700',
  HIGH: 'bg-red-100 text-red-700',
}

export default function TaskDetail({ task, onRefetch }) {
  const [editing, setEditing] = useState(false)
  const [form, setForm] = useState({
    title: task.title,
    description: task.description || '',
    priority: task.priority,
    deadline: task.deadline || '',
    statusId: task.status?.id || '',
  })
  const [statuses, setStatuses] = useState([])
  const [showAssignModal, setShowAssignModal] = useState(false)
  const [saving, setSaving] = useState(false)

  useEffect(() => {
    if (task.status) {
      statusApi.getByProjectId(task.taskList?.project?.id || '')
        .then(res => setStatuses(res.result || []))
        .catch(() => {})
    }
  }, [task])

  const handleSave = async () => {
    setSaving(true)
    try {
      await taskApi.update(task.id, form)
      setEditing(false)
      onRefetch?.()
    } finally { setSaving(false) }
  }

  const handleRemoveAssignee = async (assignmentId) => {
    await taskAssignmentApi.remove(assignmentId)
    onRefetch?.()
  }

  return (
    <>
      <div className="card p-6 space-y-4">
        {/* Title */}
        <Field label="Title:">
          {editing
            ? <input className="input flex-1" value={form.title}
                onChange={e => setForm(p => ({ ...p, title: e.target.value }))} />
            : <span className="text-sm font-medium">{task.title}</span>}
        </Field>

        {/* Priority */}
        <Field label="Priority:">
          {editing
            ? <select className="input flex-1" value={form.priority}
                onChange={e => setForm(p => ({ ...p, priority: e.target.value }))}>
                <option value="LOW">Low</option>
                <option value="MEDIUM">Medium</option>
                <option value="HIGH">High</option>
              </select>
            : <span className={`badge ${PRIORITY_COLORS[task.priority] || 'bg-slate-100 text-slate-600'}`}>
                {task.priority || '—'}
              </span>}
        </Field>

        {/* Deadline */}
        <Field label="Deadline:">
          {editing
            ? <input className="input flex-1" type="date" value={form.deadline}
                onChange={e => setForm(p => ({ ...p, deadline: e.target.value }))} />
            : <span className={`text-sm ${task.deadline && dayjs(task.deadline).isBefore(dayjs(), 'day') ? 'text-red-500 font-medium' : ''}`}>
                {task.deadline ? dayjs(task.deadline).format('D/M/YYYY') : '—'}
              </span>}
        </Field>

        {/* Status */}
        <Field label="Status:">
          {editing
            ? <select className="input flex-1" value={form.statusId}
                onChange={e => setForm(p => ({ ...p, statusId: e.target.value }))}>
                <option value="">-- Select status --</option>
                {statuses.map(s => <option key={s.id} value={s.id}>{s.name || s.status}</option>)}
              </select>
            : <span className="badge bg-blue-100 text-blue-700">
                {task.status?.status || task.status?.name || '—'}
              </span>}
        </Field>

        {/* Created at */}
        <Field label="Created at:">
          <span className="text-sm text-slate-500">{dayjs(task.createdAt).format('D/M/YYYY')}</span>
        </Field>

        {/* Assignees */}
        <div className="flex items-start gap-3">
          <label className="text-sm text-slate-500 w-24 pt-1 flex-shrink-0">Assignee:</label>
          <TaskAssigneeList
            assignments={task.taskAssignments || []}
            onRemove={handleRemoveAssignee}
            onAdd={() => setShowAssignModal(true)}
          />
        </div>

        {/* Actions */}
        <div className="flex gap-2 pt-2 border-t border-border">
          {editing ? (
            <>
              <button onClick={handleSave} disabled={saving} className="btn-primary">
                <HiSave /> {saving ? 'Saving...' : 'Save'}
              </button>
              <button onClick={() => setEditing(false)} className="btn-secondary">
                <HiX /> Cancel
              </button>
            </>
          ) : (
            <button onClick={() => setEditing(true)} className="btn-primary">
              <HiPencil /> Edit
            </button>
          )}
        </div>
      </div>

      {/* Right panels */}
      <div className="mt-4 space-y-4">
        <AttachmentPanel
          taskId={task.id}
          attachments={task.taskAttachments || []}
          onRefetch={onRefetch}
        />
        <CommentPanel
          taskId={task.id}
          comments={task.comments || []}
          onRefetch={onRefetch}
        />
      </div>

      <AssignTaskModal
        open={showAssignModal}
        onClose={() => setShowAssignModal(false)}
        taskId={task.id}
        teamId={task.taskList?.project?.team?.id || localStorage.getItem('selectedTeamId')}
        existingAssignments={task.taskAssignments || []}
        onAssigned={() => { setShowAssignModal(false); onRefetch?.() }}
      />
    </>
  )
}

function Field({ label, children }) {
  return (
    <div className="flex items-center gap-3">
      <label className="text-sm text-slate-500 w-24 flex-shrink-0">{label}</label>
      <div className="flex-1">{children}</div>
    </div>
  )
}