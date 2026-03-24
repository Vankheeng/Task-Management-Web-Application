import { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import taskListApi from '../../api/taskListApi'
import { useTree } from '../../context/TreeContext'
import taskApi from '../../api/taskApi'
import statusApi from '../../api/statusApi'
import teamMemberApi from '../../api/teamMemberApi'
import Layout from '../../components/common/Layout'
import Modal from '../../components/common/Modal'
import ConfirmDialog from '../../components/common/ConfirmDialog'
import { useToast } from '../../components/common/Toast'
import { useAuth } from '../../context/AuthContext'
import { HiPlus, HiTrash, HiCalendar, HiPencil, HiCheck, HiX, HiLockClosed } from 'react-icons/hi'
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

export default function TaskListDetailPage() {
  const { taskListId } = useParams()
  const navigate = useNavigate()
  const toast = useToast()
  const { user } = useAuth()

  const [tasks, setTasks] = useState([])
  const [statuses, setStatuses] = useState([])
  const [showCreate, setShowCreate] = useState(false)
  const [newTask, setNewTask] = useState({ title: '', priority: 'MEDIUM', deadline: '', statusId: '' })
  const [deleteTarget, setDeleteTarget] = useState(null)
  const [editingName, setEditingName] = useState(false)
  const [tlName, setTlName] = useState('')
  const [taskListName, setTaskListName] = useState('Task List')
  const [projectId, setProjectId] = useState('')
  const [myRole, setMyRole] = useState(() => localStorage.getItem('selectedTeamRole') || 'MEMBER')

  const isAdmin = myRole === 'ADMIN'
  const { refreshTree } = useTree()
  const isMember = myRole === 'MEMBER' || isAdmin
  const teamId = localStorage.getItem('selectedTeamId') || ''

  useEffect(() => {
    fetchRole()
    fetchTasksAndProject()
  }, [taskListId])

  const fetchRole = async () => {
    if (!teamId || !user) return
    try {
      const res = await teamMemberApi.getByTeamId(teamId)
      const me = (res.result || []).find(m => m.user?.id === user?.id)
      if (me?.role) setMyRole(me.role)
    } catch {}
  }

  const fetchTasksAndProject = async () => {
    try {
      // Lấy tên task list
      const tlRes = await taskListApi.getByProjectId(localStorage.getItem('selectedProjectId') || '')
      const tls = tlRes.result || []
      const currentTL = tls.find(tl => tl.id === taskListId)
      if (currentTL) setTaskListName(currentTL.name)

      const res = await taskApi.getByTaskListId(taskListId)
      const taskList = res.result || []
      setTasks(taskList)

      let pid = localStorage.getItem('selectedProjectId') || ''
      if (!pid?.includes('-') && taskList.length > 0) {
        pid = taskList[0]?.taskList?.project?.id || ''
      }
      if (pid?.includes('-')) {
        setProjectId(pid)
        localStorage.setItem('selectedProjectId', pid)
        const sRes = await statusApi.getByProjectId(pid)
        setStatuses(sRes.result || [])
      }
    } catch {}
  }

  const handleCreateTask = async () => {
    if (!newTask.title.trim()) return
    try {
      const payload = { ...newTask, taskListId }
      if (!payload.statusId) delete payload.statusId
      if (!payload.deadline) delete payload.deadline
      await taskApi.create(payload)
      setShowCreate(false)
      setNewTask({ title: '', priority: 'MEDIUM', deadline: '', statusId: '' })
      fetchTasksAndProject()
      toast.success('Task created')
    } catch (e) { toast.error(e?.message || 'Failed to create task') }
  }

  const handleDeleteTask = async (id) => {
    try {
      await taskApi.delete(id)
      fetchTasksAndProject()
      toast.success('Task deleted')
    } catch (e) { toast.error(e?.message || 'Failed to delete task') }
  }

  const handleUpdateTLName = async () => {
    if (!isAdmin) { toast.error('Only admins can rename task lists'); return }
    if (!tlName.trim()) return
    try {
      await taskListApi.update(taskListId, { name: tlName.trim(), projectId })
      setTaskListName(tlName.trim()) // cập nhật title ngay lập tức
      setEditingName(false)
      refreshTree() // cập nhật sidebar
      toast.success('Task list renamed')
    } catch (e) { toast.error(e?.message || 'Failed to rename') }
  }

  return (
    <Layout>
      <div className="fade-in">
        <div className="flex items-center gap-3 mb-6">
          {editingName && isAdmin ? (
            <div className="flex items-center gap-2">
              <input className="input text-2xl font-display font-bold py-1" value={tlName}
                onChange={e => setTlName(e.target.value)} autoFocus
                onKeyDown={e => e.key === 'Enter' && handleUpdateTLName()} />
              <button onClick={handleUpdateTLName} className="text-primary"><HiCheck /></button>
              <button onClick={() => setEditingName(false)} className="text-slate-400"><HiX /></button>
            </div>
          ) : (
            <>
              <h1 className="font-display font-bold text-3xl text-primary">{taskListName}</h1>
              {/* Rename: chỉ admin */}
              {isAdmin
                ? <button onClick={() => { setEditingName(true); setTlName(taskListName) }}
                    className="text-slate-400 hover:text-primary"><HiPencil /></button>
                : <HiLockClosed className="text-slate-300" />}
            </>
          )}
        </div>

        <div className="card overflow-hidden">
          <div className="grid grid-cols-12 px-4 py-3 bg-surface border-b border-border text-xs font-semibold text-slate-500 uppercase tracking-wide">
            <div className="col-span-3">Title</div>
            <div className="col-span-2">Assignee</div>
            <div className="col-span-2">Priority</div>
            <div className="col-span-2">Deadline</div>
            <div className="col-span-2">Status</div>
            <div className="col-span-1 text-right">Del</div>
          </div>

          {tasks.length === 0 ? (
            <div className="p-8 text-center text-slate-400 text-sm">No tasks yet</div>
          ) : (
            tasks.map(task => (
              <div key={task.id}
                className="grid grid-cols-12 items-center px-4 py-3 border-b border-border last:border-0 hover:bg-surface">
                <div className="col-span-3">
                  <button onClick={() => navigate(`/tasks/${task.id}`)}
                    className="text-primary font-medium text-sm hover:underline text-left line-clamp-1">
                    {task.title}
                  </button>
                </div>
                <div className="col-span-2 text-xs text-slate-500 truncate">
                  {task.taskAssignments?.length > 0
                    ? `${task.taskAssignments[0].user?.fullName}${task.taskAssignments.length > 1 ? `, +${task.taskAssignments.length - 1}` : ''}`
                    : <span className="text-slate-300">—</span>}
                </div>
                <div className="col-span-2">
                  {task.priority
                    ? <span className={`badge ${PRIORITY_COLORS[task.priority]}`}>{task.priority}</span>
                    : <span className="text-slate-300 text-sm">—</span>}
                </div>
                <div className={`col-span-2 flex items-center gap-1 text-sm ${
                  task.deadline && dayjs(task.deadline).isBefore(dayjs(), 'day') ? 'text-red-500 font-medium' : 'text-slate-500'
                }`}>
                  {task.deadline
                    ? <>{dayjs(task.deadline).format('D/M/YYYY')}<HiCalendar className="text-xs opacity-60" /></>
                    : <span className="text-slate-300">—</span>}
                </div>
                <div className="col-span-2">
                  <span className="badge bg-blue-100 text-blue-700">
                    {task.status?.status || task.status?.name || '—'}
                  </span>
                </div>
                <div className="col-span-1 flex justify-end">
                  {/* Xóa task: chỉ admin */}
                  {isAdmin
                    ? <button onClick={() => setDeleteTarget(task)} className="text-slate-400 hover:text-red-500"><HiTrash /></button>
                    : <HiLockClosed className="text-slate-200" />}
                </div>
              </div>
            ))
          )}

          {/* Tạo task: cả admin và member */}
          <button onClick={() => setShowCreate(true)}
            className="w-full py-3 text-sm text-primary hover:bg-surface flex items-center justify-center gap-1">
            <HiPlus /> Add a task
          </button>
        </div>
      </div>

      <Modal open={showCreate} onClose={() => setShowCreate(false)} title="Create Task">
        <div className="space-y-4">
          <div>
            <label className="text-sm text-slate-600 mb-1 block">Title *</label>
            <input className="input" placeholder="Task title" value={newTask.title}
              onChange={e => setNewTask(p => ({ ...p, title: e.target.value }))} autoFocus />
          </div>
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="text-sm text-slate-600 mb-1 block">Priority</label>
              <select className="input" value={newTask.priority}
                onChange={e => setNewTask(p => ({ ...p, priority: e.target.value }))}>
                {PRIORITY_OPTIONS.map(opt => (
                  <option key={opt.value} value={opt.value}>{opt.label}</option>
                ))}
              </select>
            </div>
            <div>
              <label className="text-sm text-slate-600 mb-1 block">Deadline</label>
              <input className="input" type="date" value={newTask.deadline}
                onChange={e => setNewTask(p => ({ ...p, deadline: e.target.value }))}
                min={dayjs().add(1, 'day').format('YYYY-MM-DD')} />
            </div>
          </div>
          <div>
            <label className="text-sm text-slate-600 mb-1 block">Status</label>
            <select className="input" value={newTask.statusId}
              onChange={e => setNewTask(p => ({ ...p, statusId: e.target.value }))}>
              <option value="">-- Auto (To Do) --</option>
              {statuses.map(s => <option key={s.id} value={s.id}>{s.name || s.status}</option>)}
            </select>
          </div>
          <div className="flex gap-2 justify-end pt-2">
            <button onClick={() => setShowCreate(false)} className="btn-secondary">Cancel</button>
            <button onClick={handleCreateTask} className="btn-primary">Create</button>
          </div>
        </div>
      </Modal>

      <ConfirmDialog open={!!deleteTarget} onClose={() => setDeleteTarget(null)}
        onConfirm={() => handleDeleteTask(deleteTarget?.id)}
        title="Delete Task" message={`Delete "${deleteTarget?.title}"?`} />
    </Layout>
  )
}