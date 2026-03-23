import { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import taskApi from '../../api/taskApi'
import taskAssignmentApi from '../../api/taskAssignmentApi'
import taskAttachmentApi from '../../api/taskAttachmentApi'
import commentApi from '../../api/commentApi'
import statusApi from '../../api/statusApi'
import teamMemberApi from '../../api/teamMemberApi'
import Layout from '../../components/common/Layout'
import ConfirmDialog from '../../components/common/ConfirmDialog'
import AssignTaskModal from '../../components/task/AssignTaskModal'
import { useToast } from '../../components/common/Toast'
import { useAuth } from '../../context/AuthContext'
import { HiTrash, HiPencil, HiCheck, HiX, HiPlus, HiPaperAirplane, HiUser, HiPaperClip, HiLockClosed } from 'react-icons/hi'
import dayjs from 'dayjs'

const PRIORITY_OPTIONS = [
  { value: 'LOW', label: 'Low' },
  { value: 'MEDIUM', label: 'Medium' },
  { value: 'HIGH', label: 'High' },
]

const PRIORITY_COLORS = {
  LOW: 'bg-green-100 text-green-700',
  MEDIUM: 'bg-yellow-100 text-yellow-700',
  HIGH: 'bg-red-100 text-red-700',
}

export default function TaskDetailPage() {
  const { taskId } = useParams()
  const navigate = useNavigate()
  const toast = useToast()
  const { user } = useAuth()

  const [task, setTask] = useState(null)
  const [editing, setEditing] = useState(false)
  const [form, setForm] = useState({})
  const [statuses, setStatuses] = useState([])
  const [newComment, setNewComment] = useState('')
  const [newAttachment, setNewAttachment] = useState({ name: '', url: '' })
  const [showAttachForm, setShowAttachForm] = useState(false)
  const [showAssignModal, setShowAssignModal] = useState(false)
  const [showAssigneeTooltip, setShowAssigneeTooltip] = useState(null)
  const [deleteTarget, setDeleteTarget] = useState(null)
  const [loading, setLoading] = useState(true)
  const [saving, setSaving] = useState(false)
  const [myRole, setMyRole] = useState(() => localStorage.getItem('selectedTeamRole') || 'MEMBER')

  const isAdmin = myRole === 'ADMIN'
  const teamId = localStorage.getItem('selectedTeamId') || ''

  const fetchRole = async () => {
    if (!teamId || !user) return
    try {
      const res = await teamMemberApi.getByTeamId(teamId)
      const me = (res.result || []).find(m => m.user?.id === user?.id)
      if (me?.role) setMyRole(me.role)
    } catch {}
  }

  const fetchTask = async () => {
    try {
      const res = await taskApi.getById(taskId)
      const t = res.result
      setTask(t)
      setForm({
        title: t.title,
        description: t.description || '',
        priority: t.priority || 'MEDIUM',
        deadline: t.deadline || '',
        statusId: t.status?.id || '',
      })

      // Lấy projectId: ưu tiên từ task response (chắc chắn đúng)
      const pid = t.taskList?.project?.id || null

      if (pid) {
        // Lưu các info liên quan để sidebar và các trang khác dùng
        localStorage.setItem('selectedProjectId', pid)
        if (t.taskList?.project?.team?.id) {
          localStorage.setItem('selectedTeamId', t.taskList.project.team.id)
          localStorage.setItem('selectedTeamName', t.taskList.project.team.name || '')
        }
        const sRes = await statusApi.getByProjectId(pid).catch(() => ({ result: [] }))
        setStatuses(sRes.result || [])
      } else {
        // Fallback: dùng localStorage nếu có
        const stored = localStorage.getItem('selectedProjectId')
        if (stored && stored.includes('-')) {
          const sRes = await statusApi.getByProjectId(stored).catch(() => ({ result: [] }))
          setStatuses(sRes.result || [])
        }
      }
    } finally { setLoading(false) }
  }

  useEffect(() => {
    fetchRole()
    fetchTask()
  }, [taskId])

  const handleSave = async () => {
    setSaving(true)
    try {
      const payload = {
        title: form.title,
        description: form.description,
        priority: form.priority,
        taskListId: task.taskList?.id,
      }
      if (form.deadline) payload.deadline = form.deadline
      if (form.statusId) payload.statusId = form.statusId
      await taskApi.update(taskId, payload)
      setEditing(false)
      fetchTask()
      toast.success('Task updated')
    } catch (e) {
      toast.error(e?.message || 'Failed to update task')
    } finally { setSaving(false) }
  }

  const handleDeleteTask = async () => {
    try {
      await taskApi.delete(taskId)
      navigate(-1)
      toast.success('Task deleted')
    } catch (e) { toast.error(e?.message || 'Failed to delete task') }
  }

  const handleAddComment = async () => {
    if (!newComment.trim()) return
    try {
      await commentApi.create({ content: newComment, taskId })
      setNewComment('')
      fetchTask()
    } catch (e) { toast.error(e?.message || 'Failed to add comment') }
  }

  const handleDeleteComment = async (commentId) => {
    try {
      await commentApi.delete(commentId)
      fetchTask()
    } catch (e) { toast.error(e?.message || 'Failed to delete comment') }
  }

  const handleAddAttachment = async () => {
    if (!isAdmin) { toast.error('Only admins can add attachments'); return }
    if (!newAttachment.name.trim() || !newAttachment.url.trim()) return
    try {
      await taskAttachmentApi.create({ fileName: newAttachment.name, fileUrl: newAttachment.url, taskId })
      setNewAttachment({ name: '', url: '' })
      setShowAttachForm(false)
      fetchTask()
      toast.success('Attachment added')
    } catch (e) { toast.error(e?.message || 'Failed to add attachment') }
  }

  const handleDeleteAttachment = async (attachId) => {
    if (!isAdmin) { toast.error('Only admins can delete attachments'); return }
    try {
      await taskAttachmentApi.delete(attachId)
      fetchTask()
      toast.success('Attachment deleted')
    } catch (e) { toast.error(e?.message || 'Failed to delete attachment') }
  }

  const handleRemoveAssignee = async (assignmentId) => {
    if (!isAdmin) { toast.error('Only admins can remove assignees'); return }
    try {
      await taskAssignmentApi.remove(assignmentId)
      fetchTask()
    } catch (e) { toast.error(e?.message || 'Failed to remove assignee') }
  }

  if (loading) return <Layout><div className="text-slate-400 text-sm p-8">Loading...</div></Layout>
  if (!task) return <Layout><div className="text-slate-400 text-sm p-8">Task not found</div></Layout>

  return (
    <Layout>
      <div className="fade-in flex gap-6">
        {/* Left */}
        <div className="flex-1">
          {/* Breadcrumb */}
          <div className="flex items-center gap-1 text-xs text-slate-400 mb-3 flex-wrap">
            {task.taskList?.project?.team && (
              <button
                onClick={() => {
                  localStorage.setItem('selectedTeamId', task.taskList.project.team.id)
                  localStorage.setItem('selectedTeamName', task.taskList.project.team.name || '')
                  navigate('/teams/' + task.taskList.project.team.id)
                }}
                className="hover:text-primary transition-colors"
              >
                {task.taskList.project.team.name}
              </button>
            )}
            {task.taskList?.project?.team && task.taskList?.project && (
              <span className="text-slate-300">›</span>
            )}
            {task.taskList?.project && (
              <button
                onClick={() => {
                  localStorage.setItem('selectedProjectId', task.taskList.project.id)
                  navigate('/projects/' + task.taskList.project.id)
                }}
                className="hover:text-primary transition-colors"
              >
                {task.taskList.project.name}
              </button>
            )}
            {task.taskList?.project && task.taskList && (
              <span className="text-slate-300">›</span>
            )}
            {task.taskList && (
              <button
                onClick={() => navigate('/task-lists/' + task.taskList.id)}
                className="hover:text-primary transition-colors"
              >
                {task.taskList.name}
              </button>
            )}
          </div>

          <div className="flex items-center justify-between mb-6">
            <h1 className="font-display font-bold text-2xl text-primary">{task.title}</h1>
            {/* Chỉ admin xóa task */}
            {isAdmin && (
              <button onClick={() => setDeleteTarget(task)} className="text-slate-400 hover:text-red-500">
                <HiTrash className="text-xl" />
              </button>
            )}
          </div>

          <div className="card p-6 space-y-4">
            <Field label="Title:">
              {editing
                ? <input className="input flex-1" value={form.title} onChange={e => setForm(p => ({ ...p, title: e.target.value }))} />
                : <span className="text-sm font-medium">{task.title}</span>}
            </Field>

            <Field label="Priority:">
              {editing
                ? <select className="input flex-1" value={form.priority} onChange={e => setForm(p => ({ ...p, priority: e.target.value }))}>
                    {PRIORITY_OPTIONS.map(o => <option key={o.value} value={o.value}>{o.label}</option>)}
                  </select>
                : <span className={`badge ${PRIORITY_COLORS[task.priority] || 'bg-slate-100 text-slate-600'}`}>{task.priority || '—'}</span>}
            </Field>

            <Field label="Deadline:">
              {editing
                ? <input className="input flex-1" type="date" value={form.deadline} onChange={e => setForm(p => ({ ...p, deadline: e.target.value }))} />
                : <span className={`text-sm ${task.deadline && dayjs(task.deadline).isBefore(dayjs(), 'day') ? 'text-red-500 font-medium' : ''}`}>
                    {task.deadline ? dayjs(task.deadline).format('D/M/YYYY') : '—'}
                  </span>}
            </Field>

            <Field label="Status:">
              {editing
                ? <select className="input flex-1" value={form.statusId} onChange={e => setForm(p => ({ ...p, statusId: e.target.value }))}>
                    <option value="">-- Select status --</option>
                    {statuses.map(s => <option key={s.id} value={s.id}>{s.name || s.status}</option>)}
                  </select>
                : <span className="badge bg-blue-100 text-blue-700">{task.status?.status || task.status?.name || '—'}</span>}
            </Field>

            <Field label="Created at:">
              <span className="text-sm text-slate-500">{dayjs(task.createdAt).format('D/M/YYYY')}</span>
            </Field>

            {/* Assignees */}
            <div className="flex items-start gap-3">
              <label className="text-sm text-slate-500 w-24 pt-1 flex-shrink-0">Assignee:</label>
              <div className="flex flex-wrap gap-2 flex-1">
                {(task.taskAssignments || []).map(a => (
                  <div key={a.id} className="relative">
                    <div className="flex items-center gap-1 badge bg-primary-light text-primary cursor-pointer"
                      onClick={() => setShowAssigneeTooltip(showAssigneeTooltip === a.id ? null : a.id)}>
                      <HiUser className="text-xs" />
                      {a.user?.fullName}
                      {isAdmin && (
                        <button onClick={e => { e.stopPropagation(); handleRemoveAssignee(a.id) }}
                          className="ml-1 text-slate-400 hover:text-red-500">
                          <HiX className="text-xs" />
                        </button>
                      )}
                    </div>
                    {showAssigneeTooltip === a.id && (
                      <div className="absolute top-7 left-0 z-10 bg-white border border-border rounded-lg shadow-card p-3 text-xs w-48">
                        <p className="font-medium">{a.user?.fullName}</p>
                        <p className="text-slate-500">Phone: {a.user?.phoneNumber}</p>
                        <p className="text-slate-500">Email: {a.user?.email}</p>
                      </div>
                    )}
                  </div>
                ))}
                {/* Cả member đều assign được */}
                <button onClick={() => setShowAssignModal(true)}
                  className="badge bg-primary text-white cursor-pointer hover:bg-primary-dark">
                  <HiPlus className="text-xs" />
                </button>
              </div>
            </div>

            {/* Actions — cả member và admin đều edit được */}
            <div className="flex gap-2 pt-2 border-t border-border">
              {editing ? (
                <>
                  <button onClick={handleSave} disabled={saving} className="btn-primary">
                    <HiCheck /> {saving ? 'Saving...' : 'Save'}
                  </button>
                  <button onClick={() => setEditing(false)} className="btn-secondary"><HiX /> Cancel</button>
                </>
              ) : (
                <button onClick={() => setEditing(true)} className="btn-primary"><HiPencil /> Edit</button>
              )}
            </div>
          </div>
        </div>

        {/* Right */}
        <div className="w-72 space-y-4">
          {/* Attachments */}
          <div className="card p-4">
            <div className="flex items-center justify-between mb-3">
              <h3 className="font-semibold text-sm text-slate-700 flex items-center gap-1">
                <HiPaperClip /> Attachment
              </h3>
              {isAdmin && (
                <button onClick={() => setShowAttachForm(p => !p)}
                  className="text-xs text-primary hover:underline flex items-center gap-0.5">
                  <HiPlus className="text-xs" /> New
                </button>
              )}
            </div>

            {showAttachForm && isAdmin && (
              <div className="mb-3 p-3 bg-surface rounded-lg border border-border space-y-2">
                <input className="input text-xs py-1" placeholder="Name"
                  value={newAttachment.name} onChange={e => setNewAttachment(p => ({ ...p, name: e.target.value }))} />
                <input className="input text-xs py-1" placeholder="Url"
                  value={newAttachment.url} onChange={e => setNewAttachment(p => ({ ...p, url: e.target.value }))} />
                <div className="flex gap-1 justify-end">
                  <button onClick={() => setShowAttachForm(false)} className="text-xs text-slate-400">Cancel</button>
                  <button onClick={handleAddAttachment} className="text-xs text-primary font-medium">Add</button>
                </div>
              </div>
            )}

            {(task.taskAttachments || []).map(a => (
              <div key={a.id} className="flex items-center justify-between py-1.5 border-b border-border last:border-0">
                <a href={a.fileUrl} target="_blank" rel="noreferrer"
                  className="text-xs text-primary hover:underline truncate flex-1">{a.fileName}</a>
                {isAdmin && (
                  <button onClick={() => handleDeleteAttachment(a.id)}
                    className="text-slate-400 hover:text-red-500 ml-1"><HiTrash className="text-xs" /></button>
                )}
              </div>
            ))}
            {!task.taskAttachments?.length && !showAttachForm && (
              <p className="text-xs text-slate-400">No attachments</p>
            )}
          </div>

          {/* Comments — mọi member đều comment được */}
          <div className="card p-4">
            <h3 className="font-semibold text-sm text-slate-700 mb-3">Comment</h3>
            <div className="space-y-3 max-h-72 overflow-y-auto">
              {(task.comments || []).map(c => (
                <div key={c.id} className="group">
                  <div className="flex items-center justify-between">
                    <span className="text-xs font-medium text-slate-700">{c.createdBy?.fullName}</span>
                    {/* Xóa comment: chỉ admin hoặc người tạo */}
                    {(isAdmin || c.createdBy?.id === user?.id) && (
                      <button onClick={() => handleDeleteComment(c.id)}
                        className="text-slate-300 hover:text-red-500 opacity-0 group-hover:opacity-100 transition-opacity">
                        <HiTrash className="text-xs" />
                      </button>
                    )}
                  </div>
                  <p className="text-xs text-slate-600 mt-0.5 bg-surface p-2 rounded-lg">{c.content}</p>
                </div>
              ))}
              {!task.comments?.length && <p className="text-xs text-slate-400">No comments yet</p>}
            </div>
            {/* Comment: tất cả đều được */}
            <div className="flex gap-2 mt-3 pt-3 border-t border-border">
              <input className="input text-xs flex-1 py-1.5" placeholder="Write a comment..."
                value={newComment} onChange={e => setNewComment(e.target.value)}
                onKeyDown={e => e.key === 'Enter' && handleAddComment()} />
              <button onClick={handleAddComment} className="text-primary hover:text-primary-dark">
                <HiPaperAirplane className="rotate-90" />
              </button>
            </div>
          </div>
        </div>
      </div>

      <AssignTaskModal
        open={showAssignModal}
        onClose={() => setShowAssignModal(false)}
        taskId={taskId}
        teamId={teamId}
        existingAssignments={task.taskAssignments || []}
        onAssigned={() => { setShowAssignModal(false); fetchTask() }}
      />

      <ConfirmDialog open={!!deleteTarget} onClose={() => setDeleteTarget(null)}
        onConfirm={handleDeleteTask}
        title="Delete Task" message={`Delete "${deleteTarget?.title}"?`} />
    </Layout>
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